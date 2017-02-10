/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * JSATDatasetHelper.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import jsat.DataSet;
import jsat.SimpleDataSet;
import jsat.classifiers.CategoricalData;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.DataPoint;
import jsat.linear.DenseVector;
import jsat.linear.Vec;
import jsat.regression.RegressionDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for converting datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class JSATDatasetHelper {

  /**
   * Converts a JSAT dataset into an Instances object.
   *
   * @param dataset	the dataset to convert
   * @return		the Instances
   */
  // TODO bug in JSAT: stores incorrect attribute names
  // TODO bug in JSAT: label '?' is interpreted as missing value
  public static Instances toInstances(DataSet dataset) {
    Instances			result;
    ArrayList<Attribute>	atts;
    List<String> 		labels;
    Instance			inst;
    CategoricalData 		cat;
    DataPoint			dp;
    double[]			values;
    int				i;
    int				n;
    Vec				num;
    int[]			cats;
    boolean			regDataset;
    boolean			catDataset;

    // header
    atts       = new ArrayList<>();
    regDataset = false;
    catDataset = false;
    for (i = 0; i < dataset.getNumNumericalVars(); i++)
      atts.add(new Attribute("numeric-" + (i+1)));
    for (i = 0; i < dataset.getNumCategoricalVars(); i++) {
      labels = new ArrayList<>();
      cat = dataset.getCategories()[i];
      for (n = 0; n < cat.getNumOfCategories(); n++)
	labels.add(cat.getOptionName(n));
      atts.add(new Attribute("categorical-" + (i+1), labels));
    }
    if (dataset instanceof RegressionDataSet) {
      atts.add(new Attribute("class"));
      regDataset = true;
    }
    else if (dataset instanceof ClassificationDataSet) {
      labels = new ArrayList<>();
      cat = ((ClassificationDataSet) dataset).getPredicting();
      for (n = 0; n < cat.getNumOfCategories(); n++)
	labels.add(cat.getOptionName(n));
      atts.add(new Attribute("class", labels));
      catDataset = true;
    }
    result = new Instances("JSAT", atts, dataset.getSampleSize());

    // data
    for (i = 0; i < dataset.getSampleSize(); i++) {
      values = new double[result.numAttributes()];
      dp     = dataset.getDataPoint(i);
      num    = dp.getNumericalValues();
      cats   = dp.getCategoricalValues();
      for (n = 0; n < num.length(); n++)
	values[n] = num.get(n);
      for (n = 0; n < cats.length; n++) {
	if (cats[n] == -1)
	  values[num.length() + n] = Utils.missingValue();
	else
	  values[num.length() + n] = cats[n];
      }
      if (regDataset) {
	values[values.length - 1] = ((RegressionDataSet) dataset).getTargetValue(i);
      }
      else if (catDataset) {
	n = ((ClassificationDataSet) dataset).getDataPointCategory(i);
	if (n == -1)
	  values[values.length - 1] = Utils.missingValue();
	else
	  values[values.length - 1] = ((ClassificationDataSet) dataset).getDataPointCategory(i);
      }
      inst = new DenseInstance(dp.getWeight(), values);
      result.add(inst);
    }

    return result;
  }

  /**
   * Turns an Instances object into a JSAT DataSet.
   *
   * @param data	the instances to convert
   * @param skipped	the skipped attributes, can be null
   * @return		the DataSet
   */
  public final static DataSet toDataSet(Instances data, List<Attribute> skipped) {
    DataSet		result;
    List<Integer>	numeric;
    List<Integer>	categorical;
    int			numClass;
    int			catClass;
    int			i;
    int			n;
    Instance		inst;
    Attribute		att;
    CategoricalData[]	cats;
    Vec			num;
    List<DataPoint> 	list;
    int[]		cat;
    SimpleDataSet	simple;

    // determine indices
    numeric     = new ArrayList<>();
    categorical = new ArrayList<>();
    numClass    = -1;
    catClass    = -1;
    if (skipped != null)
      skipped.clear();
    for (i = 0; i < data.numAttributes(); i++) {
      if (data.attribute(i).isNumeric()) {
	numeric.add(i);
	if (i == data.classIndex())
	  numClass = numeric.size() - 1;
      }
      else if (data.attribute(i).isNominal()) {
	categorical.add(i);
	if (i == data.classIndex())
	  catClass = categorical.size() - 1;
      }
      else if (skipped != null) {
	skipped.add(data.attribute(i));
      }
    }

    // assemble categorical data structures
    cats = new CategoricalData[categorical.size()];
    for (i = 0; i < categorical.size(); i++) {
      att     = data.attribute(categorical.get(i));
      cats[i] = new CategoricalData(att.numValues());
      cats[i].setCategoryName(att.name());
      for (n = 0; n < att.numValues(); n++)
	cats[i].setOptionName(att.value(n), n);
    }

    // data
    list = new ArrayList<>();
    for (i = 0; i < data.numInstances(); i++) {
      inst = data.instance(i);
      // numeric data
      num = new DenseVector(numeric.size());
      for (n = 0; n < numeric.size(); n++)
	num.set(n, inst.value(numeric.get(n)));
      // categorical
      cat = new int[categorical.size()];
      for (n = 0; n < categorical.size(); n++)
	cat[n] = (int) inst.value(categorical.get(n));
      // create row
      list.add(new DataPoint(num, cat, cats, inst.weight()));
    }

    // dataset
    simple = new SimpleDataSet(list);
    for (i = 0; i < numeric.size(); i++)
      simple.setNumericName(data.attribute(numeric.get(i)).name(), i);
    if (numClass > -1)
      result = simple.asRegressionDataSet(numClass);
    else if (catClass > -1)
      result = simple.asClassificationDataSet(catClass);
    else
      result = simple;

    return result;
  }
}

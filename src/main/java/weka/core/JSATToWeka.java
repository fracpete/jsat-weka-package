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
 * JSATToWeka.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import jsat.DataSet;
import jsat.classifiers.CategoricalData;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.DataPoint;
import jsat.linear.Vec;
import jsat.regression.RegressionDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts from JSAT to WEKA.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
// TODO bug in JSAT: stores incorrect attribute names
// TODO bug in JSAT: label '?' is interpreted as missing value
public class JSATToWeka
  extends AbstractJSATConversion<DataSet, Instances, DataPoint, Instance> {

  /** the header. */
  protected Instances m_Header;

  /** whether a regression dataset. */
  boolean m_RegDataset;

  /** whether a classification dataset. */
  boolean m_CatDataset;

  /**
   * Initializes the conversion.
   *
   * @param dataset	the from dataset (to get structure etc)
   */
  @Override
  public void initialize(DataSet dataset) {
    ArrayList<Attribute> 	atts;
    List<String> 		labels;
    CategoricalData 		cat;
    int				i;
    int				n;

    // header
    atts         = new ArrayList<>();
    m_RegDataset = false;
    m_CatDataset = false;
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
      m_RegDataset = true;
    }
    else if (dataset instanceof ClassificationDataSet) {
      labels = new ArrayList<>();
      cat = ((ClassificationDataSet) dataset).getPredicting();
      for (n = 0; n < cat.getNumOfCategories(); n++)
        labels.add(cat.getOptionName(n));
      atts.add(new Attribute("class", labels));
      m_CatDataset = true;
    }
    m_Header = new Instances("JSAT", atts, dataset.getSampleSize());
  }

  /**
   * Converts the dataset.
   *
   * @param dataset	the dataset to convert
   * @return		the converted dataset
   * @see		#initialize(Object)
   */
  @Override
  public Instances convertDataset(DataSet dataset) {
    Instances			result;
    Instance			inst;
    DataPoint			dp;
    double[]			values;
    int				i;
    int				n;
    Vec num;
    int[]			cats;

    if (m_Header == null)
      throw new IllegalStateException("initialize method not called!");

    result = new Instances(m_Header);

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
      if (m_RegDataset) {
        values[values.length - 1] = ((RegressionDataSet) dataset).getTargetValue(i);
      }
      else if (m_CatDataset) {
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
   * Converts the data row.
   * NB: Class value is always missing, since no access to dataset.
   *
   * @param row		the row to convert
   * @return		the converted row
   * @see		#initialize(Object)
   */
  @Override
  public Instance convertRow(DataPoint row) {
    Instance	inst;
    double[]	values;
    int		n;
    Vec 	num;
    int[]	cats;

    if (m_Header == null)
      throw new IllegalStateException("initialize method not called!");

    values = new double[m_Header.numAttributes()];
    num    = row.getNumericalValues();
    cats   = row.getCategoricalValues();
    for (n = 0; n < num.length(); n++)
      values[n] = num.get(n);
    for (n = 0; n < cats.length; n++) {
      if (cats[n] == -1)
        values[num.length() + n] = Utils.missingValue();
      else
        values[num.length() + n] = cats[n];
    }
    values[values.length - 1] = Utils.missingValue();
    inst = new DenseInstance(row.getWeight(), values);

    return inst;
  }
}

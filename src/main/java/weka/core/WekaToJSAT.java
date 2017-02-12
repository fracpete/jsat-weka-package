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
 * WekaToJSAT.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import jsat.DataSet;
import jsat.SimpleDataSet;
import jsat.classifiers.CategoricalData;
import jsat.classifiers.DataPoint;
import jsat.linear.DenseVector;
import jsat.linear.Vec;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts from Weka to JSAT.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class WekaToJSAT
  extends AbstractJSATConversion<Instances, DataSet, Instance, DataPoint> {

  /** the indices of the numeric attributes in the Instances object. */
  protected List<Integer> m_Numeric;

  /** the indices of the categorical attributes in the Instances object. */
  protected List<Integer> m_Categorical;

  /** the index of the numeric class (-1 if not numeric. */
  protected int m_NumClass;

  /** the index of the categorical class (-1 if not categorical). */
  protected int m_CatClass;

  /** the categorical data. */
  protected CategoricalData[] m_CategoricalData;

  /**
   * Initializes the conversion.
   *
   * @param dataset	the from dataset (to get structure etc)
   */
  @Override
  public void initialize(Instances dataset) {
    int			i;
    int			n;
    Attribute		att;

    // determine indices
    m_Numeric = new ArrayList<>();
    m_Categorical = new ArrayList<>();
    m_NumClass = -1;
    m_CatClass = -1;
    for (i = 0; i < dataset.numAttributes(); i++) {
      if (dataset.attribute(i).isNumeric()) {
	m_Numeric.add(i);
	if (i == dataset.classIndex())
	  m_NumClass = m_Numeric.size() - 1;
      }
      else if (dataset.attribute(i).isNominal()) {
	m_Categorical.add(i);
	if (i == dataset.classIndex())
	  m_CatClass = m_Categorical.size() - 1;
      }
    }

    // assemble categorical data structures
    m_CategoricalData = new CategoricalData[m_Categorical.size()];
    for (i = 0; i < m_Categorical.size(); i++) {
      att     = dataset.attribute(m_Categorical.get(i));
      m_CategoricalData[i] = new CategoricalData(att.numValues());
      m_CategoricalData[i].setCategoryName(att.name());
      for (n = 0; n < att.numValues(); n++)
	m_CategoricalData[i].setOptionName(att.value(n), n);
    }
  }

  /**
   * Converts the dataset.
   *
   * @param dataset	the dataset to convert
   * @return		the converted dataset
   * @see		#initialize(Object)
   */
  @Override
  public DataSet convertDataset(Instances dataset) {
    DataSet		result;
    int			i;
    int			n;
    Instance		inst;
    Vec 		num;
    List<DataPoint> 	list;
    int[]		cat;
    SimpleDataSet 	simple;

    // data
    list = new ArrayList<>();
    for (i = 0; i < dataset.numInstances(); i++) {
      inst = dataset.instance(i);
      // numeric data
      num = new DenseVector(m_Numeric.size());
      for (n = 0; n < m_Numeric.size(); n++)
	num.set(n, inst.value(m_Numeric.get(n)));
      // categorical
      cat = new int[m_Categorical.size()];
      for (n = 0; n < m_Categorical.size(); n++)
	cat[n] = (int) inst.value(m_Categorical.get(n));
      // create row
      list.add(new DataPoint(num, cat, m_CategoricalData, inst.weight()));
    }

    // dataset
    simple = new SimpleDataSet(list);
    for (i = 0; i < m_Numeric.size(); i++)
      simple.setNumericName(dataset.attribute(m_Numeric.get(i)).name(), i);
    if (m_NumClass > -1)
      result = simple.asRegressionDataSet(m_NumClass);
    else if (m_CatClass > -1)
      result = simple.asClassificationDataSet(m_CatClass);
    else
      result = simple;

    return result;
  }

  /**
   * Converts the data row.
   *
   * @param row		the row to convert
   * @return		the converted row
   * @see		#initialize(Object)
   */
  @Override
  public DataPoint convertRow(Instance row) {
    // TODO
    return null;
  }
}

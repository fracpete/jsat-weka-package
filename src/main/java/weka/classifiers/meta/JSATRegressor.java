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
 * JSATRegressor.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.classifiers.meta;

import jsat.classifiers.DataPoint;
import jsat.regression.MultipleLinearRegression;
import jsat.regression.RegressionDataSet;
import jsat.regression.Regressor;
import nz.ac.waikato.cms.jenericcmdline.DefaultProcessor;
import weka.classifiers.AbstractClassifier;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 <!-- globalinfo-start -->
 <!-- globalinfo-end -->
 *
 <!-- technical-bibtex-start -->
 <!-- technical-bibtex-end -->
 *
 <!-- options-start -->
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class JSATRegressor
  extends AbstractClassifier {

  public final static String REGRESSOR = "regressor";

  /** the jsat regressor to use. */
  protected Regressor m_Regressor = getDefaultRegressor();

  /**
   * Returns a string describing classifier
   *
   * @return a description suitable for displaying in the explorer/experimenter
   *         gui
   */
  public String globalInfo() {
    return "Wrapper around a JSAT regressor.";
  }

  /**
   * Returns an enumeration describing the available options.
   *
   * @return an enumeration of all the available options.
   */
  @Override
  public Enumeration listOptions() {
    Vector result = new Vector();

    result.addElement(new Option(
      "\tThe JSAT regressor to use.\n"
	+ "\t(default: " + getDefaultRegressor().getClass().getName() + ")",
      REGRESSOR, 1, "-" + REGRESSOR + " <classname + options>"));

    result.addAll(Collections.list(super.listOptions()));

    return result.elements();
  }

  /**
   * Sets the options.
   *
   * @param options	the options
   * @throws Exception	if setting fails
   */
  @Override
  public void setOptions(String[] options) throws Exception {
    DefaultProcessor proc = new DefaultProcessor();
    String tmpStr = Utils.getOption(REGRESSOR, options);
    if (tmpStr.isEmpty())
      setRegressor(getDefaultRegressor());
    else
      setRegressor((Regressor) proc.fromCommandline(tmpStr));
    super.setOptions(options);
  }

  /**
   * Returns the options.
   *
   * @return		the options
   */
  @Override
  public String[] getOptions() {
    List<String> result = new ArrayList<>();
    DefaultProcessor proc = new DefaultProcessor();

    try {
      result.add("-" + REGRESSOR);
      result.add(proc.toCommandline(getRegressor()));
    }
    catch (Exception e) {
      System.err.println("Failed to retrieve commandline for regressor: " + e);
      e.printStackTrace();
    }

    Collections.addAll(result, super.getOptions());

    return result.toArray(new String[result.size()]);
  }

  /**
   * Returns the default regression algorithm.
   *
   * @return		the default
   */
  protected Regressor getDefaultRegressor() {
    return new MultipleLinearRegression();
  }

  /**
   * Sets the regression algorithm.
   *
   * @param value	the algorithm
   */
  public void setRegressor(Regressor value) {
    m_Regressor = value;
  }

  /**
   * Returns the regression algorithm.
   *
   * @return		the algorithm
   */
  public Regressor getRegressor() {
    return m_Regressor;
  }

  /**
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String regressorTipText() {
    return "The JSAT regressor to use.";
  }

  /**
   * Returns the Capabilities of this classifier.
   *
   * @return the capabilities of this object
   * @see Capabilities
   */
  @Override
  public Capabilities getCapabilities() {
    Capabilities	result;

    result = super.getCapabilities();
    result.disableAllClasses();
    result.disableAllClassDependencies();
    result.enable(Capability.NUMERIC_CLASS);
    result.enable(Capability.MISSING_CLASS_VALUES);

    return result;
  }

  /**
   * Performs predictions.
   *
   * @param instance	the instance to classify
   * @return		the classification
   * @throws Exception	if prediction fails
   */
  @Override
  public double classifyInstance(Instance instance) throws Exception {
    DataPoint	data;

    // convert into JSAT row
    // TODO
    data = null;

    return m_Regressor.regress(data);
  }

  /**
   * Builds the regressor on the dataset.
   *
   * @param instances	the dataset to use
   * @throws Exception	if the build fails
   */
  @Override
  public void buildClassifier(Instances instances) throws Exception {
    instances = new Instances(instances);
    instances.deleteWithMissingClass();

    // convert into JSAT dataset
    // TODO
    RegressionDataSet dataset = null;

    // build
    m_Regressor.train(dataset);
  }
}

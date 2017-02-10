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
 * JSATClassifier.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.classifiers.meta;

import jsat.classifiers.CategoricalResults;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.Classifier;
import jsat.classifiers.DataPoint;
import jsat.classifiers.trees.ID3;
import nz.ac.waikato.cms.jenericcmdline.DefaultProcessor;
import weka.classifiers.AbstractClassifier;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.JSATDatasetHelper;
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
public class JSATClassifier
  extends AbstractClassifier {

  public final static String CLASSIFIER = "classifier";

  /** the jsat classifier to use. */
  protected Classifier m_Classifier = getDefaultClassifier();

  /**
   * Returns a string describing classifier
   *
   * @return a description suitable for displaying in the explorer/experimenter
   *         gui
   */
  public String globalInfo() {
    return "Wrapper around a JSAT classifier.";
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
      "\tThe JSAT classifier to use.\n"
	+ "\t(default: " + getDefaultClassifier().getClass().getName() + ")",
      CLASSIFIER, 1, "-" + CLASSIFIER + " <classname + options>"));

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
    String tmpStr = Utils.getOption(CLASSIFIER, options);
    if (tmpStr.isEmpty())
      setClassifier(getDefaultClassifier());
    else
      setClassifier((Classifier) proc.fromCommandline(tmpStr));
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
      result.add("-" + CLASSIFIER);
      result.add(proc.toCommandline(getClassifier()));
    }
    catch (Exception e) {
      System.err.println("Failed to retrieve commandline for classifier: " + e);
      e.printStackTrace();
    }

    Collections.addAll(result, super.getOptions());

    return result.toArray(new String[result.size()]);
  }

  /**
   * Returns the default classification algorithm.
   *
   * @return		the default
   */
  protected Classifier getDefaultClassifier() {
    return new ID3();
  }

  /**
   * Sets the classification algorithm.
   *
   * @param value	the algorithm
   */
  public void setClassifier(Classifier value) {
    m_Classifier = value;
  }

  /**
   * Returns the classification algorithm.
   *
   * @return		the algorithm
   */
  public Classifier getClassifier() {
    return m_Classifier;
  }

  /**
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String classifierTipText() {
    return "The JSAT classifier to use.";
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
    result.enable(Capability.NOMINAL_CLASS);
    result.enable(Capability.MISSING_CLASS_VALUES);

    return result;
  }

  /**
   * Performs predictions.
   *
   * @param instance	the instance to classify
   * @return		the class distribution
   * @throws Exception	if prediction fails
   */
  @Override
  public double[] distributionForInstance(Instance instance) throws Exception {
    double[]		result;
    CategoricalResults 	dist;
    DataPoint		data;
    int			i;

    // convert into JSAT row
    // TODO
    data = null;

    dist = m_Classifier.classify(data);
    result = new double[dist.size()];
    for (i = 0; i < dist.size(); i++)
      result[i] = dist.getProb(i);

    return result;
  }

  /**
   * Builds the classifier on the dataset.
   *
   * @param instances	the dataset to use
   * @throws Exception	if the build fails
   */
  @Override
  public void buildClassifier(Instances instances) throws Exception {
    getCapabilities().testWithFail(instances);

    instances = new Instances(instances);
    instances.deleteWithMissingClass();

    // convert into JSAT dataset
    ClassificationDataSet dataset = (ClassificationDataSet) JSATDatasetHelper.toDataSet(instances, null);

    // build
    m_Classifier.trainC(dataset);
  }
}

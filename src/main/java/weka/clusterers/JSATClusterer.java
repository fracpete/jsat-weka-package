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
 * JSATClusterer.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.clusterers;

import jsat.DataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.Clusterer;
import jsat.clustering.kmeans.NaiveKMeans;
import nz.ac.waikato.cms.jenericcmdline.DefaultProcessor;
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
public class JSATClusterer
  extends AbstractClusterer {

  public final static String REGRESSOR = "clusterer";

  /** the jsat clusterer to use. */
  protected Clusterer m_Clusterer = getDefaultClusterer();

  /**
   * Returns a string describing classifier
   *
   * @return a description suitable for displaying in the explorer/experimenter
   *         gui
   */
  public String globalInfo() {
    return "Wrapper around a JSAT clusterer.";
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
      "\tThe JSAT clusterer to use.\n"
	+ "\t(default: " + getDefaultClusterer().getClass().getName() + ")",
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
      setClusterer(getDefaultClusterer());
    else
      setClusterer((Clusterer) proc.fromCommandline(tmpStr));
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
      result.add(proc.toCommandline(getClusterer()));
    }
    catch (Exception e) {
      System.err.println("Failed to retrieve commandline for clusterer: " + e);
      e.printStackTrace();
    }

    Collections.addAll(result, super.getOptions());

    return result.toArray(new String[result.size()]);
  }

  /**
   * Returns the default clustering algorithm.
   *
   * @return		the default
   */
  protected Clusterer getDefaultClusterer() {
    return new NaiveKMeans();
  }

  /**
   * Sets the clustering algorithm.
   *
   * @param value	the algorithm
   */
  public void setClusterer(Clusterer value) {
    m_Clusterer = value;
  }

  /**
   * Returns the clustering algorithm.
   *
   * @return		the algorithm
   */
  public Clusterer getClusterer() {
    return m_Clusterer;
  }

  /**
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String clustererTipText() {
    return "The JSAT clusterer to use.";
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
    result.enable(Capability.NO_CLASS);

    return result;
  }

  /**
   * Classifies a given instance. Either this or distributionForInstance() needs
   * to be implemented by subclasses.
   *
   * @param instance the instance to be assigned to a cluster
   * @return the number of the assigned cluster as an integer
   * @throws Exception if instance could not be clustered successfully
   */
  @Override
  public int clusterInstance(Instance instance) throws Exception {
    DataSet			data;
    List<List<DataPoint>>	result;

    // convert into JSAT dataset with single row
    // TODO
    data = null;

    result = m_Clusterer.cluster(data);

    return -1;
  }

  /**
   * Returns the number of clusters.
   *
   * @return the number of clusters generated for a training dataset.
   * @exception Exception if number of clusters could not be returned
   *              successfully
   */
  @Override
  public int numberOfClusters() throws Exception {
    // TODO
    return 0;
  }

  /**
   * Builds the clusterer on the dataset.
   *
   * @param instances	the dataset to use
   * @throws Exception	if the build fails
   */
  @Override
  public void buildClusterer(Instances instances) throws Exception {
    getCapabilities().testWithFail(instances);

    instances = new Instances(instances);

    // convert into JSAT dataset
    DataSet dataset = JSATDatasetHelper.toDataSet(instances, null);

    // build
    m_Clusterer.cluster(dataset);
  }
}

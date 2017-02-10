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
 * JSATDatasetHelperTest.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import jsat.ARFFLoader;
import jsat.DataSet;
import jsat.SimpleDataSet;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Tests {@link JSATDatasetHelper}.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class JSATDatasetHelperTest
  extends TestCase {

  /**
   * Returns a reader for the dataset.
   *
   * @param dataset	the dataset, no path
   * @return		the reader
   * @throws Exception	if instantiation of reader fails
   */
  protected BufferedReader getReader(String dataset) throws Exception {
    return new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("weka/core/" + dataset)));
  }

  /**
   * Test the {@link JSATDatasetHelper#toInstances(DataSet)} method.
   */
  public void testToInstances() throws Exception {
    DataSet 		input;
    Instances 		output;
    JSATRegression 	reg;

    reg = new JSATRegression("-toInst");

    input  = ARFFLoader.loadArffFile(getReader("anneal.arff"));
    output = JSATDatasetHelper.toInstances(input);
    reg.println("\n");
    reg.println("*****************");
    reg.println("* without class *");
    reg.println("*****************");
    reg.println("\n");
    reg.println(output.toString());

    input  = ARFFLoader.loadArffFile(getReader("anneal.arff"));
    input  = ((SimpleDataSet) input).asClassificationDataSet(input.getNumCategoricalVars() - 1);
    output = JSATDatasetHelper.toInstances(input);
    reg.println("\n");
    reg.println("**************************");
    reg.println("* with categorical class *");
    reg.println("**************************");
    reg.println("\n");
    reg.println(output.toString());

    input  = ARFFLoader.loadArffFile(getReader("cpu.arff"));
    input  = ((SimpleDataSet) input).asRegressionDataSet(input.getNumNumericalVars() - 1);
    output = JSATDatasetHelper.toInstances(input);
    reg.println("\n");
    reg.println("**********************");
    reg.println("* with numeric class *");
    reg.println("**********************");
    reg.println("\n");
    reg.println(output.toString());

    reg.diff();
  }

  /**
   * Test the {@link JSATDatasetHelper#toDataSet(Instances, List)} method.
   */
  public void testToDataSet() throws Exception {
    // TODO
  }

  public static Test suite() {
    return new TestSuite(JSATDatasetHelperTest.class);
  }

  public static void main(String[] args){
    TestRunner.run(suite());
  }
}

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
 * JSATToWekaTest.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import jsat.ARFFLoader;
import jsat.DataSet;
import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Tests {@link JSATToWeka} converter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class JSATToWekaTest
  extends TestCaseWithReader {

  /**
   * Test the {@link JSATToWeka#convertDataset(DataSet)} method.
   */
  public void testConvertDataset() throws Exception {
    DataSet 		input;
    Instances 		output;
    JSATRegression 	reg;
    JSATToWeka		conv;

    conv = new JSATToWeka();
    reg  = new JSATRegression(conv.getClass(), null);

    input  = ARFFLoader.loadArffFile(getReader("anneal.arff"));
    conv.initialize(input);
    output = conv.convertDataset(input);
    reg.println("\n");
    reg.println("*****************");
    reg.println("* without class *");
    reg.println("*****************");
    reg.println("\n");
    reg.println(output.toString());

    input  = ARFFLoader.loadArffFile(getReader("anneal.arff"));
    input  = ((SimpleDataSet) input).asClassificationDataSet(input.getNumCategoricalVars() - 1);
    conv.initialize(input);
    output = conv.convertDataset(input);
    reg.println("\n");
    reg.println("**************************");
    reg.println("* with categorical class *");
    reg.println("**************************");
    reg.println("\n");
    reg.println(output.toString());

    input  = ARFFLoader.loadArffFile(getReader("cpu.arff"));
    input  = ((SimpleDataSet) input).asRegressionDataSet(input.getNumNumericalVars() - 1);
    conv.initialize(input);
    output = conv.convertDataset(input);
    reg.println("\n");
    reg.println("**********************");
    reg.println("* with numeric class *");
    reg.println("**********************");
    reg.println("\n");
    reg.println(output.toString());

    reg.diff();
  }

  /**
   * Tests the {@link JSATToWeka#convertRow(DataPoint)} method.
   */
  public void testConvertRow() {
    // TODO
  }

  public static Test suite() {
    return new TestSuite(JSATToWekaTest.class);
  }

  public static void main(String[] args){
    TestRunner.run(suite());
  }
}

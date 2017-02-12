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
 * WekaToJSATTest.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import jsat.DataSet;
import jsat.classifiers.DataPoint;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Tests {@link WekaToJSAT} converter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class WekaToJSATTest
  extends TestCaseWithIO {

  /**
   * Turns the dataset into a string.
   *
   * @param dataset	the dataset
   * @return		the generated string
   */
  protected String toString(DataSet dataset) {
    StringBuilder	result;
    int			i;

    result = new StringBuilder();
    for (i = 0; i < dataset.getSampleSize(); i++) {
      if (i > 0)
	result.append("\n");
      result.append(dataset.getDataPoint(i).toString());
    }

    return result.toString();
  }

  /**
   * Test the {@link WekaToJSAT#convertDataset(Instances)} method.
   */
  public void testConvertDataset() throws Exception {
    Instances 		input;
    DataSet 		output;
    JSATRegression 	reg;
    WekaToJSAT		conv;

    conv = new WekaToJSAT();
    reg  = new JSATRegression(conv.getClass(), "-dataset");

    input  = DataSource.read(getInputStream("anneal.arff"));
    conv.initialize(input);
    output = conv.convertDataset(input);
    reg.println("\n");
    reg.println("*****************");
    reg.println("* without class *");
    reg.println("*****************");
    reg.println("\n");
    reg.println(toString(output));

    input  = DataSource.read(getInputStream("anneal.arff"));
    input.setClassIndex(input.numAttributes() - 1);
    conv.initialize(input);
    output = conv.convertDataset(input);
    reg.println("\n");
    reg.println("**************************");
    reg.println("* with categorical class *");
    reg.println("**************************");
    reg.println("\n");
    reg.println(toString(output));

    input  = DataSource.read(getInputStream("cpu.arff"));
    input.setClassIndex(input.numAttributes() - 1);
    conv.initialize(input);
    output = conv.convertDataset(input);
    reg.println("\n");
    reg.println("**********************");
    reg.println("* with numeric class *");
    reg.println("**********************");
    reg.println("\n");
    reg.println(toString(output));

    reg.diff();
  }

  /**
   * Tests the {@link WekaToJSAT#convertRow(Instance)} method.
   */
  public void testConvertRow() throws Exception {
    Instances 		input;
    DataPoint 		output;
    JSATRegression 	reg;
    WekaToJSAT		conv;

    conv = new WekaToJSAT();
    reg  = new JSATRegression(conv.getClass(), "-row");

    input  = DataSource.read(getInputStream("anneal.arff"));
    conv.initialize(input);
    output = conv.convertRow(input.instance(0));
    reg.println(output.toString());

    reg.diff();
  }

  public static Test suite() {
    return new TestSuite(WekaToJSATTest.class);
  }

  public static void main(String[] args){
    TestRunner.run(suite());
  }
}

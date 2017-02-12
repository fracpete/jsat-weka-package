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
 * TestCaseWithIO.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Ancestor for test cases that give easy access to {@link java.io.Reader}
 * and {@link java.io.InputStream} objects.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class TestCaseWithIO
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
   * Returns a stream for the dataset.
   *
   * @param dataset	the dataset, no path
   * @return		the stream
   * @throws Exception	if instantiation of stream fails
   */
  protected InputStream getInputStream(String dataset) throws Exception {
    return ClassLoader.getSystemResourceAsStream("weka/core/" + dataset);
  }
}

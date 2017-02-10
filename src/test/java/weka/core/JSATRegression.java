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
 * JSATRegression.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import weka.test.Regression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Specicalized regression helper.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class JSATRegression
  extends Regression {

  /** Reference files have this extension. */
  protected static final String FILE_EXTENSION = ".ref";

  /**
   * The name of the system property that can be used to override the location
   * of the reference root.
   */
  protected static final String ROOT_PROPERTY = "weka.test.Regression.root";

  /** Default root location, relative to the users home direcory. */
  protected static final String DEFAULT_ROOT = "src/test/resources";

  /** Stores the root location under which reference files are stored. */
  protected static File ROOT;

  /** Stores the output to be compared against the reference. */
  protected final StringBuffer m_Output;

  /** The file where the reference output is (or will be) located */
  protected final File m_RefFile;

  /**
   * Creates a new <code>JSATRegression</code> instance.
   */
  public JSATRegression(String suffix) {
    super(JSATDatasetHelper.class);
    String relative = JSATDatasetHelper.class.getName().replace('.', File.separatorChar) + suffix + FILE_EXTENSION;
    m_RefFile = new File(getRoot(), relative);
    m_Output = new StringBuffer();
  }

  /**
   * Returns a <code>File</code> corresponding to the root of the reference
   * tree.
   *
   * @return a <code>File</code> giving the root of the reference tree.
   */
  public static File getRoot() {
    if (ROOT == null) {
      String root = System.getProperty(ROOT_PROPERTY);
      if (root == null) {
	root = System.getProperty("user.dir");
	ROOT = new File(root, DEFAULT_ROOT);
      }
      else {
	ROOT = new File(root);
      }
    }
    return ROOT;
  }

  /**
   * Adds some output to the current regression output. The accumulated output
   * will provide the material for the regression comparison.
   *
   * @param output a <code>String</code> that forms part of the regression test.
   */
  public void println(String output) {
    m_Output.append(output).append('\n');
  }

  /**
   * Returns the difference between the current output and the reference
   * version.
   *
   * @return a <code>String</code> value illustrating the differences. If this
   *         string is empty, there are no differences. If null is returned,
   *         there was no reference output found, and the current output has
   *         been written as the reference.
   * @exception IOException if an error occurs during reading or writing of the
   *              reference file.
   */
  public String diff() throws IOException {
    try {
      String reference = readReference();
      return diff(reference, m_Output.toString());
    }
    catch (FileNotFoundException fnf) {
      // No, write out the current output
      writeAsReference();
      return null;
    }
  }

  /**
   * Returns the difference between two strings, Will be the empty string if
   * there are no difference.
   *
   * @param reference a <code>String</code> value
   * @param current a <code>String</code> value
   * @return a <code>String</code> value describing the differences between the
   *         two input strings. This will be the empty string if there are no
   *         differences.
   */
  protected String diff(String reference, String current) {
    if (reference.equals(current)) {
      return "";
    }
    else {
      // Should do something more cunning here, like try to isolate the
      // actual differences. We could also try calling unix diff utility
      // if it exists.
      StringBuffer diff = new StringBuffer();
      diff.append("+++ Reference: ").append(m_RefFile).append(" +++\n")
	.append(reference).append("+++ Current +++\n").append(current)
	.append("+++\n");
      return diff.toString();
    }
  }

  /**
   * Reads the reference output from a file and returns it as a String
   *
   * @return a <code>String</code> value containing the reference output
   * @exception IOException if an error occurs.
   */
  protected String readReference() throws IOException {
    Reader r = new BufferedReader(new FileReader(m_RefFile));
    StringBuffer ref = new StringBuffer();
    char[] buf = new char[5];
    for (int read = r.read(buf); read > 0; read = r.read(buf))
      ref.append(new String(buf, 0, read));
    r.close();
    return ref.toString();
  }

  /**
   * Writes the current output as the new reference. Normally this method is
   * called automatically if diff() is called with no existing reference
   * version. You may wish to call it explicitly if you know you want to create
   * the reference version.
   *
   * @exception IOException if an error occurs.
   */
  public void writeAsReference() throws IOException {
    File parent = m_RefFile.getParentFile();
    if (!parent.exists())
      parent.mkdirs();
    Writer w = new BufferedWriter(new FileWriter(m_RefFile));
    w.write(m_Output.toString());
    w.close();
  }
}

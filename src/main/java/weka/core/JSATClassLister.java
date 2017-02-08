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
 * JSATClassLister.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import nz.ac.waikato.cms.locator.ClassLister;

/**
 * ClassLister for Supernova.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class JSATClassLister
  extends ClassLister {

  /**
   * Initializes the class lister.
   */
  public JSATClassLister() {
    super();

    m_Packages  = load("weka/core/JSATClassLister.props");
    m_Blacklist = load("weka/core/JSATClassLister.blacklist");

    initialize();
  }

  /**
   * Returns the singleton instance of the class lister.
   *
   * @return		the singleton
   */
  public static synchronized ClassLister getSingleton() {
    if (m_Singleton == null)
      m_Singleton = new JSATClassLister();

    return m_Singleton;
  }

  /**
   * Just outputs all the managed superclasses and the associated classes.
   *
   * @param args	ignored
   */
  public static void main(String[] args) {
    System.out.println(JSATClassLister.getSingleton());
  }
}

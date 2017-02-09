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
 * JSATInit.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

import weka.gui.GenericObjectEditor;

import java.util.HashSet;
import java.util.Set;

/**
 * Performs initializations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class JSATInit {

  static {
    GenericObjectEditor.determineClasses();
    for (String superclass: JSATClassLister.getSingleton().getSuperclasses()) {
      Class[] classes = JSATClassLister.getSingleton().getClasses(superclass);
      Set<String> registered = PluginManager.getPluginNamesOfType(superclass);
      // remove invalid classes
      if (registered != null) {
	Set<String> valid = new HashSet<>();
	for (Class cls: classes)
	  valid.add(cls.getName());
	registered.removeAll(valid);
	for (String cls : registered)
	  PluginManager.addToDisabledList(cls);
      }
      else {
	for (Class cls: classes)
	  PluginManager.addPlugin(superclass, superclass, cls.getName());
      }
    }
  }
}

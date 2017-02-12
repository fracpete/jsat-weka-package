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
 * AbstractJSATConversion.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package weka.core;

/**
 * Ancestor for conversions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 * @param <FD> "from dataset" type
 * @param <TD> "to dataset" type
 * @param <FR> "from row" type
 * @param <TR> "to row" type
 */
public abstract class AbstractJSATConversion<FD,TD,FR,TR> {

  /**
   * Initializes the conversion.
   *
   * @param dataset	the from dataset (to get structure etc)
   */
  public abstract void initialize(FD dataset);

  /**
   * Converts the dataset.
   *
   * @param dataset	the dataset to convert
   * @return		the converted dataset
   * @see		#initialize(Object)
   */
  public abstract TD convertDataset(FD dataset);

  /**
   * Converts the data row.
   *
   * @param row		the row to convert
   * @return		the converted row
   * @see		#initialize(Object)
   */
  public abstract TR convertRow(FR row);
}

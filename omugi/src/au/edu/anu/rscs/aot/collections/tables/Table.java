/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          * 
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  OMUGI is an API to implement graphs, as described by graph theory,    *
 *  but also as more commonly used in computing - e.g. dynamic graphs.    *
 *  It interfaces with JGraphT, an API for mathematical graphs, and       *
 *  GraphStream, an API for visual graphs.                                *
 *                                                                        *
 **************************************************************************                                       
 *  This file is part of OMUGI (One More Ultimate Graph Implementation).  *
 *                                                                        *
 *  OMUGI is free software: you can redistribute it and/or modify         *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  OMUGI is distributed in the hope that it will be useful,              *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with OMUGI.  If not, see <https://www.gnu.org/licenses/gpl.html>*
 *                                                                        *
 **************************************************************************/
package au.edu.anu.rscs.aot.collections.tables;

import fr.ens.biologie.generic.DataContainer;
import fr.ens.biologie.generic.Showable;
import fr.ens.biologie.generic.Sizeable;
import fr.ens.biologie.generic.Textable;

/**
 * An interface for multidimensional access to data.
 * Basically there are three ways to access the data
 * 1 by multidimensional integer indexes (eg (i1,i2,i3)
 * 2 by flat index, i.e. a single index i = f(i1,i2,i3,n1,n2,n3) where nis are the sizes of each dimension
 * 3 by multidimensional non-integer indexes (this is optional and decided at construction time)
 * 
 * @author J. Gignoux - 15 févr. 2017
 *
 */
// NB: a template Table<T> is not used because of a performance loss with primitive type wrappers
public interface Table 
		extends DataContainer, Sizeable, Textable, Showable {
	
	/** returns the number of dimensions (=number of Dimensioners) of this storage */
	public int ndim();
	
	/** returns the size of dimension i */
	public int size(int index);
	
	/** returns the Dimensioners of this storage */
	public Dimensioner[] getDimensioners();
		
	/** returns the flat index matching a ndim-tuple of indexes */
	public int getFlatIndexByInt(int... indexes);
	
	/** returns the flat index matching a ndim-tuple of non integer indexes (if any) */
	public int getFlatIndex(Object... indexes);
	
	/** returns a ndim-tuple of indices matching a flat index */
	public int[] getIndexes(int flatIndex);
	
	/** returns a String description of an element of this storage, accessed by flat index */
	public String elementToString(int flatIndex);
	
	/** returns the type of the table elements */	
	public String elementClassName();
	public String elementSimpleClassName();
	
//	/** returns the full content of this element as a String for saving into text files*/
//	public String elementToToken(int flatIndex);
	
	public abstract Table copy (Table from);
	
	public abstract Class<?> contentType();

	
	// Default setters for descendants - all do nothing by default
	// short
	public default void setByInt(short value, int... indexes) {}
	public default void set(short value, Object... indexes) {}
	public default void setWithFlatIndex(short value, int index) {}
	// int
	public default void setByInt(int value, int... indexes) {}
	public default void set(int value, Object... indexes) {}
	public default void setWithFlatIndex(int value, int index) {}
	// long
	public default void setByInt(long value, int... indexes) {}
	public default void set(long value, Object... indexes) {}
	public default void setWithFlatIndex(long value, int index) {}
	// byte
	public default void setByInt(byte value, int... indexes) {}
	public default void set(byte value, Object... indexes) {}
	public default void setWithFlatIndex(byte value, int index) {}
	// boolean
	public default void setByInt(boolean value, int... indexes) {}
	public default void set(boolean value, Object... indexes) {}
	public default void setWithFlatIndex(boolean value, int index) {}
	// double
	public default void setByInt(double value, int... indexes) {}
	public default void set(double value, Object... indexes) {}
	public default void setWithFlatIndex(double value, int index) {}
	// float
	public default void setByInt(float value, int... indexes) {}
	public default void set(float value, Object... indexes) {}
	public default void setWithFlatIndex(float value, int index) {}
	// char
	public default void setByInt(char value, int... indexes) {}
	public default void set(char value, Object... indexes) {}
	public default void setWithFlatIndex(char value, int index) {}
	// String
	public default void setByInt(String value, int... indexes) {}
	public default void set(String value, Object... indexes) {}
	public default void setWithFlatIndex(String value, int index) {}

	// SIZEABLE
	
	/** returns the total size of this storage (in number of items) */
//	public int getFlatSize();

}

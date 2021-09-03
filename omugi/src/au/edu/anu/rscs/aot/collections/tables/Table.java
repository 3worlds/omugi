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

import static fr.cnrs.iees.io.parsing.TextGrammar.DIM_BLOCK_DELIMITERS;
import static fr.cnrs.iees.io.parsing.TextGrammar.DIM_ITEM_SEPARATOR;
import static fr.cnrs.iees.io.parsing.TextGrammar.TABLE_BLOCK_DELIMITERS;
import static fr.cnrs.iees.io.parsing.TextGrammar.TABLE_ITEM_SEPARATOR;

import fr.cnrs.iees.OmugiException;
import fr.ens.biologie.generic.DataContainer;
import fr.ens.biologie.generic.SaveableAsText;
import fr.ens.biologie.generic.Showable;
import fr.ens.biologie.generic.Sizeable;
import fr.ens.biologie.generic.Textable;

/**
 * <p>
 * An interface for multidimensional access to data. Basically there are three
 * ways to access the data
 * </p>
 * <ol>
 * <li>by multidimensional integer indexes (eg
 * (i<sub>1</sub>,i<sub>2</sub>,i<sub>3</sub>);</li>
 * <li>by <em>flat</em> index, i.e. a single index k computed as k =
 * f(i<sub>1</sub>,i<sub>2</sub>,i<sub>3</sub>
 * ,n<sub>1</sub>,n<sub>2</sub>,n<sub>3</sub>) where n<sub>i</sub>'s are the
 * sizes of each dimension and f is a clever function doing the computation for
 * you;</li>
 * <li>by multidimensional non-integer indexes (this is optional and decided at
 * construction time).</li>
 * </ol>
 * 
 * @author J. Gignoux - 15 févr. 2017
 *
 */
// NB: a template Table<T> is not used because of a performance loss with primitive type wrappers
public interface Table extends DataContainer, Sizeable, Textable, Showable, SaveableAsText {

	// index for type of delimiters used in saving to files (cf. Textable interface)
	public static int TABLEix = 0;
	public static int DIMix = 1;

	/**
	 * The default separators between values used to convert tables to/from text. cf
	 * {@link fr.cnrs.iees.io.parsing.TextGrammar TextGrammar}.
	 * 
	 * @return the separator for dimensions and for data in a 2-cell array of char
	 */
	public static char[] getDefaultSeparators() {
		char[] isep = new char[2];
		isep[Table.DIMix] = DIM_ITEM_SEPARATOR;
		isep[Table.TABLEix] = TABLE_ITEM_SEPARATOR;
		return isep;
	}

	/**
	 * The default block delimiters between values used to convert tables to/from
	 * text. cf {@link fr.cnrs.iees.io.parsing.TextGrammar TextGrammar}.
	 * 
	 * @return the block delimiter pairs for dimension and data blocks as a 2-pair
	 *         array of char
	 */
	public static char[][] getDefaultDelimiters() {
		char[][] bdel = new char[2][2];
		bdel[Table.DIMix] = DIM_BLOCK_DELIMITERS;
		bdel[Table.TABLEix] = TABLE_BLOCK_DELIMITERS;
		return bdel;
	}

	/**
	 * returns the number of dimensions (=number of Dimensioners) of this table
	 * 
	 * @return the number of dimensions
	 */
	public int ndim();

	/**
	 * returns the size of dimension passed as argument (no overflow check)
	 * 
	 * @param index the dimension index
	 * @return the size of the index<sup>th</sup> dimension
	 */
	public int size(int index);

	/**
	 * returns the dimensioners of this table
	 * 
	 * @return an array of {@code Dimensioner}s
	 */
	public Dimensioner[] getDimensioners();

	/**
	 * returns the flat index matching a ndim-tuple of indexes (no overflow check)
	 * 
	 * @param indexes a ndim-tuple of indexes specifying one cell of the table
	 * @return a single index specifying the same cell as the argument
	 */
	public int getFlatIndexByInt(int... indexes);

	/**
	 * returns the flat index matching a ndim-tuple of non integer indexes (if any)
	 * 
	 * @param indexes a ndim-tuple of indexes specifying one cell of the table
	 * @return a single index specifying the same cell as the argument
	 */
	public int getFlatIndex(Object... indexes);

	/**
	 * returns a ndim-tuple of integer indices matching a flat index (no overflow
	 * check)
	 * 
	 * @param flatIndex a single index specifying one cell of the table
	 * @return a ndim-tuple of indexes specifying the same cell as the argument
	 */
	public int[] getIndexes(int flatIndex);

	/**
	 * returns a String description of an element of this storage, accessed by flat
	 * index
	 * 
	 * @param flatIndex a single index specifying one cell of the table
	 * @return this cell content description as a String
	 */
	public String elementToString(int flatIndex);

	/**
	 * returns the type of the table elements
	 * 
	 * @return the fully qualified class name of the table elements
	 */
	public String elementClassName();

	/**
	 * returns the type of the table elements
	 * 
	 * @return the simple class name of the table elements
	 */
	public String elementSimpleClassName();

	/**
	 * Copy all values of the argument into this instance. Both tables must have
	 * exactly the same dimensioners in the same order. CAUTION: no dimension check
	 * is performed.
	 * 
	 * @param from the table to copy
	 * @return this instance for agile programming
	 */
	public abstract Table copy(Table from);

	/**
	 * returns the type of the table elements
	 * 
	 * @return the table element class object
	 */
	public abstract Class<?> contentType();

	// Default setters for descendants - all do nothing by default
	// short
	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void setByInt(short value, int... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void set(short value, Object... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
	public default void setWithFlatIndex(short value, int index) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	// int
	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void setByInt(int value, int... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void set(int value, Object... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
	public default void setWithFlatIndex(int value, int index) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	// long
	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void setByInt(long value, int... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void set(long value, Object... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
	public default void setWithFlatIndex(long value, int index) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	// byte
	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void setByInt(byte value, int... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void set(byte value, Object... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
	public default void setWithFlatIndex(byte value, int index) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	// boolean
	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void setByInt(boolean value, int... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void set(boolean value, Object... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
	public default void setWithFlatIndex(boolean value, int index) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	// double
	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void setByInt(double value, int... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void set(double value, Object... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
	public default void setWithFlatIndex(double value, int index) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	// float
	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void setByInt(float value, int... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void set(float value, Object... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
	public default void setWithFlatIndex(float value, int index) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	// char
	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void setByInt(char value, int... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void set(char value, Object... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
	public default void setWithFlatIndex(char value, int index) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	// String
	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void setByInt(String value, int... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public default void set(String value, Object... indexes) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
	public default void setWithFlatIndex(String value, int index) {
		throw new OmugiException(new Object() {
		}.getClass().getEnclosingMethod().toGenericString() + " not implemented for Table of "
				+ contentType().getSimpleName());
	}

}

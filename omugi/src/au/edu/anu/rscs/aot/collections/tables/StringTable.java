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

import com.google.common.base.Strings;

import fr.cnrs.iees.OmugiException;
import fr.ens.biologie.generic.DataContainer;

/** 
 * A multidimensional table of {@code String}s. 
 * 
 * @author Shayne Flint - looong ago<br/>
 * modified by JG 15/2/2017 to account for {@link Table} and 
 * {@link fr.ens.biologie.generic.DataContainer DataContainer} interfaces * 
 *
 */
public class StringTable extends TableAdapter {

	protected String[] data;

	/**
	 * Constructor with dimensioners
	 * 
	 * @param dimensions dimensioners
	 */
	public StringTable(Dimensioner... dimensions) {
		super(dimensions);
		data = new String[size()];
	}

	@Override
	public StringTable clone() {
		StringTable result = cloneStructure();
		result.copy(this);
		return result;
	}

	@Override
	public StringTable cloneStructure() {
		StringTable result = new StringTable(getDimensioners());
		return result;
	}

	/**
	 * Clone this table and fills it with its argument
	 * 
	 * @param initialValue the value to fill the table with
	 * @return the new table
	 */
	public StringTable cloneStructure(String initialValue) {
		StringTable result = cloneStructure();
		result.fillWith(initialValue);
		return result;
	}

	@Override
	public String elementToString(int flatIndex) {
		return data[flatIndex];
	}

	@Override
	public String elementClassName() {
		return String.class.getName();
	}

	@Override
	public String elementSimpleClassName() {
		return String.class.getSimpleName();
	}

	@Override
	public DataContainer clear() {
		fillWith("");
		return this;
	}

	/** fills this container with a single {@code String} value */
	public StringTable fillWith(String value) {
		for (int i = 0; i < data.length; i++)
			data[i] = value;
		return this;
	}

	@Override
	public DataContainer fillWith(Object value) {
		return fillWith((String) value);
	}

	@Override
	public TableAdapter copy(Table from) {
		if (String.class.isAssignableFrom(from.contentType())) {
			StringTable st = (StringTable) from;
			for (int i = 0; i < data.length; i++)
				data[i] = st.data[i];
		}
		return this;
	}

	@Override
	public Class<?> contentType() {
		return String.class;
	}

	/** get the value stored at <em>indexes</em>*/
	public String getByInt(int... indexes) {
		return data[getFlatIndexByInt(indexes)];
	}

	@Override
	public void setByInt(String value, int... indexes) {
		data[getFlatIndexByInt(indexes)] = value;
	}

	/** get the value stored at flat <em>index</em>*/
	public String getWithFlatIndex(int index) {
		return data[index];
	}

	@Override
	public void setWithFlatIndex(String value, int index) {
		data[index] = value;
	}

	/** get the value stored at <em>indexes</em>*/
	public String get(Object... indexes) {
		return data[getFlatIndex(indexes)];
	}

	@Override
	public void set(String value, Object... indexes) {
		data[getFlatIndex(indexes)] = value;
	}

	/**
	 * Checks if this instance contains the value passed as argument
	 * 
	 * @param str the value to check
	 * @return {@code true} if value is found in the table, {@code false} otherwise
	 */
	// maybe this could be generalized but for many tables it is not very useful
	public boolean contains(String str) {
		for (int i = 0; i < flatSize; i++) {
			String s = getWithFlatIndex(i);
			// here, 'contains' means the table has a string *equal* to str
			if (s.equals(str))
				return true;
		}
		return false;
	}

	// These must be overriden to properly quote strings

	@Override
	public String toString() {
		int MAXITEMS = 10;
		StringBuilder sb = new StringBuilder(1024);
		sb.append("([").append(dimensioners[0].getLength());
		for (int i = 1; i < dimensioners.length; i++)
			sb.append(",").append(dimensioners[i].getLength());
		sb.append("]");
		for (int i = 0; i < Math.min(flatSize, MAXITEMS); i++)
			sb.append(",\"").append(elementToString(i)).append('"');
		if (flatSize > MAXITEMS)
			sb.append("...");
		sb.append(")");
		return sb.toString().replaceFirst(",", "");
	}

	/**
	 * Construct an instance from a {@code String} previously produced with 
	 * {@link TableAdapter#toSaveableString() toSaveableString()}. Uses the default block delimiters
	 * and item separators defined in {@link Table#getDefaultDelimiters()} 
	 * and {@link Table#getDefaultSeparators()}.
	 * 
	 * @param value the {@code String} to read data from
	 * @return the new instance
	 */
	public static StringTable valueOf(String value) {
		return StringTable.valueOf(value, Table.getDefaultDelimiters(), Table.getDefaultSeparators());
	}

	/**
	 * Construct an instance from a {@code String} previously produced with 
	 * {@link TableAdapter#toSaveableString toSaveableString(...)}.
	 * 
	 * @param value the {@code String} to read data from
	 * @param bdel block delimiters to use
	 * @param isep item separators to use
	 * @return the new instance
	 */
	public static StringTable valueOf(String value, char[][] bdel, char[] isep) {
		value = value.trim();
		if ((value == null) || value.isBlank() || value.isEmpty())
			return null;
		String ss = TableAdapter.getBlockContent(value, bdel[TABLEix]);
		String d = ss.substring(0, ss.indexOf(bdel[DIMix][BLOCK_CLOSE]) + 1);
		d = d.trim();
		StringTable result = new StringTable(readDimensioners(d, bdel[DIMix], isep[DIMix]));
		result.clear();
		ss = ss.substring(ss.indexOf(bdel[DIMix][BLOCK_CLOSE]) + 1);
		StringBuilder sb = new StringBuilder();
		int n = 0;
		boolean inquote = false;
		for (int i = 0; i < ss.length(); i++) {
			char c = ss.charAt(i);
			if (c == DOUBLEQUOTE)
				inquote = !inquote;
			else if (inquote)
				sb.append(c);
			else if (!inquote) {
				if (c == isep[TABLEix]) {
					if (n == result.flatSize - 1)
						throw new OmugiException("Too many values read: table size == " + result.flatSize);
					// check for null strings
					String res = sb.toString();
					if (res.isBlank() | res.isEmpty() | res.equals("null"))
						result.data[n++] = "";
					else
						result.data[n++] = res;
					sb = new StringBuilder();
				} else if (!Character.isWhitespace(c))// ignore white space when outside quotes
					sb.append(c);
			}
		}
		// check for null strings
		String res = sb.toString();
		if (res.isBlank() | res.isEmpty() | res.equals("null"))
			result.data[n++] = "";
		else
			result.data[n++] = res;
		return result;
	}

	@Override
	public String toSaveableString(char[][] bdel, char[] isep) {
		StringBuilder sb = new StringBuilder(1024);
		sb.append(bdel[TABLEix][BLOCK_OPEN]).append(bdel[DIMix][BLOCK_OPEN]).append(dimensioners[0].getLength());
		for (int i = 1; i < dimensioners.length; i++)
			sb.append(isep[DIMix]).append(dimensioners[i].getLength());
		sb.append(bdel[DIMix][BLOCK_CLOSE]);
		if (flatSize > 0)
			sb.append('"').append(Strings.nullToEmpty(elementToString(0))).append('"');
		for (int i = 1; i < flatSize; i++)
			sb.append(isep[TABLEix]).append('"')
				.append(Strings.nullToEmpty(elementToString(i)))
				.append('"');
		sb.append(bdel[TABLEix][BLOCK_CLOSE]);
		return sb.toString();
	}

}

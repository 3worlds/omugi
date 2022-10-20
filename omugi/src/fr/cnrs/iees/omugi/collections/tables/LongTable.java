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
package fr.cnrs.iees.omugi.collections.tables;

/**
 * A multidimensional table of {@code long} integers.
 * 
 * @author Shayne Flint - This code was generated by 
 * {@code fr.cnrs.iees.omugi.collections.tables/genPrimitiveClasses.py}
 *  on Thu Aug 21 10:32:35 2014<br/>
 * modified by JG 15/2/2017 to account for {@link Table} and 
 * {@link fr.cnrs.iees.omhtk.DataContainer DataContainer} interfaces
 * */ 
public class LongTable extends TableAdapter {

	protected long[] data;

	/**
	 * Constructor with dimensioners
	 * 
	 * @param dimensions dimensioners
	 */
	public LongTable(Dimensioner... dimensions) {
		super(dimensions);
		data = new long[size()];
	}

	/** get the value stored at <em>indexes</em>*/
	public long getByInt(int... indexes) {
		return data[getFlatIndexByInt(indexes)];
	}

	@Override
	public void setByInt(long value, int... indexes) {
		data[getFlatIndexByInt(indexes)] = value;
	}

	/** get the value stored at flat <em>index</em>*/
	public long getWithFlatIndex(int index) {
		return data[index];
	}

	@Override
	public void setWithFlatIndex(long value, int index) {
		data[index] = value;
	}

	/** get the value stored at <em>indexes</em>*/
	public long get(Object... indexes) {
		return data[getFlatIndex(indexes)];
	}

	@Override
	public void set(long value, Object... indexes) {
		data[getFlatIndex(indexes)] = value;
	}

	@Override
	public LongTable clone() {
		LongTable result = cloneStructure();
		result.copy(this);
		return result;
	}
	
	@Override
	public LongTable cloneStructure() {
		LongTable result = new LongTable(getDimensioners());
		return result;
	}

	/**
	 * Clone this table and fills it with its argument
	 * 
	 * @param initialValue the value to fill the table with
	 * @return the new table
	 */
	public LongTable cloneStructure(long initialValue) {
		LongTable result = cloneStructure();
		result.fillWith(initialValue);
		return result;
	}
	
	@Override
	public LongTable fillWith(long value) {
		for (int i=0; i<data.length; i++)
			data[i] = value;
		return this;
	}
	
	@Override
	public String elementToString(int flatIndex) {
		return String.valueOf(data[flatIndex]);
	}

	@Override
	public LongTable clear() {
		return fillWith(0L);
	}

	@Override
	public LongTable fillWith(Object value) {
		return fillWith(((Long)value).longValue());
	}

	@Override
	public LongTable copy(Table from) {
		if (Long.class.isAssignableFrom(from.contentType())) {
			LongTable lt = (LongTable)from;
			for (int i=0; i<data.length; i++)
				data[i] = lt.data[i];
		}
		return this;
	}

	@Override
	public String elementClassName() {
		return Long.class.getName();
	}

	@Override
	public String elementSimpleClassName() {
		return Long.class.getSimpleName();
	}

	@Override
	public Class<?> contentType() {
		return Long.class;
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
	public static LongTable valueOf(String value) {
		return LongTable.valueOf(value, Table.getDefaultDelimiters(), Table.getDefaultSeparators());
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
	public static LongTable valueOf(String value, char[][] bdel, char[] isep) {
		String ss = TableAdapter.getBlockContent(value,bdel[TABLEix]);
		String d = ss.substring(0,ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1);
		LongTable result = new LongTable(readDimensioners(d,bdel[DIMix],isep[DIMix]));
		ss = ss.substring(ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1); 
		String s = null;
		int i=0;
		while (ss.indexOf(isep[TABLEix])>0) {
			s = ss.substring(0,ss.indexOf(isep[TABLEix]));
			ss = ss.substring(ss.indexOf(isep[TABLEix])+1);
			result.data[i] = Long.valueOf(s);
			i++;
		}
		result.data[i] = Long.valueOf(ss);
		return result;
	}

}

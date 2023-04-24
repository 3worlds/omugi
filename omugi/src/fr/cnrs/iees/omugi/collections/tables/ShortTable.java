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

import java.util.Arrays;

/**
 * A multidimensional table of {@code short} integers.
 * 
 * @author Shayne Flint - This code was generated by 
 * {@code fr.cnrs.iees.omugi.collections.tables/genPrimitiveClasses.py}
 *  on Thu Aug 21 10:32:35 2014<br/>
 * modified by JG 15/2/2017 to account for {@link Table} and 
 * {@link fr.cnrs.iees.omhtk.DataContainer DataContainer} interfaces
 *
 */
public class ShortTable extends TableAdapter {

	protected short[] data;

	/**
	 * Constructor with dimensioners
	 * 
	 * @param dimensions dimensioners
	 */
	public ShortTable(Dimensioner... dimensions) {
		super(dimensions);
		data = new short[size()];
	}

	/** get the value stored at <em>indexes</em>*/
	public short getByInt(int... indexes) {
		return data[getFlatIndexByInt(indexes)];
	}

	@Override
	public void setByInt(short value, int... indexes) {
		data[getFlatIndexByInt(indexes)] = value;
	}

	/** get the value stored at flat <em>index</em>*/
	public short getWithFlatIndex(int index) {
		return data[index];
	}

	@Override
	public void setWithFlatIndex(short value, int index) {
		data[index] = value;
	}

	/** get the value stored at <em>indexes</em>*/
	public short get(Object... indexes) {
		return data[getFlatIndex(indexes)];
	}

	@Override
	public void set(short value, Object... indexes) {
		data[getFlatIndex(indexes)] = value;
	}

	@Override
	public ShortTable clone() {
		ShortTable result = cloneStructure();
		result.copy(this);
		return result;
	}
	
	@Override
	public ShortTable cloneStructure() {
		ShortTable result = new ShortTable(getDimensioners());
		return result;
	}
	
	/**
	 * Clone this table and fills it with its argument
	 * 
	 * @param initialValue the value to fill the table with
	 * @return the new table
	 */
	public ShortTable cloneStructure(short initialValue) {
		ShortTable result = cloneStructure();
		result.fillWith(initialValue);
		return result;
	}	

	@Override
	public ShortTable fillWith(Object value) {		
		return fillWith(((Short)value).shortValue());
	}

	@Override
	public ShortTable fillWith(short value) {
		Arrays.fill(data, value);
		return this;
	}
	
	@Override
	public String elementToString(int flatIndex) {
		return String.valueOf(data[flatIndex]);
	}

	@Override
	public ShortTable clear() {
		short s = 0;
		fillWith(s);
		return this;
	}

	@Override
	public ShortTable copy(Table from) {
		if (Short.class.isAssignableFrom(from.contentType())) {
			ShortTable st = (ShortTable) from;
			for (int i=0; i<data.length; i++)
				data[i] = st.data[i];
		}
		return this;
	}
	
	@Override
	public String elementClassName() {
		return Short.class.getName();
	}

	@Override
	public String elementSimpleClassName() {
		return Short.class.getSimpleName();
	}

	@Override
	public Class<?> contentType() {
		return Short.class;
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
	public static ShortTable valueOf(String value) {
		return ShortTable.valueOf(value, Table.getDefaultDelimiters(), Table.getDefaultSeparators());
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
	public static ShortTable valueOf(String value, char[][] bdel, char[] isep) {
		String ss = TableAdapter.getBlockContent(value,bdel[TABLEix]);
		String d = ss.substring(0,ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1);
		ShortTable result = new ShortTable(readDimensioners(d,bdel[DIMix],isep[DIMix]));
		ss = ss.substring(ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1); 
		String s = null;
		int i=0;
		while (ss.indexOf(isep[TABLEix])>0) {
			s = ss.substring(0,ss.indexOf(isep[TABLEix]));
			ss = ss.substring(ss.indexOf(isep[TABLEix])+1);
			result.data[i] = Short.valueOf(s);
			i++;
		}
		result.data[i] = Short.valueOf(ss);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hash==0) {
			final int prime = 31;
			hash = super.hashCode();
			hash = prime * hash + Arrays.hashCode(data);
		}
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ShortTable))
			return false;
		ShortTable other = (ShortTable) obj;
		return Arrays.equals(data, other.data);
	}

}

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

/** initial code by Shayne */
public class StringTable extends TableAdapter {
	
	protected String[] data;

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

	
	public StringTable cloneStructure(String initialValue) {
		StringTable result = cloneStructure();
		result.fillWith(initialValue);
		return result;
	}

	public static StringTable valueOf(String value, char[][] bdel, char[] isep) {
		String ss = TableAdapter.getBlockContent(value,bdel[TABLE]);
		String d = ss.substring(0,ss.indexOf(bdel[DIM][BLOCK_CLOSE])+1);
		StringTable result = new StringTable(readDimensioners(d,bdel[DIM],isep[DIM]));
		ss = ss.substring(ss.indexOf(bdel[DIM][BLOCK_CLOSE])+1); 
		String s = null;
		int i=0;
		while (ss.indexOf(isep[TABLE])>0) {
			s = ss.substring(0,ss.indexOf(isep[TABLE]));
			ss = ss.substring(ss.indexOf(isep[TABLE])+1);
			result.data[i] = s;
			i++;
		}
		result.data[i] = ss;
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
	
	public StringTable fillWith(String value) {
		for (int i=0; i<data.length; i++)
			data[i] = value;
		return this;
	}

	@Override
	public DataContainer fillWith(Object value) {
		return fillWith((String)value);
	}

	@Override
	public TableAdapter copy(Table from) {
		if (String.class.isAssignableFrom(from.contentType())) {
			StringTable st = (StringTable)from;
			for (int i=0; i<data.length; i++)
				data[i] = st.data[i];
		}
		return this;
	}

	@Override
	public Class<?> contentType() {
		return String.class;
	}

	public String getByInt(int... indexes) {
		return data[getFlatIndexByInt(indexes)];
	}

	public void setByInt(String value, int... indexes) {
		data[getFlatIndexByInt(indexes)] = value;
	}

	public String getWithFlatIndex(int index) {
		return data[index];
	}

	public void setWithFlatIndex(String value, int index) {
		data[index] = value;
	}

	public String get(Object... indexes) {
		return data[getFlatIndex(indexes)];
	}

	public void set(String value, Object... indexes) {
		data[getFlatIndex(indexes)] = value;
	}

}

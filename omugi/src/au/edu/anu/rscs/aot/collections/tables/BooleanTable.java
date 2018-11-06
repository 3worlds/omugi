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

/**
 * This code was generated by ../../codeGenerators/genPrimitiveClasses.py on Thu Aug 21 10:32:35 2014
 *
 */
 /** modified by JG 15/2/2017 to account for Table and DataContainer interfaces */
public class BooleanTable extends TableAdapter {

	protected boolean[] data;

	public BooleanTable(Dimensioner... dimensions) {
		super(dimensions);
		data = new boolean[size()];
	}

	public boolean getByInt(int... indexes) {
		return data[getFlatIndexByInt(indexes)];
	}

	public void setByInt(boolean value, int... indexes) {
		data[getFlatIndexByInt(indexes)] = value;
	}

	public boolean getWithFlatIndex(int index) {
		return data[index];
	}

	public void setWithFlatIndex(boolean value, int index) {
		data[index] = value;
	}

	// not tested because not used
	public boolean get(Object... indexes) {
		return data[getFlatIndex(indexes)];
	}

	// not tested because not used
	public void set(boolean value, Object... indexes) {
		data[getFlatIndex(indexes)] = value;
	}


	@Override
	public BooleanTable clone() {
		BooleanTable result = cloneStructure();
		result.copy(this);
		return result;
	}
	
	@Override
	public BooleanTable cloneStructure() {
		BooleanTable result = new BooleanTable(getDimensioners());
		return result;
	}

	public BooleanTable cloneStructure(boolean initialValue) {
		BooleanTable result = cloneStructure();
		result.fillWith(initialValue);
		return result;
	}
	
	public BooleanTable fillWith(boolean value) {
		for (int i=0; i<data.length; i++)
			data[i] = value;
		return this;
	}
	
	@Override
	public String elementToString(int flatIndex) {
		return String.valueOf(data[flatIndex]);
	}

	@Override
	public BooleanTable clear() {
		return fillWith(false);		
	}


	@Override
	public BooleanTable fillWith(Object value) {		
		return fillWith(((Boolean)value).booleanValue());
	}

	@Override
	public BooleanTable copy(Table from) {
		if (from.getClass().equals(getClass())) {
			BooleanTable bt = (BooleanTable)from;
			for (int i=0; i<data.length; i++)
				data[i] = bt.data[i];
		}
		return this;
	}

	@Override
	public String elementClassName() {
		return Boolean.class.getName();
	}

	@Override
	public String elementSimpleClassName() {
		return Boolean.class.getSimpleName();
	}

	@Override
	public Class<?> contentType() {
		return Boolean.class;
	}
	
	public static BooleanTable valueOf(String value, char[][] bdel, char[] isep) {
		String ss = TableAdapter.getBlockContent(value,bdel[TABLEix]);
		String d = ss.substring(0,ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1);
		BooleanTable result = new BooleanTable(readDimensioners(d,bdel[DIMix],isep[DIMix]));
		ss = ss.substring(ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1); 
		String s = null;
		int i=0;
		while (ss.indexOf(isep[TABLEix])>0) {
			s = ss.substring(0,ss.indexOf(isep[TABLEix]));
			ss = ss.substring(ss.indexOf(isep[TABLEix])+1);
			result.data[i] = Boolean.valueOf(s);
			i++;
		}
		result.data[i] = Boolean.valueOf(ss);
		return result;
	}

	
}

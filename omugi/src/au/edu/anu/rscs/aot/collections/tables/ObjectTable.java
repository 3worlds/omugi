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

import java.util.Arrays;

/** intial code from Shayne */
/** modified by JG 15/2/2017 to account for Table and DataContainer interfaces */
/** modified by JG 1/10/2018 to account for Textable interface */

// notice that by construct T cannot be primitive
@Deprecated // unfinished/flawed
public class ObjectTable<T> extends TableAdapter {

	protected Object[] data;

	public ObjectTable(Dimensioner... dimensions) {
		super(dimensions);
		data = new Object[size()];
	}

	@SuppressWarnings("unchecked")
	public T getByInt(int... indexes) {
		return (T) data[getFlatIndexByInt(indexes)];
	}

	public void setByInt(T value, int... indexes) {
		data[getFlatIndexByInt(indexes)] = value;
	}

	@SuppressWarnings("unchecked")
	public T get(Object... names) {
		return (T) data[getFlatIndex(names)];
	}

	public void set(T value, Object... indexes) {
		data[getFlatIndex(indexes)] = value;
	}

	@SuppressWarnings("unchecked")
	public T getWithFlatIndex(int index) {
		return (T)data[index];
	}

	public void setWithFlatIndex(T value, int index) {
		data[index] = value;
	}

	@Override
	public ObjectTable<T> clone() {
		ObjectTable<T> result = cloneStructure();
		result.copy(this);
		return result;
	}

	@Override
	public ObjectTable<T> cloneStructure() {
		ObjectTable<T> result = new ObjectTable<T>(getDimensioners());
		return result;
	}

	public ObjectTable<T> cloneStructure(T initialValue) {
		ObjectTable<T> result = cloneStructure();
//		result.fillWith(initialValue.clone());
		// CAUTION: THIS IS NOT GOING TO WORK !
		result.fillWith(initialValue);
		return result;
	}

	@Override
	public ObjectTable<T> fillWith(Object value) {
		Arrays.fill(data, value);
		return this;
	}

	@Override
	public String elementToString(int flatIndex) {
		if (data[flatIndex] == null)
			return "null";
		else
			return data[flatIndex].toString();
	}

	@Override
	public ObjectTable<T> clear() {
		return fillWith(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectTable<T> copy(Table from) {
		if (contentType()==null || contentType().isAssignableFrom(from.contentType())) {
			ObjectTable<T> ot = (ObjectTable<T>)from;
			for (int i=0; i<data.length; i++)
				data[i] = ot.data[i];
		}
		return null;
	}


	// These will all crash because they maybe null
	@SuppressWarnings("unchecked")
	@Override
	public String elementClassName() {
		T o = (T) data[0];		
		return  o.getClass().getName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String elementSimpleClassName() {
		T o = (T) data[0];		
		return o.getClass().getSimpleName();
	}
		
	@Override
	public Class<?> contentType() {
		for (int i = 0;i<data.length;i++) {
			if (data[i]!=null)
				return data[i].getClass();
		}
		return null;
	}
	
	// CAUTION! Possible FLAW here!
//	@Override
//	public String elementToToken(int flatIndex) {
//		return data[flatIndex].toString();
//	}

	
//	// FLAW: this is wrong. s should be converted to ? (using valueOf ?) before being set
//	// So this means T must be of a class that implements valueOf.
//	// forget this for the moment, it's probably never going to be used anyway.
//	public static ObjectTable<?> valueOf(String value) {
//		String ss = value.substring(1);
//		String s = null;
//		String d = ss.substring(1,ss.indexOf("]"));
//		ObjectTable result = new ObjectTable(readDimensioners(d));
//		ss = ss.substring(ss.indexOf("]")+2,ss.indexOf("}")); // ] + ,
//		int i=0;
//		while (ss.indexOf(",")>0) {
//			s = ss.substring(0,ss.indexOf(","));
//			ss = ss.substring(ss.indexOf(",")+1);
//			result.data[i] = s;
//			i++;
//		}
//		result.data[i] = ss;
//		return result;
//	}
	

}

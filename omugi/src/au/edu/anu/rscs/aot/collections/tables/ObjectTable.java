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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import fr.cnrs.iees.OmugiException;

/** intial code from Shayne */
/** modified by JG 15/2/2017 to account for Table and DataContainer interfaces */
/** modified by JG 1/10/2018 to account for Textable interface */

// notice that by construct T cannot be primitive
public class ObjectTable<T> extends TableAdapter {

	protected T[] data;
	private Class<T> template;

	@SuppressWarnings("unchecked")
	public ObjectTable(Class<T> contentModel,Dimensioner... dimensions) {
		super(dimensions);
		ArrayList<T> d = new ArrayList<>(size());
		for (int i=0; i<size(); i++)
			d.add(null);
		data = (T[]) d.toArray();
		template = contentModel;
	}

	public T getByInt(int... indexes) {
		return data[getFlatIndexByInt(indexes)];
	}

	public void setByInt(T value, int... indexes) {
		data[getFlatIndexByInt(indexes)] = value;
	}

	public T get(Object... indexes) {
		return data[getFlatIndex(indexes)];
	}

	public void set(T value, Object... indexes) {
		data[getFlatIndex(indexes)] = value;
	}

	public T getWithFlatIndex(int index) {
		return data[index];
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
		ObjectTable<T> result = new ObjectTable<T>(template,getDimensioners());
		return result;
	}

	public ObjectTable<T> cloneStructure(T initialValue) {
		ObjectTable<T> result = cloneStructure();
		result.fillWith(initialValue);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectTable<T> fillWith(Object value) {
		Arrays.fill(data, (T)value);
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
		return this;
	}

	@Override
	public String elementClassName() {
		return data.getClass().getComponentType().getName();
	}

	@Override
	public String elementSimpleClassName() {
		return data.getClass().getComponentType().getSimpleName();
	}
		
	// CAUTION: this doesnt work for complex types, ie Duple<Integer,Double> but is ok for simple types
	// eg Interval. For complex types write a descendant of this class
	@Override
	public Class<?> contentType() {
		return template;
	}
	
	/**
	 * A valueOf(...) method where data type, value and separators are specified. Maximal protection
	 * against wrong data types.
	 * 
	 * @param contentType
	 * @param value
	 * @param bdel
	 * @param isep
	 * @return
	 */
	public static ObjectTable<?> valueOf(Class<?> contentType, String value, char[][] bdel, char[] isep) {
		// get data string
		String ss = TableAdapter.getBlockContent(value,bdel[TABLEix]);
		// get dimension string
		String d = ss.substring(0,ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1);
		Constructor<?> cons;
		try {
			cons = ObjectTable.class.getDeclaredConstructor(Dimensioner[].class);
			ObjectTable<?> result;
			try {
				// instantiate ObjectTable for the return value
				result = (ObjectTable<?>) cons.newInstance((Object[])readDimensioners(d,bdel[DIMix],isep[DIMix]));
				// check the result content type is the same as the argument 
				if (result.contentType().equals(contentType)) {
					Method vlof;
					try {
						// check the content type has a valueOf method
						vlof = result.contentType().getMethod("valueOf",String.class);
						ss = ss.substring(ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1); 
						String s = null;
						int i=0;
						try {
							// use content type valueOf to read data
							while (ss.indexOf(isep[TABLEix])>0) {
								s = ss.substring(0,ss.indexOf(isep[TABLEix]));
								ss = ss.substring(ss.indexOf(isep[TABLEix])+1);
									vlof.invoke(result.data[i],s);
								i++;
							}
							vlof.invoke(result.data[i],ss);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							throw new OmugiException("Wrong data format for valueOf(String) method in class '"
								+contentType.getSimpleName()+"'",e);
						}
					} catch (NoSuchMethodException | SecurityException e1) {
						throw new OmugiException("'"+result.contentType().getSimpleName()
							+"' lacks a proper valueOf(String) method",e1);				
					}
					return result;
				}
				else
					throw new OmugiException("'"+value+"' cannot be read in an ObjectTable<"
						+contentType.getSimpleName()+">: '"
						+result.contentType().getSimpleName()
						+"' content type found");
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e2) {
				// I don't see how we could get here - wrong constructor in a descendant class, maybe?
				e2.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e2) {
			// NB: we should never reach here if ObjectTable constructor is valid
			e2.printStackTrace();
		}
		return null; // just in case
	}
	
	/**
	 * A valueOf(...) method where data value and separators are specified. Data type is assumed
	 * to match that of the table.
	 * 
	 * @param value
	 * @param bdel
	 * @param isep
	 * @return
	 */
	public static ObjectTable<?> valueOf(String value, char[][] bdel, char[] isep) {
		// get data string
		String ss = TableAdapter.getBlockContent(value,bdel[TABLEix]);
		// get dimension string
		String d = ss.substring(0,ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1);
		Constructor<?> cons;
		try {
			cons = ObjectTable.class.getDeclaredConstructor(Dimensioner[].class);
			ObjectTable<?> result;
			try {
				// instantiate ObjectTable for the return value
				result = (ObjectTable<?>) cons.newInstance((Object[])readDimensioners(d,bdel[DIMix],isep[DIMix]));
				Method vlof;
				try {
					// check the content type has a valueOf method
					vlof = result.contentType().getMethod("valueOf",String.class);
					ss = ss.substring(ss.indexOf(bdel[DIMix][BLOCK_CLOSE])+1); 
					String s = null;
					int i=0;
					try {
						// use content type valueOf to read data
						while (ss.indexOf(isep[TABLEix])>0) {
							s = ss.substring(0,ss.indexOf(isep[TABLEix]));
							ss = ss.substring(ss.indexOf(isep[TABLEix])+1);
								vlof.invoke(result.data[i],s);
							i++;
						}
						vlof.invoke(result.data[i],ss);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new OmugiException("Wrong data format for valueOf(String) method in class '"
							+result.contentType().getSimpleName()+"'",e);
					}
				} catch (NoSuchMethodException | SecurityException e1) {
					throw new OmugiException("'"+result.contentType().getSimpleName()
						+"' lacks a proper valueOf(String) method",e1);				
				}
				return result;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e2) {
				// I don't see how we could get here - wrong constructor in a descendant class, maybe?
				e2.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e2) {
			// NB: we should never reach here if ObjectTable constructor is valid
			e2.printStackTrace();
		}
		return null; // just in case
	}


	/**
	 * A valueOf(...) method where data type and value are specified
	 * 
	 * @param contentType
	 * @param value
	 * @return
	 */
	public static ObjectTable<?> valueOf(Class<?> contentType, String value) {
		return valueOf(contentType,value,Table.getDefaultDelimiters(),Table.getDefaultSeparators());
	}

	/**
	 * A valueOf(...) method where only data value is specified
	 * 
	 * @param contentType
	 * @param value
	 * @return
	 */
	public static ObjectTable<?> valueOf(String value) {
		return valueOf(value,Table.getDefaultDelimiters(),Table.getDefaultSeparators());
	}

}

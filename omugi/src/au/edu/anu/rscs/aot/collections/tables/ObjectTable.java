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

import java.util.ArrayList;
import java.util.Arrays;

import fr.cnrs.iees.OmugiException;

/**
 * <p>A generic {@linkplain Table} to contain any {@linkplain Object} descendant.</p>
 * <p>By construct, the implementation of {@code contentType()} at this level cannot return
 * better than the {@linkplain Object} class. To return the exact T class, a descendant of ObjectTable<T>
 * where T is explicited must be written.</p>
 * <p>Similarly, the usual static {@code valueOf()} method cannot be implemented here, only in descendant classes.</p>
 *  
 * @author Shayne Flint - looong ago
 * <ul><li>modified by JG 15/2/2017 to account for {@linkplain Table} and {@linkplain DataContainer} interfaces</li>
 * <li>modified by JG 1/10/2018 to account for {@linkplain Textable} interface</li>
 * <li>modified by JG 3/2/2021 to fix the {@code valueOf(...)} issues</li>
 * </ul>
 *
 * @param <T> Any {@linkplain Object} descendant except primitive wrappers; 
 * for {@linkplain String}, use {@linkplain StringTable}
 */
public class ObjectTable<T> extends TableAdapter {

	protected T[] data;

	@SuppressWarnings("unchecked")
	public ObjectTable(Dimensioner... dimensions) {
		super(dimensions);
		ArrayList<T> d = new ArrayList<>(size());
		for (int i=0; i<size(); i++)
			d.add(null);
		data = (T[]) d.toArray();
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
		ObjectTable<T> result = new ObjectTable<T>(getDimensioners());
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

	/**
	 * <p>This method <strong>must</strong> be overriden in descendants. Here it is impossible to know the class of T,
	 * so {@linkplain Object} is returned.</p>	/**
	 * <p>Only descendants of ObjectTable<T> can implement a valueOf(...) method as the generic
	 * constructor cannot handle unknown object types.</p>
	 */
	@Override
	public Class<?> contentType() {
		return Object.class;
	}
	
	/**
	 * <p>Only descendants of ObjectTable<T> can implement a valueOf(...) method as the generic
	 * constructor cannot handle unknown object types. This method throws an Exception.</p>
	 */
	public static ObjectTable<?> valueOf(String value, char[][] bdel, char[] isep) {
		throw new OmugiException("Only descendants of ObjectTable<T> can have a valueOf(...) method");
	}

	public static ObjectTable<?> valueOf(String value) {
		return valueOf(value,Table.getDefaultDelimiters(),Table.getDefaultSeparators());
	}

}

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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>A multidimensional generic {@link Table} to contain any {@link Object} descendant.</p>
 * <p>By construct, the implementation of {@code contentType()} at this level cannot return
 * better than the {@link Object} class. To return the exact T class, a descendant of ObjectTable<T>
 * where T is explicited must be written.</p>
 * <p>Similarly, the usual static {@code valueOf()} method cannot be implemented here, only in descendant classes.</p>
 *  
 * @author Shayne Flint - looong ago
 * <ul><li>modified by JG 15/2/2017 to account for {@link Table} and 
 * {@link fr.cnrs.iees.omhtk.DataContainer DataContainer} interfaces</li>
 * <li>modified by JG 1/10/2018 to account for {@link fr.cnrs.iees.omhtk.Textable Textable} interface</li>
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

	/** get the value stored at <em>indexes</em>*/
	public T getByInt(int... indexes) {
		return data[getFlatIndexByInt(indexes)];
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public void setByInt(T value, int... indexes) {
		data[getFlatIndexByInt(indexes)] = value;
	}

	/** get the value stored at <em>indexes</em>*/
	public T get(Object... indexes) {
		return data[getFlatIndex(indexes)];
	}

	/** set <em>value</em> at cell specified by <em>indexes</em> */
	public void set(T value, Object... indexes) {
		data[getFlatIndex(indexes)] = value;
	}

	/** get the value stored at flat <em>index</em>*/
	public T getWithFlatIndex(int index) {
		return data[index];
	}

	/** set <em>value</em> at cell specified by flat <em>index</em> */
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

	/**
	 * Clone this table and fills it with its argument
	 * 
	 * @param initialValue the value to fill the table with
	 * @return the new table
	 */
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
	 * so {@link Object} is returned.</p>	
	 */
	@Override
	public Class<?> contentType() {
		return Object.class;
	}
	
	/**
	 * <p>A default implementation throwing an {@code Exception}.
	 * Only descendants of {@code ObjectTable<T>} can implement a {@code valueOf(...)} method as the generic
	 * constructor cannot handle unknown object types.</p>
	 * 
	 * @throws UnsupportedOperationException if called.
	 */
	public static ObjectTable<?> valueOf(String value, char[][] bdel, char[] isep) {
		throw new UnsupportedOperationException("Only descendants of ObjectTable<T> can have a valueOf(...) method");
	}

	/**
	 * <p>A default implementation throwing an {@code Exception}.
	 * Only descendants of {@code ObjectTable<T>} can implement a {@code valueOf(...)} method as the generic
	 * constructor cannot handle unknown object types.</p>
	 */
	public static ObjectTable<?> valueOf(String value) {
		return valueOf(value,Table.getDefaultDelimiters(),Table.getDefaultSeparators());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hash==0) {
			final int prime = 31;
			hash = super.hashCode();
			hash = prime * hash + Arrays.deepHashCode(data);
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
		if (!(obj instanceof ObjectTable))
			return false;
		ObjectTable<?> other = (ObjectTable<?>) obj;
		return Arrays.deepEquals(data, other.data);
	}

}

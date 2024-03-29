/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
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
package fr.cnrs.iees.omugi.properties.impl;

import java.util.*;

import fr.cnrs.iees.omhtk.Textable;
import fr.cnrs.iees.omugi.graph.property.Property;
import fr.cnrs.iees.omugi.properties.ReadOnlyPropertyList;

/**
 * A read-only property list - property values can only be set at construction time.
 * Internally, the container is a sorted map so that property keys always come in the same order. 
 * 
 * @author Jacques Gignoux - 29 janv. 2019
 *
 */
public class ReadOnlyPropertyListImpl implements ReadOnlyPropertyList, Textable {

	protected SortedMap<String, Object> propertyMap = new TreeMap<String, Object>();
	// hash code for fast indexing
	private int hash = 0;
	
	// Constructors
	
	/**
	 * Construct from another property list. All values are copied in this instance.
	 * Remember that values cannot be changed after construction.
	 * 
	 * @param propertyList the list of properties
	 */
	public ReadOnlyPropertyListImpl(ReadOnlyPropertyList propertyList) {
		super();
		for (String key : propertyList.getKeysAsSet())
			propertyMap.put(key, propertyList.getPropertyValue(key));
	}

	/**
	 * Constructor from single properties.
	 * Remember that values cannot be changed after construction.
	 *  
	 * @param properties the properties copied into this instance
	 */
	public ReadOnlyPropertyListImpl(Property... properties) {
		super();
		for (Property p : properties)
			propertyMap.put(p.getKey(), p.getValue());
	}

	/**
	 * Constructor from a list of property names and a list of matching values.
	 * Remember that values cannot be changed after construction.
	 * 
	 * @param keys the property names
	 * @param values the values
	 */
	public ReadOnlyPropertyListImpl(List<String> keys, List<Object> values) {
		super();
		Iterator<Object> it = values.iterator();
		for (String key : keys)
			propertyMap.put(key, it.next());
	}
	
	// PropertyListGetters methods
	//
	
	@Override
	public Object getPropertyValue(String key) {
		return propertyMap.get(key);
	}

	@Override
	public Set<String> getKeysAsSet() {
		return propertyMap.keySet();
	}

	@Override
	public boolean hasProperty(String key) {
		return propertyMap.containsKey(key);
	}

	// Sizeable methods

	@Override
	public int size() {
		return propertyMap.size();
	}

	// DataContainer methods
	//

	@Override
	public ReadOnlyPropertyList clone() {
		return new ReadOnlyPropertyListImpl(this);
	}

	@Override
	public ReadOnlyPropertyList clear() {
		// DO NOTHING! this is read-only
		return this;
	}

	// Object methods
	//

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1024);
		boolean first = true;
		for (Map.Entry<String, Object> e : propertyMap.entrySet())
			if (first) {
				sb.append(e.getKey()).append("=").append(e.getValue());
				first = false;
			} else
				sb.append(" ").append(e.getKey()).append("=").append(e.getValue());
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hash==0)
			hash = Objects.hash(propertyMap);
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		ReadOnlyPropertyListImpl other = (ReadOnlyPropertyListImpl) obj;
		return Objects.equals(propertyMap,other.propertyMap);
	}

}

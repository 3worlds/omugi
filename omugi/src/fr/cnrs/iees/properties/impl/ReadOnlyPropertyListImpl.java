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
package fr.cnrs.iees.properties.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.ens.biologie.generic.Textable;

/**
 * A read-only property list - property values can only be set at construction time 
 * @author Jacques Gignoux - 29 janv. 2019
 *
 */
public class ReadOnlyPropertyListImpl implements ReadOnlyPropertyList, Textable {

	protected SortedMap<String, Object> propertyMap = new TreeMap<String, Object>();
	
	// Constructors
	
	public ReadOnlyPropertyListImpl(ReadOnlyPropertyList propertyList) {
		super();
		for (String key : propertyList.getKeysAsSet())
			propertyMap.put(key, propertyList.getPropertyValue(key));
	}

	public ReadOnlyPropertyListImpl(Property... properties) {
		super();
		for (Property p : properties)
			propertyMap.put(p.getKey(), p.getValue());
	}

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

}

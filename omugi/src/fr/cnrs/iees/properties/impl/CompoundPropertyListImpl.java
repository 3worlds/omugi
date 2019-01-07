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
package fr.cnrs.iees.properties.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import au.edu.anu.rscs.aot.graph.property.Property;

import java.util.Set;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.properties.PropertyListSetters;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * <p>A PropertyList made by grouping property lists in a single wrapper.
 * Storage is made in the original property lists.</p>
 * <p>Since property lists may have properties with the same names, the names must be
 * prefixed with a property list name when accessing the properties</p>
 * @author gignoux - 15 juin 2017
 *
 */
// Tested OK with version 0.0.1 on 26-10-2018 but see below:
// NOTE: the version 0.0.1 tests are incomplete because I have no ReadOnlyPropertyList 
// implementation class to test.
public class CompoundPropertyListImpl implements SimplePropertyList {
	
	public static final String SEP = ":";

	private Map<String,ReadOnlyPropertyList> readOnlyProps = new HashMap<>();
	private Map<String,SimplePropertyList> readWriteProps = new HashMap<>();
	
	public CompoundPropertyListImpl(ReadOnlyPropertyList[] props, String[] names) {
		super();
		if (props.length!=names.length)
			throw new OmugiException(getClass().getName()+": Number of property lists differs from number of property list names");
		// check there are no duplicate property lists, ie pls with exactly 
		// the same property names and types
		for (int i=0; i<props.length; i++)
			for (int j=0; j<props.length; j++)
				if ((i!=j) & (props[i]!=null) & props[j]!=null)
					if (props[i].hasTheSamePropertiesAs(props[j]))
						throw new OmugiException(getClass().getName()+": Cannot handle identical property lists");
		for (int i=0; i<props.length; i++)
			if (props[i]!=null)
				if (SimplePropertyList.class.isAssignableFrom(props[i].getClass()))
					readWriteProps.put(names[i],(SimplePropertyList) props[i]);
				else
					readOnlyProps.put(names[i],props[i]);
	}

	@Override
	public PropertyListSetters setProperty(String key, Object value) {
		String[] s = key.split(SEP);
		if (readWriteProps.containsKey(s[0]))
			readWriteProps.get(s[0]).setProperty(s[1], value);
		else
			throw new OmugiException("Key '" + key + "' not found in CompoundPropertyListImpl");
		return this;
	}

	@Override
	public PropertyListSetters setProperties(ReadOnlyPropertyList propertyList) {
		for (SimplePropertyList spl:readWriteProps.values())
			if (spl.hasTheSamePropertiesAs(propertyList))
				spl.setProperties(propertyList);
		return this;
	}

	@Override
	public Property getProperty(String key) {
		if (key.contains(SEP)) {
			String[] s = key.split(SEP);
			if (readWriteProps.containsKey(s[0]))
				return readWriteProps.get(s[0]).getProperty(s[1]);
			else if (readOnlyProps.containsKey(s[0]))
				return readOnlyProps.get(s[0]).getProperty(s[1]);
		}
		return null;
	}

	@Override
	public Object getPropertyValue(String key) {
		if (key.contains(SEP)) {
			String[] s = key.split(SEP);
			if (readWriteProps.containsKey(s[0]))
				return readWriteProps.get(s[0]).getPropertyValue(s[1]);
			else if (readOnlyProps.containsKey(s[0]))
				return readOnlyProps.get(s[0]).getPropertyValue(s[1]);
		}
		return null;
	}

	@Override
	public boolean hasProperty(String key) {
		if (key.contains(SEP)) {
			String[] s = key.split(SEP);
			if (readWriteProps.containsKey(s[0]))
				return readWriteProps.get(s[0]).hasProperty(s[1]);
			else if (readOnlyProps.containsKey(s[0]))
				return readOnlyProps.get(s[0]).hasProperty(s[1]);
		}
		return false;
	}

//	@Override
//	public boolean isPropertyType(String key, String className) {
//		try {
//			String fullClassName = ValidPropertyTypes.getJavaClassName(className);
//			Object value = getPropertyValue(key);
//			return fullClassName.equals(value.getClass().getName());
//		} catch (Exception e) {
//			return false;
//		}
//	}

	@Override
	public Set<String> getKeysAsSet() {
		Set<String> result = new HashSet<String>();
		for (String lkey:readWriteProps.keySet())
			for (String pkey:readWriteProps.get(lkey).getKeysAsSet())
				result.add(lkey+SEP+pkey);
		for (String lkey:readOnlyProps.keySet())
			for (String pkey:readOnlyProps.get(lkey).getKeysAsSet())
				result.add(lkey+SEP+pkey);
		return result;
	}

	@Override
	public CompoundPropertyListImpl clone() {
		ReadOnlyPropertyList[] pl = new ReadOnlyPropertyList[readWriteProps.size()+readOnlyProps.size()];
		String[] nl = new String[pl.length];
		int i=0;
		for (Entry<String,SimplePropertyList> e:readWriteProps.entrySet()) {
			nl[i] = e.getKey();
			pl[i] = e.getValue().clone();
			i++;
		}
		for (Entry<String,ReadOnlyPropertyList> e:readOnlyProps.entrySet()) {
			nl[i] = e.getKey();
			pl[i] = e.getValue().clone();
			i++;
		}
		return new CompoundPropertyListImpl(pl,nl);
	}

	protected CompoundPropertyListImpl cloneStructure() {
		return clone();
	}

	@Override
	public CompoundPropertyListImpl clear() {
		for (SimplePropertyList spl:readWriteProps.values())
			spl.clear();
		// I think it makes sense that the read-only properties are also cleared?
		// clear() is usually an initialisation or reset operation, so a bit
		// different than just setting null values (which is impossible).
		for (ReadOnlyPropertyList rpl:readOnlyProps.values())
			rpl.clear();		
		return this;
	}

	@Override
	public int size() {
		int size = 0;
		for (SimplePropertyList spl:readWriteProps.values())
			size += spl.size();
		for (ReadOnlyPropertyList rpl:readOnlyProps.values())
			size += rpl.size();
		return size;
	}

	@Override
	public boolean hasTheSamePropertiesAs(ReadOnlyPropertyList list) {
		boolean res = true;
		if (CompoundPropertyListImpl.class.isAssignableFrom(list.getClass())) {
			CompoundPropertyListImpl cpl = (CompoundPropertyListImpl) list; 
			// check that the list names are the same,
			// then check that the property names in the lists are the same
			if (cpl.readWriteProps.keySet().equals(readWriteProps.keySet()))
				for (String lkey:cpl.readWriteProps.keySet())
					res = res & cpl.readWriteProps.get(lkey).hasTheSamePropertiesAs(readWriteProps.get(lkey));
			else
				res = false;
			if (cpl.readOnlyProps.keySet().equals(readOnlyProps.keySet()))
				for (String lkey:cpl.readOnlyProps.keySet())
					res = res & cpl.readOnlyProps.get(lkey).hasTheSamePropertiesAs(readOnlyProps.get(lkey));
			else
				res = false;
		}
		else {
			for (SimplePropertyList spl:readWriteProps.values())
				res = res & spl.hasTheSamePropertiesAs(list);
			for (ReadOnlyPropertyList rpl:readOnlyProps.values())
				res = res & rpl.hasTheSamePropertiesAs(list);
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1024);
		boolean first = true;
		for (String lkey:readWriteProps.keySet())
			for (String pkey:readWriteProps.get(lkey).getKeysAsSet()) {
				String propname = lkey+SEP+pkey;
				if (first) {
					sb.append(propname)
						.append("=")
						.append(readWriteProps.get(lkey).getPropertyValue(pkey));
					first = false;
				} else
					sb.append(" ")
						.append(propname)
						.append("=")
						.append(readWriteProps.get(lkey).getPropertyValue(pkey));
			}
		for (String lkey:readOnlyProps.keySet())
			for (String pkey:readOnlyProps.get(lkey).getKeysAsSet()) {
				String propname = lkey+SEP+pkey;
				if (first) {
					sb.append(propname)
						.append("=")
						.append(readOnlyProps.get(lkey).getPropertyValue(pkey));
					first = false;
				} else
					sb.append(" ")
						.append(propname)
						.append("=")
						.append(readOnlyProps.get(lkey).getPropertyValue(pkey));
			}

		return sb.toString();
	}
}

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
package fr.cnrs.iees.omugi.properties;

import java.util.Set;

import fr.cnrs.iees.omugi.graph.property.Property;

/**
 * This interface isolates the generic getters for properties. 
 * 
 * 
 * @author J. Gignoux - 13 févr. 2017
 *
 */

public interface PropertyListGetters {

	/**
	 * Getter for property. Contract: efficiency - do not raise an 
	 * exception if <em>key</em> is not found
	 * 
	 * @param key the property name
	 * @return the property as a (key,value) pair
	 */
	public default Property getProperty(String key) {
		return new Property(key, getPropertyValue(key));
	}

	/**
	 * Getter for property <em>value</em>. Contract: efficiency - do 
	 * not raise an exception if  <em>key</em> is not found
	 * 
	 * @param key the property name
	 * @return the property value
	 */
	public Object getPropertyValue(String key);
	
	/**
	 * Check if a property exists in this object
	 * 
	 * @param key the property name to check
	 * @return true if found
	 */
	public boolean hasProperty(String key);

	/**
	 * Return a the String description of a property value (i.e. {@code value.toString()}).
	 * 
	 * @param key the property name
	 * @return its value as a String, or {@code "null"} if property is unset
	 */
	public default String propertyToString(String key) {
		Object value = getPropertyValue(key);
		if (value != null)
			return value.toString();
		else
			return "null";
	}

	/**
	 * Getter for property class name. Will fail if value is {@code null}.
	 * 
	 * @param key the property name
	 * @return the property class name
	 */
	public default String getPropertyClassName(String key) {
		return getPropertyClass(key).getName();
	}
		
	/**
	 * Getter for property class. will not fail if value is {@code null}.
	 * 
	 * @param key the property name
	 * @return the property class 
	 */
	public default Class<?> getPropertyClass(String key) {
		Object o = getPropertyValue(key);
		if (o!=null)
			return o.getClass();
		else
			return Object.class;
	}

	/**
	 * Getter for property names.
	 * 
	 * @return all property names, as a set
	 */
	public Set<String> getKeysAsSet();
	
	/**
	 * Getter for property names. Internally, calls {@code getKeysAsSet()}.
	 * 
	 * @return all property names, as an array
	 */
	public default String[] getKeysAsArray() {
		Set<String> keySet = getKeysAsSet();
		String[] keys = new String[keySet.size()];		
		return keySet.toArray(keys);
	}

}

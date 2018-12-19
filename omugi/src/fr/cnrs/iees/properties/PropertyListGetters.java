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
package fr.cnrs.iees.properties;

import java.util.Set;

import au.edu.anu.rscs.aot.graph.property.Property;

/**
 * This interface isolates the generic getters for properties. 
 * 
 * 
 * @author J. Gignoux - 13 févr. 2017
 *
 */

public interface PropertyListGetters {

	/**
	 * Contract: efficiency - do not raise an exception if key is not found
	 * @param key the property name
	 * @return the property as a (key, value) pair
	 */
	public default Property getProperty(String key) {
		return new Property(key, getPropertyValue(key));
	}

	/**
	 * Contract: efficiency - do not raise an exception if key is not found
	 * @param key the property name
	 * @return the property value
	 */
	public Object getPropertyValue(String key);
	
	/**
	 * check if a property exists in this object
	 * @param key the property name to check
	 * @return true if found
	 */
	public boolean hasProperty(String key);

	// refactored 
//	public String   getString(String key);
	public default String propertyToString(String key) {
		Object value = getPropertyValue(key);
		if (value != null)
			return value.toString();
		else
			return "null";
	}

	// refactored
//	public String getClassName(String key);
	// TODO: change name to getPropertyJavaClassName() 
	public default String getPropertyClassName(String key) {
		return getPropertyClass(key).getName();
	}
	
	// refactored
//	public String toSimpleClassName(String key);
	// TODO: remove. This isnt part of property types
//	public default String getPropertySimpleClassName(String key) {
////		return PropertyType.toPropertyType(getPropertyClassName(key));
//		return ValidPropertyTypes.getType(key);
//	}

	// refactored
//	public boolean isType(String key, String className);
	// TODO: remove. This isnt part of property types
//	public boolean isPropertyType(String key, String className);
	
	// safe - returns Object if value is null
	public default Class<?> getPropertyClass(String key) {
		Object o = getPropertyValue(key);
		if (o!=null)
			return o.getClass();
		else
			return Object.class;
	}

	// refactored
//	public Set<String> getKeys();
	public Set<String> getKeysAsSet();
	
	public default String[] getKeysAsArray() {
		Set<String> keySet = getKeysAsSet();
		String[] keys = new String[keySet.size()];		
		return keySet.toArray(keys);
	}

}

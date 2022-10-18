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

import java.util.List;

import au.edu.anu.omugi.graph.property.Property;

/**
 * This interface isolates the generic setters for properties. Implementing classes
 * will probably need to also implement {@link PropertyListGetters}. 
 * 
 * @author J. Gignoux - 13 févr. 2017
 *
 */

public interface PropertyListSetters {

	/**
	 * Setter for property.
	 * The contract of all property setters is: only set a value if the property exists in the
	 * associate {@link PropertyListGetters} implementation - raise an exception if key not found
	 * 
	 * @param property the property (key, value pair) to set
	 * @return this object for agile programming
	 */
	public default PropertyListSetters setProperty(Property property) {
		setProperty(property.getKey(),property.getValue());
		return this;
	}

	/**
	 * Setter for property key and value. The contract of all property setters is: only set a value if the property exists in the
	 * associate {@link PropertyListGetters} implementation - raise an exception if key not found
	 * @param key the property name to set
	 * @param value the value to set
	 * @return this object for agile programming
	 */
	public PropertyListSetters setProperty(String key, Object value);	
	
	/**
	 * Setter for properties from another property list. The contract of all property list setters is: only set a value if the property exists 
	 * in the associate {@link PropertyListGetters} implementation
	 * - raise an exception if key not found
	 * @param propertyList the PropertyListGetters to read values from
	 * @return this object for agile programming
	 */
	public default PropertyListSetters setProperties(ReadOnlyPropertyList propertyList) {
		for (String key:propertyList.getKeysAsSet()) 
			setProperty(key,propertyList.getPropertyValue(key));
		return this;
	}
	
	/**
	 * Setter for properties from a list of keys and a list of matching values.
	 * The contract of all property list setters is: only set a value if the property exists 
	 * in the associate {@link PropertyListGetters} implementation
	 * - raise an exception if key not found
	 * @param keys the property names to set
	 * @param values the values to set, in matching order
	 * @return this object for agile programming
	 */
	public default PropertyListSetters setProperties(List<String> keys, List<Object> values) {
		for (String key:keys) 
			setProperty(key,values.iterator().next());
		return this;
	}
	
	/**
	 * Setter for properties from an array of keys and an array of matching values.
	 * The contract of all property list setters is: only set a value if the property exists 
	 * in the associate {@link PropertyListGetters} implementation
	 * - raise an exception if key not found
	 * @param keys the property names to set
	 * @param values the values to set, in matching order
	 * @return this object for agile programming
	 */
	public default PropertyListSetters setProperties(String[] keys, Object[] values) {
		for (int i=0; i<keys.length; i++)
			setProperty(keys[i],values[i]);
		return this;
	}


}

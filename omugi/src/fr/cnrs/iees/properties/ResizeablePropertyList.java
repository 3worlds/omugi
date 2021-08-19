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

import au.edu.anu.rscs.aot.graph.property.Property;

/**
 * <p>This interface contains the methods required to make a property list 
 * extendable.</p> 
 * <p>The contract is as follows: {@code add...()} methods will add a new property to the list
 * while {@code set...()} methods will only set a value for an existing property. They should
 * return an error if the property does not exist.</p>
 * 
 * @author J. Gignoux - 13 févr. 2017
 *
 */
public interface ResizeablePropertyList {
	
	/**
	 * adds a new entry with the same key as the 'property' argument in this instance
	 * @param property the property which key will be used to allocate space
	 * @return this instance for agile programming
	 */
	public ResizeablePropertyList addProperty(Property property);
	
	/**
	 * adds a new entry with this 'key' in this instance
	 * @param key the new property key 
	 * @return this instance for agile programming
	 */
	public ResizeablePropertyList addProperty(String key);
	
	/**
	 * adds a new entry with this 'key' and this 'value' in this instance
	 * 
	 * @param key the new property key 
	 * @param value the value to put into the new property
	 * @return this instance for agile programming
	 */
	public ResizeablePropertyList addProperty(String key, Object value);

	/**
	 * adds all the entries found in list 'keys' in this instance
	 * 
	 * @param keys the list of keys to add
	 * @return this instance for agile programming
	 */
	public ResizeablePropertyList addProperties(List<String> keys);
		
	/**
	 * adds all the entries found in 'keys' in this instance
	 * 
	 * @param keys keys to add
	 * @return this instance for agile programming
	 */
	public ResizeablePropertyList addProperties(String... keys);

	/**
	 * adds all the entries found in argument property list in this instance
	 * 
	 * @param plist the properties to add (key and value)
	 * @return this instance for agile programming
	 */
	public ResizeablePropertyList addProperties(ReadOnlyPropertyList plist);

	
	/**
	 * get a property value matching 'key'; if no storage space for 'key', will create it
	 * and fill it with 'defaultValue' before returning it.
	 * 
	 * @param key the property to get
	 * @param defaultValue the value to set the property to if the key does not exist yet
	 * @return the property value matching 'key'
	 */
	public Object getPropertyValue(String key, Object defaultValue);
	
	/**
	 * removes the storage space for property 'key'
	 * 
	 * @param key the property to remove
	 * @return this instance for agile programming
	 */
	public ResizeablePropertyList removeProperty(String key);
	
	/**
	 * removes all properties (= all storage space) in this instance
	 * 
	 * @return this instance for agile programming
	 */
	public ResizeablePropertyList removeAllProperties();

}

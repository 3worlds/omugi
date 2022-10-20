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
package fr.cnrs.iees.omugi.properties.impl;

import java.util.*;

import fr.cnrs.iees.omhtk.Textable;
import fr.cnrs.iees.omugi.graph.property.Property;
import fr.cnrs.iees.omugi.properties.SimplePropertyList;

/**
 * Implementation of {@link SimplePropertyList}.
 * Optimisation: speed. No checks on dimensions, names or anything else. 
 * 
 * Internally, the container is a sorted map so that property keys always come in the same order.
 * 
 * @author J. Gignoux - 13 févr. 2017
 *
 */
// Tested OK with version 0.0.1 on 25/10/2018
public class SimplePropertyListImpl 
	extends ReadOnlyPropertyListImpl
	implements SimplePropertyList, Textable {

	// CONSTRUCTORS - please note that this class is not resizeable beyond
	// construction time,
	// which means no properties can be added in the map after construction.

	/**
	 * Construct from another property list. All values are copied in this instance.
	 * Remember that no more names can be added after construction.
	 * 
	 * @param propertyList the list of properties
	 */
	public SimplePropertyListImpl(SimplePropertyList propertyList) {
		super(propertyList);
	}

	/**
	 * Constructor from single properties.
	 * Remember that no more names can be added after construction.
	 *  
	 * @param properties the properties copied into this instance
	 */
	public SimplePropertyListImpl(Property... properties) {
		super(properties);
	}

	/**
	 * Constructor from a list of property names and a list of matching values.
	 * Remember that no more names can be added after construction.
	 * 
	 * @param keys the property names
	 * @param values the values
	 */
	public SimplePropertyListImpl(List<String> keys, List<Object> values) {
		super(keys,values);
	}

	/**
	 * Constructor from a list of property names.
	 * Remember that no more names can be added after construction.
	 * 
	 * @param keys the property names
	 */
	public SimplePropertyListImpl(Collection<String> keys) {
		super();
		for (String key : keys)
			propertyMap.put(key, null);
	}

	/**
	 * Constructor from property names.
	 * Remember that no more names can be added after construction.
	 * 
	 * @param keys the property names
	 */
	public SimplePropertyListImpl(String... keys) {
		super();
		for (String key : keys)
			propertyMap.put(key, null);
	}

	/** only for use in descendants since no properties are defined here */
	protected SimplePropertyListImpl() {
		super();
	}

	// PropertyListSetters methods
	//

	@Override
	public SimplePropertyList setProperty(String key, Object value) {
		if (propertyMap.containsKey(key))
			propertyMap.put(key, value);
		else
			throw new IllegalArgumentException("Key '" + key + "' not found in SimplePropertyListImpl");
		return this;
	}

	// only for use in descendants (eg in constructors) - no check on property key
	protected final void setNoCheckProperty(String key, Object value) {
		propertyMap.put(key, value);
	}

	// only for use in descendants who have no access to propertyMap.
	protected final void deleteKey(String key) {
		propertyMap.remove(key);
	}

	// only for use in descendants who have no access to propertyMap.
	protected final void deleteKeys() {
		propertyMap.clear();
	}

	// DataContainer methods
	//

	@Override
	public SimplePropertyList clone() {
		SimplePropertyListImpl clone = (SimplePropertyListImpl) cloneStructure();
		for (String key : propertyMap.keySet())
			clone.propertyMap.put(key, getPropertyValue(key));
		return clone;
	}

	protected SimplePropertyList cloneStructure() {
		SimplePropertyListImpl clone = new SimplePropertyListImpl(propertyMap.keySet());
		return clone;
	}
	
	// caution with this - potential flaw with primitive types
	@Override
	public SimplePropertyList clear() {
		for (Map.Entry<String, Object> e:propertyMap.entrySet())
			e.setValue(null);
		return this;
	}

}

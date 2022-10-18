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

import java.util.Collection;
import java.util.List;

import au.edu.anu.omugi.graph.property.Property;
import fr.cnrs.iees.properties.ExtendablePropertyList;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * <p>Implementation of {@link ExtendablePropertyList}.</p>
 * <ol>
 * <li>Storage of properties: local</li>
 * <li>Optimisation: speed. No checks on dimensions, names or anything else.</li>
 * </ol>
 * <p>Container is a sorted map so that property keys always come in the same order. Property
 * entries can be added / removed from the map after instantiation.</p>
 * 
 * @author J. Gignoux - 29 août 2017
 *
 */
// Tested OK for version 0.0.1 on 25-10-2018
public class ExtendablePropertyListImpl 
		extends SimplePropertyListImpl 
		implements ExtendablePropertyList {

	private boolean sealed = false;

	// Constructors
	//

	/**
	 * Constructs an empty list.
	 */
	public ExtendablePropertyListImpl() {
		super();
	}
	
	/**
	 * Constructor from property names.
	 * 
	 * @param keys the property names
	 */
	public ExtendablePropertyListImpl(String... keys) {
		super(keys);
	}

	/**
	 * Constructor from a list of property names and a list of matching values.
	 * 
	 * @param keys the property names
	 * @param values the values
	 */
	public ExtendablePropertyListImpl(List<String> keys, List<Object> values) {
		super(keys, values);
	}

	/**
	 * Constructor from a list of property names.
	 * 
	 * @param keys the property names
	 */
	public ExtendablePropertyListImpl(Collection<String> keys) {
		super(keys);
	}

	/**
	 * Construct from properties. All values are copied in this instance.
	 * 
	 * @param properties the list of properties
	 */
	public ExtendablePropertyListImpl(Property... properties) {
		super(properties);
	}

	/**
	 * Construct from another property list. All values are copied in this instance.
	 * 
	 * @param propertyList the list of properties
	 */
	public ExtendablePropertyListImpl(SimplePropertyList propertyList) {
		super(propertyList);
	}

	
	@Override
	public ExtendablePropertyList setProperty(String key, Object value) {
		if (hasProperty(key))
			setNoCheckProperty(key, value);
		else
			throw new IllegalArgumentException("Key '" + key + "' not found in ExtendablePropertyListImpl");
		return this;
	}


	@Override
	public ExtendablePropertyList addProperty(Property property) {
		return addProperty(property.getKey(),property.getValue());
	}
	

	@Override
	public ExtendablePropertyList addProperty(String key, Object value) {
		if (!sealed)
			setNoCheckProperty(key, value);
		else
			throw new IllegalStateException(this.getClass().getSimpleName()+" sealed: addition of new property keys is impossible.");
		return this;
	}


	@Override
	public ExtendablePropertyList addProperty(String key) {
		if (!sealed) { 
			if (!hasProperty(key)) 
				setNoCheckProperty(key, new Object());
		}
		else
			throw new IllegalStateException(this.getClass().getSimpleName()+" sealed: addition of new property keys is impossible.");
		return this;
	}

	// prudent behaviour: does not replace existing property values by blank properties
	@Override
	public ExtendablePropertyList addProperties(List<String> keys) {
		if (!sealed) { 
			for (String key:keys)
				if (!hasProperty(key))
					setNoCheckProperty(key, new Object());
		}
		else
			throw new IllegalStateException(this.getClass().getSimpleName()+" sealed: addition of new property keys is impossible.");
		return this;
	}

	// prudent behaviour: does not replace existing property values by blank properties
	@Override
	public ExtendablePropertyList addProperties(String... keys) {
		if (!sealed) { 
			for (String key:keys)
				if (!hasProperty(key))
					setNoCheckProperty(key, new Object());
		}
		else
			throw new IllegalStateException(this.getClass().getSimpleName()+" sealed: addition of new property keys is impossible.");
		return this;
	}
	
	// will replace existing property values by new values contained in plist
	@Override
	public ExtendablePropertyList addProperties(ReadOnlyPropertyList plist) {
		if (!sealed) 
			for (String key:plist.getKeysAsSet())
				setNoCheckProperty(key, plist.getPropertyValue(key));
		else
			throw new IllegalStateException(this.getClass().getSimpleName()+" sealed: addition of new property keys is impossible.");
		return this;
	}


	@Override
	public Object getPropertyValue(String key, Object defaultValue) {
		if (!hasProperty(key))
			if (sealed)
				throw new IllegalStateException(this.getClass().getSimpleName()+" sealed: addition of new property keys is impossible.");
			else
				setNoCheckProperty(key, defaultValue);
		return getPropertyValue(key);
	}

	@Override
	public ExtendablePropertyList removeProperty(String key) {
		if (!sealed) deleteKey(key);
		else
			throw new IllegalStateException(this.getClass().getSimpleName()+" sealed: deletion of property keys is impossible.");
		return this;
	}

	@Override
	public ExtendablePropertyList removeAllProperties() {
		if (!sealed) deleteKeys();
		else
			throw new IllegalStateException(this.getClass().getSimpleName()+" sealed: deletion of property keys is impossible.");
		return this;
	}

	@Override
	public final ExtendablePropertyList seal() {
		sealed = true;
		return this;
	}

	@Override
	public final boolean isSealed() {
		return sealed;
	}

	@Override
	public ExtendablePropertyList clone() {
		ExtendablePropertyList clone = (ExtendablePropertyList) super.clone();
		return clone;
	}

	protected ExtendablePropertyList cloneStructure() {
		ExtendablePropertyListImpl clone = new ExtendablePropertyListImpl(this);
		return clone;
	}

}

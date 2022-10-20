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

import java.util.Set;

import fr.cnrs.iees.omugi.graph.property.PropertyKeys;
import fr.cnrs.iees.omugi.properties.SimplePropertyList;
/**
 * <p>Implementation of {@link SimplePropertyList}.</p>
 * <ol>
 * <li>Storage of properties: keys are shared (and stored outside this class), values
 * are stored locally.</li>
 * <li>Optimisation: memory and speed. No checks on dimensions, names or anything else.</li>
 * <li>Use case: For large numbers of objects sharing the same set of properties.</li>
 * </ol>
 * <p>Properties and keys are stored in arrays so that property keys always come in the same order.</p>
 * 
 * @author J. Gignoux - 14 févr. 2017
 *
 */
// Tested OK with version 0.0.1 on 26-10-2018
public class SharedPropertyListImpl
		implements SimplePropertyList {

	protected PropertyKeys keys; // shared between many instances of this class
									// for saving space
	protected Object[] values;

	// Constructors
	//

	/**
	 * Constructor from a list of property names.
	 * 
	 * @param keys the property names
	 */
	public SharedPropertyListImpl(PropertyKeys keys) {
		super();
		this.keys = keys;
		values = new Object[this.keys.size()];
	}

	/**
	 * Construct from another property list. All values are copied in this instance.
	 * 
	 * @param sharedProperties the list of properties
	 */
	public SharedPropertyListImpl(SimplePropertyList sharedProperties) {
		this(new PropertyKeys(sharedProperties.getKeysAsArray()));
	}

	/**
	 * Constructor from property names.
	 * 
	 * @param keys the property names
	 */
	public SharedPropertyListImpl(String... keys) {
		this(new PropertyKeys(keys));
	}

	// PropertyListSetters methods
	//

	@Override
	public SimplePropertyList setProperty(String key, Object value) {
		int i = keys.indexOf(key);
		if (i != -1)
			values[i] = value;
		else
			throw new IllegalArgumentException("Key '" + key + "' not found in SharedPropertyListImpl");
		return this;
	}

	// PropertyListGetters methods
	//

	@Override
	public Object getPropertyValue(String key) {
		int i = keys.indexOf(key);
		if (i != -1)
			return values[i];
		else
			throw new IllegalArgumentException("Key '" + key + "' not found in SharedGraphPropertyListImpl");
	}

	@Override
	public final boolean hasProperty(String key) {
		return keys.indexOf(key) > 0;
	}

	@Override
	public final Set<String> getKeysAsSet() {
		return keys.getKeysAsSet();
	}

	// more efficient than the default
	@Override
	public final String[] getKeysAsArray() {
		return keys.getKeysAsArray();
	}
	
	// DataContainer methods
	//

	@Override
	public final SimplePropertyList clone() {
		SharedPropertyListImpl clone = (SharedPropertyListImpl) cloneStructure();
		for (int i = 0; i < values.length; i++)
			clone.values[i] = values[i];
		return clone;
	}

	protected SimplePropertyList cloneStructure() {
		return new SharedPropertyListImpl(keys);
	}

	@Override
	public SimplePropertyList clear() {
		for (int i = 0; i < values.length; i++)
			values[i] = null;
		return this;
	}


	// Sizeable methods
	//

	@Override
	public int size() {
		return values.length;
	}
	
	// Object methods
	//

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1024);
		for (int i=0; i<keys.size(); i++)
			if (i==0)
				sb.append(keys.getKeysAsArray()[i])
					.append("=")
					.append(values[i]);
			else
				sb.append(" ")
					.append(keys.getKeysAsArray()[i])
					.append("=")
					.append(values[i]);
		return sb.toString();
	}

}

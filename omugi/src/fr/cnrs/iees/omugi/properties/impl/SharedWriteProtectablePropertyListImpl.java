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

import java.util.Objects;

import fr.cnrs.iees.omugi.graph.property.PropertyKeys;
import fr.cnrs.iees.omugi.properties.SimplePropertyList;
import fr.cnrs.iees.omugi.properties.SimpleWriteProtectablePropertyList;

/**
 * <p>Implementation of {@link SimplePropertyList}.</p>
 * <ol>
 * <li>Storage of properties: keys are shared (and stored outside this class), values
 * are stored locally.</li>
 * <li>Optimisation: memory and speed. No checks on dimensions, names or anything else.</li>
 * <li>Use case: For large numbers of objects sharing the same set of properties and
 * when a fine control of property list edition capability is needed.</li>
 * </ol>
 * <p>Properties and keys are stored in arrays so that property keys always come in the same order.</p>
 * <p>The value of a property can only be changed if the list is in the 'writeEnable' state.
 * The current state of the list is known by calling {@code isReadOnly()}. The state can be
 * changed using {@code writeEnable()} and  {@code writeDisable()}.</p>
 * 
 * @author J. Gignoux - 14 févr. 2017
 *
 */
// Tested OK with version 0.0.1 on 26-10-2018
public class SharedWriteProtectablePropertyListImpl extends SharedPropertyListImpl
		implements SimpleWriteProtectablePropertyList {
	
	private boolean readOnly = false;
	
	// Constructors
	// 
	
	/**
	 * Constructor from a list of property names.
	 * 
	 * @param keys the property names
	 */
	public SharedWriteProtectablePropertyListImpl(PropertyKeys keys) {
		super(keys);
	}
	
	/**
	 * Construct from another property list. All values are copied in this instance.
	 * 
	 * @param sharedProperties the list of properties
	 */
	public SharedWriteProtectablePropertyListImpl(SimplePropertyList sharedProperties) {
		super(sharedProperties);
	}

	/**
	 * Constructor from property names.
	 * 
	 * @param keys the property names
	 */
	public SharedWriteProtectablePropertyListImpl(String... keys) {
		super(keys);
	}

	// PropertyListSetters methods
	// 

	@Override
	public SimpleWriteProtectablePropertyList setProperty(String key, Object value) {
		if (!readOnly) super.setProperty(key, value);
		return this;
	}
	
	// Cloneable methods
	//	
	
	// WriteProtectable methods
	//
	
	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public SimpleWriteProtectablePropertyList writeEnable() {
		readOnly = false;
		return this;
	}

	@Override
	public SimpleWriteProtectablePropertyList writeDisable() {
		readOnly = true;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hash==0) {
			final int prime = 31;
			hash = super.hashCode();
			hash = prime * hash + Objects.hash(readOnly);
		}
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof SharedWriteProtectablePropertyListImpl))
			return false;
		SharedWriteProtectablePropertyListImpl other = (SharedWriteProtectablePropertyListImpl) obj;
		return readOnly == other.readOnly;
	}

}

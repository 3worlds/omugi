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
package fr.cnrs.iees.graph.impl;

import java.util.Set;

import fr.cnrs.iees.graph.DataNode;
import fr.cnrs.iees.graph.GraphElementFactory;
import fr.cnrs.iees.properties.PropertyListSetters;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.ens.biologie.generic.DataContainer;

/**
 * <p>A node class with a list of properties.</p>
 * @author Jacques Gignoux - 27 nov. 2018
 *
 */
public class DataNodeImpl extends SimpleNodeImpl
		implements DataNode {

	private SimplePropertyList propertyList = null;

	// SimpleNodeImpl
	
	protected DataNodeImpl(SimplePropertyList props, GraphElementFactory factory) {
		super(factory);
		propertyList = props;
	}

	public DataNodeImpl(int capacity, SimplePropertyList props, GraphElementFactory factory) {
		super(capacity, factory);
		propertyList = props;
	}

	// SimplePropertyList
	
	@Override
	public PropertyListSetters setProperty(String key, Object value) {
		return propertyList.setProperty(key,value);
	}

	@Override
	public DataNodeImpl clone() {
		return new DataNodeImpl(propertyList.clone(),graphElementFactory());
	}

	@Override
	public Object getPropertyValue(String key) {
		return propertyList.getPropertyValue(key);
	}

	@Override
	public boolean hasProperty(String key) {
		return propertyList.hasProperty(key);
	}

	@Override
	public Set<String> getKeysAsSet() {
		return propertyList.getKeysAsSet();
	}

	@Override
	public int size() {
		return propertyList.size();
	}

	@Override
	public DataContainer clear() {
		return propertyList.clear();
	}
	
	// Textable

	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(super.toDetailedString());
		sb.append(' ');
		sb.append(propertyList.toString());
		return sb.toString();
	}

}

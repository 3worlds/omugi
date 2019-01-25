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

import fr.cnrs.iees.graph.DataEdge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.properties.PropertyListSetters;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.ens.biologie.generic.DataContainer;

public class DataEdgeImpl extends SimpleEdgeImpl implements DataEdge {

	private SimplePropertyList propertyList = null;

	// SimpleEdgeImpl

	protected DataEdgeImpl(Node start, Node end, SimplePropertyList props, EdgeFactory factory) {
		super(start, end, factory);
		propertyList = props;
	}

	protected DataEdgeImpl(String instanceId, Node start, Node end, 
			SimplePropertyList props, EdgeFactory factory) {
		super(instanceId, start, end, factory);
		propertyList = props;
	}
	
	protected DataEdgeImpl(String classId, String instanceId, Node start, Node end, 
			SimplePropertyList props, EdgeFactory factory) {
		super(classId, instanceId, start, end, factory);
		propertyList = props;
	}
	// SimplePropertyList
	
	@Override
	public DataEdgeImpl clone() {
		return new DataEdgeImpl(startNode(),endNode(),propertyList.clone(),edgeFactory());
	}

	@Override
	public PropertyListSetters setProperty(String key, Object value) {
		return propertyList.setProperty(key,value);
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
	
}

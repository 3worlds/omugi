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

import fr.cnrs.iees.graph.GraphElementFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.ReadOnlyDataEdge;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * Straightforward implementation of {@linkplain ReadOnlyDataEdge} 
 * @author Jacques Gignoux - 29 nov. 2018
 *
 */
public class ReadOnlyDataEdgeImpl extends SimpleEdgeImpl implements ReadOnlyDataEdge {

	private ReadOnlyPropertyList propertyList = null;

	// SimpleEdgeImpl

	protected ReadOnlyDataEdgeImpl(Node start, Node end, ReadOnlyPropertyList props, GraphElementFactory factory) {
		super(start, end, factory);
		propertyList = props;
	}
	
	// SimplePropertyList

	@Override
	public ReadOnlyDataEdgeImpl clone() {
		return new ReadOnlyDataEdgeImpl(startNode(),endNode(),propertyList.clone(),factory());
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

}

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

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.DataEdge;
import fr.cnrs.iees.graph.DataNode;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.GraphElementFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.ReadOnlyDataEdge;
import fr.cnrs.iees.graph.ReadOnlyDataNode;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;

/**
 * A simple factory for graph elements - mainly for testing purposes. Nodes and Edges
 * have no properties.
 * Ideally this should be grouped with the Graph interface in a graph implementation (to
 * make node and edge creation consistent with graph constraints).
 * 
 * @author Jacques Gignoux - 7 nov. 2018
 *
 */
public class DefaultGraphFactory 
	implements GraphElementFactory, PropertyListFactory {
	
	private int capacity;
	
	public DefaultGraphFactory() {
		this(2);
	}

	/**
	 * 
	 * @param cap the initial storage capacity for edge lists (inner and outer edges)
	 */
	public DefaultGraphFactory(int cap) {
		super();
		capacity = cap;
	}

	@Override
	public Node makeNode() {
		return new SimpleNodeImpl(capacity,this);
	}
	
	// this is used in AotNode to instantiate a simple node within the AotNode
	public static Node makeSimpleNode(GraphElementFactory factory) {
		return new SimpleNodeImpl(factory);
	}
	
	// this is used in AotEdge to instantiate a simple edge within the AotEdge
	public static Edge makeSimpleEdge(Node start, Node end, GraphElementFactory factory) {
		return new SimpleEdgeImpl(start,end,factory);
	}

	@Override
	public Edge makeEdge(Node start, Node end) {
		return new SimpleEdgeImpl(start,end,this);
	}

	@Override
	public DataNode makeNode(SimplePropertyList props) {
		if (props==null)
			throw new OmugiException("makeNode(SimplePropertyList): property list cannot be null. Use makeNode() if you want no properties.");
		return new DataNodeImpl(capacity,props,this);
	}

	@Override
	public DataEdge makeEdge(Node start, Node end, SimplePropertyList props) {
		if (props==null)
			throw new OmugiException("makeEdge(Node,Node,SimplePropertyList): property list cannot be null. Use makeEdge(Node,Node) if you want no properties.");
		return new DataEdgeImpl(start,end,props,this);
	}

	@Override
	public ReadOnlyDataNode makeNode(ReadOnlyPropertyList props) {
		if (props==null)
			throw new OmugiException("makeNode(ReadOnlyPropertyList): property list cannot be null. Use makeNode() if you want no properties.");
		return new ReadOnlyDataNodeImpl(capacity,props,this);
	}

	@Override
	public ReadOnlyDataEdge makeEdge(Node start, Node end, ReadOnlyPropertyList props) {
		if (props==null)
			throw new OmugiException("makeEdge(Node,Node,ReadOnlyPropertyList): property list cannot be null. Use makeEdge(Node,Node) if you want no properties.");
		return new ReadOnlyDataEdgeImpl(start,end,props,this);
	}

	@Override
	public ReadOnlyPropertyList makeReadOnlyPropertyList(Property... properties) {
		return new SimplePropertyListImpl(properties);
	}

	@Override
	public ReadOnlyPropertyList makeReadOnlyPropertyList(String... propertyKeys) {
		return new SimplePropertyListImpl(propertyKeys);
	}

	@Override
	public SimplePropertyList makePropertyList(Property... properties) {
		return new SimplePropertyListImpl(properties);
	}

	@Override
	public SimplePropertyList makePropertyList(String... propertyKeys) {
		return new SimplePropertyListImpl(propertyKeys);
	}

}

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
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;

/**
 * A simple factory for graph elements - mainly for testing purposes. 
 * 
 * @author Jacques Gignoux - 7 nov. 2018
 *
 */
public class DefaultGraphFactory 
	implements NodeFactory, EdgeFactory, PropertyListFactory {
	
	
	public DefaultGraphFactory() {
		super();
	}

	@Override
	public Node makeNode() {
		return new SimpleNodeImpl(this);
	}
	
	// this is used in AotNode to instantiate a simple node within the AotNode
	public static Node makeSimpleNode(NodeFactory factory) {
		return new SimpleNodeImpl(factory);
	}
	
	// this is used in AotEdge to instantiate a simple edge within the AotEdge
	public static Edge makeSimpleEdge(Node start, Node end, EdgeFactory factory) {
		return new SimpleEdgeImpl(start,end,factory);
	}

	@Override
	public Edge makeEdge(Node start, Node end) {
		return new SimpleEdgeImpl(start,end,this);
	}

	@Override
	public Node makeNode(ReadOnlyPropertyList props) {
		if (props==null)
			throw new OmugiException("makeNode(ReadOnlyPropertyList): property list cannot be null. Use makeNode() if you want no properties.");
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataNodeImpl((SimplePropertyList) props,this);
		return new ReadOnlyDataNodeImpl(props,this);
	}

	@Override
	public Edge makeEdge(Node start, Node end, ReadOnlyPropertyList props) {
		if (props==null)
			throw new OmugiException("makeEdge(Node,Node,ReadOnlyPropertyList): property list cannot be null. Use makeEdge(Node,Node) if you want no properties.");
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataEdgeImpl(start,end,(SimplePropertyList) props,this);
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

	@Override
	public Edge makeEdge(Node start, Node end, String classId, String instanceId, 
			ReadOnlyPropertyList props) {
		if (props==null)
			return new SimpleEdgeImpl(instanceId,start,end,this);
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataEdgeImpl(instanceId,start,end,(SimplePropertyList) props,this);
		return new ReadOnlyDataEdgeImpl(instanceId,start,end,props,this);
	}

	@Override
	public Node makeNode(String classId, String instanceId, ReadOnlyPropertyList props) {
		if (props==null)
			return new SimpleNodeImpl(instanceId,this);
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataNodeImpl(instanceId,(SimplePropertyList) props,this);
		return new ReadOnlyDataNodeImpl(instanceId,props,this);
	}

}

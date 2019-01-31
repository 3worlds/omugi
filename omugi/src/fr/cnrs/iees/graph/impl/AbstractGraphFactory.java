/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.identity.impl.LocalScope;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.ReadOnlyPropertyListImpl;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;

/**
 * Common ancestor for DefaultGraphFactory and TreeGraphFactory
 * 
 * @author Jacques Gignoux - 29 janv. 2019
 *
 */
public abstract class AbstractGraphFactory implements PropertyListFactory, EdgeFactory {

	protected IdentityScope scope;
	protected static String defaultNodeId = "node0";
	protected static String defaultEdgeId = "edge0";
	
	protected Logger log = null;
	
	// these to be filled by descendant constructors
	protected Map<String,Class<? extends Edge>> edgeLabels = new HashMap<>();
	protected Map<Class<? extends Edge>,String> edgeClassNames = new HashMap<>();
	
	protected AbstractGraphFactory(String factoryName) {
		super();
		scope = new LocalScope(factoryName);
	}

	// PropertyListFactory
	
	@Override
	public ReadOnlyPropertyList makeReadOnlyPropertyList(Property... properties) {
		return new ReadOnlyPropertyListImpl(properties);
	}

	@Override
	public SimplePropertyList makePropertyList(Property... properties) {
		return new SimplePropertyListImpl(properties);
	}

	@Override
	public SimplePropertyList makePropertyList(String... propertyKeys) {
		return new SimplePropertyListImpl(propertyKeys);
	}

	// EdgeFactory
	
	@Override
	public Edge makeEdge(Node start, Node end) {
		return new SimpleEdgeImpl(scope.newId(defaultEdgeId),start,end,this);
	}

	@Override
	public Edge makeEdge(Node start, Node end, String proposedId) {
		return new SimpleEdgeImpl(scope.newId(proposedId),start,end,this);
	}

	@Override
	public Edge makeEdge(Node start, Node end, ReadOnlyPropertyList props) {
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataEdgeImpl(scope.newId(defaultEdgeId),start,end,(SimplePropertyList)props,this);
		else
			return new ReadOnlyDataEdgeImpl(scope.newId(defaultEdgeId),start,end,props,this);
	}

	@Override
	public Edge makeEdge(Node start, Node end, String proposedId, ReadOnlyPropertyList props) {
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataEdgeImpl(scope.newId(proposedId),start,end,(SimplePropertyList)props,this);
		else
			return new ReadOnlyDataEdgeImpl(scope.newId(proposedId),start,end,props,this);
	}

	@Override
	public String edgeClassName(Class<? extends Edge> edgeClass) {
		return edgeClassNames.get(edgeClass);
	}

	@Override
	public Class<? extends Edge> edgeClass(String label) {
		return edgeLabels.get(label);
	}

	// TIP: getConstructor(...) fails to return the constructor even if the parameter types are
	// correct because it is meant to return only PUBLIC constructors and the Edge descendant
	// constructors are all PROTECTED. getDeclaredConstructor(...) fixes the problem.
	private Constructor<? extends Edge> getEdgeConstructorNoProps(Class<? extends Edge> edgeClass) {
		Constructor<? extends Edge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				EdgeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
		}
		return c;
	}

	// TIP: getConstructor(...) fails to return the constructor even if the parameter types are
	// correct because it is meant to return only PUBLIC constructors and the Edge descendant
	// constructors are all PROTECTED. getDeclaredConstructor(...) fixes the problem.
	private Constructor<? extends Edge> getEdgeConstructorProps(Class<? extends Edge> edgeClass) {
		Constructor<? extends Edge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				ReadOnlyPropertyList.class,EdgeFactory.class);
		} catch (Exception e) {
			try {
				c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
					SimplePropertyList.class,NodeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
			}			
		}
		return c;
	}

	@Override
	public Edge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, 
		String proposedId,ReadOnlyPropertyList props) {
		Constructor<? extends Edge> c = getEdgeConstructorProps(edgeClass);
		Identity id = scope.newId(proposedId);
		try {
			return c.newInstance(id,start,end,props,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Edge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, String proposedId) {
		Constructor<? extends Edge> c = getEdgeConstructorNoProps(edgeClass);
		Identity id = scope.newId(proposedId);
		try {
			return c.newInstance(id,start,end,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Edge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, ReadOnlyPropertyList props) {
		Constructor<? extends Edge> c = getEdgeConstructorProps(edgeClass);
		Identity id = scope.newId();
		try {
			return c.newInstance(id,start,end,props,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Edge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end) {
		Constructor<? extends Edge> c = getEdgeConstructorNoProps(edgeClass);
		Identity id = scope.newId();
		try {
			return c.newInstance(id,start,end,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}


}

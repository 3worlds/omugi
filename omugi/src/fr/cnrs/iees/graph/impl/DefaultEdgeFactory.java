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
import java.util.logging.Logger;

import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 *  A default factory for edges, for multiple inheritance in descendants.
 *  It is actually almost an implementation. It must be in this package because it
 *  instantiates classes in this package which have protected constructors.
 *  
 * @author Jacques Gignoux - 4 avr. 2019
 *
 */
public interface DefaultEdgeFactory extends EdgeFactory {

	public static String defaultEdgeId = "edge0";
	
	@Override
	public default Edge makeEdge(Node start, Node end) {
		return new SimpleEdgeImpl(scope().newId(defaultEdgeId),start,end,this);
	}

	@Override
	public default Edge makeEdge(Node start, Node end, 
			ReadOnlyPropertyList props) {
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataEdgeImpl(scope().newId(defaultEdgeId),start,end,(SimplePropertyList)props,this);
		else
			return new ReadOnlyDataEdgeImpl(scope().newId(defaultEdgeId),start,end,props,this);
	}

	@Override
	public default Edge makeEdge(Node start, Node end, 
			String proposedId) {
		return new SimpleEdgeImpl(scope().newId(proposedId),start,end,this);
	}

	@Override
	public default Edge makeEdge(Node start, Node end, 
			String proposedId, 
			ReadOnlyPropertyList props) {
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataEdgeImpl(scope().newId(proposedId),start,end,(SimplePropertyList)props,this);
		else
			return new ReadOnlyDataEdgeImpl(scope().newId(proposedId),start,end,props,this);
	}

	// TIP: In all following methods, getConstructor(...) fails to return the constructor even if the parameter types are
	// correct because it is meant to return only PUBLIC constructors and the Edge descendant
	// constructors are all PROTECTED. getDeclaredConstructor(...) fixes the problem.
	
	@Override
	public default Edge makeEdge(Class<? extends Edge> edgeClass, 
			Node start, Node end, 
			String proposedId,
			ReadOnlyPropertyList props) {
		Logger log = Logger.getLogger(DefaultEdgeFactory.class.getName());
		Constructor<? extends Edge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				ReadOnlyPropertyList.class,EdgeFactory.class);
		} catch (Exception e) {
			try {
				c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
					SimplePropertyList.class,EdgeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope().newId(proposedId);
		try {
			return c.newInstance(id,start,end,props,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public default Edge makeEdge(Class<? extends Edge> edgeClass, 
			Node start, Node end, 
			String proposedId) {
		Logger log = Logger.getLogger(DefaultEdgeFactory.class.getName());
		Constructor<? extends Edge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				EdgeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
		}
		Identity id = scope().newId(proposedId);
		try {
			return c.newInstance(id,start,end,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public default Edge makeEdge(Class<? extends Edge> edgeClass, 
			Node start, Node end, 
			ReadOnlyPropertyList props) {
		return makeEdge(edgeClass,start,end,defaultEdgeId,props);
	}

	@Override
	public default Edge makeEdge(Class<? extends Edge> edgeClass, 
			Node start, Node end) {
		return makeEdge(edgeClass,start,end,defaultEdgeId);
	}

}

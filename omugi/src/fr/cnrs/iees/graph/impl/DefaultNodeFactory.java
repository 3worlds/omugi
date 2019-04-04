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

import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 *  A default factory for nodes, for multiple inheritance in descendants.
 *  It is actually almost an implementation. It must be in this package because it
 *  instantiates classes in this package which have protected constructors.
 *  
 * @author Jacques Gignoux - 4 avr. 2019
 *
 */
public interface DefaultNodeFactory extends NodeFactory {

	public static String defaultNodeId = "node0";
	
	@Override
	public default Node makeNode() {
		return new SimpleNodeImpl(scope().newId(defaultNodeId),this);
	}

	@Override
	public default Node makeNode(ReadOnlyPropertyList props) {
		return makeNode(defaultNodeId,props);
	}

	@Override
	public default Node makeNode(String proposedId) {
		return new SimpleNodeImpl(scope().newId(proposedId),this);
	}

	@Override
	public default Node makeNode(String proposedId, ReadOnlyPropertyList props) {
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataNodeImpl(scope().newId(proposedId),(SimplePropertyList)props,this);
		else
			return new ReadOnlyDataNodeImpl(scope().newId(proposedId),props,this);
	}
	
	// TIP: In all following methods, getConstructor(...) fails to return the constructor even if the parameter types are
	// correct because it is meant to return only PUBLIC constructors and the Node/TreeNode descendant
	// constructors are all PROTECTED. getDeclaredConstructor(...) fixes the problem.

	@Override
	public default Node makeNode(Class<? extends Node> nodeClass, 
			String proposedId, 
			ReadOnlyPropertyList props) {
		Logger log = Logger.getLogger(DefaultNodeFactory.class.getName());
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,ReadOnlyPropertyList.class,NodeFactory.class);
		} catch (Exception e) {
			try {
				c = nodeClass.getDeclaredConstructor(Identity.class,SimplePropertyList.class,NodeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope().newId(proposedId);
		try {
			return c.newInstance(id,props,this);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public default Node makeNode(Class<? extends Node> nodeClass, String proposedId) {
		Logger log = Logger.getLogger(DefaultNodeFactory.class.getName());
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,NodeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
		}
		Identity id = scope().newId(proposedId);
		try {
			return c.newInstance(id,this);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public default Node makeNode(Class<? extends Node> nodeClass, ReadOnlyPropertyList props) {
		return makeNode(nodeClass,defaultNodeId,props);
	}

	@Override
	public default Node makeNode(Class<? extends Node> nodeClass) {
		return makeNode(nodeClass,defaultNodeId);
	}

}

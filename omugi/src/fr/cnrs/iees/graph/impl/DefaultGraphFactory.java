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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * A simple factory for graph elements - mainly for testing purposes. Only makes plain
 * Edges, Nodes, Property lists and TreeNodes.
 * 
 * @author Jacques Gignoux - 7 nov. 2018
 *
 */
public class DefaultGraphFactory 
	extends AbstractGraphFactory
	implements NodeFactory, TreeNodeFactory {
	
	private Logger log = Logger.getLogger(DefaultGraphFactory.class.getName());
	private Map<String,Class<? extends Node>> labels = new HashMap<>();
	private Map<Class<? extends Node>,String> nodeClassNames = new HashMap<>();
	
	public DefaultGraphFactory() {
		super("DGF");
	}
	
	@SuppressWarnings("unchecked")
	public DefaultGraphFactory(Map<String,String> labels) {
		super("DGF");
		for (String label:labels.keySet()) {
			try {
				Class<? extends Node> c = (Class<? extends Node>) Class.forName(labels.get(label));
				this.labels.put(label,c);
				nodeClassNames.put(c,label);
			} catch (ClassNotFoundException e) {
				log.severe(()->"Node class \""+labels.get(label)+"\" for label \""+label+"\" not found");
			}
		}
	}

	// NodeFactory
	
	@Override
	public Node makeNode() {
		return new SimpleNodeImpl(scope.newId(defaultNodeId),this);
	}

	@Override
	public Node makeNode(String proposedId) {
		return new SimpleNodeImpl(scope.newId(proposedId),this);
	}
	
	@Override
	public Node makeNode(ReadOnlyPropertyList props) {
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataNodeImpl(scope.newId(defaultNodeId),(SimplePropertyList)props,this);
		else
			return new ReadOnlyDataNodeImpl(scope.newId(defaultNodeId),props,this);
	}
	
	@Override
	public Node makeNode(String proposedId, ReadOnlyPropertyList props) {
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataNodeImpl(scope.newId(proposedId),(SimplePropertyList)props,this);
		else
			return new ReadOnlyDataNodeImpl(scope.newId(proposedId),props,this);
	}
	
	@Override
	public String nodeClassName(Class<? extends Node> nodeClass) {
		return nodeClassNames.get(nodeClass);
	}

	@Override
	public Class<? extends Node> nodeClass(String label) {
		return labels.get(label);
	}
	
	private Constructor<? extends Node> getNodeConstructorNoProps(Class<? extends Node> nodeClass) {
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getConstructor(Identity.class,NodeFactory.class);
		} catch (NoSuchMethodException e) {
			log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	private Constructor<? extends Node> getNodeConstructorProps(Class<? extends Node> nodeClass) {
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getConstructor(Identity.class,ReadOnlyPropertyList.class,NodeFactory.class);
		} catch (NoSuchMethodException e) {
			try {
				c = nodeClass.getConstructor(Identity.class,SimplePropertyList.class,NodeFactory.class);
			} catch (NoSuchMethodException e1) {
				log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}


	@Override 
	public Node makeNode(Class<? extends Node> nodeClass, 
			String proposedId, 
			ReadOnlyPropertyList props) {
		Constructor<? extends Node> c = getNodeConstructorProps(nodeClass);
		Identity id = scope.newId(proposedId);
		Node n;
		try {
			n = c.newInstance(id,props,this);
			return n;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Node makeNode(Class<? extends Node> nodeClass, 
			String proposedId) {
		try {
			Constructor<? extends Node> c = 
				nodeClass.getConstructor(Identity.class,NodeFactory.class);
			Identity id = scope.newId(proposedId);
			Node n = c.newInstance(id,this);
			return n;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Node makeNode(Class<? extends Node> nodeClass, 
			ReadOnlyPropertyList props) {
		try {
			Constructor<? extends Node> c = 
				nodeClass.getConstructor(ReadOnlyPropertyList.class,NodeFactory.class);
			Identity id = scope.newId();
			Node n = c.newInstance(id,props,this);
			return n;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Node makeNode(Class<? extends Node> nodeClass) {
		try {
			Constructor<? extends Node> c =	nodeClass.getConstructor(NodeFactory.class);
			Identity id = scope.newId();
			Node n = c.newInstance(id,this);
			return n;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	
	// TreeNodeFactory

	@Override
	public TreeNode makeTreeNode(TreeNode parent) {
		TreeNode result = new SimpleTreeNodeImpl(scope.newId(defaultNodeId),this);
		result.setParent(parent);
		if (parent!=null)
			parent.addChild(result);
		return result;
	}

	@Override
	public TreeNode makeTreeNode(TreeNode parent, String proposedId) {
		TreeNode result = new SimpleTreeNodeImpl(scope.newId(proposedId),this);
		result.setParent(parent);
		if (parent!=null)
			parent.addChild(result);
		return result;
	}

	@Override
	public TreeNode makeTreeNode(TreeNode parent, SimplePropertyList properties) {
		TreeNode result = new DataTreeNodeImpl(scope.newId(defaultNodeId),properties,this);
		result.setParent(parent);
		if (parent!=null)
			parent.addChild(result);
		return result;
	}

	@Override
	public TreeNode makeTreeNode(TreeNode parent, String proposedId, SimplePropertyList properties) {
		TreeNode result = new DataTreeNodeImpl(scope.newId(proposedId),properties,this);
		result.setParent(parent);
		if (parent!=null)
			parent.addChild(result);
		return result;
	}

}

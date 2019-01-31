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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * A simple factory for graph elements - mainly for testing purposes. Can instantiate any descendant
 * of Node, TreeNode, and Edge.
 * 
 * @author Jacques Gignoux - 7 nov. 2018
 *
 */
public class DefaultGraphFactory 
	extends AbstractGraphFactory
	implements NodeFactory, TreeNodeFactory {
	
	private Map<String,Class<? extends Node>> nodeLabels = new HashMap<>();
	private Map<Class<? extends Node>,String> nodeClassNames = new HashMap<>();
	private Map<String,Class<? extends TreeNode>> treeNodeLabels = new HashMap<>();
	private Map<Class<? extends TreeNode>,String> treeNodeClassNames = new HashMap<>();
	
	// constructors
	
	public DefaultGraphFactory() {
		super("DGF");
		log = Logger.getLogger(DefaultGraphFactory.class.getName());
	}
	
	@SuppressWarnings("unchecked")
	public DefaultGraphFactory(Map<String,String> labels) {
		super("DGF");
		log = Logger.getLogger(DefaultGraphFactory.class.getName());
		for (String label:labels.keySet()) {
			try {
				Class<?> c = Class.forName(labels.get(label));
				if (Node.class.isAssignableFrom(c)) {
					nodeLabels.put(label,(Class<? extends Node>) c);
					nodeClassNames.put((Class<? extends Node>) c,label);
				}
				else if (Edge.class.isAssignableFrom(c)) {
					edgeLabels.put(label,(Class<? extends Edge>) c);
					edgeClassNames.put((Class<? extends Edge>) c, label);
				}
				else if (TreeNode.class.isAssignableFrom(c)) {
					treeNodeLabels.put(label,(Class<? extends TreeNode>) c);
					treeNodeClassNames.put((Class<? extends TreeNode>) c,label);
				}
			} catch (ClassNotFoundException e) {
				log.severe(()->"Class \""+labels.get(label)+"\" for label \""+label+"\" not found");
			}
		}
	}

	// NodeFactory
	
	@Override
	public Node makeNode() {
		return makeNode(defaultNodeId);
	}

	@Override
	public Node makeNode(String proposedId) {
		return new SimpleNodeImpl(scope.newId(proposedId),this);
	}
	
	@Override
	public Node makeNode(ReadOnlyPropertyList props) {
		return makeNode(defaultNodeId,props);
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
		return nodeLabels.get(label);
	}
	
	@Override 
	public Node makeNode(Class<? extends Node> nodeClass, 
			String proposedId, 
			ReadOnlyPropertyList props) {
		Constructor<? extends Node> c = getNodeConstructorProps(nodeClass);
		Identity id = scope.newId(proposedId);
		try {
			return c.newInstance(id,props,this);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Node makeNode(Class<? extends Node> nodeClass, 
			String proposedId) {
		Constructor<? extends Node> c = getNodeConstructorNoProps(nodeClass);
		Identity id = scope.newId(proposedId);
		try {
			return c.newInstance(id,this);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Node makeNode(Class<? extends Node> nodeClass, 
			ReadOnlyPropertyList props) {
		return makeNode(nodeClass,defaultNodeId,props);
	}

	@Override
	public Node makeNode(Class<? extends Node> nodeClass) {
		return makeNode(nodeClass,defaultNodeId);
	}
	
	// TreeNodeFactory

	@Override
	public TreeNode makeTreeNode(TreeNode parent) {
		return makeTreeNode(parent,defaultNodeId);
	}

	@Override
	public TreeNode makeTreeNode(TreeNode parent, String proposedId) {
		TreeNode result = new SimpleTreeNodeImpl(scope.newId(proposedId),this);
		connectToParent(result,parent);
		return result;
	}

	@Override
	public TreeNode makeTreeNode(TreeNode parent, SimplePropertyList properties) {
		return makeTreeNode(parent,defaultNodeId,properties);
	}

	@Override
	public TreeNode makeTreeNode(TreeNode parent, String proposedId, SimplePropertyList properties) {
		TreeNode result = new DataTreeNodeImpl(scope.newId(proposedId),properties,this);
		connectToParent(result,parent);
		return result;
	}
	
	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent) {
		return makeTreeNode(treeNodeClass,parent,defaultNodeId);
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent,
			SimplePropertyList properties) {
		return makeTreeNode(treeNodeClass,parent,defaultNodeId,properties);
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, 
			TreeNode parent, String proposedId) {
		Constructor<? extends TreeNode> c = getTreeNodeConstructorNoProps(treeNodeClass);
		Identity id = scope.newId(proposedId);
		try {
			TreeNode tn = c.newInstance(id,this); 
			connectToParent(tn,parent);
			return tn;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+treeNodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent, String proposedId,
			SimplePropertyList properties) {
		Constructor<? extends TreeNode> c = getTreeNodeConstructorProps(treeNodeClass);
		Identity id = scope.newId(proposedId);
		try {
			TreeNode tn = c.newInstance(id,properties,this); 
			connectToParent(tn,parent);
			return tn;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+treeNodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	public String treeNodeClassName(Class<? extends TreeNode> nodeClass) {
		return treeNodeClassNames.get(nodeClass);
	}
	
	public Class<? extends TreeNode> treeNodeClass(String label) {
		return treeNodeLabels.get(label);
	}
	
	// TIP: In all following methods, getConstructor(...) fails to return the constructor even if the parameter types are
	// correct because it is meant to return only PUBLIC constructors and the Node/TreeNode descendant
	// constructors are all PROTECTED. getDeclaredConstructor(...) fixes the problem.

	private Constructor<? extends Node> getNodeConstructorNoProps(Class<? extends Node> nodeClass) {
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,NodeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
		}
		return c;
	}

	private Constructor<? extends Node> getNodeConstructorProps(Class<? extends Node> nodeClass) {
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
		return c;
	}
	
	private Constructor<? extends TreeNode> getTreeNodeConstructorNoProps(Class<? extends TreeNode> nodeClass) {
		Constructor<? extends TreeNode> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,TreeNodeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
		}
		return c;
	}

	private Constructor<? extends TreeNode> getTreeNodeConstructorProps(Class<? extends TreeNode> nodeClass) {
		Constructor<? extends TreeNode> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,
				ReadOnlyPropertyList.class,TreeNodeFactory.class);
		} catch (Exception e) {
			try {
				c = nodeClass.getDeclaredConstructor(Identity.class,
					SimplePropertyList.class,TreeNodeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
			}			
		}
		return c;
	}


}

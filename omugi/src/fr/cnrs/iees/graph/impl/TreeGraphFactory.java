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

import fr.cnrs.iees.OmugiClassLoader;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * A factory for TreeGraphs, ie nodes are both Nodes and TreeNodes. Can instantiate any descendant
 * of TreeGraphNode and Edge.
 * 
 * @author Jacques Gignoux - 25 janv. 2019
 *
 */
public class TreeGraphFactory 
	extends AbstractGraphFactory
	implements TreeNodeFactory, NodeFactory {

	private Map<String,Class<? extends TreeGraphNode>> nodeLabels = new HashMap<>();
	private Map<Class<? extends TreeGraphNode>,String> nodeClassNames = new HashMap<>();

	// Constructors ---------------------------------------------------------
	
	public TreeGraphFactory() {
		super("TGF");
		log = Logger.getLogger(TreeGraphFactory.class.getName());
	}
	
	@SuppressWarnings("unchecked")
	public TreeGraphFactory(Map<String,String> labels) {
		super("TGF");
		log = Logger.getLogger(DefaultGraphFactory.class.getName());
		for (String label:labels.keySet()) {
			try {
				Class<?> c = Class.forName(labels.get(label),false,OmugiClassLoader.getClassLoader());
				if (TreeGraphNode.class.isAssignableFrom(c)) {
					nodeLabels.put(label,(Class<? extends TreeGraphNode>) c);
					nodeClassNames.put((Class<? extends TreeGraphNode>) c,label);
				}
				else if (Edge.class.isAssignableFrom(c)) {
					edgeLabels.put(label,(Class<? extends Edge>) c);
					edgeClassNames.put((Class<? extends Edge>) c, label);
				}
			} catch (ClassNotFoundException e) {
				log.severe(()->"Class \""+labels.get(label)+"\" for label \""+label+"\" not found");
			}
		}
	}
	
	
	// NodeFactory ------------------------------------------------------------
	// CAUTION: all these methods return a free-floating node, i.e. with parent 
	// not set and no children.
	// Use with caution (prefer makeTreeNode(...))

	@Override
	public TreeGraphNode makeNode(String proposedId, ReadOnlyPropertyList props) {
		TreeGraphNode result = new TreeGraphNode(scope.newId(proposedId),this,this,props);
		return result;
	}
	
	@Override
	public TreeGraphNode makeNode(ReadOnlyPropertyList props) {
		return makeNode(defaultNodeId,props);
	}

	@Override
	public TreeGraphNode makeNode() {
		return makeNode(defaultNodeId,null);
	}

	@Override
	public TreeGraphNode makeNode(String proposedId) {
		return makeNode(proposedId,null);
	}

	private Constructor<? extends TreeGraphNode> getNodeConstructor(Class<? extends TreeGraphNode> nodeClass) {
		Constructor<? extends TreeGraphNode> c=null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class, TreeNodeFactory.class,
				NodeFactory.class,ReadOnlyPropertyList.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
		}
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public TreeGraphNode makeNode(Class<? extends Node> nodeClass, 
			String proposedId, ReadOnlyPropertyList props) {
		Constructor<? extends TreeGraphNode> c = 
			getNodeConstructor((Class<? extends TreeGraphNode>) nodeClass);
		Identity id = scope.newId(proposedId);
		try {
			TreeGraphNode tgn = c.newInstance(id,this,this,props);
			return tgn;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public TreeGraphNode makeNode(Class<? extends Node> nodeClass, String proposedId) {
		return makeNode(nodeClass,proposedId,null);
	}

	@Override
	public TreeGraphNode makeNode(Class<? extends Node> nodeClass, ReadOnlyPropertyList props) {
		return makeNode(nodeClass,defaultNodeId,props);
	}

	@Override
	public TreeGraphNode makeNode(Class<? extends Node> nodeClass) {
		return makeNode(nodeClass,defaultNodeId,null);
	}
	
	@Override
	public String nodeClassName(Class<? extends Node> nodeClass) {
		return nodeClassNames.get(nodeClass);
	}

	@Override
	public Class<? extends Node> nodeClass(String label) {
		return nodeLabels.get(label);
	}

	
	// TreeNodeFactory ------------------------------------------------------------
	// These methods return a node properly placed in the tree, according to its parent argument. This
	// This should be the proper way to create TreeGraphNodes.
	
	@Override
	public TreeGraphNode makeTreeNode(TreeNode parent, String proposedId, SimplePropertyList properties) {
		TreeGraphNode result = new TreeGraphNode(scope.newId(proposedId),this,this,properties);
		connectToParent(result,parent);
		return result;
	}

	@Override
	public TreeGraphNode makeTreeNode(TreeNode parent, SimplePropertyList properties) {
		return makeTreeNode(parent,defaultNodeId,properties);
	}
	
	@Override
	public TreeGraphNode makeTreeNode(TreeNode parent,String proposedId) {
		return makeTreeNode(parent,proposedId,null);
	}
	
	@Override
	public TreeGraphNode makeTreeNode(TreeNode parent) {
		return makeTreeNode(parent,defaultNodeId,null);
	}

	@Override
	public TreeGraphNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent) {
		return makeTreeNode(treeNodeClass,parent,defaultNodeId,null);
	}

	@Override
	public TreeGraphNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent,
			SimplePropertyList properties) {
		return makeTreeNode(treeNodeClass,parent,defaultNodeId,properties);
	}

	@Override
	public TreeGraphNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent, 
			String proposedId) {
		return makeTreeNode(treeNodeClass,parent,proposedId,null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TreeGraphNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent, 
			String proposedId,SimplePropertyList properties) {
		Constructor<? extends TreeGraphNode> c = 
			getNodeConstructor((Class<? extends TreeGraphNode>) treeNodeClass);
		Identity id = scope.newId(proposedId);
		try {
			TreeGraphNode tgn = c.newInstance(id,this,this,properties);
			connectToParent(tgn,parent);
			return tgn;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+treeNodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}
	
	public String treeNodeClassName(Class<? extends TreeNode> nodeClass) {
		return nodeClassNames.get(nodeClass);
	}
	
	public Class<? extends TreeNode> treeNodeClass(String label) {
		return nodeLabels.get(label);
	}
	
}

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.ens.biologie.generic.utils.Logging;

/**
 * The factory for simple trees - caution: this is NOT an Edge factory
 * @author Jacques Gignoux - 13 mai 2019
 *
 */
public class SimpleTreeFactory 
		extends NodeFactoryAdapter {

	private static Logger log = Logging.getLogger(ALGraphFactory.class);
	private Set<Tree<SimpleTreeNode>> trees = new HashSet<>();

	/**
	 * constructor with labels for sub-classes of (Tree)Node 
	 * @param scopeName the scope identifier, e.g. "GraphFactory"
	 * @param labels a Map of (labels,class names) associating a label to a java class name
	 */
	public SimpleTreeFactory(String scopeName,Map<String,String> labels) {
		super(scopeName,labels);
	}

	/**
	 * basic constructor with scope only 
	 * @param scopeName the scope identifier, e.g. "GraphFactory"
	 */
	public SimpleTreeFactory(String scopeName) {
		super(scopeName);
	}
	
	/**
	 * basic constructor, only requires a scope
	 */
	public SimpleTreeFactory() {
		super("DTF"); // stands for "default tree factory"
	}


	@SuppressWarnings("unchecked")
	@Override
	public void manageGraph(NodeSet<? extends Node> graph) {
		if (graph instanceof SimpleTree)
			trees.add((SimpleTree<SimpleTreeNode>) graph);
	}

	@Override
	public void unmanageGraph(NodeSet<? extends Node> graph) {
		trees.remove(graph);
	}
	
	private void addNodeToTrees(SimpleTreeNode node) {
		for (Tree<SimpleTreeNode> tree:trees)
			tree.addNode(node);
	}
	
	@Override
	public SimpleTreeNode makeNode() {
		return makeNode(defaultNodeId);
	}

	@Override
	public SimpleTreeNode makeNode(String proposedId) {
		SimpleTreeNode result = new SimpleTreeNode(scope.newId(true,proposedId),this);
		addNodeToTrees(result);
		return result;
	}

	@Override
	public SimpleTreeNode makeNode(ReadOnlyPropertyList props) {
		return makeNode(defaultNodeId,props);
	}
	
	@Override
	public SimpleTreeNode makeNode(String proposedId, ReadOnlyPropertyList props) {
		SimpleTreeNode result = null;
		if (props instanceof SimplePropertyList)
			result = new SimpleDataTreeNode(scope.newId(true,proposedId),(SimplePropertyList) props,this);
		else
			result = new SimpleReadOnlyDataTreeNode(scope.newId(true,proposedId),props,this);
		addNodeToTrees(result);
		return result;
	}
	
	@Override
	public SimpleTreeNode makeNode(Class<? extends Node> nodeClass, ReadOnlyPropertyList props) {
		return makeNode(nodeClass,defaultNodeId,props);
	}

	@Override
	public SimpleTreeNode makeNode(Class<? extends Node> nodeClass, String proposedId, ReadOnlyPropertyList props) {
		SimpleTreeNode result = null;
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,
				ReadOnlyPropertyList.class,
				NodeFactory.class);
		} catch (Exception e) {
			try {
				c = nodeClass.getDeclaredConstructor(Identity.class,
					SimplePropertyList.class,
					NodeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope.newId(true,proposedId);
		try {
			result = (SimpleTreeNode) c.newInstance(id,props,this);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		addNodeToTrees(result);
		return result;
	}

	@Override
	public SimpleTreeNode makeNode(Class<? extends Node> nodeClass) {
		return makeNode(nodeClass,defaultNodeId);
	}

	@Override
	public SimpleTreeNode makeNode(Class<? extends Node> nodeClass, String proposedId) {
		SimpleTreeNode result = null;
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,NodeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
		}
		Identity id = scope.newId(true,proposedId);
		try {
			result = (SimpleTreeNode) c.newInstance(id,this);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		addNodeToTrees(result);
		return result;
	}

	protected void onParentChanged() {
		for (Tree<SimpleTreeNode> tree:trees)
			tree.onParentChanged();
	}

	
}

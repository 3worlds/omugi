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

import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
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
	
	public DefaultGraphFactory() {
		super("DGF");
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

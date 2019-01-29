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

import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * A factory for TreeGraphs, ie nodes are both Nodes and TreeNodes. Only makes plain
 * Edges, Property lists, and TreeGraphNodes.
 * 
 * @author Jacques Gignoux - 25 janv. 2019
 *
 */
public class TreeGraphFactory 
	extends AbstractGraphFactory
	implements TreeNodeFactory, NodeFactory, EdgeFactory {

	private IdentityScope scope;
	
	public TreeGraphFactory() {
		super("TGF");
	}
	
	// NodeFactory

	/**
	 * returns a free-floating node, i.e. with parent not set and no children.
	 * Use with caution (prefer makeTreeNode(...)
	 */
	@Override
	public TreeGraphNode makeNode(String proposedId, ReadOnlyPropertyList props) {
		TreeGraphNode result = new TreeGraphNode(scope.newId(proposedId),this,this,props);
		return result;
	}
	
	/**
	 * returns a free-floating node, i.e. with parent not set and no children.
	 * Use with caution (prefer makeTreeNode(...)
	 */
	@Override
	public TreeGraphNode makeNode(ReadOnlyPropertyList props) {
		TreeGraphNode result = new TreeGraphNode(scope.newId(defaultNodeId),this,this,props);
		return result;
	}

	
	// TreeNodeFactory

	/**
	 * returns a node properly placed in the tree, according to its parent argument. This
	 * should be the proper way to create TreeGraphNodes.
	 */
	@Override
	public TreeGraphNode makeTreeNode(TreeNode parent, String proposedId, SimplePropertyList properties) {
		TreeGraphNode result = new TreeGraphNode(scope.newId(proposedId),this,this,properties);
		result.setParent(parent);
		if (parent!=null)
			parent.addChild(result);
		return result;
	}

	/**
	 * returns a node properly placed in the tree, according to its parent argument. This
	 * should be the proper way to create TreeGraphNodes.
	 */
	@Override
	public TreeGraphNode makeTreeNode(TreeNode parent, SimplePropertyList properties) {
		TreeGraphNode result = new TreeGraphNode(scope.newId(defaultNodeId),this,this,properties);
		result.setParent(parent);
		if (parent!=null)
			parent.addChild(result);
		return result;
	}
	
	
}

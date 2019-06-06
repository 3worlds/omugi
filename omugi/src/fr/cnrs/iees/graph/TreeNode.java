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
package fr.cnrs.iees.graph;

import java.util.Collection;

import au.edu.anu.rscs.aot.collections.QuickListOfLists;

public interface TreeNode extends Node {

	/**
	 * Gets the parent node. Returns null if this is the tree root.
	 * @return the parent of this node
	 */
	public TreeNode getParent();
	
	/**
	 * Set the argument as this node's parent. CAUTION: no consistency checks! this is the
	 * tree's job.
	 * 
	 * @param parent the parent node
	 */
	void connectParent(TreeNode parent);
	
	/**
	 * Gets the children nodes.
	 * @return the list of children nodes.
	 */
	public Iterable<? extends TreeNode> getChildren();
	
	/**
	 * Adds a node as a child. CAUTION: no consistency checks! this is the
	 * tree's job.
	 * 
	 * @param child the node to add
	 */
	public void connectChild(TreeNode child);
	
	/**
	 * Adds a set of nodes as children
	 * @param children the nodes to add
	 */
	public default void connectChildren(TreeNode... children) {
		for (TreeNode child:children)
			connectChild(child);
	}
	
	public default void connectChildren(Iterable<? extends TreeNode> children) {
		for (TreeNode child:children)
			connectChild(child);
	}

	public default void connectChildren(Collection<? extends TreeNode> children) {
		for (TreeNode child:children)
			connectChild(child);
	}
	
	public boolean hasChildren();

	/**
	 * Gets the number of children of this TreeNode
	 * @return the number of child nodes
	 */
	public int nChildren();
	
	@SuppressWarnings("unchecked")
	public default Iterable<? extends TreeNode> subTree() {
		QuickListOfLists<TreeNode> result = new QuickListOfLists<>();
		if (hasChildren()) {
			result.addList((Iterable<TreeNode>) getChildren());
			for (TreeNode child: getChildren()) 
				result.addList((Iterable<TreeNode>) child.subTree());
		}
		return result;
	}
	
}

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
package fr.cnrs.iees.omugi.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The node interface to use in {@link Tree}s.
 * 
 * @author Jacques Gignoux - 17 août 2021
 *
 */
public interface TreeNode extends Node {

	/**
	 * Gets the parent node. Returns {@code null} if this is the tree root.
	 * 
	 * @return the parent of this node
	 */
	public TreeNode getParent();
	
	/**
	 * Set the argument as this node's parent. CAUTION: no consistency checks! this is the
	 * tree's job (cf {@link Tree#onParentChanged()}).
	 * 
	 * @param parent the parent node
	 */
	void connectParent(TreeNode parent);
	
	/**
	 * Gets the children nodes.
	 * 
	 * @return an immutable collection of children nodes.
	 */
	public Collection<? extends TreeNode> getChildren();
	
	/**
	 * Adds a node as a child. CAUTION: no consistency checks! this is the
	 * tree's job (cf {@link Tree#onParentChanged()}).
	 * 
	 * @param child the node to add
	 */
	public void connectChild(TreeNode child);
	
	/**
	 * Adds nodes as children
	 * 
	 * @param children the nodes to add
	 */
	public default void connectChildren(TreeNode... children) {
		for (TreeNode child:children)
			connectChild(child);
	}
	
	/**
	 * Adds a set of nodes as children.
	 * 
	 * @param children the nodes to add
	 */
	public default void connectChildren(Collection<? extends TreeNode> children) {
		for (TreeNode child:children)
			connectChild(child);
	}
	
	/**
	 * Test if this instance is a leaf node.
	 * 
	 * @return {@code true} if this node has children
	 */
	public boolean hasChildren();

	/**
	 * Get the number of children of this node.
	 * 
	 * @return the number of child nodes
	 */
	public int nChildren();
	
	// recursive
	private void subTree(List<TreeNode> list, TreeNode parent) {
		list.add(parent);
		if (parent.hasChildren())
			for (TreeNode tn:parent.getChildren())
				subTree(list,tn);
	}
	
	/**
	 * Get all the sub-tree starting at this instance.
	 * 
	 * @return the subtree starting at this node (=this node + all its children's children)
	 */
	public default Collection<? extends TreeNode> subTree() {
		List<TreeNode> result = new LinkedList<TreeNode>();
		subTree(result,this);
		return Collections.unmodifiableCollection(result);
	}
	
}

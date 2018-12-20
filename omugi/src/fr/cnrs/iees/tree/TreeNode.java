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
package fr.cnrs.iees.tree;

import java.util.Collection;

import fr.cnrs.iees.Identifiable;
import fr.ens.biologie.generic.Textable;

/**
 * A Node member of a tree graph. Introduces the concepts of parents and children.
 * 
 * @author Jacques Gignoux - 4 déc. 2018
 *
 */
public interface TreeNode extends Identifiable, Textable {
	
	public static String TREE_NODE_LABEL = "tree_node";

	/**
	 * Gets the parent node. Returns null if this is the tree root.
	 * @return the parent of this node
	 */
	public TreeNode getParent();
	
	/**
	 * Set the argument as this node's parent.
	 * @param parent the parent node
	 */
	public void setParent(TreeNode parent);
	
	/**
	 * Gets the children nodes.
	 * @return the list of children nodes.
	 */
	public Iterable<TreeNode> getChildren();
	
	/**
	 * Adds a node as a child.
	 * @param child the node to add
	 */
	public void addChild(TreeNode child);
	
	/**
	 * Adds a set of nodes as children
	 * @param children the nodes to add
	 */
	public void setChildren(TreeNode... children);
	public void setChildren(Iterable<TreeNode> children);
	public void setChildren(Collection<TreeNode> children);
	
	public boolean hasChildren();
	
	@Override
	public default String classId() {
		return TREE_NODE_LABEL;
	}

	public TreeNodeFactory factory();
	
}

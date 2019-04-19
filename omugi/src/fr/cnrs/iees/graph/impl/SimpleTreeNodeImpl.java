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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;

/**
 * Basic implementation of {@link TreeNode} without data and no possibility to set
 * the instance ID (it's entirely automatic).
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
public class SimpleTreeNodeImpl implements TreeNode {
	
	private TreeNodeFactory factory = null;
	private TreeNode parent = null;
	private Set<TreeNode> children = null;
	private Identity id = null;
	
	// --- Constructors

	protected SimpleTreeNodeImpl(Identity id, TreeNodeFactory factory) {
		super();
		this.id = id;
		children = new HashSet<TreeNode>();
		this.factory = factory;
	}
	
	// Identity

	@Override
	public IdentityScope scope() {
		return id.scope();
	}

	@Override
	public String id() {
		return id.id();
	}

	// --- TreeNode
	
	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public void setParent(TreeNode parent) {
		this.parent = parent;
		// Shouldn't this be done here?
		//parent.addChild(this);
	}

	@Override
	public Iterable<TreeNode> getChildren() {
		return children;
	}
	
	@Override
	public void addChild(TreeNode child) {
		children.add(child);
	}

	@Override
	public void setChildren(TreeNode... children) {
		for (TreeNode child:children)
			this.children.add(child);
	}

	@Override
	public void setChildren(Iterable<TreeNode> children) {
		for (TreeNode child:children)
			this.children.add(child);
	}

	@Override
	public void setChildren(Collection<TreeNode> children) {
		this.children.addAll(children);
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}
	
	@Override
	public boolean hasChild(TreeNode child) {
		return children.contains(child);
	}

	@Override
	public int nChildren() {
		return children.size();
	}

	// Textable
	
	/**
	 * Displays a TreeNode as follows (on a single line):
	 * 
	 * <pre>
	 * node_label:node_name=[
	 *    ↑parent_label:parent_name      // the parent node, or ROOT if null
	 *    ↓child_label:child_name        // child node, repeated as needed
	 * ] 
	 * </pre>
	 * <p>e.g.: {@code Node:0=[↑Node:1 ↓Node:2 ↓Node:3]}</p>
	 */
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		sb.append(' ');
		if (parent!=null)
			sb.append("↑").append(getParent().toShortString());
		else
			sb.append("ROOT");
		if (hasChildren()) {
			for (TreeNode n:getChildren()) {
				sb.append(" ↓").append(n.toShortString());
			}
		}
		return sb.toString();
	}


	@Override
	public final String toUniqueString() {
		return id.universalId();
	}
	
	@Override
	public TreeNodeFactory treeNodeFactory() {
		return factory;
	}
	
	// Object
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

	@Override
	public boolean equals(Object obj) {
		if (!TreeNode.class.isAssignableFrom(obj.getClass()))
			return false;
		// this should be most efficient, but not always possible
		if (SimpleTreeNodeImpl.class.isAssignableFrom(obj.getClass())) {
			SimpleTreeNodeImpl stn = (SimpleTreeNodeImpl) obj;
			return (((stn.parent==null) & (parent==null)) &&
					stn.parent.equals(parent) &&
					stn.factory.equals(factory) &&
					stn.children.equals(children));
		}
		// this is the general case
		TreeNode tn = (TreeNode) obj;
		if (!tn.treeNodeFactory().equals(factory))
			return false;
		if (!tn.getParent().equals(parent))
			return false;
		int count = 0;
		for (TreeNode child:tn.getChildren()) {
			if (!children.contains(child))
				return false;
			count++;
		}
		if (count!=children.size())
			return false;
		return true;
	}


}

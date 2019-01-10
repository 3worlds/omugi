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
package fr.cnrs.iees.tree.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.TreeNodeFactory;

/**
 * Basic implementation of {@link TreeNode} without data
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
public class SimpleTreeNodeImpl implements TreeNode {
	
	private TreeNodeFactory factory = null;
	private TreeNode parent = null;
	private Set<TreeNode> children = null;
	
	// --- Constructors

	protected SimpleTreeNodeImpl(TreeNodeFactory factory) {
		super();
		children = new HashSet<TreeNode>();
		this.factory = factory;
	}
	
	protected SimpleTreeNodeImpl(int capacity,TreeNodeFactory factory) {
		super();
		children = new HashSet<TreeNode>(capacity);
		this.factory = factory;
	}
	
	// --- TreeNode
	
	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public void setParent(TreeNode parent) {
		this.parent = parent;
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
	public String instanceId() {
		// cf. Object.toString() doc for this code
		return Integer.toHexString(hashCode());
	}

	@Override
	public int nChildren() {
		return children.size();
	}

	// Textable

	@Override
	public String toUniqueString() {
		return getClass().getSimpleName()+ " id=" + uniqueId();
	}

	@Override
	public String toShortString() {
		return getClass().getSimpleName();
	}
	
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toUniqueString());
		sb.append(' ');
		if (parent!=null)
			sb.append(Direction.IN)
				.append("=(")
				.append(parent.toUniqueString())
				.append(") ");
		if (hasChildren()) {
			sb.append(Direction.OUT)
				.append("=(");
			for (TreeNode child:children)
				sb.append(child.toUniqueString())
					.append(' ');
			sb.append(')');
		}
		return sb.toString();
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

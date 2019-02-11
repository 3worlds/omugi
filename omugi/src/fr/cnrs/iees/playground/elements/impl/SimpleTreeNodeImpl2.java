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
package fr.cnrs.iees.playground.elements.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.playground.elements.ITreeNode;
import fr.cnrs.iees.playground.factories.ITreeNodeFactory;

/**
 * Basic implementation of {@link TreeNode} without data and no possibility to set
 * the instance ID (it's entirely automatic).
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
public class SimpleTreeNodeImpl2 implements ITreeNode {
	
	private ITreeNodeFactory factory = null;
	private ITreeNode parent = null;
	private Set<ITreeNode> children = null;
	private Identity id = null;
	
	// --- Constructors

	public SimpleTreeNodeImpl2(Identity id, ITreeNodeFactory factory) {
		super();
		this.id = id;
		children = new HashSet<ITreeNode>();
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
	public ITreeNode getParent() {
		return parent;
	}

	@Override
	public void setParent(ITreeNode parent) {
		this.parent = parent;
	}

	@Override
	public Iterable<ITreeNode> getChildren() {
		return children;
	}
	
	@Override
	public void addChild(ITreeNode child) {
		children.add(child);
	}

	@Override
	public void setChildren(ITreeNode... children) {
		for (ITreeNode child:children)
			this.children.add(child);
	}

	@Override
	public void setChildren(Iterable<ITreeNode> children) {
		for (ITreeNode child:children)
			this.children.add(child);
	}

	@Override
	public void setChildren(Collection<ITreeNode> children) {
		this.children.addAll(children);
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}
	
	@Override
	public boolean hasChild(ITreeNode child) {
		return children.contains(child);
	}

	@Override
	public int nChildren() {
		return children.size();
	}

	// Textable

	@Override
	public String toUniqueString() {
		return getClass().getSimpleName()+ " id=" + id();
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
			for (ITreeNode child:children)
				sb.append(child.toUniqueString())
					.append(' ');
			sb.append(')');
		}
		return sb.toString();
	}

	@Override
	public ITreeNodeFactory treeNodeFactory() {
		return factory;
	}
	
	// Object
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

	@Override
	public boolean equals(Object obj) {
		if (!ITreeNode.class.isAssignableFrom(obj.getClass()))
			return false;
		// this should be most efficient, but not always possible
		if (SimpleTreeNodeImpl2.class.isAssignableFrom(obj.getClass())) {
			SimpleTreeNodeImpl2 stn = (SimpleTreeNodeImpl2) obj;
			return (((stn.parent==null) & (parent==null)) &&
					stn.parent.equals(parent) &&
					stn.factory.equals(factory) &&
					stn.children.equals(children));
		}
		// this is the general case
		ITreeNode tn = (ITreeNode) obj;
		if (!tn.treeNodeFactory().equals(factory))
			return false;
		if (!tn.getParent().equals(parent))
			return false;
		int count = 0;
		for (ITreeNode child:tn.getChildren()) {
			if (!children.contains(child))
				return false;
			count++;
		}
		if (count!=children.size())
			return false;
		return true;
	}

	@Override
	public String classId() {
		// TODO Auto-generated method stub
		return null;
	}


}

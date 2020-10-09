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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.ElementAdapter;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.identity.Identity;

/**
 * basic TreeNode implementation. By convention Direction.IN = up the tree (to
 * parent) and Direction.OUT = down the tree (to children) NB All constructors
 * must be protected.
 * 
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
// Tested OK with version 0.2.0 on 17/5/2019
public class SimpleTreeNode extends ElementAdapter implements TreeNode {

	private NodeFactory factory;
	private TreeNode parent = null;
	private Set<TreeNode> children = new HashSet<>();

	protected SimpleTreeNode(Identity id, NodeFactory factory) {
		super(id);
		this.factory = factory;
	}

	// NB: this method will disconnect node from its children since
	// connectChild(...)
	// will reset the child's parent to the new parent (a child can only have one
	// parent)
	@Override
	public void addConnectionsLike(Node node) {
		if (node instanceof TreeNode) {
			if (parent != null)
				parent.disconnectFrom(this);
			TreeNode tn = (TreeNode) node;
			List<TreeNode> chlist = new LinkedList<>();
			for (TreeNode c : tn.getChildren())
				chlist.add(c);
			for (TreeNode c : chlist)
				tn.disconnectFrom(c);
			connectParent(tn.getParent());
			connectChildren(chlist);
		}
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public boolean isRoot() {
		return (parent == null);
	}

	@Override
	public int degree(Direction direction) {
		switch (direction) {
		case IN:
			if (parent == null)
				return 0;
			else
				return 1;
		case OUT:
			return children.size();
		}
		return 0;
	}

	@Override
	public Collection<? extends Edge> edges(Direction direction) {
		throw new OmugiException("A TreeNode has no edge");
	}

	@Override
	public Collection<? extends Edge> edges() {
		throw new OmugiException("A TreeNode has no edge");
	}

	@Override
	public Collection<? extends Node> nodes(Direction direction) {
		switch (direction) {
		case IN:
			List<TreeNode> l = new LinkedList<>();
			if (parent != null)
				l.add(parent);
			return Collections.unmodifiableCollection(l);
		case OUT:
			return Collections.unmodifiableCollection(children);
		}
		return null;
	}

	@Override
	public Collection<? extends Node> nodes() {
		if (parent == null)
			return Collections.unmodifiableCollection(children);
		else {
			List<TreeNode> l = new LinkedList<>();
			l.addAll(children);
			l.add(parent);
			return Collections.unmodifiableCollection(l);
		}
	}

	@Override
	public Edge connectTo(Direction direction, Node node) {
		if (node instanceof SimpleTreeNode)
			switch (direction) {
			case IN:
				connectParent((SimpleTreeNode) node);
				break;
			case OUT:
				connectChild((SimpleTreeNode) node);
				break;
			}
		return null;
	}

	@Override
	public void connectTo(Direction direction, Collection<? extends Node> nodes) {
		throw new OmugiException("connectTo has no effect on TreeNodes - use connectParent or connectChildren instead");
	}

	@Override
	public void disconnect() {
		if (parent != null)
			disconnectFrom(parent);
		if (!children.isEmpty()) {
			for (TreeNode child : children)
				child.connectParent(null);
			children.clear();
		}

	}

	// caution: cross recursion
	// cannot be called in a loop on children --> concurrent modification exception
	@Override
	public void disconnectFrom(Node node) {
		if (node instanceof TreeNode) {
			TreeNode tn = (TreeNode) node;
			if (tn == parent) {
				parent = null;
				tn.disconnectFrom(this);
			}
			if (children.contains(tn)) {
				children.remove(tn);
				tn.disconnectFrom(this);
			}
		}
	}
	
	@Override
	public void disconnectFrom(Direction direction, Node node) {
		if (node instanceof TreeNode) {
			TreeNode tn = (TreeNode) node;
			// convention: parent relations are from parent to child
			// hence parent = start node
			if ((direction.equals(Direction.IN))&&(tn == parent)) {
				parent = null;
				tn.disconnectFrom(this);
			}
			// and child = end node
			if ((direction.equals(Direction.OUT))&&(children.contains(tn))) {
				children.remove(tn);
				tn.disconnectFrom(this);
			}
		}
	}


	private Collection<TreeNode> traversal(Collection<TreeNode> list, TreeNode node, int distance) {
		list.add(node);
		if (distance > 0) {
			TreeNode tn = node.getParent();
			if (tn != null)
				if (!(tn == this))
					traversal(list, tn, distance - 1);
			for (TreeNode c : node.getChildren())
				if (!(c == this))
					traversal(list, c, distance - 1);
		}
		return list;
	}

	private Collection<TreeNode> traversal(Collection<TreeNode> list, TreeNode node, int distance,
			Direction direction) {
		list.add(node); // there should not be double insertions here
		if (distance > 0) {
			switch (direction) {
			case IN:
				TreeNode tn = node.getParent();
				if (tn != null)
					traversal(list, tn, distance - 1, direction);
				break;
			case OUT:
				for (TreeNode c : node.getChildren())
					traversal(list, c, distance - 1, direction);
				break;
			}
		}
		return list;
	}

	@Override
	public Collection<? extends Node> traversal(int distance) {
		Collection<TreeNode> list = new LinkedList<>();
		list = traversal(list, this, distance);
		return Collections.unmodifiableCollection(list);
	}

	@Override
	public Collection<? extends Node> traversal(int distance, Direction direction) {
		Collection<TreeNode> list = new LinkedList<>();
		list = traversal(list, this, distance, direction);
		return Collections.unmodifiableCollection(list);
	}

	// TreeNode

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public Collection<? extends TreeNode> getChildren() {
		return Collections.unmodifiableCollection(children);
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	@Override
	public int nChildren() {
		return children.size();
	}

	@Override
	public NodeFactory factory() {
		return factory;
	}

	// caution: cross recursion with connectChild
	@Override
	public void connectParent(TreeNode parent) {
		if (this.parent != parent) {
			if (parent != null) {
				if (parent.getParent() == this) { // this to avoid simple loops where child==parent
					if (getParent() != null)
						getParent().disconnectFrom(this);
					parent.disconnectFrom(this);
				}
				parent.connectChild(this);
			}
			this.parent = parent;
			// this is bad code, but will see later if we implement
			// a complete listening system between nodes and graphs/trees
			if (factory instanceof SimpleTreeFactory)
				((SimpleTreeFactory) factory).onParentChanged();
		}
	}

	// caution: cross recursion with connectParent
	@Override
	public void connectChild(TreeNode child) {
		if (!children.contains(child)) {
			if (getParent() == child) { // this to avoid simple loops where child==parent
				child.disconnectFrom(this);
				if (child.getParent() != null)
					child.getParent().disconnectFrom(child);
			}
			children.add(child);
			child.connectParent(this);
		}
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
	 * <p>
	 * e.g.: {@code Node:0=[↑Node:1 ↓Node:2 ↓Node:3]}
	 * </p>
	 */
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		sb.append(' ');
		if (parent != null)
			sb.append("↑").append(getParent().toShortString());
		else
			sb.append("ROOT");
		if (hasChildren()) {
			for (TreeNode n : getChildren()) {
				sb.append(" ↓").append(n.toShortString());
			}
		}
		return sb.toString();
	}

	/*
	 * renaming can only take if the id is a SimpleIdentity and if the old id exists
	 * and the new does not exist.
	 */
	@Override
	public void rename(String oldId, String newId) {
		id.rename(oldId, newId);
	}

}

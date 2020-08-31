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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.identity.Identity;

/**
 * <p>A class for a tree node which can have edges, ie "cross-links" within an otherwise
 * hierarchical tree. The hierarchical relation (the tree) is kept separate from the other
 * relations using Edges. The former is accessed using the {@link TreeNode} methods while
 * the latter is accessed using the {@link Node} methods.</p>
 * <p>By convention for node accessor methods and degrees, parent node is counted in 
 * the 'IN' direction while child nodes are counted in the 'OUT' direction</p>
 * 
 * @author Jacques Gignoux - 14 mai 2019
 *
 */
// tested ok with version 0.2.0 on 20/5/2019 
public class TreeGraphNode extends ALNode implements TreeNode {

	// re implementation as in SimpleTreeNode - old implementation using an internal
	// SimpleTreeNode was flawed.
	private TreeNode parent = null;
	private Set<TreeNode> children = new HashSet<>();

	public TreeGraphNode(Identity id, 
			GraphFactory gfactory) {
		super(id, gfactory);
	}
	
	// TreeNode

	@Override
	public TreeNode getParent() {
		return parent;
	}

	// caution: cross recursion with connectChild
	@Override
	public void connectParent(TreeNode parent) {
		if (this.parent!=parent) {
			if (parent!=null) {
				if (parent.getParent()==this) { // this to avoid simple loops where child==parent
					if (getParent()!=null)
						getParent().disconnectFrom(this);
					parent.disconnectFrom(this);
				}
				parent.connectChild(this);
			}
			this.parent = parent;
			// this is bad code, but will see later if we implement
			// a complete listening system between nodes and graphs/trees
			if (factory() instanceof TreeGraphFactory)
				((TreeGraphFactory)factory()).onParentChanged();
		}
	}

	// caution: cross recursion with connectParent
	@Override
	public void connectChild(TreeNode child) {
		if (!children.contains(child))  {
			if (getParent()==child) { // this to avoid simple loops where child==parent
				child.disconnectFrom(this);
				if (child.getParent()!=null)
					child.getParent().disconnectFrom(child);
			}
			children.add(child);
			child.connectParent(this);
		}
	}

	@Override
	public Iterable<? extends TreeNode> getChildren() {
		return children;
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	@Override
	public int nChildren() {
		return children.size();
	}
	
	// Node
	
	/**
	 * returns all nodes linked to this node, including parent, children, in- and out-edge 
	 * linked nodes
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterable<? extends Node> nodes() {
		Set<Node> list = (Set<Node>) super.nodes();
		if (!children.isEmpty())
				list.addAll(children);
		if (parent!=null) 
			list.add(parent);
		return list;
	}
	
	/**
	 * returns all nodes linked in a given direction, parent being counted with the IN nodes
	 * and children with the OUT nodes. To only get the parent or the children nodes, use
	 * the more specific getParent() and getChildren() methods.
	 */
	@Override
	public Iterable<? extends Node> nodes(Direction direction) {
		Set<Node> list = new HashSet<>();
		for (ALEdge e:edges.get(direction)) 
			list.add(e.otherNode(this));
		switch (direction) {
		case IN:
			if (parent!=null)
				list.add(parent);
			break;
		case OUT:
			list.addAll(children);
			break;
		}
		return list;
	}

	/**
	 * takes into account the tree links in the computation of the degree, the parent link being
	 * counted with direction=IN and the child links being counted with direction=OUT
	 */
	@Override
	public int degree(Direction direction) {
		switch (direction) {
		case IN:
			if (parent==null)
				return super.degree(direction);
			else
				return 1+super.degree(direction);
		case OUT:
			return children.size()+super.degree(direction);
		}
		return 0;
	}

	/**
	 * returns <strong>true</strong> if there are no OUT edges <em>and</em> no children.
	 */
	@Override
	public boolean isLeaf() {
		return edges.get(Direction.OUT).isEmpty() && children.isEmpty();
	}

	/**
	 * returns <strong>true</strong> if there are no IN edges <em>and</em> no parent.
	 */
	@Override
	public boolean isRoot() {
		return edges.get(Direction.IN).isEmpty() && (parent==null);
	}

	@Override
	public void disconnectFrom(Node node) {
		// disconnect from tree structure
		if (node instanceof TreeNode) {
			TreeNode tn = (TreeNode) node;
			if (tn==parent) {
				parent = null;
				tn.disconnectFrom(this);
			}
			if (children.contains(tn)) {
				children.remove(tn);
				tn.disconnectFrom(this);
			}
		}
		// disconnect from cross-links
		super.disconnectFrom(node);
	}
	
	
	// CAUTION: this method CANNOT disconnect parent or children !!! only edge-related nodes
//	@Override
//	public void disconnectFrom(Direction direction, Node node) {
//		// disconnect from cross-links
//		super.disconnectFrom(direction,node);
//	}
	
	@Override
	public final void disconnect() {
		// disconnect from tree
		if (parent!=null)
			disconnectFrom(parent);
		if (!children.isEmpty()) {
			for (TreeNode child:children)
				child.connectParent(null);
			children.clear();
		}
		// this is bad code, but will see later if we implement
		// a complete listening system between nodes and graphs/trees
		if (factory() instanceof TreeGraphFactory)
			((TreeGraphFactory)factory()).onParentChanged();
		// disconnect from cross-links
		super.disconnect();
	}

	@Override
	public void addConnectionsLike(Node node) {
		// connect to tree
		if (node instanceof TreeNode) {
			if (parent!=null)
				parent.disconnectFrom(this);
			TreeNode tn = (TreeNode) node;
			List<TreeNode> chlist = new LinkedList<>();
			for (TreeNode c:tn.getChildren())
				chlist.add(c);
			for (TreeNode c:chlist)
				tn.disconnectFrom(c);
			connectParent(tn.getParent());
			connectChildren(chlist);
		}
		// connect cross-links
		super.addConnectionsLike(node);
	}
	
	// TODO: traversals...
	
	// -------------------  Textable

	/**
	 * Displays a TreeGraphNode as follows (on a single line):
	 * 
	 * <pre>
	 * node_label:node_name=[
	 *    ↑parent_label:parent_name      // the parent node, or ROOT if null
	 *    ↓child_label:child_name        // child node, repeated as needed
	 *    →out_node_label:out_node_name  // end node of outgoing edge, repeated as needed
	 *    ←in_node_label:in_node_name    // start node of incoming edge, repeated as needed
	 * ] 
	 * </pre>
	 * <p>e.g.: {@code Node:0=[↑Node:1 ↓Node:2 ←Node:27 ←Node:4 →Node:5 →Node:18]}</p>
	 */
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		sb.append("=[");
		if (parent!=null)
			sb.append("↑").append(parent.toShortString());
		else
			sb.append("ROOT");
		if (hasChildren()) {
			for (TreeNode n:children) {
				sb.append(" ↓").append(n.toShortString());
			}
		}
		if (edges(Direction.IN).iterator().hasNext()) {
			for (Edge e:edges(Direction.IN))
				sb.append(" ←").append(e.startNode().toShortString());
		}
		if (edges(Direction.OUT).iterator().hasNext()) {
			for (Edge e:edges(Direction.OUT))
				sb.append(" →").append(e.endNode().toShortString());
		}
		sb.append("]");
		return sb.toString();
	}

}

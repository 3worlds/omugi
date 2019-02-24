/**************************************************************************
 *  AOT - Aspect-Oriented Thinking                                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  AOT is a method to generate elaborate software code from a series of  *
 *  independent domains of knowledge. It enables one to manage and        *
 *  maintain software from explicit specifications that can be translated *
 *  into any programming language.          							  *
 **************************************************************************                                       
 *  This file is part of AOT (Aspect-Oriented Thinking).                  *
 *                                                                        *
 *  AOT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  AOT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.graph.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import au.edu.anu.rscs.aot.collections.QuickListOfLists;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.ens.biologie.generic.Textable;

/**
 * <p>
 * A graph that is both a tree and a graph, ie it has a tree structure with
 * parent and children treeNodes, but may also have cross-link edges between
 * nodes. The node must be of the {@link TreeGraphNode} class, edges can be of
 * any edge class.
 * </p>
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */

public class ImmutableTreeGraphImpl<N extends TreeGraphNode, E extends Edge> implements Tree<N>, Graph<N, E>, Textable {

//	protected Set<N> nodes; 
	protected Collection<N> nodes;
	private N root;

	// constructors
	public ImmutableTreeGraphImpl() {
		super();
		this.nodes = new HashSet<>();
	}

	public ImmutableTreeGraphImpl(Iterable<N> list) {
		this();
		for (N n : list)
			nodes.add(n);
		// order is undefined so must search?
		root = root();
	}

	/**
	 * builds the graph from a tree root by inserting all children of the argument
	 * 
	 * @param root
	 */
	public ImmutableTreeGraphImpl(N root) {
		this();
		this.root = root;
		insertChildren(root);
	}

	protected void clearRoot() {
		root = null;
	}

	@SuppressWarnings("unchecked")
	private void insertChildren(TreeNode parent) {
		for (TreeNode child : parent.getChildren()) {
			nodes.add((N) child);
			insertChildren(child);
		}
	}

	@Override
	public Iterable<N> leaves() {
		List<N> result = new ArrayList<>(nodes.size());
		for (N n : nodes)
			if (n.isLeaf())
				result.add(n);
		return result;
	}

	@Override
	public Iterable<N> nodes() {
		return nodes;
	}

	@Override
	public int size() {
		return nodes.size();
	}

	// -------------------- GRAPH <N, AOTEDGE>
	@Override
	public boolean contains(N n) {
		return nodes.contains(n);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<E> edges() {
		QuickListOfLists<E> edges = new QuickListOfLists<>();
		for (N n : nodes)
			edges.addList((Iterable<E>) n.getEdges(Direction.OUT));
		return edges;
	}

	@Override
	public Iterable<N> roots() {
		List<N> result = new ArrayList<>(nodes.size());
		for (N n : nodes)
			if (n.getParent() == null)
				result.add(n);
		return result;
	}

	// ------------------ TREE -------------------------------------
	@Override
	public Iterable<N> findNodesByReference(String reference) {
		List<N> found = new ArrayList<>(nodes.size()); // this may be a bad idea for big graphs
		for (N n : nodes)
			if (Tree.matchesReference(n, reference))
				found.add(n);
		return found;
	}

	private N findRoot() {
		List<N> roots = (List<N>) roots();
		if (roots.size() == 1)
			return roots.get(0);
		return null;
	}

	@Override
	public N root() {
		if (root == null)
			root = findRoot();
		return root;
	}

	@Override
	public Tree<N> subTree(N parent) {
		return new ImmutableTreeGraphImpl<N, E>(parent);
	}

	// LOCAL

	private int nEdges() {
		int n = 0;
		for (Node node : nodes)
			n += node.degree(Direction.OUT);
		return n;
	}

	// -------------------------- Textable --------------------------

	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder();
		sb.append(toShortString()).append(" = {");
		int count = 0;
		for (N n : nodes) {
			sb.append(n.toDetailedString());
			if (count < nodes.size() - 1)
				sb.append(',');
			count++;
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String toShortString() {
		return toUniqueString() + "(" + nodes.size() + " tree nodes / " + nEdges() + " cross-links)";
	}

	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName() + ptr;
	}

	// -------------------------- Object --------------------------

	@Override
	public final String toString() {
		return "[" + toDetailedString() + "]";
	}

	@Override
	public int maxDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int minDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

}

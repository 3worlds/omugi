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

import java.util.*;
import au.edu.anu.omhtk.collections.QuickListOfLists;
import fr.cnrs.iees.graph.*;
import fr.cnrs.iees.omhtk.Textable;

/**
 * <p>Implementation of a "treegraph", i.e. a graph with an internal tree structure or
 * a tree with cross-links between its nodes.</p>
 * 
 * @author Jacques Gignoux - 15 mai 2019
 *
 * @param <N> The implementation of {@link fr.cnrs.iees.graph.Node Node} used in this graph ({@link TreeGraphNode} or a sub-class)
 * @param <E> The implementation of {@link fr.cnrs.iees.graph.Edge Edge} used in this graph ({@link ALEdge} or a sub-class)
 */
// tested OK with version 0.2.0 on 20/5/2019
public class TreeGraph<N extends TreeGraphNode,E extends ALEdge> 
	implements Tree<N>, EdgeSet<E>, Textable {

	private Map<String,N> nodes = new HashMap<>();
	private N root = null;
	private List<N> roots = new ArrayList<N>(10);
	private GraphFactory factory;
	
	/**
	 * Instantiate a {@code TreeGraph} with a {@code GraphFactory}.
	 * 
	 * @param factory the factory to use to populate this graph with nodes and edges
	 */
	public TreeGraph(GraphFactory factory) {
		super();
		this.factory = factory;
		this.factory.manageGraph(this);
	}
	
	private void resetRoot() {
		if (roots.size()==1)
			root = roots.get(0);
		else 
			root = null;
	}
	
	@Override
	public Collection<N> nodes() {
		return Collections.unmodifiableCollection(nodes.values());
	}

	@Override
	public Collection<N> roots() {
		return Collections.unmodifiableCollection(roots);
	}

	@Override
	public boolean contains(N node) {
		return nodes.values().contains(node);
	}

	@Override
	public NodeFactory nodeFactory() {
		return factory;
	}

	@Override
	public void addNode(N node) {
		if (nodes.put(node.id(),node)!=node)
			if (node.getParent()==null) {
				roots.add(node);
				resetRoot();
			}
	}

	@Override
	public void removeNode(N node) {
		nodes.remove(node.id());
		if (roots.contains(node))
			roots.remove(node);
		if (root==node)
			root = null;
		resetRoot();
	}

	@Override
	public int nNodes() {
		return nodes.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<E> edges() {
		QuickListOfLists<E> edges = new QuickListOfLists<>();
		for (N n:nodes.values())
			edges.addList((Collection<E>) n.edges(Direction.OUT));
		return edges;
	}

	@Override
	public EdgeFactory edgeFactory() {
		return factory;
	}

	// slow
	@Override
	public int nEdges() {
		// a tree has nNodes()-1 edges
		return ALGraph.countEdges(nodes.values())-nNodes()+1;
	}

	@Override
	public N root() {
		return root;
	}

	// recursive
	@SuppressWarnings("unchecked")
	private void addToTree(List<N> tree,N n) {
		tree.add(n);
		for (TreeNode child:n.getChildren())
			addToTree(tree,(N) child);
	}
	
	@Override
	public Collection<N> subTree(N node) {
		List<N> result = new LinkedList<>();
		addToTree(result,node);
		return Collections.unmodifiableCollection(result);
	}

	// Caution: different code from SimpleTree
	@Override
	public void onParentChanged() {
		roots.clear();
		for (N n:nodes.values())
			if (n.getParent()==null) // this is the 'tree only' check for root
				roots.add(n);
		resetRoot();
	}
	
	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName() + ptr;
	}

	@Override
	public String toShortString() {
		return toUniqueString() + "(" + nodes.size() + " tree nodes / " + nEdges() + " cross-links)";
	}

	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		StringBuilder zb = new StringBuilder();
		sb.append(" NODES=(");
		for (N n: nodes.values()) {
			sb.append(n.toShortString() + ",");
			for (ALEdge e: n.edges(Direction.OUT))
				zb.append(e.toShortString() + ",");
		}
		if (sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		if (zb.length()>0)
			zb.deleteCharAt(zb.length()-1);
		sb.append(") EDGES=(").append(zb.toString()).append(')');
		return sb.toString();
	}

	// Object
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

	@Override
	public N findNode(String id) {
		return nodes.get(id);
	}

	// slow and inefficient in this implementation
	@Override
	public E findEdge(String id) {
		for (E e:edges())
			if (e.id().equals(id))
				return e;
		return null;
	}

}

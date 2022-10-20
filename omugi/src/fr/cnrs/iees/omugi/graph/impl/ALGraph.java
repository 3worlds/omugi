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
package fr.cnrs.iees.omugi.graph.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import au.edu.anu.omhtk.collections.QuickListOfLists;
import fr.cnrs.iees.omugi.graph.*;

/**
 * <p>A graph implemented as an adjacency list (hence the "AL" prefix). An adjacency list stores the list of edges
 * of every node in the graph. In this implementation, each graph node stores a list of {@code IN}
 * or {@code OUT} edges (cf. the {@link Direction} enum), i.e. it is a directed graph by default.</p>
 * 
 * <p>This implementation requires that nodes and edges are {@link ALNode}s and {@link ALEdge}s
 * or any descendant class.</p>
 * 
 * @author Jacques Gignoux - 10 mai 2019
 * 
 * @param <N> The implementation of {@link fr.cnrs.iees.omugi.graph.Node Node} used in this graph ({@code ALNode} or a sub-class) 
 * @param <E> The implementation of {@link fr.cnrs.iees.omugi.graph.Edge Edge} used in this graph ({@code ALEdge} or a sub-class)
 *
 */
// tested OK with version 0.2.0 on 17/5/2019
public class ALGraph<N extends ALNode,E extends ALEdge> implements Graph<N,E> {

	/** the adjacency list (= list of all nodes, each node storing its edges */
	private Map<String,N> nodes = new HashMap<>();
	
	private GraphFactory factory = null;
	
	/**
	 * Instantiate a graph with a {@code GraphFactory}.
	 * 
	 * @param nfactory the factory to use to populate this graph with nodes and edges
	 */
	public ALGraph(GraphFactory nfactory) {
		super();
		factory = nfactory;
		factory.manageGraph(this);
	}
	
	@Override
	public int nNodes() {
		return nodes.size();
	}

	@Override
	public Collection<N> nodes() {
		return Collections.unmodifiableCollection(nodes.values());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<E> edges() {
		QuickListOfLists<E> edges = new QuickListOfLists<>();
		for (ALNode n:nodes.values())
			edges.addList((Collection<E>)n.edges(Direction.OUT));
		return edges;
	}

	@Override
	public boolean contains(N node) {
		return nodes.containsKey(node.id());
	}

	// Textable
	
	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName()+ptr; 
	}

	// slow
	@Override
	public int nEdges() {
		return countEdges(nodes.values());
	}

	@Override
	public NodeFactory nodeFactory() {
		return factory;
	}

	@Override
	public void addNode(N node) {
		nodes.put(node.id(),node);
	}

	@Override
	public void removeNode(N node) {
		nodes.remove(node.id());		
	}
	
	protected static int countEdges(Collection<? extends ALNode> nodes) {
		int n=0;
		for (ALNode node:nodes)
			n += node.degree(Direction.OUT);
		return n;

	}

	@Override
	public EdgeFactory edgeFactory() {
		return factory;
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

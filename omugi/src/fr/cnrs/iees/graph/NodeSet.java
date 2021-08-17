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
package fr.cnrs.iees.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fr.ens.biologie.generic.Textable;

/**
 * <p>The set of nodes of a graph. Graph G is defined as G=(N,E,f) where N is a set of nodes, 
 * E a set of edges connecting the nodes, and f the incidence function mapping every edge to a 
 * pair of nodes.</p>
 * 
 * <p>A {@code NodeSet} must be associated with an {@link NodeFactory}.</p>
 * 
 * @author Jacques Gignoux - 14 mai 2019
 * 
 * @param <N> The {@link Node} subclass used to construct the graph
 */
public interface NodeSet<N extends Node>  extends Textable {
	
	/**
	 * Read-only accessor to all Nodes.
	 * 
	 * @return an unmodifiable collection of all Nodes
	 */
	public Collection<N> nodes();
	
	/**
	 * Read-only accessor to all <em>leaf</em> nodes (if any). A leaf node is a node without
	 * {@code OUT} edges.
	 * 
	 * @return an unmodifiable collection of all leaf nodes
	 */
	public default Collection<N> leaves() {
		List<N> result = new ArrayList<>(nNodes());
		for (N n : nodes())
			if (n.isLeaf())
				result.add(n);
		return Collections.unmodifiableCollection(result);
	}

	/**
	 * Read-only accessor to all <em>root</em> nodes (if any). A root node is a node without
	 * {@code IN} edges.
	 * 
	 * @return an unmodifiable collection of all root nodes
	 */
	public default Collection<N> roots() {
		List<N> result = new ArrayList<>(nNodes());
		for (N n:nodes())
			if (n.isRoot())
				result.add(n);
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * Checks if this graph contains a particular node
	 * 
	 * @param node the node to search for
	 * @return {@code true} if node was found in the graph
	 */
	public boolean contains(N node);
	
	/**
	 * The node factory used to instantiate nodes for this node set / graph.
	 * 
	 * @return the node factory
	 */
	public NodeFactory nodeFactory();
	
	/**
	 * Use this method to add nodes into a graph. This method is invoked by
	 * {@link NodeFactory}{@code .makeNode(...)} methods at node instantiation time, so that there
	 * is usually no need to call it explicitly.
	 * 
	 * @param node the node to add to the graph
	 */
	public void addNode(N node);
	
	/**
	 * Use this method to remove a node from a node set.
	 * 
	 * @param node the node to remove from the graph
	 */
	public void removeNode(N node);

	/**
	 * The size of this node set.
	 * 
	 * @return the number of nodes in this set
	 */
	public int nNodes();
	
	@Override
	public default String toShortString() {
		return toUniqueString() + " (" + nNodes() + " nodes)"; 
	}
	
	@Override
	public default String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		sb.append(" NODES=(");
		for (N n: nodes()) {
			sb.append(n.toShortString() + ",");
		}
		if (sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		sb.append(')');
		return sb.toString();
	}
	
	/**
	 * Finds an {@code Node} based on its unique ID.
	 * 
	 * @param id the unique ID of the node to search for
	 * @return the matching node instance, {@code null} if not found
	 */
	public N findNode(String id);

}

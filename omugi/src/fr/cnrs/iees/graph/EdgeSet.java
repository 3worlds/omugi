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

import fr.ens.biologie.generic.Textable;

/**
* <p>A Graph is defined as a G=(N,E,f) where N is a set of nodes, E a set of edges connecting the 
 * nodes, and f the incidence function mapping every edge to a pair of nodes.</p>
 * <p>This interface is used to represent E, the set of edges of a graph.</p>
 * <p>Some graph implementations (e.g. {@link Tree}) may have no use for this interface since
 * their edges are implicit.</p>
 * 
 * @author Jacques Gignoux - 14 mai 2019
 *
 * @param <E> The {@link Edge} subclass used to construct the graph
 * @param <N> The {@link Node} subclass connected by Edges of class E
 */
public interface EdgeSet<E extends Edge>  extends Textable {

	/**
	 * Read-only accessor to all Edges
	 * @return an Iterable of all Edges
	 */
	public Iterable<E> edges();

	/**
	 * The edge factory used to instantiate edges for this graph.
	 * @return the edge factory
	 */
	public EdgeFactory edgeFactory();

	/**
	 * Use this method to add edges into a graph. This method is usually invoked by
	 * {@link EdgeFactory}{@code .makeEdge(...)} at edge instantiation time, so that there
	 * is no need to call it explicitly.
	 * 
	 * @param edge the edge to add to the graph
	 */
	public default void addEdge(Node start, Node end, E edge) {
		// do nothing
	}
	
	/**
	 * Use this method to remove an edge from a graph.
	 * @param edge the edge to remove from the graph
	 */
	public default void removeEdge(Node start, Node end, E edge) {
		// do nothing
	}

	public int nEdges();
	
	@Override
	public default String toShortString() {
		return toUniqueString() + " (" + nEdges() + " edges)"; 
	}
	
	@Override
	public default String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		sb.append(" EDGES=(");
		for (E e: edges()) {
			sb.append(e.toShortString() + ",");
		}
		if (sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		sb.append(')');
		return sb.toString();
	}
	
	public E findEdge(String id);

}

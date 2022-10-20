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
package fr.cnrs.iees.omugi.graph;

import java.util.Collection;

/**
 * Interface for connected elements that are used to construct a graph, i.e. ancestor to the
 * {@link Node} and {@link Edge} interfaces.
 * 
 * @author Jacques Gignoux - 9 mai 2019
 *
 * @param <T> this class subclass, i.e. {@code Edge} is declared as {@code Connected<Edge>} and 
 * {@code Node} as {@code Connected<Node>}
 */
public interface Connected<T extends Connected<?>> {
	
	/**
	 * Safely disconnects this connected element (node or edge) from the graph (by taking care 
	 * of references to it by neighbouring connected elements)
	 */
	public void disconnect();
	
	/**
	 * Disconnects this connected element (node or edge) from the node passed as argument.
	 * 
	 * @param node the node to disconnect from
	 */
	public void disconnectFrom(Node node);
	
	/**
	 * Disconnects this connected element (node or edge) from the node passed as argument.
	 * 
	 * @param direction the direction in which to disconnect (if {@code IN}, node is the start node)
	 * @param node the node to disconnect from
	 */
	public void disconnectFrom(Direction direction, Node node);
	
	/**
	 * Connects this connected element (node or edge) <em>exactly</em> like the argument, 
	 * i.e by losing its former connections before copying those of the argument.
	 * 
	 * @param element the connected element (node or edge) to copy connections from
	 */
	public void connectLike(T element);
	
	/**
	 * Replaces another connected element of the same type (node or edge) by this instance.
	 * This instance adds the argument connections to its former connections, and the argument 
	 * is disconnected from the graph.
	 * @param element the connected element (node or edge) to replace
	 */
	public void replace(T element);

	/**
	 * As {@link Connected#traversal() traversal()}, but stopping after <em>distance</em> recursion steps. For example,
	 * {@code node.traversal(1)} will return only {@code node}, while 
	 * {@code node.traversal(2)} will return all the nodes connected with only 1 edge to this one.
	 * 
	 * @param distance the number of recursion steps to search
	 * @return the connected component (=sub-graph) containing this instance
	 */
	public Collection<? extends Node> traversal(int distance);

	/**
	 * As {@link Connected#traversal() traversal()}, but following only one direction 
	 * from the starting connected element (node or edge) and
	 * stopping after <em>distance</em> recursion steps.
	 * 
	 * @param distance the number of recursion steps to search
	 * @param direction the direction in which to search 
	 * @return the connected component (=sub-graph) containing this instance
	 */
	public Collection<? extends Node> traversal(int distance, Direction direction);	

	/**  
	 * <p>Fetches all the connected elements (nodes or edges) connected to this instance,
	 * and returns the result as a {@code Graph}. For <a href = "https://en.wikipedia.org/wiki/Component_(graph_theory)">connected components</a>,
	 * this will return the whole connected component. For graphs with unconnected parts,
	 * this will not return the whole graph, but the connected component to which this
	 * instance belongs.</p>
	 * <p>NOTE: depending on the graph size, this may be slow.</p>
	 * 
	 * @return the connected component (=sub-graph) containing this instance
	 */
	public default Collection<? extends Node> traversal() {
		return traversal(Integer.MAX_VALUE);
	}

	/**
	 * As {@link Connected#traversal() traversal()}, but following only one direction 
	 * from the starting connected element (edge or node).
	 * 
	 * @param direction the direction in which to search 
	 * @return the connected component (=sub-graph) containing this instance
	 */
	public default Collection<? extends Node> traversal(Direction direction) {
		return traversal(Integer.MAX_VALUE,direction);
	}

}

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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public interface Node extends Element, Connected<Node> {

	/**
	 * Add to this node the connections found in the argument node. 
	 * @param node the node to copy connections from.
	 */
	public void addConnectionsLike(Node node);

	/**
	 * Tests if this Node is a leaf (=has no OUT edges)
	 * @return
	 */
	public boolean isLeaf();
	
	/**
	 * Tests if this Node is a root (=has no IN edges)
	 * @return
	 */
	public boolean isRoot();

	/**
	 * 
	 * @param direction the direction (IN or OUT)
	 * @return the degree of this node (=the number of edges) in requested direction
	 */
	public int degree(Direction direction);

	/**
	 * 
	 * @return the degree of this node (=the number of edges)
	 */
	public default int degree() {
		return degree(Direction.IN)+degree(Direction.OUT);
	}

	/**
	 * Read-only accessor to edges according to direction.
	 * @param direction the direction (IN or OUT)
	 * @return an immutable list of edges matching the direction
	 */
	public Iterable<? extends Edge> edges(Direction direction);
	
	/**
	 * Read-only accessor to all edges.
	 * @return an immutable list of edges 
	 */
	public Iterable<? extends Edge> edges(); 
	
	/**
	 * Read-only accessor to the nodes connected to this node, following direction.
	 * @param direction the direction (IN or OUT)
	 * @return an immutable list of nodes matching the direction
	 */
	public Iterable<? extends Node> nodes(Direction direction);
	
	/**
	 * Read-only accessor to all nodes connected to this node.
	 * @return an immutable list of nodes
	 */
	public Iterable<? extends Node> nodes();

	/**
	 * connects this node to end and return the resulting edge (will create an edge).
	 * @param end the node to connect to
	 * @return the edge with startNode=this and endNode=end
	 */
	public default Edge connectTo(Node end) {
		return connectTo(Direction.OUT,end);
	}
	
	/**
	 * connects this node to node and return the resulting edge (will create an edge),
	 * according to specified direction.
	 * @param direction the direction in which to connect
	 * @param node the node to connect to
	 * @return the edge 
	 */
	public Edge connectTo(Direction direction, Node node);
	
	/**
	 * connects this node to a list of other nodes, with this node = start of the resulting edges.
	 * @param nodes the list of nodes to connect to.
	 */
	public default void connectTo(Iterable<Node> nodes) {
		connectTo(Direction.OUT,nodes);
	}
	
	/**
	 * connects this node to a list of other nodes.
	 * @param direction the direction in which to connect to the nodes
	 * @param nodes the list of nodes to connect to.
	 */
	public void connectTo(Direction direction, Iterable<Node> nodes);
	
	/**
	 * accessor to the graph which instantiated this node
	 * @return
	 */
	public NodeFactory factory();

	@Override
	public default void connectLike(Node element) {
		disconnect();
		addConnectionsLike(element);
	}

	@Override
	public default void replace(Node element) {
		addConnectionsLike(element);
		element.disconnect();
	}
	
	/**
	 * recursive helper method to construct a traversal
	 * @param list the list to fill up with nodes (initially empty)
	 * @param node the node that will be searched next (initially, this one)
	 * @param distance search depth
	 * @return a list of Nodes connected to this node within <em>distance</em> steps
	 */
	private Collection<Node> traversal(Collection<Node> list, 
			Node node, 
			int distance) {
		if (distance>0) {
			if (!list.contains(node)) {
				list.add(node);
				for (Edge e:node.edges(Direction.IN))
					traversal(list,e.startNode(),distance-1);
				for (Edge e:node.edges(Direction.OUT))
					traversal(list,e.endNode(),distance-1);
			}
		}
		return list;
	}
	
	/**
	 * recursive helper method to construct a traversal - returns a list of Nodes connected to this node
	 * @param list the list to fill up with nodes (initially empty)
	 * @param node the node that will be searched next (initially, this one)
	 * @param distance search depth
	 * @param direction direction in which to search
	 * @return a list of Nodes connected to this node within <em>distance</em> steps in direction <em>IN</em> or <em>OUT</em>
	 */
	private Collection<Node> traversal(Collection<Node> list, 
			Node node, 
			int distance, 
			Direction direction) {
		if (distance>0) {
			if (!list.contains(node)) {
				list.add(node);
				for (Edge e:node.edges(direction))
					if (direction.equals(Direction.OUT))
						traversal(list,e.endNode(),distance-1,direction);
					else 
						traversal(list,e.startNode(),distance-1,direction);
			}
		}
		return list;
	}

	
	@Override
	public default Collection<? extends Node> traversal(int distance) {
		List<Node> result = new LinkedList<Node>(); 
		traversal(result,this,distance);
		return result;
	}

	@Override
	public default Collection<? extends Node> traversal(int distance, Direction direction) {
		List<Node> result = new LinkedList<Node>(); 
		traversal(result,this,distance,direction);
		return result;
	}
	
}

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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.cnrs.iees.omugi.properties.ReadOnlyPropertyList;

/**
 * <p>Ancestor to all node classes.</p> 
 * 
 * <p>Nodes do not need edges to exist: a graph may contain unconnected nodes.</p>
 * 
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
public interface Node extends Element, Connected<Node> {

	/**
	 * Add to this node the connections found in the argument node. 
	 * 
	 * @param node the node to copy connections from.
	 */
	public void addConnectionsLike(Node node);

	/**
	 * Tests if this Node is a leaf (=has no OUT edges)
	 * 
	 * @return {@code true} if this instance is a leaf node
	 */
	public boolean isLeaf();
	
	/**
	 * Tests if this Node is a root (=has no IN edges)
	 * 
	 * @return {@code true} if this instance is a root node
	 */
	public boolean isRoot();

	/**
	 * The number of edges connected to this node (= <a href="https://en.wikipedia.org/wiki/Degree_(graph_theory)">degree</a>) 
	 * in the IN or OUT direction.
	 * 
	 * @param direction the direction (IN or OUT)
	 * @return the degree in the requested direction
	 */
	public int degree(Direction direction);

	/**
	 * The number of edges connected to this node (= <a href="https://en.wikipedia.org/wiki/Degree_(graph_theory)">degree</a>).
	 * 
	 * @return the degree
	 */
	public default int degree() {
		return degree(Direction.IN)+degree(Direction.OUT);
	}

	/**
	 * Read-only accessor to edges according to direction.
	 * 
	 * @param direction the direction (IN or OUT)
	 * @return an immutable list of edges matching the direction
	 */
	public Collection<? extends Edge> edges(Direction direction);
	
	/**
	 * Read-only accessor to all edges.
	 * 
	 * @return an immutable list of edges 
	 */
	public Collection<? extends Edge> edges(); 
	
	/**
	 * Read-only accessor to the nodes connected to this node, following direction.
	 * 
	 * @param direction the direction (IN or OUT)
	 * @return an immutable list of nodes matching the direction
	 */
	public Collection<? extends Node> nodes(Direction direction);
	
	/**
	 * Read-only accessor to all nodes connected to this node.
	 * 
	 * @return an immutable list of nodes
	 */
	public Collection<? extends Node> nodes();

	/**
	 * Connects this node to the argument and return the resulting edge (will instantiate an edge).
	 * 
	 * @param end the node to connect to
	 * @return the edge with startNode={@code this} and endNode=end
	 */
	public default Edge connectTo(Node end) {
		return connectTo(Direction.OUT,end);
	}
	
	/**
	 * Connects this node to the node argument and return the resulting edge 
	 * (will instantiate an edge),
	 * according to specified direction.
	 * 
	 * @param direction the direction in which to connect
	 * @param node the node to connect to
	 * @return the edge 
	 */
	public Edge connectTo(Direction direction, Node node);
	
	/**
	 * Connects this node to the node argument and return the resulting edge (will instantiate an edge),
	 * according to specified direction, adding the property list to the edge (if compatible
	 * with the {@link EdgeFactory} used to instantiate the edge).
	 * The default implementation of this method ignores the {@code edgeProperty} argument.
	 * 
	 * @param direction the direction in which to connect
	 * @param node the node to connect to
	 * @param edgeProperties the property list to attach to the edge
	 * @return the edge 
	 */
	public default Edge connectTo(Direction direction, Node node, ReadOnlyPropertyList edgeProperties) {
		return connectTo(direction,node);
	}

	
	/**
	 * connects this node to a list of other nodes, with this node = start of the resulting edges.
	 * @param nodes the list of nodes to connect to.
	 */
	public default void connectTo(Collection<? extends Node> nodes) {
		connectTo(Direction.OUT,nodes);
	}
	
	/**
	 * Connects this node to a list of other nodes.
	 * 
	 * @param direction the direction in which to connect to the nodes
	 * @param nodes the list of nodes to connect to.
	 */
	public void connectTo(Direction direction, Collection<? extends Node> nodes);
	
	/**
	 * Accessor to the factory which instantiated this node
	 * 
	 * @return the factory
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
		return Collections.unmodifiableCollection(result);
	}

	@Override
	public default Collection<? extends Node> traversal(int distance, Direction direction) {
		List<Node> result = new LinkedList<Node>(); 
		traversal(result,this,distance,direction);
		return Collections.unmodifiableCollection(result);
	}
	
	@Override
	public default String classId() {
		String s = factory().nodeClassName(this.getClass());
		if (s!=null)
			return s;
		else
			return Element.super.classId();
	}
	
}

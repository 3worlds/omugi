/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
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

/**
 * The basic features any Node in any graph should have
 * @author gignoux - 17 août 2017
 *
 */
public interface Node extends GraphElement, Specialized {
	
	public static String NODE_LABEL = "node";

	/**
	 * Adds an edge to this Node, in the appropriate direction
	 * @param edge the edge to add
	 * @param direction the direction in which to add it
	 * @return true if edge was successfully added
	 * <p>CAUTION: does not update the Edge start/end nodes</p>
	 */
	public boolean addEdge(Edge edge, Direction direction);
	
	/**
	 * Removes an edge from this node, in the appropriate direction list (if known) 
	 * @param edge the edge to remove
	 * @param direction the direction list in which to search for it
	 * @return true if edge was successfully removed
	 * <p>CAUTION: does not update the Edge start/end nodes</p>
	 */
	public boolean removeEdge(Edge edge, Direction direction);
	
	/**
	 * Removes an edge from this Node, without knowing the direction list (slower)
	 * @param edge the edge to remove
	 * @return true if edge was successfully removed
	 * <p>CAUTION: does not update the Edge start/end nodes</p>
	 */
	public default boolean removeEdge(Edge edge) {
		// the | (NOT ||) because of loop edges. otherwise fine.
		return (removeEdge(edge,Direction.IN)|removeEdge(edge,Direction.OUT));
	}
	
	/**
	 * Adds an edge to this Node, in the proper direction list ONLY IF edge.startNode() or edge.endNode()
	 * equals this Node. Otherwise, nothing is inserted.
	 * @param edge the edge to add
	 * @return true if edge was successfully added
	 * <p>CAUTION: does not update the Edge start/end nodes</p>
	 */
	public boolean addEdge(Edge edge);
	
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
	 * Read-only accessor to edges according to direction.
	 * @param direction the direction (IN or OUT)
	 * @return an immutable list of edges matching the direction
	 */
	public Iterable<? extends Edge> getEdges(Direction direction);
	
	/**
	 * Read-only accessor to all edges.
	 * @return an immutable list of edges 
	 */
	public Iterable<? extends Edge> getEdges(); 
		
	/**
	 * 
	 * @return the degree of this node (=the number of edges)
	 */
	public default int degree() {
		return degree(Direction.IN)+degree(Direction.OUT);
	}
	
	/**
	 * 
	 * @return the Nodefactory with which this Node was instantiated
	 */
	public NodeFactory nodeFactory();
	
	/**
	 * 
	 * @param direction the direction (IN or OUT)
	 * @return the degree of this node (=the number of edges) in requested direction
	 */
	public int degree(Direction direction);

	@Override
	public default Node addConnectionsLike(GraphElement element) {
		Node node = (Node) element;
		for (Edge e:node.getEdges(Direction.IN)) {
			EdgeFactory f = (EdgeFactory) e.edgeFactory();
			f.makeEdge(e.startNode(), this);			
		}
		for (Edge e:node.getEdges(Direction.OUT)) {
			EdgeFactory f = (EdgeFactory) e.edgeFactory();
			f.makeEdge(this, e.endNode());
		}
		return this;
	}
	
	/**
	 * The "label" or classId of a Node is a String matching its java class name. It is
	 * known by the nodeFactory.
	 * This method should not be overridden.
	 * 
	 * Should this, therefore, be in NodeAdapter as final.
	 * 
	 * @return
	 */
	public default String classId() {
		String s = nodeFactory().nodeClassName(this.getClass());
		if (s==null)
			s = this.getClass().getSimpleName();
		return s;
	}

	@Override
	public default String toShortString() {
		return classId()+":"+id();
	}

}

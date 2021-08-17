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

/**
 * <p>Ancestor to all edge classes.</p>
 * 
 * <p>In all graph definitions, an edge is a connection between two nodes, that cannot exist
 * in the absence of nodes. In other words, edges are subordinate to nodes: a set containing only
 * one node or only unconnected nodes is a valid graph, but a set containing only one edge
 * without its two end nodes is not a graph. A free-floating edge is nonsense.</p> 
 * 
 * <p>For this reason, in all the {@code Edge} implementing classes here, all edge constructors and methods 
 * instantiating an edge require two pre-existing nodes.</p>
 * 
 * @author Jacques Gignoux - 10 mai 2019
 */
public interface Edge extends Element, Connected<Edge> {

	/**
	 * Accessor to the start node of this instance.
	 * 
	 * @return the start Node
	 */
	public Node startNode();
	
	/**
	 * Accessor to the end node of this instance.
	 * 
	 * @return the end Node of this edge
	 */
	public Node endNode();

	/**
	 * Accessor to the node at the opposite end of this instance.  
	 * 
	 * @param other a node at one of the two ends of this edge
	 * @return the opposite end of this edge - {@code null} if the argument is not found in this edge
	 */
	public Node otherNode(Node other);

	/**
	 * Setter for both start and end Nodes
	 * 
	 * @param start the new start node
	 * @param end the new end node
	 */
	public void connect(Node start, Node end);

	/**
	 * Accessor to the factory which instantiated this edge
	 * 
	 * @return the factory
	 */
	public EdgeFactory factory();

	
	@Override
	public default String classId() {
		String s = factory().edgeClassName(this.getClass());
		if (s!=null)
			return s;
		else
			return Element.super.classId();
	}

}

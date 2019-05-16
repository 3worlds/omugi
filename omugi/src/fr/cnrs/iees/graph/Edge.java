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
 * Ancestor to all Edge classes. NB: Edges are meant to work with a particular type of Node, 
 * hence the type parameter.
 * @author Jacques Gignoux - 10 mai 2019
 *
 * @param <N>
 */
public interface Edge extends Element, Connected<Edge> {

	/**
	 * Getter for
	 * @return the start Node of this edge
	 */
	public Node startNode();
	
	/**
	 * Getter for
	 * @return the end Node of this edge
	 */
	public Node endNode();

	/**
	 * Getter for 
	 * @param other a node at one of the two ends of this edge
	 * @return the opposite end of this edge - null if the argument is not found in this edge
	 */
	public Node otherNode(Node other);

	/**
	 * Setter for both start and end Nodes
	 * @param start the new start node
	 * @param end the new end node
	 */
	public void connect(Node start, Node end);

	/**
	 * accessor to the graph which instantiated this edge
	 * @return
	 */
	public EdgeFactory factory();

}

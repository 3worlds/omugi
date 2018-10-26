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
package fr.cnrs.iees.graph.generic;

/**
 * The basic features any edge in any graph should have
 * @author gignoux - 17 août 2017
 * <p>Setters make sure the graph stays valid, ie they update the Node edge lists if needed</p>
 *
 */
public interface Edge extends Element {
	
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
	 * Setter for
	 * @param node the start Node of this edge
	 */
	public Edge setStartNode(Node node);
	
	/**
	 * Setter for
	 * @param node the end Node of this edge
	 */
	public Edge setEndNode(Node node);
	
	/**
	 * Setter for both start and end Nodes
	 * @param start the new start node
	 * @param end the new end node
	 */
	public default Edge setNodes (Node start, Node end) {
		setStartNode(start);
		setEndNode(end);
		return this;
	}

	
}

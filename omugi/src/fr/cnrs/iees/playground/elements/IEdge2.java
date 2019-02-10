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
package fr.cnrs.iees.playground.elements;

import fr.cnrs.iees.graph.GraphElement;
import fr.cnrs.iees.playground.factories.IEdgeFactory;

/**
 * The basic features any edge in any graph should have
 * 
 * @author gignoux - 17 août 2017
 *         <p>
 *         Setters make sure the graph stays valid, ie they update the Node edge
 *         lists if needed
 *         </p>
 *
 */
public interface IEdge2 extends GraphElement {

	public static String EDGE_LABEL = "edge";

	/**
	 * Getter for
	 * 
	 * @return the start Node of this edge
	 */
	public INode2 startNode();

	/**
	 * Getter for
	 * 
	 * @return the end Node of this edge
	 */
	public INode2 endNode();

	/**
	 * Getter for
	 * 
	 * @param other a node at one of the two ends of this edge
	 * @return the opposite end of this edge - null if the argument is not found in
	 *         this edge
	 */
	public INode2 otherNode(INode2 other);

	/**
	 * Setter for
	 * 
	 * @param node the start Node of this edge
	 */
	public IEdge2 setStartNode(INode2 node);

	/**
	 * Setter for
	 * 
	 * @param node the end Node of this edge
	 */
	public IEdge2 setEndNode(INode2 node);

	/**
	 * Setter for both start and end Nodes
	 * 
	 * @param start the new start node
	 * @param end   the new end node
	 */
	public default IEdge2 setNodes(INode2 start, INode2 end) {
		setStartNode(start);
		setEndNode(end);
		return this;
	}

	/**
	 * 
	 * @return the EdgeFactory with which this Edge was instantiated
	 */
	public IEdgeFactory edgeFactory();

	/**
	 * The "label" or "classId" is now a String matching the java class name. The
	 * edgeFactory knows this String.
	 * 
	 * @return
	 */

	public String classId();
//	{
//		String s = edgeFactory().edgeClassName(this.getClass());
//		if (s == null)
//			s = this.getClass().getSimpleName();
//		return s;
//	}

}

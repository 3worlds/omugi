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

import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * <p>An interface to give a Graph the ability to create Edges in an appropriate way. cf.
 * {@link NodeFactory} for details.</p>
 * 
 * <p>All graph elements (nodes and edges) are uniquely identified by a class ID / instance ID
 * pair. This pair is used for deciding if nodes / edges are equal (Object.equals(...) method).</p>
 * 
 * <p>Since Edges cannot exist without Nodes, implementing classes should make sure that 
 * the creation of an Edge is appropriately reflected in Node instances (eg by inserting the 
 * edge in its start node and end node edge lists).</p>
 * 
 * <p>Typically, an EdgeFactory should have a constructor taking a {@link Graph} as a parameter
 * so that Edge creation is consistent with the current graph context.</p> 
 * 
 * @author Jacques Gignoux 7-11-2018
 *
 * @param <N>
 */
public interface EdgeFactory {

	/**
	 * Create an edge with no properties. The class ID is set to the default (= the implementing
	 * class name). The instance ID is automatically generated and is unique.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @return
	 */
	public default Edge makeEdge(Node start, Node end) {
		return makeEdge(start,end,null,null,null);
	}
	
	/**
	 * Create an edge with properties. The class ID is set to the default (= the implementing
	 * class name). The instance ID is automatically generated and is unique.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param props properties
	 * @return
	 */
	public default Edge makeEdge(Node start, Node end, ReadOnlyPropertyList props) {
		return makeEdge(start,end,null,null,props);
	}
	
	/**
	 * Create an edge with no properties and a particular class ID. The instance ID is 
	 * automatically generated and is unique.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param classId the class identifier
	 * @return
	 */
	public default Edge makeEdge(Node start, Node end, String classId) {
		return makeEdge(start,end,classId,null,null);
	}

	/**
	 * Create an edge with no properties, a particular class ID and instance ID. Implementing
	 * classes should make sure the (classId,instanceId) pair enables to uniquely identify
	 * the instance returned.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param classId the class identifier
	 * @param instanceId the instance identifier
	 * @return
	 */
	public default Edge makeEdge(Node start, Node end, String classId, String instanceId) {
		return makeEdge(start,end,classId,instanceId,null);
	}

	/**
	 * Create an edge with a particular class ID and properties. The instance ID is automatically 
	 * generated and is unique.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param classId the class identifier
	 * @param props properties
	 * @return
	 */
	public default Edge makeEdge(Node start, Node end, String classId, ReadOnlyPropertyList props) {
		return makeEdge(start,end,classId,null,props);
	}

	/**
	 * Create an edge with a particular class ID and instance ID and properties. Implementing
	 * classes should make sure the (classId,instanceId) pair enables to uniquely identify
	 * the instance returned.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param classId the class identifier
	 * @param instanceId the instance identifier
	 * @param props properties
	 * @return 
	 */
	public Edge makeEdge(Node start, Node end, String classId, String instanceId, ReadOnlyPropertyList props);

}

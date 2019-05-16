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

import fr.cnrs.iees.OmugiClassLoader;
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
	
	public static String defaultEdgeId = "edge0";

	/**
	 * Create an edge with no properties. The class ID is set to the default (= the implementing
	 * class name). The instance ID is automatically generated and is unique.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @return
	 */
	public default Edge makeEdge(Node start, Node end) {
		return makeEdge(start,end,defaultEdgeId);
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
		return makeEdge(start,end,defaultEdgeId,props);
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
	public Edge makeEdge(Node start, Node end, String proposedId);

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
	public Edge makeEdge(Node start, Node end, String proposedId, ReadOnlyPropertyList props);

	/**
	 * returns the "label" of an edge class as known by this factory. For use in descendants
	 * which use labels to identify edge class types.
	 * 
	 * @param edgeClass
	 * @return
	 */
	public default String edgeClassName(Class<? extends Edge> edgeClass) {
		return edgeClass.getSimpleName();
	}
	
	/**
	 * returns the class type matching an edge "label". Default behaviour is to pretend
	 * label is a class name in the package fr.cnrs.iees.graph.impl
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public default Class<? extends Edge> edgeClass(String label) {
		try {
			return (Class<? extends Edge>) Class.forName("fr.cnrs.iees.graph.impl."+label,false,OmugiClassLoader.getClassLoader());
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Create an edge of a particular sub-class passed as the first argument
	 * 
	 * @param edgeClass
	 * @param start
	 * @param end
	 * @param proposedId
	 * @param props
	 * @return
	 */
	public Edge makeEdge(Class<? extends Edge> edgeClass, 
		Node start, Node end, String proposedId, ReadOnlyPropertyList props);

	public Edge makeEdge(Class<? extends Edge> edgeClass,
		Node start, Node end, String proposedId);
	
	public default Edge makeEdge(Class<? extends Edge> edgeClass,
		Node start, Node end, ReadOnlyPropertyList props) {
		return makeEdge(edgeClass,start,end,defaultEdgeId,props);
	}
	
	public default Edge makeEdge(Class<? extends Edge> edgeClass,Node start, Node end) {
		return makeEdge(edgeClass,start,end,defaultEdgeId);
	}
	
	
	
}

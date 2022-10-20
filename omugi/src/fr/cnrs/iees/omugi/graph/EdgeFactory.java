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
package fr.cnrs.iees.omugi.graph;

import fr.cnrs.iees.omugi.OmugiClassLoader;
import fr.cnrs.iees.omugi.properties.PropertyListFactory;
import fr.cnrs.iees.omugi.properties.ReadOnlyPropertyList;

/**
 * <p>A factory to create {@link Edge}s in an appropriate way. cf.
 * {@link GraphFactory} for the rationale behind factories.</p>
 *  
 * <p>Since edges cannot exist without nodes, implementing classes should make sure that 
 * the creation of an edge is appropriately reflected in node instances (e.g. in {@link fr.cnrs.iees.omugi.graph.impl.ALNode ALNode},
 * by inserting the edge in its start node and end node edge lists).</p>
 * 
 * <p>Implementing classes must have a constructor taking a {@linkplain fr.cnrs.iees.omugi.identity.IdentityScope scope} as a parameter, or
 * internally building a {@code IdentityScope}, to guarantee unicity of edge IDs.</p> 
 * 
 * @author Jacques Gignoux 7-11-2018
 *
 */
public interface EdgeFactory {

	/** A default edge identifier (e.g. to start a {@link fr.cnrs.iees.omugi.identity.impl.LocalScope LocalScope})*/
	public static String defaultEdgeId = "edge0";

	/**
	 * Create an edge with no properties. The class ID is set to the default (= the implementing
	 * class name). The instance ID is automatically generated and is unique within the scope of
	 * this factory.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @return the new {@code Edge} instance
	 */
	public default Edge makeEdge(Node start, Node end) {
		return makeEdge(start,end,defaultEdgeId);
	}
	
	/**
	 * Create an edge with properties. The class ID is set to the default (= the implementing
	 * class name). The instance ID is automatically generated and is unique within the scope of
	 * this factory.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param props properties
	 * @return the new {@code Edge} instance
	 */
	public default Edge makeEdge(Node start, Node end, ReadOnlyPropertyList props) {
		return makeEdge(start,end,defaultEdgeId,props);
	}
	
	/**
	 * Create an edge with no properties and a proposed ID. The class ID is set to the default (= the implementing
	 * class name). The proposed ID is checked for unicity within the scope of this factory, and
	 * if not, is modified appropriately to be unique. Therefore, the returned instance may have a unique
	 * ID different from the one proposed as an argument.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param proposedId the instance identifier
	 * @return the new {@code Edge} instance
	 */
	public Edge makeEdge(Node start, Node end, String proposedId);

	/**
	 * Create an edge with a particular instance ID and properties. The class ID is set to the default (= the implementing
	 * class name). The proposed ID is checked for unicity within the scope of this factory, and
	 * if not, is modified appropriately to be unique. Therefore, the returned instance may have a unique
	 * ID different from the one proposed as an argument.
	 * 
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param proposedId the instance identifier
	 * @param props properties
	 * @return the new {@code Edge} instance
	 */
	public Edge makeEdge(Node start, Node end, String proposedId, ReadOnlyPropertyList props);

	/**
	 * Factories may use <em>labels</em> (= just {@code String}s), as aliases for {@code Edge} descendant
	 * class names. This method returns the label of an edge class as known by this factory. 
	 * 
	 * @param edgeClass the edge class
	 * @return the edge class label recorded in this factory. Defaults to {@link Class#getSimpleName()}.
	 */
	public default String edgeClassName(Class<? extends Edge> edgeClass) {
		return edgeClass.getSimpleName();
	}
	
	/**
	 * Factories may use <em>labels</em> (= just {@code String}s), as aliases for {@code Edge} descendant
	 * class names. This method returns the class type matching an edge label. 
	 * Default behaviour is to pretend that
	 * <em>label</em> is a class name in the package {@code fr.cnrs.iees.omugi.graph.impl}.
	 * 
	 * @param label the label to get the class associated to it
	 * @return the class name matching the label argument
	 */
	@SuppressWarnings("unchecked")
	public default Class<? extends Edge> edgeClass(String label) {
		try {
			return (Class<? extends Edge>) Class.forName("fr.cnrs.iees.omugi.graph.impl."+label,false,OmugiClassLoader.getAppClassLoader());
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Create an edge of a particular sub-class with properties and a proposed ID. The class ID is set 
	 * to the implementing class name or, if recorded in this factory, to its label. 
	 * The proposed ID is checked for unicity within the scope of this factory, and
	 * if not, is modified appropriately to be unique. Therefore, the returned instance may have a unique
	 * ID different from the one proposed as an argument.
	 * 
	 * @param edgeClass the {@code Edge} implementation to use to create this instance
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param proposedId the instance identifier
	 * @param props properties
	 * @return the new {@code Edge} instance, {@code null} if construction failed
	 */
	public Edge makeEdge(Class<? extends Edge> edgeClass, 
		Node start, Node end, String proposedId, ReadOnlyPropertyList props);

	/**
	 * Create an edge of a particular sub-class with no properties and a proposed ID. The class ID is set 
	 * to the implementing class name or, if recorded in this factory, to its label. 
	 * The proposed ID is checked for unicity within the scope of this factory, and
	 * if not, is modified appropriately to be unique. Therefore, the returned instance may have a unique
	 * ID different from the one proposed as an argument.
	 * 
	 * @param edgeClass the {@code Edge} implementation to use to create this instance
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param proposedId the instance identifier
	 * @return the new {@code Edge} instance, {@code null} if construction failed
	 */
	public Edge makeEdge(Class<? extends Edge> edgeClass,
		Node start, Node end, String proposedId);
	
	/**
	 * Create an edge of a particular sub-class with properties. The class ID is set 
	 * to the implementing class name or, if recorded in this factory, to its label. 
	 * The instance ID is automatically generated and is unique within the scope of
	 * this factory.
	 * 
	 * @param edgeClass the {@code Edge} implementation to use to create this instance
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @param props properties
	 * @return the new {@code Edge} instance, {@code null} if construction failed
	 */
	public default Edge makeEdge(Class<? extends Edge> edgeClass,
		Node start, Node end, ReadOnlyPropertyList props) {
		return makeEdge(edgeClass,start,end,defaultEdgeId,props);
	}
	
	/**
	 * Create an edge of a particular sub-class with no properties. The class ID is set 
	 * to the implementing class name or, if recorded in this factory, to its label. 
	 * The instance ID is automatically generated and is unique within the scope of
	 * this factory.
	 * 
	 * @param edgeClass the {@code Edge} implementation to use to create this instance
	 * @param start the start node for this edge
	 * @param end the end node for this edge
	 * @return the new {@code Edge} instance, {@code null} if construction failed
	 */
	public default Edge makeEdge(Class<? extends Edge> edgeClass,Node start, Node end) {
		return makeEdge(edgeClass,start,end,defaultEdgeId);
	}
	
	/**
	 * Accessor to the {@link PropertyListFactory} associated to this factory.
	 * 
	 * @return the propertyListfactory used with this {@code EdgeFactory}
	 */
	public default PropertyListFactory edgePropertyFactory() {
		return new PropertyListFactory() {};
	}
	
}

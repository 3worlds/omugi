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
 * <p>A factory to create {@link Node}s in an appropriate way. cf.
 * {@link GraphFactory} for the rationale behind factories.
 * </p>
 * 
 * <p>
 * A factory manages a list of graphs, i.e. every time a new node is created, it is added
 * to all its associated graphs. Removal of nodes is managed at the graph level.
 * To build different graphs, you must use a different factory for each of them <em>or</em> carefully
 * manage insertion of nodes and edges into the graphs using the 
 * {@link fr.cnrs.iees.omugi.graph.NodeFactory#manageGraph(NodeSet) manageGraph(...)} and
 * {@link fr.cnrs.iees.omugi.graph.NodeFactory#unmanageGraph(NodeSet) unmanageGraph(...)} methods.</p>
 * 
 * <p>Implementing classes must have a constructor taking a {@link fr.cnrs.iees.omugi.identity.IdentityScope IdentityScope} as a parameter, or
 * internally building a {@code Scope}, to guarantee unicity of node IDs.</p> 
 * 
 * @author Jacques Gignoux 7-11-2018
 *
 */
public interface NodeFactory {

	/** A default node identifier (e.g. to start a {@link fr.cnrs.iees.omugi.identity.impl.LocalScope LocalScope})*/
	public static String defaultNodeId = "node0";

	/**
	 * Create a node with no properties. The class ID is set to the default (= the implementing
	 * class name). The instance ID is automatically generated and is unique within the scope of
	 * this factory.
	 * 
	 * @return the new {@code Node} instance
	 */
	public default Node makeNode() {
		return makeNode(defaultNodeId);
	}

	/**
	 * Create a node with properties. The class ID is set to the default (= the
	 * implementing class name). The instance ID is automatically generated and is unique 
	 * within the scope of this factory.
	 * 
	 * @param props properties
	 * @return the new {@code Node} instance
	 */
	public default Node makeNode(ReadOnlyPropertyList props) {
		return makeNode(defaultNodeId, props);
	}

	/**
	 * Create a node with no properties and a proposed ID. The class ID is set to the default (= the implementing
	 * class name). The proposed ID is checked for unicity within the scope of this factory, and
	 * if not, is modified appropriately to be unique. Therefore, the returned instance may have a unique
	 * ID different from the one proposed as an argument.
	 * 
	 * @param proposedId the instance identifier
	 * @return the new {@code Node} instance
	 */
	public Node makeNode(String proposedId);

	/**
	 * Create a node with properties and a proposed ID. The class ID is set to the default (= the implementing
	 * class name). The proposed ID is checked for unicity within the scope of this factory, and
	 * if not, is modified appropriately to be unique. Therefore, the returned instance may have a unique
	 * ID different from the one proposed as an argument.
	 * 
	 * @param proposedId the instance identifier
	 * @param props   properties
	 * @return the new {@code Node} instance
	 */
	public Node makeNode(String proposedId, ReadOnlyPropertyList props);

	/**
	 * Factories may use <em>labels</em> (= just {@code String}s), as aliases for {@code Node} descendant
	 * class names. This method returns the label of a node class as known by this factory. 
	 * 
	 * @param nodeClass the node class
	 * @return the node class label recorded in this factory. Defaults to {@link Class#getSimpleName()}.
	 */
	public default String nodeClassName(Class<? extends Node> nodeClass) {
		return nodeClass.getSimpleName();
	}

	/**
	 * Factories may use <em>labels</em> (= just {@code String}s), as aliases for {@code Node} descendant
	 * class names. This method returns the class type matching a node label. 
	 * Default behaviour is to pretend that
	 * <em>label</em> is a class name in the package {@code fr.cnrs.iees.omugi.graph.impl}.
	 * 
	 * @param label the label to get the class associated to it
	 * @return the class name matching the label argument
	 */
	@SuppressWarnings("unchecked")
	public default Class<? extends Node> nodeClass(String label) {
		try {
			return (Class<? extends Node>) Class.forName("fr.cnrs.iees.omugi.graph.impl." + label, false,
					OmugiClassLoader.getAppClassLoader());
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Create a node of a particular sub-class with properties  and a proposed ID.  
	 * The class ID is set 
	 * to the implementing class name or, if recorded in this factory, to its label. 
	 * The proposed ID is checked for unicity within the scope of this factory, and
	 * if not, is modified appropriately to be unique. Therefore, the returned instance may have a unique
	 * ID different from the one proposed as an argument.
	 * 
	 * @param nodeClass the {@code Node} implementation to use to create this instance
	 * @param proposedId the instance identifier
	 * @param props   properties
	 * @return the new {@code Node} instance, {@code null} if construction failed
	 */
	public Node makeNode(Class<? extends Node> nodeClass, String proposedId, ReadOnlyPropertyList props);

	/**
	 * Create a node of a particular sub-class with no properties and a proposed ID.  
	 * The class ID is set 
	 * to the implementing class name or, if recorded in this factory, to its label. 
	 * The proposed ID is checked for unicity within the scope of this factory, and
	 * if not, is modified appropriately to be unique. Therefore, the returned instance may have a unique
	 * ID different from the one proposed as an argument.
	 * 
	 * @param nodeClass the {@code Node} implementation to use to create this instance
	 * @param proposedId the instance identifier
	 * @return the new {@code Node} instance, {@code null} if construction failed
	 */
	public Node makeNode(Class<? extends Node> nodeClass, String proposedId);

	/**
	 * Create a node of a particular sub-class with properties.  The class ID is set 
	 * to the implementing class name or, if recorded in this factory, to its label. 
	 * The instance ID is automatically generated and is unique within the scope of
	 * this factory.
	 * 
	 * @param nodeClass the {@code Node} implementation to use to create this instance
	 * @param props   properties
	 * @return the new {@code Node} instance, {@code null} if construction failed
	 */
	public default Node makeNode(Class<? extends Node> nodeClass, ReadOnlyPropertyList props) {
		return makeNode(nodeClass, defaultNodeId, props);
	}

	/**
	 * Create a node of a particular sub-class with no properties.  The class ID is set 
	 * to the implementing class name or, if recorded in this factory, to its label. 
	 * The instance ID is automatically generated and is unique within the scope of
	 * this factory.
	 * 
	 * @param nodeClass the {@code Node} implementation to use to create this instance
	 * @return the new {@code Node} instance, {@code null} if construction failed
	 */
	public default Node makeNode(Class<? extends Node> nodeClass) {
		return makeNode(nodeClass, defaultNodeId);
	}

	/**
	 * Associate a graph instance to this factory. Further instances created by 
	 * calls to one of the {@code makeNode(...)} methods will be added into this graph. 
	 * 
	 * @param graph the {@link Graph} or {@link Tree} instance to manage
	 */
	public void manageGraph(NodeSet<? extends Node> graph);

	/**
	 * Dissociate a graph instance from this factory. Further instances created by 
	 * calls to one of the {@code makeNode(...)} methods will no more be added into this graph. 
	 * 
	 * @param graph the {@link Graph} or {@link Tree} instance to manage
	 */
	public void unmanageGraph(NodeSet<? extends Node> graph);

	/**
	 * Accessor to the {@link PropertyListFactory} associated to this factory.
	 * 
	 * @return the propertyListfactory used with this {@code NodeFactory}
	 */
	public default PropertyListFactory nodePropertyFactory() {
		return new PropertyListFactory() {
		};
	}

}

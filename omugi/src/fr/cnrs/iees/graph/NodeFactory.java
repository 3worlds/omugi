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
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * <p>
 * An interface to give a Graph the ability to create Nodes in an appropriate
 * way.
 * </p>
 * 
 * <p>
 * Although nodes and edges could in theory exist without the context of a
 * graph, as soon as one starts to instantiate them a graph starts to exist. If
 * we want to put some constraints on this graph (e.g. directed/undirected
 * graph, acyclic graph, tree, multigraph, etc.) then we must be able to
 * constrain node and edge creation, and a generic public constructor for edges
 * and nodes does not allow this. Even worse, it could break the graph rules
 * unintentionnally. To secure this, nodes and edges must exist only within the
 * context of a graph.
 * </p>
 * 
 * <p>
 * But sometimes it makes sense that a node belongs to more than one graph. In
 * order to allow for this possibility, we separate the node creation ability
 * from the node addition into the graph. This way, a node made by one graph
 * could be added to another. Each Node or Edge will record which factory
 * created it, but not which graphs it belongs to. This way, other instances of
 * the same type can be made by calling the initial factory.
 * </p>
 * 
 * <p>
 * All graph elements (nodes and edges) are uniquely identified by a class ID /
 * instance ID pair. This pair is used for deciding if nodes / edges are equal
 * (Object.equals(...) method).
 * </p>
 * 
 * <p>
 * Typically, a NodeFactory should have a constructor taking a {@link Graph} as
 * a parameter so that Node creation is consistent with the current graph
 * context.
 * </p>
 * 
 * @author Jacques Gignoux 7-11-2018
 *
 */
public interface NodeFactory {

	public static String defaultNodeId = "node0";

	/**
	 * Create a Node with no properties. The class ID is set to the default (= the
	 * implementing class name). The instance ID is automatically generated and is
	 * unique.
	 * 
	 * @return
	 */
	public default Node makeNode() {
		return makeNode(defaultNodeId);
	}

	/**
	 * Create a node with properties. The class ID is set to the default (= the
	 * implementing class name). The instance ID is automatically generated and is
	 * unique.
	 * 
	 * @param props properties
	 * @return
	 */
	public default Node makeNode(ReadOnlyPropertyList props) {
		return makeNode(defaultNodeId, props);
	}

	/**
	 * Create a node with no properties and a particular class ID. The instance ID
	 * is automatically generated and is unique.
	 * 
	 * @param classId the class identifier
	 * @return
	 */
	public Node makeNode(String proposedId);

	/**
	 * Create a node with a particular class ID and properties. The instance ID is
	 * automatically generated and is unique.
	 * 
	 * @param classId the class identifier
	 * @param props   properties
	 * @return
	 */
	public Node makeNode(String proposedId, ReadOnlyPropertyList props);

	/**
	 * returns the "label" of a node class as known by this factory. For use in
	 * descendants which use labels to identify node class types.
	 * 
	 * @param nodeClass
	 * @return
	 */
	public default String nodeClassName(Class<? extends Node> nodeClass) {
		return nodeClass.getSimpleName();
	}

	/**
	 * returns the class type matching a node "label". Default behaviour is to
	 * pretend label is a class name in the package fr.cnrs.iees.graph.impl
	 * 
	 * @param label
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public default Class<? extends Node> nodeClass(String label) {
		try {
			return (Class<? extends Node>) Class.forName("fr.cnrs.iees.graph.impl." + label, false,
					OmugiClassLoader.getClassLoader());
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Create a node of a particular node sub-class passed as the first argument
	 * 
	 * @param nodeClass  the Node subclass to instantiate
	 * @param proposedId the proposed unique id
	 * @param props      property list
	 * @return a new instance, or null if fails
	 */
	public Node makeNode(Class<? extends Node> nodeClass, String proposedId, ReadOnlyPropertyList props);

	public Node makeNode(Class<? extends Node> nodeClass, String proposedId);

	public default Node makeNode(Class<? extends Node> nodeClass, ReadOnlyPropertyList props) {
		return makeNode(nodeClass, defaultNodeId, props);
	}

	public default Node makeNode(Class<? extends Node> nodeClass) {
		return makeNode(nodeClass, defaultNodeId);
	}

	public void manageGraph(NodeSet<? extends Node> graph);

	public void unmanageGraph(NodeSet<? extends Node> graph);

	/*
	 * TODO: to avoid trashing the purity of this, we could provide a method just to
	 * TwConfigFactory and VisualNode factory to do this.
	 * 
	 * By typecasting the factories of these node and edge classes we have access to
	 * protected members i.e. scopeId and can safely do this operation
	 */
	public void removeNode(Node node);

	/**
	 * 
	 * @return the propertyListfactory used with this NodeFactory
	 */
	public default PropertyListFactory nodePropertyFactory() {
		return new PropertyListFactory() {
		};
	}

}

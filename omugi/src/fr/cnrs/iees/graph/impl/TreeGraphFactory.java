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
package fr.cnrs.iees.graph.impl;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.ens.biologie.generic.utils.Logging;

/**
 * <p>The factory for {@link TreeGraph}s (node and edge factory).</p>
 * 
 * @author Jacques Gignoux - 17 août 2021
 *
 */
public class TreeGraphFactory extends GraphFactoryAdapter {

	private static Logger log = Logging.getLogger(TreeGraphFactory.class);
	/** the list of graphs managed by this factory */
	protected Set<TreeGraph<TreeGraphNode, ALEdge>> graphs = new HashSet<>();

	/**
	 * Basic constructor, only requires a scope
	 * 
	 * @param scopeName the scope identifier, e.g. "MyTreeGraphFactory"
	 */
	public TreeGraphFactory(String scopeName) {
		super(scopeName);
	}

	/**
	 * Basic constructor with default scope "TGF" (for "tree graph factory").
	 */
	public TreeGraphFactory() {
		super("TGF");
	}

	/**
	 * Constructor with labels for sub-classes of {@link TreeGraphNode} and {@link ALEdge}
	 * 
	 * @param scopeName the scope identifier, e.g. "MyTreeGraphFactory"
	 * @param labels    a map of (labels,class names) associating a label to the valid java class name of a 
	 * descendant of {@code TreeGraphNode} or {@code ALEdge}
	 */
	public TreeGraphFactory(String scopeName, Map<String, String> labels) {
		super(scopeName, labels);
	}

	protected void addNodeToGraphs(TreeGraphNode node) {
		for (TreeGraph<TreeGraphNode, ALEdge> g : graphs)
			g.addNode(node);
	}

	@Override
	public TreeGraphNode makeNode(String proposedId) {
		TreeGraphNode result = new TreeGraphNode(scope.newId(true, proposedId), this);
		addNodeToGraphs(result);
		return result;
	}

	@Override
	public TreeGraphNode makeNode(String proposedId, ReadOnlyPropertyList props) {
		TreeGraphNode result = null;
		if (props instanceof SimplePropertyList)
			result = new TreeGraphDataNode(scope.newId(true, proposedId), (SimplePropertyList) props, this);
		else
			result = new TreeGraphReadOnlyDataNode(scope.newId(true, proposedId), props, this);
		addNodeToGraphs(result);
		return result;
	}

	@Override
	public TreeGraphNode makeNode(Class<? extends Node> nodeClass, String proposedId, ReadOnlyPropertyList props) {
		TreeGraphNode result = null;
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class, ReadOnlyPropertyList.class, GraphFactory.class);
		} catch (Exception e) {
			try {
				c = nodeClass.getDeclaredConstructor(Identity.class, SimplePropertyList.class, GraphFactory.class);
			} catch (Exception e1) {
				log.severe(() -> "Constructor for class \"" + nodeClass.getName() + "\" not found.\n" + e1);
			}
		}
		Identity id = scope.newId(true, proposedId);
		try {
			result = (TreeGraphNode) c.newInstance(id, props, this);
			addNodeToGraphs(result);
		} catch (Exception e) {
			log.severe(() -> "Node of class \"" + nodeClass.getName() + "\" could not be instantiated.");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public TreeGraphNode makeNode(Class<? extends Node> nodeClass, String proposedId) {
		TreeGraphNode result = null;
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class, GraphFactory.class);
		} catch (Exception e) {
			log.severe(() -> "Constructor for class \"" + nodeClass.getName() + "\" not found");
		}
		Identity id = scope.newId(true, proposedId);
		try {
			result = (TreeGraphNode) c.newInstance(id, this);
			addNodeToGraphs(result);
		} catch (Exception e) {
			log.severe(() -> "Node of class \"" + nodeClass.getName() + "\" could not be instantiated");
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void manageGraph(NodeSet<? extends Node> graph) {
		if (graph instanceof TreeGraph)
			graphs.add((TreeGraph<TreeGraphNode, ALEdge>) graph);
	}

	@Override
	public void unmanageGraph(NodeSet<? extends Node> graph) {
		graphs.remove(graph);
	}

	protected void onParentChanged() {
		for (TreeGraph<TreeGraphNode, ALEdge> tg : graphs)
			tg.onParentChanged();
	}


}

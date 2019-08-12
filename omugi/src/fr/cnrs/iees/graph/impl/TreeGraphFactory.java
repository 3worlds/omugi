package fr.cnrs.iees.graph.impl;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

public class TreeGraphFactory extends GraphFactoryAdapter {

	private Logger log = Logger.getLogger(TreeGraphFactory.class.getName());
	/** the list of graphs managed by this factory */
	private Set<TreeGraph<TreeGraphNode, ALEdge>> graphs = new HashSet<>();

	/**
	 * basic constructor, only requires a scope
	 * 
	 * @param scopeName the scope identifier, e.g. "GraphFactory"
	 */
	public TreeGraphFactory(String scopeName) {
		super(scopeName);
	}

	/**
	 * basic constructor, only requires a scope
	 * 
	 * @param scopeName the scope identifier, e.g. "GraphFactory"
	 */
	public TreeGraphFactory() {
		super("TGF");
	}

	/**
	 * constructor with labels for sub-classes of Node and Edge
	 * 
	 * @param scopeName the scope identifier, e.g. "GraphFactory"
	 * @param labels    a Map of (labels,class names) associating a label to a java
	 *                  class name
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

	@Override
	public void removeNode(Node node) {
		/*
		 * Its important for graph editors that all trace of a deleted element be
		 * removed for the system.
		 */
		scope.removeId(node.id());
		for (Edge e : node.edges())
			scope.removeId(e.id());
		for (TreeGraph<TreeGraphNode, ALEdge> g : graphs)
			g.removeNode((TreeGraphNode) node);
	}

}

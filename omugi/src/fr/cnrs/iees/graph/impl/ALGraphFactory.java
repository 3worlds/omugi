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

/**
 * The factory for ALGraphs (node and edge factory).
 * A factory manages a list of graphs, i.e. every time a new node/edge is created, it is added
 * to all graphs in the list. Removal of nodes is treated at the graph level.
 * To have different graphs, you must have a different factory (is this a flaw?)
 * 
 * @author Jacques Gignoux - 13 mai 2019
 *
 */
public class ALGraphFactory extends GraphFactoryAdapter {

	private Logger log = Logger.getLogger(ALGraphFactory.class.getName());
	
	/** the list of graphs managed by this factory */
	private Set<ALGraph<ALNode,ALEdge>> graphs = new HashSet<>();

	/**
	 * basic constructor, only requires a scope
	 * @param scopeName the scope identifier, e.g. "GraphFactory"
	 */
	public ALGraphFactory(String scopeName) {
		super(scopeName);
	}
	
	/**
	 * basic constructor with default scope
	 */
	public ALGraphFactory() {
		super();
	}

	/**
	 * constructor with labels for sub-classes of Node and Edge
	 * @param scopeName the scope identifier, e.g. "GraphFactory"
	 * @param labels a Map of (labels,class names) associating a label to a java class name
	 */
	public ALGraphFactory(String scopeName,Map<String,String> labels) {
		super(scopeName,labels);
	}

	// NodeFactory

	@SuppressWarnings("unchecked")
	@Override
	public void manageGraph(NodeSet<? extends Node> graph) {
		if (graph instanceof ALGraph)
			graphs.add((ALGraph<ALNode, ALEdge>) graph);
	}
	
	@Override
	public void unmanageGraph(NodeSet<? extends Node> graph) {
		graphs.remove(graph);
	}

	private void addNodeToGraphs(ALNode node) {
		for (ALGraph<ALNode,ALEdge> g:graphs)
			g.addNode(node);
	}

	@Override
	public ALNode makeNode(String proposedId) {
		ALNode result = new ALNode(scope.newId(true,proposedId),this);
		addNodeToGraphs(result);
		return result;
	}

	@Override
	public ALNode makeNode(String proposedId, ReadOnlyPropertyList props) {
		ALNode result = null;
		if (props instanceof SimplePropertyList)
			result = new ALDataNode(scope.newId(true,proposedId),(SimplePropertyList) props,this);
		else
			result = new ALReadOnlyDataNode(scope.newId(true,proposedId),props,this);
		addNodeToGraphs(result);
		return result;
	}

	@Override
	public ALNode makeNode(Class<? extends Node> nodeClass, String proposedId, ReadOnlyPropertyList props) {
		ALNode result = null;
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,
				ReadOnlyPropertyList.class,
				GraphFactory.class);
		} catch (Exception e) {
			try {
				c = nodeClass.getDeclaredConstructor(Identity.class,
					SimplePropertyList.class,
					GraphFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope.newId(true,proposedId);
		try {
			result = (ALNode) c.newInstance(id,props,this);
			addNodeToGraphs(result);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return result;
	}

	@Override
	public ALNode makeNode(Class<? extends Node> nodeClass, String proposedId) {
		ALNode result = null;
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,GraphFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
		}
		Identity id = scope.newId(true,proposedId);
		try {
			result = (ALNode) c.newInstance(id,this);
			addNodeToGraphs(result);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return result;
	}

	@Override
	public void removeNode(Node node) {
		scope.removeId(node.id());
		for (ALGraph<ALNode,ALEdge> g:graphs)
			g.removeNode((ALNode) node);	
	}

}

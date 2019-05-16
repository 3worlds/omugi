package fr.cnrs.iees.graph.impl;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

public class TreeGraphFactory extends GraphFactoryAdapter<TreeGraphNode,ALEdge> {

	private Logger log = Logger.getLogger(TreeGraphFactory.class.getName());
	/** the list of graphs managed by this factory */
	private Set<TreeGraph<TreeGraphNode,ALEdge>> graphs = new HashSet<>();
	
	/**
	 * basic constructor, only requires a scope
	 * @param scopeName the scope identifier, e.g. "GraphFactory"
	 */
	public TreeGraphFactory(String scopeName) {
		super(scopeName);
	}
	
	/**
	 * constructor with labels for sub-classes of Node and Edge
	 * @param scopeName the scope identifier, e.g. "GraphFactory"
	 * @param labels a Map of (labels,class names) associating a label to a java class name
	 */
	public TreeGraphFactory(String scopeName, Map<String,String> labels) {
		super(scopeName,labels);
	}
	
	private void addNodeToGraphs(TreeGraphNode node) {
		for (TreeGraph<TreeGraphNode,ALEdge> g:graphs)
			g.addNode(node);
	}

	@Override
	public TreeGraphNode makeNode(String proposedId) {
		return new TreeGraphNode(scope.newId(proposedId),this);
	}

	@Override
	public TreeGraphNode makeNode(String proposedId, ReadOnlyPropertyList props) {
		TreeGraphNode result = null;
		if (props instanceof SimplePropertyList)
			result =  new TreeGraphDataNode(scope.newId(proposedId),this,(SimplePropertyList) props);
		else
			result =  new TreeGraphReadOnlyDataNode(scope.newId(proposedId),this,props);
		addNodeToGraphs(result);
		return result;
	}

	@Override
	public TreeGraphNode makeNode(Class<? extends TreeGraphNode> nodeClass, String proposedId,
			ReadOnlyPropertyList props) {
		TreeGraphNode result = null;
		Constructor<? extends TreeGraphNode> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,
				ReadOnlyPropertyList.class,
				NodeFactory.class);
		} catch (Exception e) {
			try {
				c = nodeClass.getDeclaredConstructor(Identity.class,
					SimplePropertyList.class,
					NodeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope.newId(proposedId);
		try {
			result = c.newInstance(id,props,this);
			addNodeToGraphs(result);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return result;
	}

	@Override
	public TreeGraphNode makeNode(Class<? extends TreeGraphNode> nodeClass, String proposedId) {
		TreeGraphNode result = null;
		Constructor<? extends TreeGraphNode> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,NodeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
		}
		Identity id = scope.newId(proposedId);
		try {
			result = c.newInstance(id,this);
			addNodeToGraphs(result);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void manageGraph(NodeSet<? extends TreeGraphNode> graph) {
		if (graph instanceof TreeGraph)
			graphs.add((TreeGraph<TreeGraphNode, ALEdge>) graph);
	}

	@Override
	public void unmanageGraph(NodeSet<? extends TreeGraphNode> graph) {
		graphs.remove(graph);
	}
	
	// NB all makeEdge methods are the same as in ALGraphFactory...

	@Override
	public ALEdge makeEdge(Node start, Node end, String proposedId) {
		return new ALEdge(scope.newId(proposedId),start,end,this);
	}

	@Override
	public ALEdge makeEdge(Node start, Node end, String proposedId, ReadOnlyPropertyList props) {
		if (props instanceof SimplePropertyList)
			return new ALDataEdge(scope.newId(proposedId),start,end,(SimplePropertyList)props,this);
		else
			return new ALReadOnlyDataEdge(scope.newId(proposedId),start,end,props,this);
	}

	@Override
	public ALEdge makeEdge(Class<? extends ALEdge> edgeClass, Node start, Node end, String proposedId,
			ReadOnlyPropertyList props) {
		Constructor<? extends ALEdge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				ReadOnlyPropertyList.class,EdgeFactory.class);
		} catch (Exception e) {
			try {
				c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
					SimplePropertyList.class,EdgeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope.newId(proposedId);
		try {
			return c.newInstance(id,start,end,props,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public ALEdge makeEdge(Class<? extends ALEdge> edgeClass, Node start, Node end, String proposedId) {
		Constructor<? extends ALEdge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				EdgeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
		}
		Identity id = scope.newId(proposedId);
		try {
			return c.newInstance(id,start,end,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}


}

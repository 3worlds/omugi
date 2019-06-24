package fr.cnrs.iees.graph.impl;

import java.util.HashMap;
import java.util.Map;
import au.edu.anu.rscs.aot.collections.QuickListOfLists;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.NodeFactory;

/**
 * A graph implemented as an adjacency list ("AL")
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
// tested OK with version 0.2.0 on 17/5/2019
public class ALGraph<N extends ALNode,E extends ALEdge> implements Graph<N,E> {

	/** the adjacency list (= list of all nodes, each node storing its edges */
	private Map<String,N> nodes = new HashMap<>();
	
	private GraphFactory factory = null;
	
	// constructors
	public ALGraph(GraphFactory nfactory) {
		super();
		factory = nfactory;
		factory.manageGraph(this);
	}
	
	@Override
	public int nNodes() {
		return nodes.size();
	}

	@Override
	public Iterable<N> nodes() {
		return nodes.values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<E> edges() {
		QuickListOfLists<E> edges = new QuickListOfLists<>();
		for (ALNode n:nodes.values())
			edges.addList((Iterable<E>) n.edges(Direction.OUT));
		return edges;
	}

	@Override
	public boolean contains(N node) {
		return nodes.containsKey(node.id());
	}

	// Textable
	
	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName()+ptr; 
	}

	// slow
	@Override
	public int nEdges() {
		return countEdges(nodes.values());
	}

	@Override
	public NodeFactory nodeFactory() {
		return factory;
	}

	@Override
	public void addNode(N node) {
		nodes.put(node.id(),node);
	}

	@Override
	public void removeNode(N node) {
		nodes.remove(node.id());		
	}
	
	protected static int countEdges(Iterable<? extends ALNode> nodes) {
		int n=0;
		for (ALNode node:nodes)
			n += node.degree(Direction.OUT);
		return n;

	}

	@Override
	public EdgeFactory edgeFactory() {
		return factory;
	}
	
	// Object
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

	@Override
	public N find(String id) {
		return nodes.get(id);
	}

}

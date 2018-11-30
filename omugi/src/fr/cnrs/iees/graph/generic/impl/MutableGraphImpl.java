package fr.cnrs.iees.graph.generic.impl;

import java.util.HashMap;
import java.util.Map;

import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.graph.generic.Direction;
import fr.cnrs.iees.graph.generic.DynamicGraph;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Matrix;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.ens.biologie.generic.Sizeable;
import fr.ens.biologie.generic.Textable;

/**
 * A very lightweight implementation of a Mutable graph - it only keeps the root
 * nodes of the graph, everything else is floating in the air. Veeery slow for
 * most operations !
 * 
 * @author gignoux - 6 sept. 2017
 *
 */
public class MutableGraphImpl<N extends Node, E extends Edge> 
		implements Graph<N,E>, DynamicGraph<N, E>, Sizeable, Textable {

	boolean changed = false;
	/** for fast searching on node Id */	
	protected Map<Uid,N> nodes = new HashMap<>();
	
	// Consider using AotList for comodification problem
	
	// Constructors

	public MutableGraphImpl() {
		super();
	}

	public MutableGraphImpl(Iterable<N> list) {
		super();
		for (N n:list)
			nodes.put(n.getId(),n);
	}

	public MutableGraphImpl(GraphImporter<N, E> gl) {
		this(gl.getGraph().nodes());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addEdge(E edge) {
		N node = (N) edge.startNode();
		if (nodes.containsKey(node.getId()))
			node.addEdge(edge, Direction.OUT);
		else
			nodes.put(node.getId(), node);
		node = (N) edge.endNode();
		if (nodes.containsKey(node.getId()))
			node.addEdge(edge, Direction.IN);
		else
			nodes.put(node.getId(), node);
		changed = true;
	}

	// DynamicGraph
	
	@Override
	public void addNode(N node) {
		nodes.put(node.getId(), node);
		changed = true;
	}

	// when an edge is removed from the graph, this has no consequences on Nodes
	// Note: the edge is NOT disconnected from the node, that's another issue
	@Override
	public void removeEdge(Edge edge) {
	}

	// when a Node is removed from the graph, this has no consequences on edges
	// Note: the node is NOT disconnected - that's another issue
	@Override
	public void removeNode(Node node) {
		nodes.remove(node.getId(), node);
		changed = true;
	}

	@Override
	public void addNodes(Iterable<N> nodelist) {
		for (N n : nodelist) {
			nodes.put(n.getId(), n);
		}
	}

	@Override
	public void removeNodes(Iterable<N> nodelist) {
		for (N n : nodelist)
			removeNode(n);
	}

	@Override
	public void clear() {
		nodes.clear();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterable<N> nodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<E> edges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<N> roots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<N> leaves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(N node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Matrix adjacencyMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix incidenceMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public N findNode(Uid id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E findEdge(Uid id) {
		// TODO Auto-generated method stub
		return null;
	}
}

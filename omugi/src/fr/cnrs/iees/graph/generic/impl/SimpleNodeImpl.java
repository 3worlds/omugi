package fr.cnrs.iees.graph.generic.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.cnrs.iees.graph.generic.Direction;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.generic.NodeAdapter;

/**
 * <p>The simplest possible Node implementation. This class only has:</p>
 * <ul>
 * <li>a unique id</li>
 * <li>a list of in edges</li> 
 * <li>a list of out edges</li>
 * </ul>
 * <p>It uses {@link ArrayList}s to store edges, as these are faster than {@link LinkedList}s. Initial capacity
 * is 10 for each list of edges, that should be sufficient for most cases. If needed, a
 * constructor with other initial capacities is provided. Constructors are protected
 * and are accessible only through NodeFactory, as some characteristics of the graph
 * (eg, a graph with no loops) impose restrictions on which nodes and edges can be created
 * and this cannot be known by the nodes or edges.</p>
 * 
 * @author gignoux - 30 août 2017
 *
 */
// Tested OK with version 0.0.1 on 7/11/2018
public class SimpleNodeImpl extends NodeAdapter {
	
	// Constructors

	/**
	 * Standard constructor with initial capacity of edge lists of 10
	 */
	protected SimpleNodeImpl(GraphElementFactory<? extends Node, ? extends Edge> factory) {
		super(factory);
		edges = new EnumMap<Direction,Collection<Edge>>(Direction.class);
		// initial capacity is 10 - should be enough for most graphs
		edges.put(Direction.IN,new ArrayList<Edge>());
		edges.put(Direction.OUT,new ArrayList<Edge>());
	}

	/**
	 * Constructor with settable capacity to optimize code (eg when memory must be spared)
	 * @param capacity
	 */
	protected SimpleNodeImpl(int capacity,
			GraphElementFactory<? extends Node, ? extends Edge> factory) {
		super(factory);
		edges = new EnumMap<Direction,Collection<Edge>>(Direction.class);
		edges.put(Direction.IN,new ArrayList<Edge>(capacity));
		edges.put(Direction.OUT,new ArrayList<Edge>(capacity));
	}
	
	// NODE

	@Override
	public final Iterable<? extends Edge> getEdges() {
		Collection<Edge> inEdges = edges.get(Direction.IN);
		Collection<Edge> outEdges = edges.get(Direction.OUT);
		Set<Edge> result = new HashSet<Edge>(inEdges.size()+outEdges.size());
		result.addAll(inEdges);
		result.addAll(outEdges);
		return result;
	}

	// ELEMENT
	
	@Override
	public final Collection<Node> traversal(int distance) {
		List<Node> result = new LinkedList<Node>(); 
		traversal(result,this,distance);
		return result;
	}

	@Override
	public final Collection<? extends Node> traversal(int distance, Direction direction) {
		List<Node> result = new LinkedList<Node>(); 
		traversal(result,this,distance,direction);
		return result;
	}

	@Override
	public Node clone() {
		return new SimpleNodeImpl(factory());
	}
}

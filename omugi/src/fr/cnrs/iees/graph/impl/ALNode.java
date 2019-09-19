package fr.cnrs.iees.graph.impl;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.ElementAdapter;
import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * <p>AL stands for "Adjacency List". This class of Node records its links to its neighbouring nodes
 * as a list of Edges. It can only work with ALEdges, ie edges recording their start and end nodes 
 * By default, the edges are directed</p>
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
// Tested OK with version 0.2.0 on 16/5/2019
public class ALNode extends ElementAdapter implements Node {
	
	// for consistency, a graph using these nodes must use compatible edges
	private GraphFactory factory;
	
	// the adjacency list for this node, directed.
	protected EnumMap<Direction,Collection<ALEdge>> edges = null;
	
	/**
	 * Standard constructor
	 * @param id the identifier for this node
	 * @param factory the graph which constructs this node
	 */
	protected ALNode(Identity id, GraphFactory factory) {
		super(id);
		this.factory = factory;
		edges = new EnumMap<Direction,Collection<ALEdge>>(Direction.class);
		// Now using sets to prevent accidental insertion of duplicate edges (duplicates sensu .equals())
		edges.put(Direction.IN,new HashSet<>());
		edges.put(Direction.OUT,new HashSet<>());		
	}
	
	// helper methods
	
	// remove an edge in a given direction
	// caution: do not use while looping (use iterator.remove() for that)
	boolean removeEdge(ALEdge edge, Direction direction) {
		return edges.get(direction).remove(edge);
	}

	// add an edge into an edge list - NB for housekeeping only
	final boolean addEdge(ALEdge edge, Direction direction) {
		if ((direction==Direction.IN)&&(!edge.endNode().equals(this)))
			return false;
		if ((direction==Direction.OUT)&&(!edge.startNode().equals(this)))
			return false;
		return edges.get(direction).add(edge);
	}


	// Node
	
	@Override
	public void disconnect() {
		for (ALEdge e:edges.get(Direction.IN)) 
			e.startNode().removeEdge(e,Direction.OUT);
		for (ALEdge e:edges.get(Direction.OUT)) 
			e.endNode().removeEdge(e,Direction.IN);
		edges.get(Direction.IN).clear();
		edges.get(Direction.OUT).clear();
	}

	// remember: in IN edges, I am the end node; in OUT edges, I am the start node.
	// NB: after a call to this method, this Edge is free-floating - no more references.
	@Override
	public void disconnectFrom(Node node) {
		for (Iterator<ALEdge> it = edges.get(Direction.IN).iterator(); it.hasNext();) {
			ALEdge e = it.next();
			if (e.startNode().equals(node)) {
				e.startNode().removeEdge(e, Direction.OUT);
				it.remove(); // only safe way to remove while looping on the list
			}
		}
		for (Iterator<ALEdge> it = edges.get(Direction.OUT).iterator(); it.hasNext();) {
			ALEdge e = it.next();
			if (e.endNode().equals(node)) {
				e.endNode().removeEdge(e, Direction.IN);
				it.remove();
			}
		}
	}

	@Override
	public void addConnectionsLike(Node node) {
		for (Edge e:node.edges(Direction.IN)) {
			factory.makeEdge(e.startNode(), this);			
		}
		for (Edge e:node.edges(Direction.OUT)) {
			factory.makeEdge(this, e.endNode());
		}
	}

	@Override
	public boolean isLeaf() {
		return edges.get(Direction.OUT).isEmpty();
	}

	@Override
	public boolean isRoot() {
		return edges.get(Direction.IN).isEmpty();
	}

	@Override
	public int degree(Direction direction) {
		return edges.get(direction).size();
	}

	@Override
	public Iterable<? extends ALEdge> edges(Direction direction) {
		return edges.get(direction);
	}

	@Override
	public Iterable<? extends Edge> edges() {
		Set<Edge> result = new HashSet<>();
		result.addAll(edges.get(Direction.IN));
		result.addAll(edges.get(Direction.OUT));
		return result;
	}

	@Override
	public Iterable<? extends Node> nodes(Direction direction) {
		List<Node> list = new LinkedList<>();
		for (ALEdge e:edges.get(direction)) 
			list.add(e.otherNode(this));
		return list;
	}

	@Override
	public Iterable<? extends Node> nodes() {
		Set<Node> list = new HashSet<>();
		for (ALEdge e:edges.get(Direction.IN))
			list.add(e.startNode());
		for (ALEdge e:edges.get(Direction.OUT))
			list.add(e.endNode());
		return list;
	}

	@Override
	public Edge connectTo(Direction direction, Node node) {
		Edge result = null;
		if (direction==Direction.IN)
			result = factory.makeEdge(node,this);
		else if (direction==Direction.OUT)
			result = factory.makeEdge(this,node);
		return result;
	}

	@Override
	public Edge connectTo(Direction direction, Node node, ReadOnlyPropertyList edgeProperties) {
		Edge result = null;
		if (direction==Direction.IN)
			result = factory.makeEdge(node,this,edgeProperties);
		else if (direction==Direction.OUT)
			result = factory.makeEdge(this,node,edgeProperties);
		return result;
	}


	
	@Override
	public void connectTo(Direction direction, Iterable<? extends Node> nodes) {
		for (Node n:nodes)
			connectTo(direction,n);
	}

	@Override
	public NodeFactory factory() {
		return factory;
	}
	
	// Textable 
	
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		for (Edge e:edges(Direction.IN))
			sb.append(" ←").append(e.startNode().toShortString());
		for (Edge e:edges(Direction.OUT))
			sb.append(" →").append(e.endNode().toShortString());
		return sb.toString();
	}

}

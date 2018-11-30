package fr.cnrs.iees.graph.generic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.graph.generic.Direction;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Matrix;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.ens.biologie.generic.Sizeable;
import fr.ens.biologie.generic.Textable;
import fr.ens.biologie.optimisation.QuickListOfLists;


/**
 * Implementation of an immutable Graph (no change in Nodes or Edges lists).
 * Meant to be fast for searching by id. Edges are not stored in the graph as they
 * are stored in the nodes. 
 * 
 * @author gignoux - 6 sept. 2017
 *
 */
// Tested OK with version 0.0.1 on 7/11/2018
// except 3 unimplemented methods (adjacencymatrix(), incidencematrix() and constructor from graphImporter)
public class ImmutableGraphImpl<N extends Node,E extends Edge> 
		implements Graph<N,E>, Sizeable, Textable {

	/** for fast searching on node Id */	
	protected Map<Uid,N> nodes = new HashMap<>();
	/** for fast iteration on nodes */
	protected ArrayList<N> nodeList = null; // ArrayList --> comodification error but normally one should never remove a node from this class
	
	// for descendants only
	protected ImmutableGraphImpl() {
		super();
		nodeList = new ArrayList<N>();
	}

	/**
	 * Construction from a list of free floating nodes
	 * @param list
	 */
	public ImmutableGraphImpl(Iterable<N> list) {
		super();
		for (N n:list)
			nodes.put(n.getId(),n);
		nodeList = new ArrayList<N>(nodes.size());
		nodeList.addAll(nodes.values());
	}
	
	/**
	 * Construction from a Graph importer
	 * @param gl
	 */
	public ImmutableGraphImpl(GraphImporter<N,E> gl) {
		this(gl.getGraph().nodes());
	}
	
	// LOCAL

	private int nEdges() {
		int n=0;
		for (N node:nodes.values())
			n += node.degree(Direction.OUT);
		return n;
	}
	
	// GRAPH
	
	@Override
	public Iterable<N> nodes() {
		return nodes.values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<E> edges() {
		QuickListOfLists<E> edges = new QuickListOfLists<>();
		for (N n:nodes.values())
			edges.addList((Iterable<E>) n.getEdges(Direction.OUT));
		return edges;
	}

	@Override
	public Iterable<N> roots() {
		List<N> result = new ArrayList<>(nodes.size());
		for (N n:nodes.values())
			if (n.isRoot())
				result.add(n);
		return result;
	}

	@Override
	public Iterable<N> leaves() {
		List<N> result = new ArrayList<>(nodes.size());
		for (N n:nodes.values())
			if (n.isLeaf())
				result.add(n);
		return result;
	}

	@Override
	public boolean contains(N node) {
		return nodes.containsKey(node.getId());
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
		return nodes.get(id);
	}

	// Damn slow and inefficient - never use it !
	@Override
	public E findEdge(Uid id) {
		for (E e:edges())
			if (e.getId().equals(id))
				return e;
		return null;
	}
	
	// SIZEABLE

	// NB. according to graph theory, the graph size is the number of edges
	// The number of nodes is called the graph order.
	// Well... 
	@Override
	public int size() {
		return nodes.size();
	}

	// TEXTABLE	

	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName()+ptr; 
	}

	@Override
	public String toShortString() {
		return toUniqueString() + "(" + nodes.size() + " nodes / " + nEdges() + " edges)"; 
	}

	@Override
	public String toDetailedString() {
		String s = toShortString();
		String z = "";
		s += " NODES=(";
		for (N n: nodes.values()) {
			s += n.toShortString() + ",";
			for (Edge e: n.getEdges(Direction.OUT))
				z += e.toShortString() + ",";
		}
		s = s.substring(0, s.length()-1);
		z = z.substring(0, z.length()-1);
		s += ") ";
		s += "EDGES=(";
		s += z;
		s += ")";
		return s;
	}

	// OBJECT
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}


}

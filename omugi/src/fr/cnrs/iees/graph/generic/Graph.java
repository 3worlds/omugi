package fr.cnrs.iees.graph.generic;

import au.edu.anu.rscs.aot.util.Uid;

/**
 * An immutable graph (no possibility to add or remove elements as it is now unrelated
 * to List).
 * @author gignoux - 16 août 2017
 *
 */
public interface Graph<N extends Node, E extends Edge> {
	
	/**
	 * Read-only accessor to all Nodes
	 * @return an Iterable of all Nodes
	 */
	public Iterable<N> nodes();
	
	/**
	 * Read-only accessor to all Edges
	 * @return an Iterable of all Edges
	 */
	public Iterable<E> edges();
	
	/**
	 * Read-only accessor to all root Nodes (if any)
	 * @return an Iterable on all root Nodes
	 */
	public Iterable<N> roots();
	
	/**
	 * Read-only accessor to all leaf Nodes (if any)
	 * @return an Iterable on all leaf Nodes
	 */
	public Iterable<N> leaves();
	
	/**
	 * Checks if this graph contains a particular Node
	 * @param node the Node to search for
	 * @return true if node was found in the graph
	 */
	public boolean contains(N node);
	
	/**
	 * Shows this graph as an adjacency matrix
	 * @return the adjacency matrix
	 */
	public Matrix adjacencyMatrix();
	
	/**
	 * shows this graph as an incidence matrix
	 * @return the incidence matrix
	 */
	public Matrix incidenceMatrix();
	
	/**
	 * finds a Node in the graph based on its ID
	 * @param id
	 * @return
	 */
	public N findNode(Uid id);
	
	/**
	 * finds an Edge in the graph based on its ID
	 * @param id
	 * @return
	 */
	public E findEdge(Uid id);
	
	
}

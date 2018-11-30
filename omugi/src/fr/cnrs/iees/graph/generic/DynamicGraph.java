package fr.cnrs.iees.graph.generic;

/**
 * A mutable graph, with the possibility to add or remove Elements
 * @author gignoux - 25 août 2017
 *
 */
public interface DynamicGraph<N extends Node, E extends Edge> {
	
	public void addEdge(E edge);
	
	public void addNode(N node);
	
	public void removeEdge(E edge);
	
	public void removeNode(N node);
	
	public void addNodes(Iterable<N> nodelist);
	
	public void removeNodes(Iterable<N> nodelist);
	
	public void clear();

}

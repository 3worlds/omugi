package fr.cnrs.iees.graph.generic;

import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.graph.properties.SimplePropertyList;

/**
 * <p>An interface to give a Graph the ability to create Nodes and Edges in an appropriate way.</p>
 * 
 * <p>Although nodes and edges could in theory exist without the context of a graph, as soon as
 * one starts to instantiate them a graph starts to exist. If we want to put some constraints on this graph 
 * (e.g. directed/undirected graph, acyclic graph, tree, multigraph, etc.) then we must be 
 * able to constrain node and edge creation, and a generic public constructor for edges and nodes
 * does not allow this. Even worse, it could break the graph rules unintentionnally. To secure
 * this, nodes and edges must exist only within the context of a graph.</p>
 * 
 * <p>But sometimes it makes sense that a node belongs to more than one graph. In order to allow
 * for this possibility, we separate the node creation ability from the node addition into the graph.
 * This way, a node made by one graph could be added to another. Each Node or Edge will record
 * which factory created it, but not which graphs it belongs to. This way, other instances of the
 * same type can be made by calling the initial factory.</p> 
 * 
 * 
 * @author Jacques Gignoux 7-11-2018
 *
 * @param <N>
 */
public interface GraphElementFactory {

	public Node makeNode();
	
	public ReadOnlyDataNode makeNode(ReadOnlyPropertyList props);

	public DataNode makeNode(SimplePropertyList props);

	public Edge makeEdge(Node start, Node end);
	
	public ReadOnlyDataEdge makeEdge(Node start, Node end, ReadOnlyPropertyList props);
	
	public DataEdge makeEdge(Node start, Node end, SimplePropertyList props);
	
}

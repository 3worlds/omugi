package fr.cnrs.iees.graph;

import java.util.ArrayList;
import java.util.List;

import fr.ens.biologie.generic.Textable;

/**
 * <p>A Graph is defined as a G=(N,E,f) where N is a set of nodes, E a set of edges connecting the 
 * nodes, and f the incidence function mapping every edge to a pair of nodes.</p>
 * <p>This interface is used to represent N, the set of nodes of a graph.</p>
 * 
 * 
 * @author Jacques Gignoux - 14 mai 2019
 * 
 * @param <N> The {@link Node} subclass used to construct the graph
 */
public interface NodeSet<N extends Node>  extends Textable {
	
	/**
	 * Read-only accessor to all Nodes
	 * @return an Iterable of all Nodes
	 */
	public Iterable<N> nodes();
	
	/**
	 * Read-only accessor to all leaf Nodes (if any). Note: a leaf node is a node without
	 * OUT edges.
	 * @return an Iterable on all leaf Nodes
	 */
	public default Iterable<N> leaves() {
		List<N> result = new ArrayList<>(nNodes());
		for (N n : nodes())
			if (n.isLeaf())
				result.add(n);
		return result;
	}

	/**
	 * Read-only accessor to all root Nodes (if any). Note: a root node is a node without
	 * IN edges.
	 * @return an Iterable on all root Nodes
	 */
	public default Iterable<N> roots() {
		List<N> result = new ArrayList<>(nNodes());
		for (N n:nodes())
			if (n.isRoot())
				result.add(n);
		return result;
	}
	
	/**
	 * Checks if this graph contains a particular Node
	 * @param node the Node to search for
	 * @return true if node was found in the graph
	 */
	public boolean contains(N node);
	
	/**
	 * The node factory used to instantiate nodes for this graph.
	 * @return the node factory
	 */
	public NodeFactory<N> nodeFactory();
	
	/**
	 * Use this method to add nodes into a graph. This method is usually invoked by
	 * {@link NodeFactory}{@code .makeNode(...)} at node instantiation time, so that there
	 * is no need to call it explicitly.
	 * 
	 * @param node the node to add to the graph
	 */
	public void addNode(N node);
	
	/**
	 * Use this method to remove a node from a graph.
	 * @param node the node to remove from the graph
	 */
	public void removeNode(N node);

	public int nNodes();
	
	@Override
	public default String toShortString() {
		return toUniqueString() + " (" + nNodes() + " nodes)"; 
	}
	
	@Override
	public default String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		sb.append(" NODES=(");
		for (N n: nodes()) {
			sb.append(n.toShortString() + ",");
		}
		if (sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		sb.append(')');
		return sb.toString();
	}

}

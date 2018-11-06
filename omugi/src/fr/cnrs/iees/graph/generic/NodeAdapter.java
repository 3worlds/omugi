package fr.cnrs.iees.graph.generic;

import java.util.Collection;
import java.util.EnumMap;

/**
 * A base implementation of Node with all the methods that should be universal in descendants
 * @author gignoux - 16 août 2017
 *
 */
public abstract class NodeAdapter extends ElementAdapter implements Node, Element {
	
	protected EnumMap<Direction,Collection<Edge>> edges = null;

	// NODE ==================================================================
	
	@Override
	public final boolean addEdge(Edge edge, Direction direction) {
		return edges.get(direction).add(edge);
	}
	
	@Override
	public final boolean addEdge(Edge edge) {
		if (edge.startNode().equals(this))
			return edges.get(Direction.OUT).add(edge);
		if (edge.endNode().equals(this))
			return edges.get(Direction.IN).add(edge);
		return false;
	}

	@Override
	public final boolean removeEdge(Edge edge, Direction direction) {
		return edges.get(direction).remove(edge);
	}

	@Override
	public final int degree(Direction direction) {
		return edges.get(direction).size();
	}

	@Override
	public final boolean isLeaf() {
		return edges.get(Direction.OUT).isEmpty();
	}

	@Override
	public final boolean isRoot() {
		return edges.get(Direction.IN).isEmpty();
	}

	@Override
	public final Iterable<? extends Edge> getEdges(Direction direction) {
		return edges.get(direction);
	}

	// ELEMENT ==================================================================

	@Override
	public final Node disconnect() {
		for (Edge e:getEdges(Direction.IN)) 
			e.startNode().removeEdge(e,Direction.OUT);
		for (Edge e:getEdges(Direction.OUT)) 
			e.endNode().removeEdge(e,Direction.IN);
		edges.get(Direction.IN).clear();
		edges.get(Direction.OUT).clear();
		return this;
	}

	/**
	 * recursive helper method to construct a traversal
	 * @param list the list to fill up with nodes (initially empty)
	 * @param node the node that will be searched next (initially, this one)
	 * @param distance search depth
	 * @return a list of Nodes connected to this node within <em>distance</em> steps
	 */
	protected final Collection<Node> traversal(Collection<Node> list, Node node, int distance) {
		if (distance>0) {
			if (!list.contains(node)) {
				list.add(node);
				for (Edge e:node.getEdges(Direction.IN))
					traversal(list,e.endNode(),distance-1);
				for (Edge e:node.getEdges(Direction.OUT))
					traversal(list,e.endNode(),distance-1);
			}
		}
		return list;
	}

	/**
	 * recursive helper method to construct a traversal - returns a list of Nodes connected to this node
	 * @param list the list to fill up with nodes (initially empty)
	 * @param node the node that will be searched next (initially, this one)
	 * @param distance search depth
	 * @param direction direction in which to search
	 * @return a list of Nodes connected to this node within <em>distance</em> steps in direction <em>IN</em> or <em>OUT</em>
	 */
	protected final Collection<Node> traversal(Collection<Node> list, Node node, int distance, Direction direction) {
		if (distance>0) {
			if (!list.contains(node)) {
				list.add(node);
				for (Edge e:node.getEdges(direction))
					traversal(list,e.endNode(),distance-1,direction);
			}
		}
		return list;
	}
	
	@Override
	public final String toShortString() {
		return super.toDetailedString();
	}
	
	@Override
	public String toDetailedString() {
		String result = super.toDetailedString();
		result += " " + Direction.IN +"=(";
		for (Edge e:getEdges(Direction.IN))
			result += "["+e.toDetailedString()+"]";
		result += ") " + Direction.OUT +"=(";
		for (Edge e:getEdges(Direction.OUT))
			result += "["+e.toDetailedString()+"]";
		result += ")";
		return result;
	}

	
}

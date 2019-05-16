package fr.cnrs.iees.graph.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.ElementAdapter;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.identity.Identity;

/**
 * 
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
public class ALEdge extends ElementAdapter implements Edge {

	// for consistency, a graph using these nodes must use compatible edges
	private EdgeFactory factory;
	private ALNode start = null;
	private ALNode end = null;

	/**
	 * The only valid constructor for an Edge: an Edge cannot exist without a start and end node
	 * @param start the start Node
	 * @param end the end Node
	 */
	protected ALEdge(Identity id, Node start, Node end, 
			EdgeFactory graph) {
		super(id);
		this.factory = graph;
		if ((start instanceof ALNode) && (end instanceof ALNode)) {
		this.start = (ALNode) start;
		this.end = (ALNode) end;
		this.start.addEdge(this, Direction.OUT);
		this.end.addEdge(this, Direction.IN);
		}
		else
			throw new OmugiException("ALEdge can only link ALNode descendants");
	}
	
	/**
	 * This constructor never to be called: an Edge without a start and end at construction
	 * is invalid
	 */
	@SuppressWarnings("unused")
	private ALEdge() {}

	@Override
	public final void disconnect() {
		start.removeEdge(this, Direction.OUT);
		end.removeEdge(this, Direction.IN);
	}

	// caution: after a call to this method, the Edge is invalid, i.e. with a free-floating end
	@Override
	public final void disconnectFrom(Node node) {
		if (start.equals(node))
			start = null;
		else if (end.equals(node))
			end = null;
	}

	@Override
	public Collection<? extends Node> traversal(int distance) {
		int dist=distance;
		Collection<? extends Node> result1 = start.traversal(distance);
		Collection<? extends Node> result2 = end.traversal(dist);
		Set<Node> result = new HashSet<Node>();
		result.addAll(result1);
		result.addAll(result2);
		return result;
	}

	@Override
	public Collection<? extends Node> traversal(int distance, Direction direction) {
		if (direction==Direction.IN)
			return start.traversal(distance,direction);
		else if (direction==Direction.OUT)
			return end.traversal(distance,direction);
		return null;
	}

	@Override
	public ALNode startNode() {
		return start;
	}

	@Override
	public ALNode endNode() {
		return end;
	}

	@Override
	public final void connect(Node startNode, Node endNode) {
		if ((startNode instanceof ALNode) && (endNode instanceof ALNode)) {
			disconnect();
			start = (ALNode) startNode;
			start.addEdge(this, Direction.OUT);
			end = (ALNode) endNode;
			end.addEdge(this, Direction.IN);
		}
		else
			throw new OmugiException("ALEdge cannot connect non-ALNode nodes.");
	}

	@Override
	public EdgeFactory factory() {
		return factory;
	}

	@Override
	public final void connectLike(Edge element) {
		disconnect();
		connect(element.startNode(),element.endNode());
	}

	@Override
	public final void replace(Edge element) {
		connect(element.startNode(),element.endNode());
		element.disconnect();
	}

	@Override
	public ALNode otherNode(Node other) {
		if (other.equals(end)) return start;
		else if (other.equals(start)) return end;
		throw new OmugiException("Node " + other + " is not part of edge " + this);
	}

}

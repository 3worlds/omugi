package fr.cnrs.iees.graph.generic;

import java.util.Collection;

import fr.cnrs.iees.OmugiException;


/**
 * A base implementation of Edge with all the methods that should be universal in descendants
 * @author gignoux - 16 août 2017
 *
 */
public abstract class EdgeAdapter extends ElementAdapter implements Element, Edge {
	
	private Node start = null;
	private Node end = null;
	
	/**
	 * This constructor never to be called: an Edge without a start and end at construction
	 * is invalid
	 */
	protected EdgeAdapter() {
		super();
		throw new OmugiException("An Edge must be created with a start and an end node");
	}
	
	/**
	 * The only valid constructor for an Edge: an Edge cannot exist without a start and end node
	 * @param start the start Node
	 * @param end the end Node
	 */
	protected EdgeAdapter(Node start, Node end) {
		super();
		this.start = start;
		this.end = end;
		start.addEdge(this, Direction.OUT);
		end.addEdge(this, Direction.IN);
	}
	
	// ELEMENT ==================================================================

	@Override
	public final Edge addConnectionsLike(Element element) {
		Edge edge = (Edge) element;
		start = edge.startNode();
		start.addEdge(this, Direction.OUT);
		end = edge.endNode();
		end.addEdge(this, Direction.IN);
		return this;
	}

	@Override
	public final Edge disconnect() {
		start.removeEdge(this, Direction.OUT);
//		start = null; // error prone - this Edge is floating in the air anyway...
		end.removeEdge(this, Direction.IN);
//		end = null;
		return this;
	}
	
	@Override
	public final Collection<Node> traversal(int distance) {
		// NB: since the start node and end node are connected through this edge, any traversal
		// will contain both of them
		return start.traversal(distance);
	}

	@Override
	public final Collection<? extends Node> traversal(int distance, Direction direction) {
		if (direction==Direction.IN)
			return end.traversal(distance,direction);
		else if (direction==Direction.OUT)
			return start.traversal(distance,direction);
		return null;
	}
	
	
	// EDGE ==================================================================
		
	@Override
	public final Node startNode() {
		return start;
	}

	@Override
	public final Node endNode() {
		return end;
	}

	@Override
	public final Node otherNode(Node other) {
		if (other.equals(end)) return start;
		else if (other.equals(start)) return end;
		throw new OmugiException("Node " + other + " is not part of edge " + this);
	}

	@Override
	public final Edge setStartNode(Node node) {
		start.removeEdge(this, Direction.OUT);
		start = node;
		start.addEdge(this, Direction.OUT);
		return this;
	}

	@Override
	public final Edge setEndNode(Node node) {
		end.removeEdge(this, Direction.IN);
		end = node;
		end.addEdge(this, Direction.IN);
		return this;
	}
	
	@Override
	public final String toShortString() {
		return super.toDetailedString();
	}

	@Override
	public String toDetailedString() {
		return super.toDetailedString()+ " ["+start.toShortString()+ "]-->[" + end.toShortString()+"]";
	}

	
}

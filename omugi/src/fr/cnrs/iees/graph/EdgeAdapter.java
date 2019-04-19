/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          * 
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  OMUGI is an API to implement graphs, as described by graph theory,    *
 *  but also as more commonly used in computing - e.g. dynamic graphs.    *
 *  It interfaces with JGraphT, an API for mathematical graphs, and       *
 *  GraphStream, an API for visual graphs.                                *
 *                                                                        *
 **************************************************************************                                       
 *  This file is part of OMUGI (One More Ultimate Graph Implementation).  *
 *                                                                        *
 *  OMUGI is free software: you can redistribute it and/or modify         *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  OMUGI is distributed in the hope that it will be useful,              *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with OMUGI.  If not, see <https://www.gnu.org/licenses/gpl.html>*
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.identity.Identity;


/**
 * A base implementation of Edge with all the methods that should be universal in descendants
 * @author gignoux - 16 août 2017
 *
 */
public abstract class EdgeAdapter extends GraphElementAdapter implements Edge {
	
	private EdgeFactory factory;
	private Node start = null;
	private Node end = null;
	
	/**
	 * This constructor never to be called: an Edge without a start and end at construction
	 * is invalid
	 */
	@SuppressWarnings("unused")
	private EdgeAdapter() {}
	
	/**
	 * The only valid constructor for an Edge: an Edge cannot exist without a start and end node
	 * @param start the start Node
	 * @param end the end Node
	 */
	protected EdgeAdapter(Identity id, Node start, Node end, EdgeFactory factory) {
		super(id);
		this.factory = factory;
		this.start = start;
		this.end = end;
		start.addEdge(this, Direction.OUT);
		end.addEdge(this, Direction.IN);
	}
	
	// ELEMENT ==================================================================

	@Override
	public final Edge addConnectionsLike(GraphElement element) {
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
		int dist=distance;
		Collection<? extends Node> result1 = start.traversal(distance);
		Collection<? extends Node> result2 = end.traversal(dist);
		Set<Node> result = new HashSet<Node>();
		result.addAll(result1);
		result.addAll(result2);
		return result;
	}

	@Override
	public final Collection<? extends Node> traversal(int distance, Direction direction) {
		if (direction==Direction.IN)
			return start.traversal(distance,direction);
		else if (direction==Direction.OUT)
			return end.traversal(distance,direction);
		return null;
	}

	@Override
	public final EdgeFactory edgeFactory() {
		return factory;
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
	public String toDetailedString() {
		return toShortString()+ " ["+start.toShortString()+ "]→[" + end.toShortString()+"]";
	}

	// Object
	// Important: unicity of edges not only depends on their uniqueId, but also on their end and start
	// nodes
	
	@Override
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		if (!Edge.class.isAssignableFrom(obj.getClass()))
			return false;
		Edge e = (Edge) obj;
		return (scope().id().equals(e.scope().id()) &&
			id().equals(e.id()) &&
			(startNode()==e.startNode()) && // identity of node objects, not equality
			(endNode()==e.endNode())); // identity of node objects, not equality
	}
	
	@Override
	public int hashCode() {
		return (scope().id()+id()+startNode().id()+endNode().id()).hashCode();
	}


}

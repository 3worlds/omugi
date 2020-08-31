/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
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
// Tested OK with version 0.2.0 on 17/5/2019
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
	
	@Override
	public final void disconnectFrom(Direction direction, Node node) {
		if (direction.equals(Direction.IN))
			start.removeEdge(this, Direction.OUT);
		else
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
		else return null;
//		throw new OmugiException("Node " + other + " is not part of edge " + this);
	}

	// Textable
	
	@Override
	public String toDetailedString() {
		if (start==null)
			if (end==null)
				return toShortString() + " [free-floating edge]";
			else
				return toShortString()+ " [NULL]→[" + end.toShortString()+"]";
		else 
			if (end==null)
				return toShortString()+ " ["+start.toShortString()+ "]→[NULL]";
			else
				return toShortString()+ " ["+start.toShortString()+ "]→[" + end.toShortString()+"]";
	}
	/*
	 * renaming can only take if the id is a SimpleIdentity and if the old id exists
	 * and the new does not exist.
	 */

	@Override
	public void rename(String oldId, String newId) {
		id.rename(oldId, newId);
	}	
}

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
package fr.cnrs.iees.playground.elements.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.playground.elements.IEdge;
import fr.cnrs.iees.playground.elements.IGraphElement2;
import fr.cnrs.iees.playground.elements.INode;
import fr.cnrs.iees.playground.factories.IEdgeFactory;


/**
 * A base implementation of Edge with all the methods that should be universal in descendants
 * @author gignoux - 16 août 2017
 *
 */
public  class SimpleEdgeImpl2 extends GraphElementAdapter2 implements IEdge {
	
	private IEdgeFactory factory;
	private INode start = null;
	private INode end = null;
	
	/**
	 * This constructor never to be called: an Edge without a start and end at construction
	 * is invalid
	 */
	@SuppressWarnings("unused")
	private SimpleEdgeImpl2() {}
	
	/**
	 * The only valid constructor for an Edge: an Edge cannot exist without a start and end node
	 * @param start the start Node
	 * @param end the end Node
	 */
	protected SimpleEdgeImpl2(Identity id, INode start, INode end, IEdgeFactory factory) {
		super(id);
		this.factory = factory;
		this.start = start;
		this.end = end;
		start.addEdge(this, Direction.OUT);
		end.addEdge(this, Direction.IN);
	}
	
	// ELEMENT ==================================================================

	@Override
	public final IEdge addConnectionsLike(IGraphElement2 element) {
		IEdge edge = (IEdge) element;
		start = edge.startNode();
		start.addEdge(this, Direction.OUT);
		end = edge.endNode();
		end.addEdge(this, Direction.IN);
		return this;
	}

	@Override
	public final IEdge disconnect() {
		start.removeEdge(this, Direction.OUT);
//		start = null; // error prone - this Edge is floating in the air anyway...
		end.removeEdge(this, Direction.IN);
//		end = null;
		return this;
	}
	
	@Override
	public final Collection<INode> traversal(int distance) {
		int dist=distance;
		Collection<INode> result1 = start.traversal(distance);
		Collection<INode> result2 = end.traversal(dist);
		Set<INode> result = new HashSet<INode>();
		result.addAll(result1);
		result.addAll(result2);
		return result;
	}

	@Override
	public final Collection<? extends INode> traversal(int distance, Direction direction) {
		if (direction==Direction.IN)
			return start.traversal(distance,direction);
		else if (direction==Direction.OUT)
			return end.traversal(distance,direction);
		return null;
	}

	@Override
	public final IEdgeFactory edgeFactory() {
		return factory;
	}
	
	// EDGE ==================================================================
		
	@Override
	public final INode startNode() {
		return start;
	}

	@Override
	public final INode endNode() {
		return end;
	}

	@Override
	public final INode otherNode(INode other) {
		if (other.equals(end)) return start;
		else if (other.equals(start)) return end;
		throw new OmugiException("Node " + other + " is not part of edge " + this);
	}

	@Override
	public final IEdge setStartNode(INode node) {
		start.removeEdge(this, Direction.OUT);
		start = node;
		start.addEdge(this, Direction.OUT);
		return this;
	}

	@Override
	public final IEdge setEndNode(INode node) {
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

	// Object
	// Important: unicity of edges not only depends on their uniqueId, but also on their end and start
	// nodes
	
	@Override
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		if (!IEdge.class.isAssignableFrom(obj.getClass()))
			return false;
		IEdge e = (IEdge) obj;
		return (scope().id().equals(e.scope().id()) &&
			id().equals(e.id()) &&
			(startNode()==e.startNode()) && // identity of node objects, not equality
			(endNode()==e.endNode())); // identity of node objects, not equality
	}
	
	@Override
	public int hashCode() {
		return (scope().id()+id()+startNode().id()+endNode().id()).hashCode();
	}

	@Override
	public String classId() {
		// TODO Auto-generated method stub
		return null;
	}


}

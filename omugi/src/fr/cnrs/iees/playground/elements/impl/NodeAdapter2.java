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
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.playground.elements.IEdge;
import fr.cnrs.iees.playground.elements.IGraphElement2;
import fr.cnrs.iees.playground.elements.INode;
import fr.cnrs.iees.playground.factories.IEdgeFactory;

public abstract class NodeAdapter2 extends GraphElementAdapter2 implements INode {
	
	protected EnumMap<Direction,Collection<IEdge>> edges = null;
	
	// Constructors --------
	
	protected NodeAdapter2(Identity id) {
		super(id);
		edges = new EnumMap<Direction,Collection<IEdge>>(Direction.class);
		// Now using sets to prevent accidental insertion of duplicate edges (duplicates sensu .equals())
		edges.put(Direction.IN,new HashSet<>());
		edges.put(Direction.OUT,new HashSet<>());

	}
	
	// NODE ==================================================================
	
	@Override
	public final boolean addEdge(IEdge edge, Direction direction) {
		if ((direction==Direction.IN)&&(!edge.endNode().equals(this)))
			return false;
		if ((direction==Direction.OUT)&&(!edge.startNode().equals(this)))
			return false;
		return edges.get(direction).add(edge);
	}
	
	@Override
	public final boolean addEdge(IEdge edge) {
		if (edge.startNode().equals(this))
			return edges.get(Direction.OUT).add(edge);
		if (edge.endNode().equals(this))
			return edges.get(Direction.IN).add(edge);
		return false;
	}

	@Override
	public final boolean removeEdge(IEdge edge, Direction direction) {
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
	public final Iterable<? extends IEdge> getEdges(Direction direction) {
		return edges.get(direction);
	}

	// ELEMENT ==================================================================

	@Override
	public final INode disconnect() {
		for (IEdge e:getEdges(Direction.IN)) 
			e.startNode().removeEdge(e,Direction.OUT);
		for (IEdge e:getEdges(Direction.OUT)) 
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
	protected final Collection<INode> traversal(Collection<INode> list, INode node, int distance) {
		if (distance>0) {
			if (!list.contains(node)) {
				list.add(node);
				for (IEdge e:node.getEdges(Direction.IN))
					traversal(list,e.startNode(),distance-1);
				for (IEdge e:node.getEdges(Direction.OUT))
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
	protected final Collection<INode> traversal(Collection<INode> list, INode node, int distance, Direction direction) {
		if (distance>0) {
			if (!list.contains(node)) {
				list.add(node);
				for (IEdge e:node.getEdges(direction))
					if (direction.equals(Direction.OUT))
						traversal(list,e.endNode(),distance-1,direction);
					else 
						traversal(list,e.startNode(),distance-1,direction);
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
		for (IEdge e:getEdges(Direction.IN))
			result += "["+e.toDetailedString()+"]";
		result += ") " + Direction.OUT +"=(";
		for (IEdge e:getEdges(Direction.OUT))
			result += "["+e.toDetailedString()+"]";
		result += ")";
		return result;
	}

	@Override
	public INode addConnectionsLike(IGraphElement2 element) {
		INode node = (INode) element;
		for (IEdge e:node.getEdges(Direction.IN)) {
			IEdgeFactory f =  e.edgeFactory();
			f.makeEdge(e.startNode(), this,e.id());			
		}
		for (IEdge e:node.getEdges(Direction.OUT)) {
			IEdgeFactory f =  e.edgeFactory();
			f.makeEdge(this, e.endNode(),e.id());
		}
		return this;
	}
	@Override
	public final Iterable<? extends IEdge> getEdges() {
		Collection<IEdge> inEdges = edges.get(Direction.IN);
		Collection<IEdge> outEdges = edges.get(Direction.OUT);
		Set<IEdge> result = new HashSet<IEdge>(inEdges.size()+outEdges.size());
		result.addAll(inEdges);
		result.addAll(outEdges);
		return result;
	}
	@Override
	public final Collection<INode> traversal(int distance) {
		List<INode> result = new LinkedList<INode>(); 
		traversal(result,this,distance);
		return result;
	}

	@Override
	public final Collection<? extends INode> traversal(int distance, Direction direction) {
		List<INode> result = new LinkedList<INode>(); 
		traversal(result,this,distance,direction);
		return result;
	}

}

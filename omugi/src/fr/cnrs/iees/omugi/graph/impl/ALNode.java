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
package fr.cnrs.iees.omugi.graph.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.cnrs.iees.omugi.graph.DataHolder;
import fr.cnrs.iees.omugi.graph.Direction;
import fr.cnrs.iees.omugi.graph.Edge;
import fr.cnrs.iees.omugi.graph.ElementAdapter;
import fr.cnrs.iees.omugi.graph.GraphFactory;
import fr.cnrs.iees.omugi.graph.Node;
import fr.cnrs.iees.omugi.graph.NodeFactory;
import fr.cnrs.iees.omugi.graph.ReadOnlyDataHolder;
import fr.cnrs.iees.omugi.identity.Identity;
import fr.cnrs.iees.omugi.properties.ReadOnlyPropertyList;

/**
 * <p>The {@link Node} implementation to use with {@link ALGraph}.
 * AL stands for "Adjacency List". This class of node records its links to its neighbouring nodes
 * as a list of {@link ALEdge}s. By default, the edges are directed.</p>
 * 
 * <p>Internally, edges are stored in two {@link Set}s, so that duplicate edges are not permitted.
 * The test for equality between edges is based on their unique identifier {@link Edge#id()}.
 * The constructor is hidden (protected): only {@link ALGraphFactory} can instantiate 
 * {@code ALNode}s.</p>
 * 
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
// Tested OK with version 0.2.0 on 16/5/2019
public class ALNode extends ElementAdapter implements Node {
	
	// for consistency, a graph using these nodes must use compatible edges
	private GraphFactory factory;
	
	// the adjacency list for this node, directed.
	protected EnumMap<Direction,Collection<ALEdge>> edges = null;
	
	/**
	 * This constructor must only be invoked through a {@link NodeFactory}.
	 * 
	 * @param id the identifier for this node
	 * @param factory the factory which constructs this node
	 */
	protected ALNode(Identity id, GraphFactory factory) {
		super(id);
		this.factory = factory;
		edges = new EnumMap<Direction,Collection<ALEdge>>(Direction.class);
		// Now using sets to prevent accidental insertion of duplicate edges (duplicates sensu .equals())
		edges.put(Direction.IN,new HashSet<>());
		edges.put(Direction.OUT,new HashSet<>());		
	}
	
	// helper methods
	
	// remove an edge in a given direction
	// caution: do not use while looping (use iterator.remove() for that)
	boolean removeEdge(ALEdge edge, Direction direction) {
		return edges.get(direction).remove(edge);
	}

	// add an edge into an edge list - NB for housekeeping only
	final boolean addEdge(ALEdge edge, Direction direction) {
		if ((direction==Direction.IN)&&(!edge.endNode().equals(this)))
			return false;
		if ((direction==Direction.OUT)&&(!edge.startNode().equals(this)))
			return false;
		return edges.get(direction).add(edge);
	}


	// Node
	
	@Override
	public void disconnect() {
		for (ALEdge e:edges.get(Direction.IN)) 
			e.startNode().removeEdge(e,Direction.OUT);
		for (ALEdge e:edges.get(Direction.OUT)) 
			e.endNode().removeEdge(e,Direction.IN);
		edges.get(Direction.IN).clear();
		edges.get(Direction.OUT).clear();
	}

	// remember: in IN edges, I am the end node; in OUT edges, I am the start node.
	// NB: after a call to this method, this Edge is free-floating - no more references.
	@Override
	public void disconnectFrom(Node node) {
		for (Iterator<ALEdge> it = edges.get(Direction.IN).iterator(); it.hasNext();) {
			ALEdge e = it.next();
			if (e.startNode().equals(node)) {
				e.startNode().removeEdge(e, Direction.OUT);
				it.remove(); // only safe way to remove while looping on the list
			}
		}
		for (Iterator<ALEdge> it = edges.get(Direction.OUT).iterator(); it.hasNext();) {
			ALEdge e = it.next();
			if (e.endNode().equals(node)) {
				e.endNode().removeEdge(e, Direction.IN);
				it.remove();
			}
		}
	}
	
	@Override
	public void disconnectFrom(Direction direction, Node node) {
		if (direction.equals(Direction.IN)) {
			for (Iterator<ALEdge> it = edges.get(Direction.IN).iterator(); it.hasNext();) {
				ALEdge e = it.next();
				if (e.startNode().equals(node)) {
					e.startNode().removeEdge(e, Direction.OUT);
					it.remove(); // only safe way to remove while looping on the list
				}
			}
		}
		else {
			for (Iterator<ALEdge> it = edges.get(Direction.OUT).iterator(); it.hasNext();) {
				ALEdge e = it.next();
				if (e.endNode().equals(node)) {
					e.endNode().removeEdge(e, Direction.IN);
					it.remove();
				}
			}
		}
	}


	@Override
	public void addConnectionsLike(Node node) {
		for (Edge e:node.edges(Direction.IN)) {
			if (e instanceof DataHolder)
				factory.makeEdge(e.startNode(),this,((DataHolder)e).properties());
			else if (e instanceof ReadOnlyDataHolder)
				factory.makeEdge(e.startNode(),this,((ReadOnlyDataHolder)e).properties());
			else
				factory.makeEdge(e.startNode(), this);			
		}
		for (Edge e:node.edges(Direction.OUT)) {
			if (e instanceof DataHolder)
				factory.makeEdge(this,e.endNode(),((DataHolder)e).properties());
			else if (e instanceof ReadOnlyDataHolder)
				factory.makeEdge(this,e.endNode(),((ReadOnlyDataHolder)e).properties());
			else
				factory.makeEdge(this,e.endNode());			
		}
	}

	@Override
	public boolean isLeaf() {
		return edges.get(Direction.OUT).isEmpty();
	}

	@Override
	public boolean isRoot() {
		return edges.get(Direction.IN).isEmpty();
	}

	@Override
	public int degree(Direction direction) {
		return edges.get(direction).size();
	}

	@Override
	public Collection<? extends ALEdge> edges(Direction direction) {
		return Collections.unmodifiableCollection(edges.get(direction));
	}

	@Override
	public Collection<? extends Edge> edges() {
		Set<Edge> result = new HashSet<>();
		result.addAll(edges.get(Direction.IN));
		result.addAll(edges.get(Direction.OUT));
		return Collections.unmodifiableCollection(result);
	}

	@Override
	public Collection<? extends Node> nodes(Direction direction) {
		List<Node> list = new LinkedList<>();
		for (ALEdge e:edges.get(direction)) 
			list.add(e.otherNode(this));
		return Collections.unmodifiableCollection(list);
	}

	@Override
	public Collection<? extends Node> nodes() {
		Set<Node> list = new HashSet<>();
		for (ALEdge e:edges.get(Direction.IN))
			list.add(e.startNode());
		for (ALEdge e:edges.get(Direction.OUT))
			list.add(e.endNode());
		return Collections.unmodifiableCollection(list);
	}

	@Override
	public Edge connectTo(Direction direction, Node node) {
		Edge result = null;
		if (direction==Direction.IN)
			result = factory.makeEdge(node,this);
		else if (direction==Direction.OUT)
			result = factory.makeEdge(this,node);
		return result;
	}

	@Override
	public Edge connectTo(Direction direction, Node node, ReadOnlyPropertyList edgeProperties) {
		Edge result = null;
		if (direction==Direction.IN)
			result = factory.makeEdge(node,this,edgeProperties);
		else if (direction==Direction.OUT)
			result = factory.makeEdge(this,node,edgeProperties);
		return result;
	}
	
	@Override
	public void connectTo(Direction direction, Collection<? extends Node> nodes) {
		for (Node n:nodes)
			connectTo(direction,n);
	}

	@Override
	public NodeFactory factory() {
		return factory;
	}
	
	// Textable 
	
	/**
	 * Displays an ALNode as follows (on a single line):
	 * 
	 * <pre>
	 * node_label:node_name=[
	 *    →out_node_label:out_node_name  // end node of outgoing edge, repeated as needed
	 *    ←in_node_label:in_node_name    // start node of incoming edge, repeated as needed
	 * ] 
	 * </pre>
	 * <p>e.g.: {@code Node:0=[←Node:1 ←Node:2 →Node:3 →Node:4]}</p>
	 */
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		for (Edge e:edges(Direction.IN))
			sb.append(" ←").append(e.startNode().toShortString());
		for (Edge e:edges(Direction.OUT))
			sb.append(" →").append(e.endNode().toShortString());
		return sb.toString();
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

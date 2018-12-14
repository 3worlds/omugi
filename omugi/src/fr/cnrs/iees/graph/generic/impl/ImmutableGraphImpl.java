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
package fr.cnrs.iees.graph.generic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.rscs.aot.collections.QuickListOfLists;
import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.graph.generic.Direction;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Matrix;
import fr.cnrs.iees.graph.generic.Node;
import fr.ens.biologie.generic.Sizeable;
import fr.ens.biologie.generic.Textable;


/**
 * Implementation of an immutable Graph (no change in Nodes or Edges lists).
 * Meant to be fast for searching by id. Edges are not stored in the graph as they
 * are stored in the nodes. 
 * 
 * @author gignoux - 6 sept. 2017
 *
 */
// Tested OK with version 0.0.1 on 7/11/2018
// except 3 unimplemented methods (adjacencymatrix(), incidencematrix() and constructor from graphImporter)
public class ImmutableGraphImpl<N extends Node,E extends Edge> 
		implements Graph<N,E>, Sizeable, Textable {

	/** for fast searching on node Id */	
	private Map<Uid,N> nodes = new HashMap<>();
	/** for fast iteration on nodes */
	private ArrayList<N> nodeList = null; // ArrayList --> comodification error but normally one should never remove a node from this class
	
	// for descendants only
	protected ImmutableGraphImpl() {
		super();
		nodeList = new ArrayList<N>();
	}

	/**
	 * Construction from a list of free floating nodes
	 * @param list
	 */
	public ImmutableGraphImpl(Iterable<N> list) {
		super();
		for (N n:list)
			nodes.put(n.getId(),n);
		nodeList = new ArrayList<N>(nodes.size());
		nodeList.addAll(nodes.values());
	}
	
	// LOCAL

	private int nEdges() {
		int n=0;
		for (N node:nodes.values())
			n += node.degree(Direction.OUT);
		return n;
	}
	
	// GRAPH
	
	@Override
	public Iterable<N> nodes() {
		return nodes.values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<E> edges() {
		QuickListOfLists<E> edges = new QuickListOfLists<>();
		for (N n:nodes.values())
			edges.addList((Iterable<E>) n.getEdges(Direction.OUT));
		return edges;
	}

	@Override
	public Iterable<N> roots() {
		List<N> result = new ArrayList<>(nodes.size());
		for (N n:nodes.values())
			if (n.isRoot())
				result.add(n);
		return result;
	}

	@Override
	public Iterable<N> leaves() {
		List<N> result = new ArrayList<>(nodes.size());
		for (N n:nodes.values())
			if (n.isLeaf())
				result.add(n);
		return result;
	}

	@Override
	public boolean contains(N node) {
		return nodes.containsKey(node.getId());
	}

	@Override
	public Matrix adjacencyMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix incidenceMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public N findNode(Uid id) {
		return nodes.get(id);
	}

	// Damn slow and inefficient - never use it !
	@Override
	public E findEdge(Uid id) {
		for (E e:edges())
			if (e.getId().equals(id))
				return e;
		return null;
	}
	
	// SIZEABLE

	// NB. according to graph theory, the graph size is the number of edges
	// The number of nodes is called the graph order.
	// Well... 
	@Override
	public int size() {
		return nodes.size();
	}

	// TEXTABLE	

	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName()+ptr; 
	}

	@Override
	public String toShortString() {
		return toUniqueString() + "(" + nodes.size() + " nodes / " + nEdges() + " edges)"; 
	}

	@Override
	public String toDetailedString() {
		String s = toShortString();
		String z = "";
		s += " NODES=(";
		for (N n: nodes.values()) {
			s += n.toShortString() + ",";
			for (Edge e: n.getEdges(Direction.OUT))
				z += e.toShortString() + ",";
		}
		if (s.length()>0)
			s = s.substring(0, s.length()-1);
		if (z.length()>0)
			z = z.substring(0, z.length()-1);
		s += ") ";
		s += "EDGES=(";
		s += z;
		s += ")";
		return s;
	}

	// OBJECT
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}


}

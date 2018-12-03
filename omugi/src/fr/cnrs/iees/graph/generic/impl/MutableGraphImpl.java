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
import java.util.List;

import au.edu.anu.rscs.aot.collections.DynamicList;
import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.graph.generic.Direction;
import fr.cnrs.iees.graph.generic.DynamicGraph;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Matrix;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.ens.biologie.generic.Sizeable;
import fr.ens.biologie.generic.Textable;
import fr.ens.biologie.optimisation.QuickListOfLists;

/**
 * A very lightweight implementation of a Mutable graph - it only keeps the root
 * nodes of the graph, everything else is floating in the air. Veeery slow for
 * most operations !
 * 
 * @author gignoux - 6 sept. 2017
 *
 */
// TODO: idea: a sealable graph, dynamic for a while and then immutable -- first using
// DynamicList, then ArrayList ; use case = configuration in aot.
public class MutableGraphImpl<N extends Node, E extends Edge> 
		implements Graph<N,E>, DynamicGraph<N, E>, Sizeable, Textable {

	/** for easy dynamics */	
	private List<N> nodes = new DynamicList<>();
	
	// Consider using AotList for comodification problem
	
	// Constructors

	public MutableGraphImpl() {
		super();
	}

	public MutableGraphImpl(Iterable<N> list) {
		super();
		nodes = new DynamicList<>(list);
	}

	public MutableGraphImpl(GraphImporter<N, E> gl) {
		this(gl.getGraph().nodes());
	}

	@Override
	public void addEdge(E edge) {
	}

	// DynamicGraph
	
	@Override
	public void addNode(N node) {
		nodes.add(node);
	}

	// when an edge is removed from the graph, this has no consequences on Nodes
	// Note: the edge is NOT disconnected from the node, that's another issue
	@Override
	public void removeEdge(Edge edge) {
	}

	// when a Node is removed from the graph, this has no consequences on edges
	// Note: the node is NOT disconnected - that's another issue
	@Override
	public void removeNode(Node node) {
		nodes.remove(node);
	}

	@Override
	public void addNodes(Iterable<N> nodelist) {
		for (N n : nodelist)
			nodes.add(n);
	}

	@Override
	public void removeNodes(Iterable<N> nodelist) {
		for (N n : nodelist)
			removeNode(n);
	}

	@Override
	public void clear() {
		nodes.clear();
	}

	@Override
	public int size() {
		return nodes.size();
	}

	@Override
	public Iterable<N> nodes() {
		return nodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<E> edges() {
		QuickListOfLists<E> edges = new QuickListOfLists<>();
		for (N n:nodes)
			edges.addList((Iterable<E>) n.getEdges(Direction.OUT));
		return edges;
	}

	@Override
	public Iterable<N> roots() {
		List<N> result = new ArrayList<>(nodes.size());
		for (N n:nodes)
			if (n.isRoot())
				result.add(n);
		return result;
	}

	@Override
	public Iterable<N> leaves() {
		List<N> result = new ArrayList<>(nodes.size());
		for (N n:nodes)
			if (n.isLeaf())
				result.add(n);
		return result;
	}

	@Override
	public boolean contains(N node) {
		return nodes.contains(node);
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
		for (N node:nodes)
			if (node.getId().equals(id))
				return node;
		return null;
	}

	// Damn slow and inefficient - never use it !
	@Override
	public E findEdge(Uid id) {
		for (E e:edges())
			if (e.getId().equals(id))
				return e;
		return null;
	}
}

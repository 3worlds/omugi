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

package fr.cnrs.iees.graph.impl;

import au.edu.anu.rscs.aot.collections.DynamicList;
import fr.cnrs.iees.graph.DynamicGraph;
import fr.cnrs.iees.graph.Edge;

/**
 * Author Ian Davies
 *
 * Date 23 Feb. 2019
 */
public class MutableTreeGraphImpl<N extends TreeGraphNode, E extends Edge> extends ImmutableTreeGraphImpl<N, E>
		implements DynamicGraph<N, E> {

	public MutableTreeGraphImpl() {
		super();
		nodes = new DynamicList<>();
	}

//	@Override
//	public void addEdge(E edge) {
//		// do nothing since this is handled by Node at Edge creation in this system. So
//		// why have it in the interface???
//	}

    //root will be found on next attempted access
	@Override
	public void addNode(N node) {
		nodes.add(node);
		clearRoot();
	}

	/*
	 * when an edge is removed from the graph, this has no consequences on Nodes
	 * Note: the edge is NOT disconnected from the node, that's another issue
	 */
//	@Override
//	public void removeEdge(E edge) {
//		//why is this here at all??
//	}

	@Override
	public void removeNode(N node) {
		nodes.remove(node);
		clearRoot();
	}

	@Override
	public void addNodes(Iterable<N> nodelist) {
		for (N node : nodelist)
			nodes.add(node);
		clearRoot();
	}

	@Override
	public void removeNodes(Iterable<N> nodelist) {
		for (N node : nodelist)
			nodes.remove(node);
		clearRoot();
	}

	@Override
	public void clear() {
		nodes.clear();
		clearRoot();
	}

}

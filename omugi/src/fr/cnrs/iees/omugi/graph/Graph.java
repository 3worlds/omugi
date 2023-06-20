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
package fr.cnrs.iees.omugi.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.omhtk.Textable;

/**
 * <p>The root interface for graphs. Just as in the mathematical definition of a Graph G as
 * G=(N,E,f) where N is a set of nodes and E a set of edges connecting the nodes, this
 * interface extends the {@link NodeSet} and the {@link EdgeSet} interfaces.</p>
 * 
 * <p>A Graph depends on a {@link GraphFactory} to instantiate its
 * components (implementations of {@link Node}s and {@link Edge}s). Therefore, constructors 
 * of implementing classes must always provide a factory as parameters.</p>
 * 
 * @author Jacques Gignoux - 14 mai 2019
 *
 * @param <N> The {@link Node} subclass used to construct the graph
 * @param <E> The {@link Edge} subclass used to construct the graph
 */
public interface Graph<N extends Node, E extends Edge> 
		extends NodeSet<N>, EdgeSet<E>, Textable {
	
	public static enum searchAlgorithm {
		firstDepthSearch;
	}

	@Override
	public default String toShortString() {
		return toUniqueString() + " (" + nNodes() + " nodes / " + nEdges() + " edges)"; 
	}

	@Override
	public default String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		StringBuilder zb = new StringBuilder();
		sb.append(" NODES=(");
		for (N n: nodes()) {
			sb.append(n.toShortString() + ",");
			for (Edge e: n.edges(Direction.OUT))
				zb.append(e.toShortString() + ",");
		}
		if (sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		if (zb.length()>0)
			zb.deleteCharAt(zb.length()-1);
		sb.append(") EDGES=(").append(zb.toString()).append(')');
		return sb.toString();
	}
	
	/**
	 * <p>Computation of 
	 * <a href="https://en.wikipedia.org/wiki/Component_(graph_theory)">connected components</a>
	 *  within a graph.</p>
	 *  <p>The search algorithm can be specified by overriding the {@code searchAlgorithm()} method.
	 *  Default is the <em>First depth search</em> algorithm.</p> 
	 * 
	 * @return a collection of sets of connected nodes
	 */
	public default Collection<Set<Node>> connectedComponents() {
		switch (searchAlgorithm()) {
		case firstDepthSearch: 
			return firstDepthSearch();
		default:
			throw new UnsupportedOperationException("Invalid search agorithm for connected component search");
		}
	}
	
	/**
	 * <p>Selection of a graph search algorithm for finding connected components in a graph.
	 * Default is the <em>First depth search</em> algorithm for undirected graphs as implemented from 
	 * <a href="https://fr.wikipedia.org/wiki/Algorithme_de_parcours_en_profondeur#Impl%C3%A9mentation_r%C3%A9cursive">there</a>.
	 * </p>
	 * <p><strong>NB:</strong> at the moment only <em>First depth search</em> algorithm is implemented.</p>
	 * 
	 * @return an enum value of class {@code searchAlgorithm}
	 */
	public default searchAlgorithm searchAlgorithm() {
		return searchAlgorithm.firstDepthSearch;
	}
	
	// recursive helper method for firstDepthSearch
	private void exploreGraph(Node n, Set<Node> tagged) {
		tagged.add(n);
		for (Edge e:n.edges()) {
			Node nn = e.otherNode(n);
			if (!tagged.contains(nn))
				exploreGraph(nn,tagged);
		}
	}
	
	// implementation of the First depth search algorithm for connected component search
	private Collection<Set<Node>> firstDepthSearch() {
		Set<Node> tagged = new HashSet<>();
		Set<Set<Node>> connectedComponents = new HashSet<>();
		Set<Node> previouslyTagged = new HashSet<>();
		for (Node n:nodes()) {
			if (!tagged.contains(n)) {
				exploreGraph(n,tagged);
				Set<Node> setDif = new HashSet<>(tagged);
				setDif.removeAll(previouslyTagged);				
				connectedComponents.add(setDif);	
				previouslyTagged.addAll(setDif);
			}
		}
		return connectedComponents;
	}
	

}

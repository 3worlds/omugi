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
package fr.cnrs.iees.graph;

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

	@Override
	public default String toShortString() {
		return toUniqueString() + " (" + nNodes() + " nodes / " + nEdges() + " edges)"; 
	}

	@Override
	default String toDetailedString() {
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

}

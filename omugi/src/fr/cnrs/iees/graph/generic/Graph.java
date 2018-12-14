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
package fr.cnrs.iees.graph.generic;

import au.edu.anu.rscs.aot.util.Uid;
import fr.ens.biologie.generic.Sizeable;

/**
 * An immutable graph (no possibility to add or remove elements as it is now unrelated
 * to List).
 * @author gignoux - 16 août 2017
 *
 */
public interface Graph<N extends Node, E extends Edge> extends Sizeable {
	
	/**
	 * Read-only accessor to all Nodes
	 * @return an Iterable of all Nodes
	 */
	public Iterable<N> nodes();
	
	/**
	 * Read-only accessor to all Edges
	 * @return an Iterable of all Edges
	 */
	public Iterable<E> edges();
	
	/**
	 * Read-only accessor to all root Nodes (if any)
	 * @return an Iterable on all root Nodes
	 */
	public Iterable<N> roots();
	
	/**
	 * Read-only accessor to all leaf Nodes (if any)
	 * @return an Iterable on all leaf Nodes
	 */
	public Iterable<N> leaves();
	
	/**
	 * Checks if this graph contains a particular Node
	 * @param node the Node to search for
	 * @return true if node was found in the graph
	 */
	public boolean contains(N node);
	
	/**
	 * Shows this graph as an adjacency matrix
	 * @return the adjacency matrix
	 */
	public Matrix adjacencyMatrix();
	
	/**
	 * shows this graph as an incidence matrix
	 * @return the incidence matrix
	 */
	public Matrix incidenceMatrix();
	
	/**
	 * finds a Node in the graph based on its ID
	 * @param id
	 * @return
	 */
	public N findNode(Uid id);
	
	/**
	 * finds an Edge in the graph based on its ID
	 * @param id
	 * @return
	 */
	public E findEdge(Uid id);
	
	
}

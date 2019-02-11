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
package fr.cnrs.iees.playground.graphs;

import fr.cnrs.iees.OmugiException;
import fr.ens.biologie.generic.Sizeable;

/**
 * An interface to represent the most generic properties of any graph. Meant to be the
 * root ancestor of the graph interface hierarchy
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */
public interface IMinimalGraph<N> extends Sizeable {

	/**
	 * Read-only accessor to all Nodes
	 * @return an Iterable of all Nodes
	 */
	public Iterable<N> nodes();

	/**
	 * Read-only accessor to all leaf Nodes (if any)
	 * @return an Iterable on all leaf Nodes
	 */
	public Iterable<N> leaves();

	/**
	 * Finds the node matching a reference - will issue an Exception if more than one node match
	 * @param reference
	 * @return the matching node, or null if nothing found
	 */
public default N findNodeByReference(String reference) {
		Iterable<N> list = findNodesByReference(reference);
		int i=0;
		N found = null;
		for (N n:list) {
			found = n;
			i++;
		}
		if (i<=1)
			return found;
		else
			throw new OmugiException("more than one Node matching ["+reference+"] found");
	}
	/**
	 * Finds all the nodes matching a reference.
	 * @param reference
	 * @return a read-only list of matching nodes
	 */
	public Iterable<N> findNodesByReference(String reference);
	/**
	 * Read-only accessor to all root Nodes (if any)
	 * @return an Iterable on all root Nodes
	 */
	public Iterable<N> roots();
	/**
	 * Checks if this graph contains a particular Node
	 * @param node the Node to search for
	 * @return true if node was found in the graph
	 */
	public boolean contains(N node);

}

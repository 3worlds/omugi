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
package fr.cnrs.iees.graph;

import java.util.Collection;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.identity.Identity;
import fr.ens.biologie.generic.Textable;

/**
 * The base features common to Edges and Nodes of a graph. NB: all elements must be cloneable
 * @author gignoux - 17 août 2017
 *
 * DECISION: as of 30/8/2017 I decided to get rid of the fluent API interface - it's messing 
 * too much with the OOP hierarchy and generates too many 'just typecasting' descendant methods.
 * TODO: remove all the fluency I added before
 *
 */
public interface GraphElement extends Textable, Identity {
	
	/**
	 * Safely disconnects this Element from the Graph (by taking care of references to it by
	 * neighbouring Graph Elements)
	 */
	public GraphElement disconnect();
	
	/**
	 * Connects this Element <em>exactly</em> like the argument, i.e by losing its former connections.
	 * (= a call to disconnect() followed by addConnectionsLike(element)) 
	 * @param element the element to copy connections from
	 */
	public default GraphElement connectLike(GraphElement element) {
		disconnect();
		addConnectionsLike(element);
		return this;
	}
	
	/**
	 * Replaces another Element <em>element</em> of the same type (Node or Edge) by this instance
	 * by a successive call to addConnectionsLike(<em>element</em>) and <em>element</em>.disconnect().
	 * This instance keeps its former connections.
	 * @param element the Element to replace
	 */
	public default GraphElement replace(GraphElement element) {
		addConnectionsLike(element);
		element.disconnect();
		return this;
	}
	
	/**
	 * Add to this Element the connections found in the argument element. 
	 * @param element the element to copy connections from.
	 */
	public GraphElement addConnectionsLike(GraphElement element);
	
	/**    public static final String LABEL_NAME_SEPARATOR     = ":";

	 * Fetches all the Elements connected to this one and returns the result as a Graph. Use
	 * with caution in unconnected Graphs (will not find subgraphs unconnected to this element).
	 * @return the connected Graph containing this instance
	 */
	public default Collection<Node> traversal() {
		return traversal(Integer.MAX_VALUE);
	}

	/**
	 * As (traversal(), but following only one direction from the starting Element.
	 * @param direction the direction in which to search 
	 * @return the connected Graph containing this instance
	 */
	public default Collection<? extends Node> traversal(Direction direction) {
		return traversal(Integer.MAX_VALUE);
	}
	
	/**
	 * As traversal(), but stopping after <em>distance</em> recursion steps. For example,
	 * node.traversal(1) will return only node, while 
	 * node.traversal(2) will return all the Nodes connected with only 1 edge to this one.
	 * @param distance the number of recursion steps to search
	 * @return the connected Graph containing this instance
	 */
	public Collection<Node> traversal(int distance);

	/**
	 * As traversal(), but following only one direction from the starting Element and
	 * stopping after <em>distance</em> recursion steps.
	 * @param distance the number of recursion steps to search
	 * @param direction the direction in which to search 
	 * @return the connected Graph containing this instance
	 */
	public Collection<? extends Node> traversal(int distance, Direction direction);	
	
	/**
	 * This for descendants only.
	 * @return
	 */
	public default String classId() {
		throw new OmugiException("classId is not defined for Elements - revise your class hierarchy");
	}
	
}

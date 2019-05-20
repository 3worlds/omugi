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

import java.util.Collection;

/**
 * 
 * @author Jacques Gignoux - 9 mai 2019
 *
 * @param <T>
 */
public interface Connected<T extends Connected<?>> {
	
	/**
	 * Safely disconnects this Element from the Graph (by taking care of references to it by
	 * neighbouring Graph Elements)
	 * 
	 * @return this element
	 */
	public void disconnect();
	
	/**
	 * Disconnects this element from the node passed as argument.
	 * 
	 * @param node the node to disconnect from
	 */
	public void disconnectFrom(Node node);
	
	/**
	 * Connects this Element <em>exactly</em> like the argument, i.e by losing its former connections.
	 * (= a call to disconnect() followed by addConnectionsLike(element)) 
	 * @param element the element to copy connections from
	 */
	public void connectLike(T element);
	
	/**
	 * Replaces another Element <em>element</em> of the same type (Node or Edge) by this instance
	 * by a successive call to addConnectionsLike(<em>element</em>) and <em>element</em>.disconnect().
	 * This instance keeps its former connections.
	 * @param element the Element to replace
	 */
	public void replace(T element);

	/**
	 * As traversal(), but stopping after <em>distance</em> recursion steps. For example,
	 * node.traversal(1) will return only node, while 
	 * node.traversal(2) will return all the Nodes connected with only 1 edge to this one.
	 * @param distance the number of recursion steps to search
	 * @return the connected Graph containing this instance
	 */
	public Collection<? extends Node> traversal(int distance);

	/**
	 * As traversal(), but following only one direction from the starting Element and
	 * stopping after <em>distance</em> recursion steps.
	 * @param distance the number of recursion steps to search
	 * @param direction the direction in which to search 
	 * @return the connected Graph containing this instance
	 */
	public Collection<? extends Node> traversal(int distance, Direction direction);	

	/**    public static final String LABEL_NAME_SEPARATOR     = ":";

	 * Fetches all the Elements connected to this one and returns the result as a Graph. Use
	 * with caution in unconnected Graphs (will not find subgraphs unconnected to this element).
	 * @return the connected Graph containing this instance
	 */
	public default Collection<? extends Node> traversal() {
		return traversal(Integer.MAX_VALUE);
	}

	/**
	 * As (traversal(), but following only one direction from the starting Element.
	 * @param direction the direction in which to search 
	 * @return the connected Graph containing this instance
	 */
	public default Collection<? extends Node> traversal(Direction direction) {
		return traversal(Integer.MAX_VALUE,direction);
	}

}
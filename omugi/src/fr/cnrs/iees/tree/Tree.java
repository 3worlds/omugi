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
package fr.cnrs.iees.tree;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.io.parsing.impl.ReferenceParser;
import fr.cnrs.iees.io.parsing.impl.ReferenceTokenizer;

/**
 * <p>A tree, i.e. a graph with a hierarchical structure. Its nodes must implement the TreeNode
 * interface, i.e. have getParent() and getChildren() methods.</p>
 * 
 * @author Jacques Gignoux - 17 déc. 2018
 *
 */
public interface Tree<N extends TreeNode> extends MinimalGraph<N> {

	/**
	 * Accessor to the tree root (a tree has 0 or 1 root).
	 * 
	 * @return the Node at the root of the tree
	 */
	public N root();
	
	public int maxDepth();
	
	public int minDepth();
	
	public Tree<N> subTree(N node);
	
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
	 * <p>Checks that the node passed as argument matches the String reference passed as 
	 * argument. The reference is a locator referring to at most one node in a tree.
	 * 
	 * @param node the node to check
	 * @param ref the String reference
	 * @return
	 */
	public static boolean matchesReference(TreeNode node, String ref) {
		ReferenceTokenizer tk = new ReferenceTokenizer(ref);
		ReferenceParser p = tk.parser();
		return p.matches(node);
	}

}

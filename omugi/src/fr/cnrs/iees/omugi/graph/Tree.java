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

import fr.cnrs.iees.omhtk.Textable;

/**
 * <p>The root interface for trees.</p>
 * <p>A tree is a special case of a graph where nodes are connected only through <em>parent-child</em>
 * relations. A node can only have 0..1 parent and 0..* children. This particular behaviour is
 * implemented by using specific methods and not relying on explicit edges.</p>
 *  
 * @author Jacques Gignoux - 9 mai 2019
 *
 * @param <N> The {@link TreeNode} subclass used to construct the tree
 */
public interface Tree<N extends TreeNode> extends NodeSet<N>, Textable {
	
	/**
	 * Accessor to the tree root (a tree has 0 or 1 root).
	 * 
	 * @return the Node at the root of the tree
	 */
	public N root();
	
	/**
	 * @param node The node that is the root of the required sub-tree
	 * @return The sub-tree nodes with N as the root.
	 */
	@SuppressWarnings("unchecked")
	public default Collection<N> subTree(N node) {
		return (Collection<N>) node.subTree();
	}

	/**
	 * Actions taken when a parent node is changed.
	 */
	// TODO: ugly code - to fix one day.
	public void onParentChanged();
	
	@Override
	public default String toShortString() {
		return toUniqueString() + "(" + nNodes() + " nodes)"; 
	}

	@Override
	public default String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		if (nNodes()>0) {
			sb.append(" NODES=(");
			int last = nNodes()-1;
			int i=0;
			for (N n:nodes()) {
				if (i==last)
					sb.append(n.toShortString());
				else
					sb.append(n.toShortString()).append(',');
				i++;
			}
			sb.append(')');
		}
		return sb.toString();
	}

	/**
	 * Prints a nice hierarchical view of a tree (multi-line).
	 * 
	 * @param parent the node where to start the print
	 * @param indent the String used to indent nodes according to hierarchy
	 */
	public static void printTree(TreeNode parent, String indent) {
		System.out.println(indent + parent.classId() + ":" + parent.id());
		if (parent instanceof ReadOnlyDataHolder)
			for (String key : ((ReadOnlyDataHolder) parent).properties().getKeysAsSet())
				System.out.println(indent + "    " + "-(" + key + "="
						+ ((ReadOnlyDataHolder) parent).properties().getPropertyValue(key) + ")");
		for (TreeNode child : parent.getChildren())
			printTree(child, indent + "    ");
	}

}

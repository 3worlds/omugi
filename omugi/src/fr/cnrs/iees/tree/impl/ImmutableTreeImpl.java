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
package fr.cnrs.iees.tree.impl;

import java.util.ArrayList;
import java.util.List;

import fr.cnrs.iees.tree.Tree;
import fr.cnrs.iees.tree.TreeNode;
import fr.ens.biologie.generic.Textable;

/**
 * An immutable strict tree (not changeable after construction).
 * 
 * @author Jacques Gignoux - 18 déc. 2018
 *
 * @param <N>
 */
public class ImmutableTreeImpl<N extends TreeNode> 
		implements Tree<N>, Textable {
	
	private N root = null;
	/** for fast iteration on nodes */
	private List<N> nodeList = null; // ArrayList --> comodification error but normally one should never remove a node from this class
	
	private int minDepth = 0;
	private int maxDepth = 0;

	// Constructors
	
	// for descendants only
	protected ImmutableTreeImpl() {
		super();
		nodeList = new ArrayList<N>();
	}
	
	/**
	 * Construction from a list of free floating nodes.
	 * The first node found without a parent is assumed to be the root
	 * CAUTION: no check that the nodes are properly linked
	 * @param list
	 */
	public ImmutableTreeImpl(Iterable<N> list) {
		super();
		nodeList = new ArrayList<N>();
		for (N node:list) {
			nodeList.add(node);
			if (root==null)
				if (node.getParent()==null)
					root = node;
		}
		computeDepths(root);
	}
	
	// recursive to insert a whole tree into the node list
	@SuppressWarnings("unchecked")
	private void insertAllChildren(N parent, List<N> list) {
		for (TreeNode child:parent.getChildren()) {
			list.add((N)child);
			insertAllChildren((N)child,list); // comodification error ?
		}
	}
	
	/**
	 * Construction from an initial node used as root
	 * @param root
	 */
	public ImmutableTreeImpl(N root) {
		super();
		this.root = root;
		insertAllChildren(root,nodeList);
		computeDepths(root);
	}
	
	// Tree
	
	@Override
	public int size() {
		return nodeList.size();
	}

	@Override
	public Iterable<N> nodes() {
		return nodeList;
	}

	@Override
	public N root() {
		return root;
	}

	@Override
	public Iterable<N> leaves() {
		List<N> result = new ArrayList<>(nodeList.size()); // this may be a bad idea for big graphs
		for (N n:nodeList)
			if (!n.hasChildren())
				result.add(n);
		return result;
	}
	
	protected void computeDepths(N parent) {
		if (parent!=null) {
			// TODO !
			// will do it if it's really useful...
		}
	}

	@Override
	public int maxDepth() {
		return maxDepth;
	}

	@Override
	public int minDepth() {
		return minDepth;
	}

	@Override
	public Tree<N> subTree(N node) {
		return new ImmutableTreeImpl<N>(node);
	}

	@Override
	public Iterable<N> findNodesByReference(String reference) {
		List<N> found = new ArrayList<>(nodeList.size()); // this may be a bad idea for big graphs
		for (N n:nodeList)
			if (Tree.matchesReference(n,reference))
				found.add(n);
		return found;
	}
	
	// Textable
	
	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName()+ptr; 
	}

	@Override
	public String toShortString() {
		return toUniqueString() + "(" + size() + " nodes)"; 
	}

	// TODO: rewrite this - the tree structure is lost
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		sb.append(" NODES=(");
		N last = nodeList.get(nodeList.size()-1);
		for (N n:nodeList) {
			if (n==last)
				sb.append(n.toShortString());
			else
				sb.append(n.toShortString()).append(',');
		}
		sb.append(')');
		return sb.toString();
	}

	// Object
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

}

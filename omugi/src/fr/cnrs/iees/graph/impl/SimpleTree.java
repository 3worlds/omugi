package fr.cnrs.iees.graph.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.Tree;

/**
 * NB this is actually a forest, ie it may have more than one root
 * @author Jacques Gignoux - 13 mai 2019
 *
 */
public class SimpleTree<N extends SimpleTreeNode> implements Tree<N> {
	
	/** the tree root, if unique - set to null if multiple roots **/
	private N root = null;
	/** the tree roots, if root is not unique **/
	private List<N> roots = new ArrayList<N>(10);
	
	private NodeFactory factory = null;
	
	/** the list of nodes */
	private Set<N> nodes = new HashSet<N>();
	
	public SimpleTree(NodeFactory factory) {
		super();
		this.factory = factory;
		this.factory.manageGraph(this);
	}

	@Override
	public Iterable<N> nodes() {
		return nodes;
	}

	@Override
	public Iterable<N> roots() {
		return roots;
	}

	@Override
	public boolean contains(SimpleTreeNode node) {
		return nodes.contains(node);
	}

	@Override
	public N root() {
		return root;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<N> subTree(SimpleTreeNode node) {
		List<N> result = new LinkedList<N>();
		for (Node n:node.traversal(Direction.OUT))
			result.add((N) n);
		return result;
	}

	@Override
	public void addNode(N node) {
		if (nodes.add(node))
			if (node.isRoot()) {
				roots.add(node);
				resetRoot();
			}
	}
	
	private void resetRoot() {
		if (roots.size()==1)
			root = roots.get(0);
		else 
			root = null;
	}

	@Override
	public void removeNode(N node) {
		nodes.remove(node);
		if (roots.contains(node))
			roots.remove(node);
		if (root==node)
			root = null;
		resetRoot();
	}

	@Override
	public void onParentChanged(N child) {
		// child is now a root, means it wasnt before
		if (child.isRoot()) {
			roots.add(child);
			resetRoot();
		}
		// child has changed parent, maybe it was a root before
		else if (roots.contains(child)) {
			roots.remove(child);
			resetRoot();
		}
	}

	@Override
	public NodeFactory nodeFactory() {
		return factory;
	}

	@Override
	public int nNodes() {
		return nodes.size();
	}

	// Textable
	
	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName()+ptr; 
	}

}

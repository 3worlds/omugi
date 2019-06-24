package fr.cnrs.iees.graph.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.Tree;

/**
 * NB this is actually a forest, ie it may have more than one root
 * @author Jacques Gignoux - 13 mai 2019
 *
 */
// tested OK with version 0.2.0
public class SimpleTree<N extends TreeNode> implements Tree<N> {
	
	/** the tree root, if unique - set to null if multiple roots **/
	private N root = null;
	/** the tree roots, if root is not unique **/
	private List<N> roots = new ArrayList<N>(10);
	
	private NodeFactory factory = null;
	
	/** the list of nodes */
	private Map<String,N> nodes = new HashMap<String,N>();
	
	public SimpleTree(NodeFactory factory) {
		super();
		this.factory = factory;
		this.factory.manageGraph(this);
	}

	@Override
	public Iterable<N> nodes() {
		return nodes.values();
	}

	@Override
	public Iterable<N> roots() {
		return roots;
	}

	@Override
	public boolean contains(TreeNode node) {
		return nodes.values().contains(node);
	}

	@Override
	public N root() {
		return root;
	}

	@Override
	public void addNode(N node) {
		if (nodes.put(node.id(),node)!=node)
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
		nodes.remove(node.id());
		if (roots.contains(node))
			roots.remove(node);
		if (root==node)
			root = null;
		resetRoot();
	}

	@Override
	public void onParentChanged() {
		roots.clear();
		for (N n:Tree.super.roots())
			roots.add(n);
		resetRoot();
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

	// Object
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

	@Override
	public N find(String id) {
		return nodes.get(id);
	}

}

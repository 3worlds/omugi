package fr.cnrs.iees.graph.generic.impl;

import java.util.ArrayList;
import java.util.List;

import fr.cnrs.iees.graph.generic.Tree;
import fr.cnrs.iees.graph.generic.TreeNode;

public class ImmutableTreeImpl<N extends TreeNode> implements Tree<N> {
	
	private N root = null;
	/** for fast iteration on nodes */
	private ArrayList<N> nodeList = null; // ArrayList --> comodification error but normally one should never remove a node from this class

	// for descendants only
	protected ImmutableTreeImpl() {
		super();
		nodeList = new ArrayList<N>();
	}
	
	/**
	 * Construction from a list of free floating nodes.
	 * The first node found without a parent is assumed to be the root
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
	}
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int maxDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int minDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Tree<N> subTree(N node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public N findNode(String reference) {
		// TODO Auto-generated method stub
		return null;
	}

}

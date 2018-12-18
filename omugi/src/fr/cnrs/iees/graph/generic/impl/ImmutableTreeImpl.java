package fr.cnrs.iees.graph.generic.impl;

import java.util.ArrayList;
import java.util.List;

import fr.cnrs.iees.graph.generic.Tree;
import fr.cnrs.iees.graph.generic.TreeNode;

/**
 * An immutable strict tree (not changeable after construction).
 * 
 * @author Jacques Gignoux - 18 déc. 2018
 *
 * @param <N>
 */
public class ImmutableTreeImpl<N extends TreeNode> implements Tree<N> {
	
	private N root = null;
	/** for fast iteration on nodes */
	private ArrayList<N> nodeList = null; // ArrayList --> comodification error but normally one should never remove a node from this class
	
	private int minDepth = 0;
	private int maxDepth = 0;

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
		List<N> result = new ArrayList<N>(nodeList.size());
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
	public N findNode(String reference) {
		// Note: maybe recursing from the top of the tree is more efficient than
		// scanning the whole list?
		for (N n:nodeList)
			if (Tree.matchesReference(n,reference))
				return n;
		return null;
	}

}

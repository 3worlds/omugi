package fr.cnrs.iees.graph.generic;

import fr.ens.biologie.generic.Sizeable;

/**
 * <p>A tree, i.e. a graph with a hierarchical structure. Its nodes must implement the TreeNode
 * interface, i.e. have getParent() and getChildren() methods.</p>
 * 
 * @author Jacques Gignoux - 17 déc. 2018
 *
 */
public interface Tree<N extends TreeNode> extends Sizeable {
	
	/**
	 * Read-only accessor to all Nodes
	 * @return an Iterable of all Nodes
	 */
	public Iterable<N> nodes();

	/**
	 * Accessor to the tree root (a tree has 0 or 1 root).
	 * 
	 * @return the Node at the root of the tree
	 */
	public N root();
	
	/**
	 * Read-only accessor to all leaf Nodes
	 * @return an Iterable on all leaf Nodes
	 */
	public Iterable<N> leaves();
	
	public int maxDepth();
	
	public int minDepth();
	
	public Tree<N> subTree(N node);
	
	public N findNodeByReference(String reference);
	
	public static boolean matchesReference(TreeNode node, String ref) {
		return false;
	}

}

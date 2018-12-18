package fr.cnrs.iees.graph.generic;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.io.tree.ReferenceParser;
import fr.cnrs.iees.io.tree.ReferenceTokenizer;
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
		p.parse();
		return false;
	}

}

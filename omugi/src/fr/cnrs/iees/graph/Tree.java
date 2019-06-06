package fr.cnrs.iees.graph;

import fr.ens.biologie.generic.Textable;

/**
 * The root interface for trees.
 *  
 * @author Jacques Gignoux - 9 mai 2019
 *
 * @param <N> The {@link Node} subclass used to construct the tree
 */
public interface Tree<N extends TreeNode> extends NodeSet<N>, Textable {
	
	/**
	 * Accessor to the tree root (a tree has 0 or 1 root).
	 * 
	 * @return the Node at the root of the tree
	 */
	public N root();
	
	@SuppressWarnings("unchecked")
	public default Iterable<N> subTree(N node) {
		return (Iterable<N>) node.subTree();
	}

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

}

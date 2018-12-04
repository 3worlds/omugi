package fr.cnrs.iees.graph.generic;

import java.util.Collection;

/**
 * A Node member of a tree graph. Introduces the concepts of parents and children.
 * 
 * @author Jacques Gignoux - 4 déc. 2018
 *
 */
// ideas: implement a 'strict tree node' where all the node Edge manipulation methods are
// disabled - edges are only created through the addChild / setParent methods
// and implement a 'relaxed tree node' where parent and child relations coexist
// with cross-links (= the AotNode)
public interface TreeNode extends Node {

	/**
	 * Gets the parent node. Returns null if this is the tree root.
	 * @return the parent of this node
	 */
	public TreeNode getParent();
	
	/**
	 * Set the argument as this node's parent.
	 * @param parent the parent node
	 */
	public void setParent(TreeNode parent);
	
	/**
	 * Gets the children nodes.
	 * @return the list of children nodes.
	 */
	public Iterable<TreeNode> getChildren();
	
	/**
	 * Adds a node as a child.
	 * @param child the node to add
	 */
	public void addChild(TreeNode child);
	
	/**
	 * Adds a set of nodes as children
	 * @param children the nodes to add
	 */
	public void setChildren(TreeNode... children);
	public void setChildren(Iterable<TreeNode> children);
	public void setChildren(Collection<TreeNode> children);

}

package fr.cnrs.iees.tree.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.tree.TreeNode;

/**
 * Basic implementation of {@link TreeNode} without data
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
public class SimpleTreeNodeImpl implements TreeNode {
	
	private TreeNode parent = null;
	private Set<TreeNode> children = null;

	protected SimpleTreeNodeImpl() {
		super();
		children = new HashSet<TreeNode>();
	}
	
	protected SimpleTreeNodeImpl(int capacity) {
		super();
		children = new HashSet<TreeNode>(capacity);
	}
	
	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	@Override
	public Iterable<TreeNode> getChildren() {
		return children;
	}

	@Override
	public void addChild(TreeNode child) {
		children.add(child);
	}

	@Override
	public void setChildren(TreeNode... children) {
		for (TreeNode child:children)
			this.children.add(child);
	}

	@Override
	public void setChildren(Iterable<TreeNode> children) {
		for (TreeNode child:children)
			this.children.add(child);
	}

	@Override
	public void setChildren(Collection<TreeNode> children) {
		this.children.addAll(children);
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	@Override
	public String instanceId() {
		// uses Object.toString() to generate a unique id
		return super.toString();
	}

	// Textable

	@Override
	public String toUniqueString() {
		return getClass().getSimpleName()+ " id=" + uniqueId();
	}

	@Override
	public String toShortString() {
		return getClass().getSimpleName();
	}
	
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toUniqueString());
		sb.append(' ');
		if (parent!=null)
			sb.append(Direction.IN)
				.append("=(")
				.append(parent.toUniqueString())
				.append(") ");
		if (hasChildren()) {
			sb.append(Direction.OUT)
				.append("=(");
			for (TreeNode child:children)
				sb.append(child.toUniqueString())
					.append(' ');
			sb.append(')');
		}
		return sb.toString();
	}
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

}

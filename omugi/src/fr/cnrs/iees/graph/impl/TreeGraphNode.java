package fr.cnrs.iees.graph.impl;

import java.util.Collection;

import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.identity.Identity;

/**
 * 
 * @author Jacques Gignoux - 14 mai 2019
 *
 */
public class TreeGraphNode extends ALNode implements TreeNode {

	// this only holds the children and parent nodes
	private TreeNode treenode;

	public TreeGraphNode(Identity id, 
			GraphFactory<TreeGraphNode,ALEdge> gfactory) {
		super(id, gfactory);
		treenode = new SimpleTreeNode(id,gfactory);
	}

	@Override
	public TreeNode getParent() {
		return treenode.getParent();
	}

	@Override
	public void connectParent(TreeNode parent) {
		treenode.connectParent(parent);
	}

	@Override
	public Iterable<? extends TreeNode> getChildren() {
		return treenode.getChildren();
	}

	@Override
	public void connectChild(TreeNode child) {
		treenode.connectChild(child);
	}

	@Override
	public void connectChildren(TreeNode... children) {
		treenode.connectChildren(children);
	}

	@Override
	public void connectChildren(Iterable<? extends TreeNode> children) {
		treenode.connectChildren(children);
	}

	@Override
	public void connectChildren(Collection<? extends TreeNode> children) {
		treenode.connectChildren(children);
	}

	@Override
	public boolean hasChildren() {
		return treenode.hasChildren();
	}

	@Override
	public int nChildren() {
		return treenode.nChildren();
	}

	@Override
	public NodeFactory<? extends TreeNode> factory() {
		return treenode.factory();
	}

	
}

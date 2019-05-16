package fr.cnrs.iees.graph.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.ElementAdapter;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.identity.Identity;

/**
 * basic TreeNode implementation.
 * By convention Direction.IN = up the tree (to parent) and 
 * Direction.OUT = down the tree (to children)
 * NB All constructors must be protected.
 * 
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
public class SimpleTreeNode extends ElementAdapter implements TreeNode {

	private NodeFactory<? extends TreeNode> factory;
	private SimpleTreeNode parent = null;
	private Set<SimpleTreeNode> children = null;
	
	protected SimpleTreeNode(Identity id, NodeFactory<? extends TreeNode> factory) {
		super();
		this.factory = factory;
	}

	// NB: this method will disconnect node from its children since connectChild(...)
	// will reset the child's parent to the new parent (a child can only have one parent)
	@Override
	public void addConnectionsLike(Node node) {
		if (node instanceof TreeNode) {
			TreeNode tn = (TreeNode) node;
			connectParent(tn.getParent());
			connectChildren(tn.getChildren());
		}
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public boolean isRoot() {
		return (parent==null);
	}

	@Override
	public int degree(Direction direction) {
		switch (direction) {
		case IN:
			if (parent==null)
				return 0;
			else
				return 1;
		case OUT:
			return children.size();
		}
		return 0;
	}

	@Override
	public Iterable<? extends Edge> edges(Direction direction) {
		throw new OmugiException("A TreeNode has no edge");
	}

	@Override
	public Iterable<? extends Edge> edges() {
		throw new OmugiException("A TreeNode has no edge");
	}

	@Override
	public Iterable<? extends Node> nodes(Direction direction) {
		switch (direction) {
		case IN:
			List<SimpleTreeNode> l = new LinkedList<>();
			l.add(parent);
			return l;
		case OUT:
			return children;
		}
		return null;
	}

	@Override
	public Iterable<? extends Node> nodes() {
		if (parent==null)
			return children;
		else {
			List<SimpleTreeNode> l = new LinkedList<>();
			l.addAll(children);
			l.add(parent);
			return l;
		}
	}

	@Override
	public Edge connectTo(Direction direction, Node node) {
		if (node instanceof SimpleTreeNode)
			switch (direction) {
			case IN:
				connectParent((SimpleTreeNode) node);
				break;
			case OUT: 
				connectChild((SimpleTreeNode) node);
				break;
			}
		return null;
	}

	@Override
	public void connectTo(Direction direction, Iterable<Node> nodes) {
		throw new OmugiException("connectTo has no effect on TreeNodes - use connectParent or connectChildren instead");
	}

	@Override
	public void disconnect() {
		if (parent!=null)
			disconnectFrom(parent);
		if (!children.isEmpty())
			for (TreeNode child:children)
				disconnectFrom(child);
	}

	// caution: cross recursion
	@Override
	public void disconnectFrom(Node node) {
		if (node instanceof SimpleTreeNode) {
			SimpleTreeNode tn = (SimpleTreeNode) node;
			if (tn==parent) {
				parent = null;
				tn.disconnectFrom(this);
			}
			if (children.contains(tn)) {
				children.remove(tn);
				tn.disconnectFrom(this);
			}
		}
	}

	private Collection<SimpleTreeNode> traversal(Collection<SimpleTreeNode> list, 
			SimpleTreeNode node,
			int distance) {
		if (distance>0) {
			list.add(node);
			SimpleTreeNode tn = node.getParent();
			if (tn!=null)
				return traversal(list,tn,distance-1);
			for (SimpleTreeNode c:node.getChildren())
				if (!(c==this))
					return traversal(list,c,distance-1);
		}
		return list;
	}
	
	private Collection<SimpleTreeNode> traversal(Collection<SimpleTreeNode> list, 
			SimpleTreeNode node,
			int distance,
			Direction direction) {
		if (distance>0) {
			list.add(node); // there should not be double insertions here
			switch (direction) {
			case IN:
				SimpleTreeNode tn = node.getParent();
				if (tn!=null)
					return traversal(list,tn,distance-1,direction);
			case OUT:
				for (SimpleTreeNode c:node.getChildren())
					return traversal(list,c,distance-1,direction);
			}
		}
		return list;
	}
	
	@Override
	public Collection<? extends Node> traversal(int distance) {
		Collection<SimpleTreeNode> list = new LinkedList<>();
		list = traversal(list,this,distance);
		return list;
	}

	@Override
	public Collection<? extends Node> traversal(int distance, Direction direction) {
		Collection<SimpleTreeNode> list = new LinkedList<>();
		list = traversal(list,this,distance,direction);
		return list;
	}
	
	// TreeNode

	@Override
	public SimpleTreeNode getParent() {
		return parent;
	}

	@Override
	public Iterable<? extends SimpleTreeNode> getChildren() {
		return children;
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	@Override
	public int nChildren() {
		return children.size();
	}

	@Override
	public NodeFactory<? extends TreeNode> factory() {
		return factory;
	}

	// caution: cross recursion with connectChild
	@Override
	public void connectParent(TreeNode parent) {
		if (parent instanceof SimpleTreeNode)
			if (this.parent!=parent) {
				this.parent = (SimpleTreeNode) parent;
				if (parent!=null)
					parent.connectChild(this);
				// this is bad code, but will see later if we implement
				// a complete listening system between nodes and graphs/trees
				if (factory instanceof SimpleTreeFactory)
					((SimpleTreeFactory)factory).onParentChanged(this);
			}
	}

	// caution: cross recursion with connectParent
	@Override
	public void connectChild(TreeNode child) {
		if (child instanceof SimpleTreeNode)
			if (!children.contains(child))  {
				children.add((SimpleTreeNode) child);
				child.connectParent(this);
			}
	}

	@Override
	public void connectChildren(TreeNode... children) {
		for (TreeNode child:children)
			connectChild(child);
	}

	@Override
	public void connectChildren(Iterable<? extends TreeNode> children) {
		for (TreeNode child:children)
			connectChild(child);
	}

	@Override
	public void connectChildren(Collection<? extends TreeNode> children) {
		for (TreeNode child:children)
			connectChild(child);
	}

}

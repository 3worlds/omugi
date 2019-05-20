package fr.cnrs.iees.graph.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import au.edu.anu.rscs.aot.collections.QuickListOfLists;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.EdgeSet;
import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.ens.biologie.generic.Textable;

/**
 * 
 * @author Jacques Gignoux - 15 mai 2019
 *
 * @param <N>
 * @param <E>
 */
// tested OK with version 0.2.0 on 20/5/2019
public class TreeGraph<N extends TreeGraphNode,E extends ALEdge> 
	implements Tree<N>, EdgeSet<E>, Textable {

	private Set<N> nodes = new HashSet<>();
	private N root = null;
	private List<N> roots = new ArrayList<N>(10);
	private GraphFactory factory;
	
	public TreeGraph(GraphFactory factory) {
		super();
		this.factory = factory;
		this.factory.manageGraph(this);
	}
	
	private void resetRoot() {
		if (roots.size()==1)
			root = roots.get(0);
		else 
			root = null;
	}
	
	@Override
	public Iterable<N> nodes() {
		return nodes;
	}

	@Override
	public Iterable<N> roots() {
		return roots;
	}

	@Override
	public boolean contains(N node) {
		return nodes.contains(node);
	}

	@Override
	public NodeFactory nodeFactory() {
		return factory;
	}

	@Override
	public void addNode(N node) {
		if (nodes.add(node))
			if (node.getParent()==null) {
				roots.add(node);
				resetRoot();
			}
	}

	@Override
	public void removeNode(N node) {
		nodes.remove(node);
		if (roots.contains(node))
			roots.remove(node);
		if (root==node)
			root = null;
		resetRoot();
	}

	@Override
	public int nNodes() {
		return nodes.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<E> edges() {
		QuickListOfLists<E> edges = new QuickListOfLists<>();
		for (N n:nodes)
			edges.addList((Iterable<E>) n.edges(Direction.OUT));
		return edges;
	}

	@Override
	public EdgeFactory edgeFactory() {
		return factory;
	}

	// slow
	@Override
	public int nEdges() {
		// a tree has nNodes()-1 edges
		return ALGraph.countEdges(nodes)-nNodes()+1;
	}

	@Override
	public N root() {
		return root;
	}

	// recursive
	@SuppressWarnings("unchecked")
	private void addToTree(List<N> tree,N n) {
		tree.add(n);
		for (TreeNode child:n.getChildren())
			addToTree(tree,(N) child);
	}
	
	@Override
	public Iterable<N> subTree(N node) {
		List<N> result = new LinkedList<>();
		addToTree(result,node);
		return result;
	}

	// Caution: different code from SimpleTree
	@Override
	public void onParentChanged() {
		roots.clear();
		for (N n:nodes)
			if (n.getParent()==null) // this is the 'tree only' check for root
				roots.add(n);
		resetRoot();
	}
	
	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName() + ptr;
	}

	@Override
	public String toShortString() {
		return toUniqueString() + "(" + nodes.size() + " tree nodes / " + nEdges() + " cross-links)";
	}

	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		StringBuilder zb = new StringBuilder();
		sb.append(" NODES=(");
		for (N n: nodes) {
			sb.append(n.toShortString() + ",");
			for (ALEdge e: n.edges(Direction.OUT))
				zb.append(e.toShortString() + ",");
		}
		if (sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		if (zb.length()>0)
			zb.deleteCharAt(zb.length()-1);
		sb.append(") EDGES=(").append(zb.toString()).append(')');
		return sb.toString();
	}

}

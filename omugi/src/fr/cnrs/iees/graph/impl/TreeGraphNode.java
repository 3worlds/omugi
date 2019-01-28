/**************************************************************************
 *  AOT - Aspect-Oriented Thinking                                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  AOT is a method to generate elaborate software code from a series of  *
 *  independent domains of knowledge. It enables one to manage and        *
 *  maintain software from explicit specifications that can be translated *
 *  into any programming language.          							  *
 **************************************************************************                                       
 *  This file is part of AOT (Aspect-Oriented Thinking).                  *
 *                                                                        *
 *  AOT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  AOT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.graph.impl;

import java.util.Collection;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.impl.SimpleNodeImpl;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.TreeNodeFactory;
import fr.cnrs.iees.tree.impl.DefaultTreeFactory;

/**
 * A Node that is also a TreeNode, ie has out and in edges but also a parent and children nodes.
 * It is assumed to be the member of a tree with cross-links.
 * 
 * @author Jacques Gignoux - 24 janv. 2019
 *
 */
public class TreeGraphNode extends SimpleNodeImpl 
		implements Node, TreeNode {

	// this only holds the children and parent nodes
	private TreeNode treenode;
	// this holds the properties
	protected ReadOnlyPropertyList properties = null;

	// ----------------------- Constructors

	/**
	 * This constructor will set the instanceId to the argument and will fail if the argument 
	 * is null. To set an empty instanceId, use the empty String "".
	 * @param classId the class id - can be null (will default to class name in this case)
	 * @param instanceId the instance id - cannot be null
	 * @param tf the treeNode factory
	 * @param nf the node factory
	 * @param props a property list - can be null
	 */
	protected TreeGraphNode(String classId, String instanceId, TreeNodeFactory tf, NodeFactory nf, ReadOnlyPropertyList props) {
		super(classId,instanceId,nf);
		treenode = DefaultTreeFactory.makeSimpleTreeNode(null,tf);
		properties = props;
	}
	
	/**
	 * This constructor will generate a unique instanceId.
	 * @param classId the class id - can be null (will default to class name in this case)
	 * @param tf the treeNode factory
	 * @param nf the node factory
	 * @param props a property list - can be null
	 */
	protected TreeGraphNode(String instanceId, TreeNodeFactory tf, NodeFactory nf, ReadOnlyPropertyList props) {
		super(instanceId,nf);
		treenode = DefaultTreeFactory.makeSimpleTreeNode(null,tf);
		properties = props;
	}

	protected TreeGraphNode(TreeNodeFactory tf, NodeFactory nf, ReadOnlyPropertyList props) {
		super(nf);
		treenode = DefaultTreeFactory.makeSimpleTreeNode(null,tf);
		properties = props;
	}

	// ---------------- TreeNode

	@Override
	public void addChild(TreeNode child) {
		treenode.addChild(child);
	}

	@Override
	public Iterable<? extends TreeNode> getChildren() {
		return treenode.getChildren();
	}

	@Override
	public TreeNode getParent() {
		return treenode.getParent();
	}

	@Override
	public boolean hasChildren() {
		return treenode.hasChildren();
	}

	@Override
	public void setChildren(TreeNode... children) {
		treenode.setChildren(children);
	}

	@Override
	public void setChildren(Iterable<TreeNode> children) {
		treenode.setChildren(children);
	}

	@Override
	public void setChildren(Collection<TreeNode> children) {
		treenode.setChildren(children);
	}

	/**
	 * CAUTION: to prevent problems, parent can only be set if it was null. It should
	 * be set by the factory in the makeTreeNode(...) methods
	 */
	@Override
	public void setParent(TreeNode parent) {
		if (treenode.getParent()==null)
			treenode.setParent(parent);
	}
//<T extends TreeNodeFactory> T
	@Override
	public <T extends TreeNodeFactory> T treeNodeFactory() {
//		public TreeNodeFactory treeNodeFactory() {
		return treenode.treeNodeFactory();
	}

	@Override
	public int nChildren() {
		return treenode.nChildren();
	}

	// -------------------  Textable

	@Override
	public String toUniqueString() {
		return uniqueId();
	}

	/**
	 * Displays a TreeGraphNode as follows (on a single line):
	 * 
	 * <pre>
	 * node_label:node_name=[
	 *    ↑parent_label:parent_name      // the parent node, or ROOT if null
	 *    ↓child_label:child_name        // child node, repeated as needed
	 *    →out_node_label:out_node_name  // end node of outgoing edge, repeated as needed
	 *    ←in_node_label:in_node_name    // start node of incoming edge, repeated as needed
	 * ] 
	 * </pre>
	 * <p>e.g.: {@code AOTNode:=[↑AOTNode:1 ↓AOTNode:2 ←AOTNode:2 ←AOTNode: →AOTNode: →AOTNode:1]}
	 *  (in this case, the name is missing).</p>
	 */
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toUniqueString());
		sb.append("=[");
		if (treenode.getParent()!=null)
			sb.append("↑").append(treenode.getParent().toUniqueString());
		else
			sb.append("ROOT");
		if (treenode.hasChildren()) {
			for (TreeNode n:treenode.getChildren()) {
				sb.append(" ↓").append(n.toUniqueString());
			}
		}
		if (getEdges(Direction.IN).iterator().hasNext()) {
			for (Edge e:getEdges(Direction.IN))
				sb.append(" ←").append(e.startNode().toUniqueString());
		}
		if (getEdges(Direction.OUT).iterator().hasNext()) {
			for (Edge e:getEdges(Direction.OUT))
				sb.append(" →").append(e.endNode().toUniqueString());
		}
		if (properties!=null)
			if (properties.size()>0)
				sb.append(' ').append(properties.toString());
		sb.append("]");
		return sb.toString();
	}
		
}
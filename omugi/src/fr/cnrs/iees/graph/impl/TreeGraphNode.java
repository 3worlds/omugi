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
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.ReadOnlyDataNode;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.graph.impl.SimpleNodeImpl;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * A Node that is also a TreeNode, ie has out and in edges but also a parent and children nodes.
 * It is assumed to be the member of a tree with cross-links.
 * 
 * @author Jacques Gignoux - 24 janv. 2019
 *
 */
public class TreeGraphNode extends SimpleNodeImpl 
		implements ReadOnlyDataNode, TreeNode {

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
	protected TreeGraphNode(Identity id, TreeNodeFactory tf, NodeFactory nf, ReadOnlyPropertyList props) {
		super(id,nf);
		treenode = new SimpleTreeNodeImpl(id,tf); // same id as its owner - is it safe ?
		properties = props;
	}
	
	// ---------------- TreeNode

	@Override
	public String classId() {
		String s = nodeFactory().nodeClassName(this.getClass()); 
		if (s==null)
			s = this.getClass().getSimpleName();
		return s;
	}
	
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
	public TreeNodeFactory treeNodeFactory() {
//		public TreeNodeFactory treeNodeFactory() {
		return treenode.treeNodeFactory();
	}

	@Override
	public int nChildren() {
		return treenode.nChildren();
	}

	// -------------------  Textable

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
	 * <p>e.g.: {@code Node:0=[↑Node:1 ↓Node:2 ←Node:27 ←Node:4 →Node:5 →Node:18]}</p>
	 */
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toShortString());
		sb.append("=[");
		if (treenode.getParent()!=null)
			sb.append("↑").append(treenode.getParent().toShortString());
		else
			sb.append("ROOT");
		if (treenode.hasChildren()) {
			for (TreeNode n:treenode.getChildren()) {
				sb.append(" ↓").append(n.toShortString());
			}
		}
		if (getEdges(Direction.IN).iterator().hasNext()) {
			for (Edge e:getEdges(Direction.IN))
				sb.append(" ←").append(e.startNode().toShortString());
		}
		if (getEdges(Direction.OUT).iterator().hasNext()) {
			for (Edge e:getEdges(Direction.OUT))
				sb.append(" →").append(e.endNode().toShortString());
		}
		if (properties!=null)
			if (properties.size()>0)
				sb.append(' ').append(properties.toString());
		sb.append("]");
		return sb.toString();
	}

	@Override
	public ReadOnlyPropertyList properties() {
		return properties;
	}
		
	// this because this method is found in two ancestors
	@Override
	public String toShortString() {
		return classId()+":"+id();
	}

}

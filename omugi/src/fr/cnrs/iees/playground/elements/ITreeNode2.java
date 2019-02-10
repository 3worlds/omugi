/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          * 
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  OMUGI is an API to implement graphs, as described by graph theory,    *
 *  but also as more commonly used in computing - e.g. dynamic graphs.    *
 *  It interfaces with JGraphT, an API for mathematical graphs, and       *
 *  GraphStream, an API for visual graphs.                                *
 *                                                                        *
 **************************************************************************                                       
 *  This file is part of OMUGI (One More Ultimate Graph Implementation).  *
 *                                                                        *
 *  OMUGI is free software: you can redistribute it and/or modify         *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  OMUGI is distributed in the hope that it will be useful,              *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with OMUGI.  If not, see <https://www.gnu.org/licenses/gpl.html>*
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.playground.elements;

import java.util.Collection;

import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.playground.factories.ITreeNodeFactory;
import fr.ens.biologie.generic.Textable;

/**
 * A Node member of a tree graph. Introduces the concepts of parents and children.
 * 
 * @author Jacques Gignoux - 4 déc. 2018
 *
 */
public interface ITreeNode2 extends Identity, Textable {
	
	/**
	 * Gets the parent node. Returns null if this is the tree root.
	 * @return the parent of this node
	 */
//	public  <T extends TreeNode> T getParent();
	public ITreeNode2 getParent();
	
	/**
	 * Set the argument as this node's parent. CAUTION: no consistency checks! this is the
	 * tree's job.
	 * 
	 * @param parent the parent node
	 */
	public void setParent(ITreeNode2 parent);
	
	/**
	 * Gets the children nodes.
	 * @return the list of children nodes.
	 */
	//	public Iterable<? extends TreeNode> getChildren();

	public Iterable<? extends ITreeNode2> getChildren();
	
	/**
	 * Adds a node as a child. CAUTION: no consistency checks! this is the
	 * tree's job.
	 * 
	 * @param child the node to add
	 */
	public void addChild(ITreeNode2 child);
	
	/**
	 * Adds a set of nodes as children
	 * @param children the nodes to add
	 */
	public void setChildren(ITreeNode2... children);
	public void setChildren(Iterable<ITreeNode2> children);
	public void setChildren(Collection<ITreeNode2> children);
	
	public boolean hasChildren();
	
	/**
	 * Checks that a child is in a node. NOT efficient - should be overloaded in descendants
	 * @param child the child to check
	 * @return true if the child was found;
	 */
	public default boolean hasChild(ITreeNode2 child) {
		for (ITreeNode2 c:getChildren())
			if (c==child)
				return true;
		return false;
	}
	
	public  ITreeNodeFactory treeNodeFactory();
	
	/**
	 * Gets the number of children of this TreeNode
	 * @return the number of child nodes
	 */
	public int nChildren();
	
	/**
	 * The "label" or classId of a TreeNode is a String matching its java class name. It is
	 * known by the treeNodeFactory.
	 * This method should not be overridden.
	 * 
	 * @return
	 */
	public String classId();
//		public default String classId() {
//		String s = treeNodeFactory().treeNodeClassName(this.getClass());
//		if (s==null)
//			s = this.getClass().getSimpleName();
//		return s;
//	}

}

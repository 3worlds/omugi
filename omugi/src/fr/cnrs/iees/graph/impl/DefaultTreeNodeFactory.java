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
package fr.cnrs.iees.graph.impl;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 *  A default factory for TreeNodes, for multiple inheritance in descendants.
 *  It is actually almost an implementation. It must be in this package because it
 *  instantiates classes in this package which have protected constructors.
 * 
 * @author Jacques Gignoux - 4 avr. 2019
 *
 */
public interface DefaultTreeNodeFactory extends TreeNodeFactory {

	public static String defaultTreeNodeId = "treenode0";
	
	// utility for descendants
	public default void connectToParent(TreeNode child, TreeNode parent) {
		child.setParent(parent);
		if (parent!=null)
			parent.addChild(child);
	}
	
	@Override
	default TreeNode makeTreeNode(TreeNode parent) {
		return makeTreeNode(parent,defaultTreeNodeId);
	}

	@Override
	default TreeNode makeTreeNode(TreeNode parent, SimplePropertyList properties) {
		return makeTreeNode(parent,defaultTreeNodeId,properties);
	}

	@Override
	default TreeNode makeTreeNode(TreeNode parent, String proposedId) {
		TreeNode result = new SimpleTreeNodeImpl(scope().newId(proposedId),this);
		connectToParent(result,parent);
		return result;
	}

	@Override
	default TreeNode makeTreeNode(TreeNode parent, String proposedId, SimplePropertyList properties) {
		TreeNode result = new DataTreeNodeImpl(scope().newId(proposedId),properties,this);
		connectToParent(result,parent);
		return result;
	}

	@Override
	default TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, 
			TreeNode parent) {
		return makeTreeNode(treeNodeClass,parent,defaultTreeNodeId);
	}

	@Override
	default TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, 
			TreeNode parent,
			SimplePropertyList properties) {
		return makeTreeNode(treeNodeClass,parent,defaultTreeNodeId,properties);
	}

	// TIP: In all following methods, getConstructor(...) fails to return the constructor even if the parameter types are
	// correct because it is meant to return only PUBLIC constructors and the Node/TreeNode descendant
	// constructors are all PROTECTED. getDeclaredConstructor(...) fixes the problem.

	@Override
	default TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, 
			TreeNode parent, 
			String proposedId) {
		Logger log = Logger.getLogger(DefaultTreeNodeFactory.class.getName());
		Constructor<? extends TreeNode> c = null;
		try {
			c = treeNodeClass.getDeclaredConstructor(Identity.class,TreeNodeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+treeNodeClass.getName()+ "\" not found");
		}
		Identity id = scope().newId(proposedId);
		try {
			TreeNode tn = c.newInstance(id,this); 
			connectToParent(tn,parent);
			return tn;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+treeNodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	default TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, 
			TreeNode parent, 
			String proposedId,
			SimplePropertyList properties) {
		Logger log = Logger.getLogger(DefaultTreeNodeFactory.class.getName());
		Constructor<? extends TreeNode> c = null;
		try {
			c = treeNodeClass.getDeclaredConstructor(Identity.class,
				ReadOnlyPropertyList.class,TreeNodeFactory.class);
		} catch (Exception e) {
			try {
				c = treeNodeClass.getDeclaredConstructor(Identity.class,
					SimplePropertyList.class,TreeNodeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+treeNodeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope().newId(proposedId);
		try {
			TreeNode tn = c.newInstance(id,properties,this); 
			connectToParent(tn,parent);
			return tn;
		} catch (Exception e) {
			log.severe(()->"Node of class \""+treeNodeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

}

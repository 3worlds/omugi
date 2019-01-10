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
package fr.cnrs.iees.tree.impl;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;
import fr.cnrs.iees.tree.DataTreeNode;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.TreeNodeFactory;

/**
 * 
 * @author Jacques Gignoux - 20 déc. 2018
 *
 */
public class DefaultTreeFactory implements TreeNodeFactory, PropertyListFactory {

	@Override
	public ReadOnlyPropertyList makeReadOnlyPropertyList(Property... properties) {
		return new SimplePropertyListImpl(properties);
	}

	@Override
	public ReadOnlyPropertyList makeReadOnlyPropertyList(String... propertyKeys) {
		return new SimplePropertyListImpl(propertyKeys);
	}

	@Override
	public SimplePropertyList makePropertyList(Property... properties) {
		return new SimplePropertyListImpl(properties);
	}

	@Override
	public SimplePropertyList makePropertyList(String... propertyKeys) {
		return new SimplePropertyListImpl(propertyKeys);
	}

	@Override
	public TreeNode makeTreeNode(TreeNode parent) {
		TreeNode result = new SimpleTreeNodeImpl(this);
		result.setParent(parent);
		if (parent!=null)
			parent.addChild(result);
		return result;
	}
	
	// this is used in AotNode to instantiate a simple node within the AotNode
	public static TreeNode makeSimpleTreeNode(TreeNode parent, TreeNodeFactory factory) {
		TreeNode result = new SimpleTreeNodeImpl(factory);
		result.setParent(parent);
		if (parent!=null)
			parent.addChild(result);
		return result;
	}

	@Override
	public DataTreeNode makeDataTreeNode(TreeNode parent, SimplePropertyList properties) {
		DataTreeNode result = new DataTreeNodeImpl(properties,this);
		result.setParent(parent);
		if (parent!=null)
			parent.addChild(result);
		return result;
	}

}

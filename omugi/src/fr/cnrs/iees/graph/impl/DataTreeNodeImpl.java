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

import fr.cnrs.iees.graph.DataTreeNode;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * Basic implementation of {@link TreeNode} with read-write properties.
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
public class DataTreeNodeImpl extends SimpleTreeNodeImpl 
		implements DataTreeNode {
		
	private ReadOnlyPropertyList propertyList = null;

	// Constructors
	
	protected DataTreeNodeImpl(Identity id, ReadOnlyPropertyList props, TreeNodeFactory factory) {
		super(id,factory);
		propertyList = props;
	}

	// DataTreeNode

	@Override
	public ReadOnlyPropertyList properties() {
		return propertyList;
	}

	// Textable

	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(super.toDetailedString());
		sb.append(' ');
		sb.append(propertyList.toString());
		return sb.toString();
	}

	// TODO: implement toString()
	@Override
	public boolean equals(Object obj) {
		if (!TreeNode.class.isAssignableFrom(obj.getClass()))
			return false;
		if (!SimplePropertyList.class.isAssignableFrom(obj.getClass()))
			return false;
		TreeNode tn = (TreeNode) obj;
		SimplePropertyList p = (SimplePropertyList) obj;
		return (tn.equals(this) && p.equals(this));
	}

}

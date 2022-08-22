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

import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.ReadOnlyDataHolder;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * A {@link SimpleTreeNode} sub-class with read-only (immutable) data.
 * 
 * @author Jacques Gignoux - 13 mai 2019
 *
 */
public class SimpleReadOnlyDataTreeNode extends SimpleTreeNode implements ReadOnlyDataHolder {

	private ReadOnlyPropertyList properties;

	/**
	 * This constructor must only be invoked through a {@link NodeFactory}.
	 * 
	 * @param id Unique {@link Identity}.
	 * @param props The node's property list
	 * @param factory The {@link NodeFactory} that makes this node class.
	 */
	public SimpleReadOnlyDataTreeNode(Identity id, 
			ReadOnlyPropertyList props, NodeFactory factory) {
		super(id, factory);
		properties = props;
	}

	@Override
	public ReadOnlyPropertyList properties() {
		return properties;
	}

	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(super.toDetailedString());
		sb.append(' ');
		sb.append(properties.toString());
		return sb.toString();
	}

}

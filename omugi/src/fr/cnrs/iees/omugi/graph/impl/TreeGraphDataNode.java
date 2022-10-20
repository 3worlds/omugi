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
package fr.cnrs.iees.omugi.graph.impl;

import fr.cnrs.iees.omugi.graph.DataHolder;
import fr.cnrs.iees.omugi.graph.GraphFactory;
import fr.cnrs.iees.omugi.graph.NodeFactory;
import fr.cnrs.iees.omugi.identity.Identity;
import fr.cnrs.iees.omugi.properties.SimplePropertyList;

/**
 * A {@link TreeGraphNode} sub-class with read-write data.
 * 
 * @author Jacques Gignoux - 15 mai 2019
 *
 */
// tested OK with version 0.2.0 on 20/5/2019
public class TreeGraphDataNode extends TreeGraphNode implements DataHolder {

	private SimplePropertyList properties;
	
	/**
	 * This constructor must only be invoked through a {@link NodeFactory}.
	 * 
	 * @param id Unique {@link Identity}.
	 * @param props The node's property list
	 * @param gfactory The {@link NodeFactory} that makes this node class.
	 */
	public TreeGraphDataNode(Identity id, SimplePropertyList props, GraphFactory gfactory) {
		super(id, gfactory);
		properties = props;
	}

	@Override
	public SimplePropertyList properties() {
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

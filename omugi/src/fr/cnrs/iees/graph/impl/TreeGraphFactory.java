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

import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.TreeNodeFactory;

/**
 * A default factory for TreeGraphs
 * 
 * @author Jacques Gignoux - 25 janv. 2019
 *
 */
public class TreeGraphFactory implements TreeNodeFactory, NodeFactory, EdgeFactory {

	@Override
	public Edge makeEdge(Node start, Node end, String classId, String instanceId, ReadOnlyPropertyList props) {
		if (props==null)
			return new SimpleEdgeImpl(classId,instanceId,start,end,this);
		if (SimplePropertyList.class.isAssignableFrom(props.getClass()))
			return new DataEdgeImpl(classId,instanceId,start,end,(SimplePropertyList) props,this);
		return new ReadOnlyDataEdgeImpl(classId,instanceId,start,end,props,this);
	}

	@Override
	public TreeGraphNode makeTreeNode(TreeNode parent, String classId, String instanceId, SimplePropertyList properties) {
		return new TreeGraphNode(classId,instanceId,this,this,properties);
	}

	@Override
	public TreeGraphNode makeNode(String classId, String instanceId, ReadOnlyPropertyList props) {
		return new TreeGraphNode(classId,instanceId,this,this,props);
	}

}

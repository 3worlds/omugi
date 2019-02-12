/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
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
package fr.cnrs.iees.playground.graphs;

import fr.cnrs.iees.playground.elements.IEdge;
import fr.cnrs.iees.playground.elements.INode;
import fr.cnrs.iees.playground.elements.ITreeNode;
import fr.cnrs.iees.playground.factories.IEdgeFactory;
import fr.cnrs.iees.playground.factories.ITreeNodeFactory;
import fr.cnrs.iees.playground.io.TreeGraphTokenizer2;
import fr.cnrs.iees.properties.impl.ExtendablePropertyListImpl;

public class AotGraph2 extends TreeGraph2<AotNode2, AotEdge2> implements ITreeNodeFactory, IEdgeFactory// ,
																										// ConfiguarableGraph,Textable
{

	public AotGraph2() {
		super();
	}

	protected AotGraph2(TreeGraphTokenizer2 tokenizer) {
		this();
		// TODO Auto-generated constructor stub
		// no its a mess
	}

	@Override
	public IEdge makeEdge(INode start, INode end, String proposedId) {
		// could each start,end pair have an IndentityScope or should the scope be graph
		// wide?
		return new AotEdge2(edgeScope.newId(proposedId), start, end, new ExtendablePropertyListImpl(), this);
	}

	@Override
	public ITreeNode makeTreeNode(String proposedId) {
		AotNode2 node = new AotNode2(nodeScope.newId(proposedId), new ExtendablePropertyListImpl(), this);
		nodes.add(node);
		return node;
	}

//	public static AotGraph2 importGraph(File file){
//		// what about a reader pattern?
//		TreeGraphTokenizer2 tokenizer;
//		try {
//			tokenizer = new TreeGraphTokenizer2(Files.readAllLines(file.toPath()));
//			return new AotGraph2(tokenizer);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;		
//	}

}

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
package fr.cnrs.iees.io.parsing.impl;

import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.io.parsing.Parser;

public class ImportParser extends Parser {
	/*
	 * How do we insert the imported graph into the parent graph?
	 * 
	 * Depends if Minimal, Graph or Tree/TreeGraph
	 * 
	 * if its a minimal graph the just add all nodes. For this you need the graph.
	 * 
	 * if Graph<N,E> add edge from parent node to new root. For this you need  a
	 * node and the graph to add all imported nodes to the nodelist
	 * 
	 * If Tree or TreeGraph make new graph child of parentnode. For this you need a
	 * parent tree node and the graph to add imported nodes to its nodelist
	 * 
	 * So for minimal graph the parent node will be null.
	 * 
	 * Note: We can't construct a graph of mixed node types!!! so need a check on
	 * this. Perhaps the best way is to create the imported graph and have
	 * factory.equals(importFactory) function. This should ensure that all classes
	 * in the lookup map are the same!
	 */

	private Node parentNode;
	private MinimalGraph<? extends Node> parentGraph;
	private TreeTokenizer tokenizer;

	public ImportParser(TreeTokenizer tokenizer, Node parentNode, MinimalGraph<? extends Node> parentGraph) {
		this.tokenizer = tokenizer;
		this.parentNode = parentNode;
		this.parentGraph = parentGraph;
		parse();
	}

	@Override
	protected void parse() {

		/*
		 * First go ahead and create the imported graph instance no matter what it is, so we can get the factory.
		 * 
		 * tokens must be: import <ext> <"fromFile|fromResource> : <filename> import dsl
		 * fromResource: "3wA-root.dsl" inPackage:
		 * "fr.ens.biologie.threeWorlds.ui.configuration.archetype3w" if resource look
		 * for package name if file look for dir
		 */

		// tokenizer.getString("import");
		// String typeStr = tokenizer.getString("dsl", "xml", "xmi");
		// ImportType type = ImportType.valueOf(typeStr.toUpperCase());
		// String source = tokenizer.getTag("fromFile:", "fromResource:", "fromRepo:");
		// get the file type
		// get the resource source
		//
		// go to end of line
		// create the sub-graph

		// If parentNode.factory().equals(newGraph.root().factory()) then
		// add it somehow to the parent:
		//

	}

	@Override
	public MinimalGraph<?> graph() {
		// TODO Auto-generated method stub
		return null;
	}

}

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
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.io.parsing.Parser;

public class ImportParser extends Parser {
	/*
	 * How do we insert the imported graph into the parent graph? There must be a
	 * parent?
	 * 
	 * So we don't care about the parent graph!
	 * 
	 * But we can't construct a graph of mixed node types!!! so need a check on this
	 */

	private TreeNode parentNode;
	private TreeTokenizer tokenizer;

	public ImportParser(TreeTokenizer tokenizer, TreeNode parentNode) {
		this.tokenizer = tokenizer;
		this.parentNode = parentNode;
		parse();
	}

	@Override
	protected void parse() {
		/* tokens must be:
		 * import <ext> <"fromFile|fromResource> : <filenmae> 
		 * import dsl fromResource: "3wA-root.dsl" inPackage:  "fr.ens.biologie.threeWorlds.ui.configuration.archetype3w"
		 if resource look for package name
		 if file look for dir
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
		// add root as child of parent

	}

	@Override
	public MinimalGraph<?> graph() {
		// TODO Auto-generated method stub
		return null;
	}

}

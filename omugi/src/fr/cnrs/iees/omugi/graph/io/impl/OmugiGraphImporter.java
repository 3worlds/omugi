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
package fr.cnrs.iees.omugi.graph.io.impl;

import java.io.File;

import fr.cnrs.iees.omugi.graph.NodeSet;
import fr.cnrs.iees.omugi.graph.io.GraphImporter;
import fr.cnrs.iees.omugi.io.parsing.Parser;
import fr.cnrs.iees.omugi.io.parsing.PreTokenizer;

/**
 * Importer for graphs / trees in the omugi simple text format.
 * 
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
// tested OK with version 0.0.1 on 17/12/2018
// tested OK with version 0.0.10 on 31/1/2019
public class OmugiGraphImporter implements GraphImporter {

	private PreTokenizer tokenizer;
	private Parser parser;
	
	/**
	 * 
	 * @param infile a text file to import as a graph
	 */
	public OmugiGraphImporter(File infile) {
		super();
		tokenizer = new PreTokenizer(infile);
		parser = tokenizer.parser();
	}
	
	/**
	 * 
	 * @param parser a Parser to immport a graph from
	 */
	public OmugiGraphImporter(Parser parser) {
		super();
		this.parser = parser;
	}
	
	@Override
	public NodeSet<?> getGraph() {
		return parser.graph();
	}

	/**
	 * @param factory the parent's graph factory.
	 */
	public void setFactory(Object factory) {
		parser.setFactory(factory);		
	}

}

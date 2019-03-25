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
package fr.cnrs.iees.graph.io.impl;

import java.io.File;

import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.io.parsing.FileTokenizer;
import fr.cnrs.iees.io.parsing.Parser;

/**
 * Importer for graphs / trees in the omugi simple text format.
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
// tested OK with version 0.0.1 on 17/12/2018
// tested OK with version 0.0.10 on 31/1/2019
public class OmugiGraphImporter implements GraphImporter {

	private FileTokenizer tokenizer;
	private Parser parser;
	
	public OmugiGraphImporter(File infile) {
		super();
		tokenizer = new FileTokenizer(infile);
		parser = tokenizer.parser();
	}
	
	@Override
	public MinimalGraph<?> getGraph() {
		return parser.graph();
	}

}

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
package fr.cnrs.iees.graph.io;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.util.Resources;
import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.graph.io.impl.OmugiGraphImporter;
import fr.cnrs.iees.io.parsing.impl.GraphParser;
import fr.cnrs.iees.io.parsing.impl.GraphTokenizer;
import fr.cnrs.iees.io.parsing.impl.TreeGraphParser;
import fr.cnrs.iees.io.parsing.impl.TreeGraphTokenizer;
import fr.cnrs.iees.io.parsing.impl.TreeParser;
import fr.cnrs.iees.io.parsing.impl.TreeTokenizer;

import static fr.cnrs.iees.io.parsing.impl.TreeGraphTokens.COMMENT;

/**
 * 
 * @author Shayne - long ago. 
 * Heavily refactored by J.Gignoux nov. 2017
 *
 */
// static methods tested OK with version 0.2.0 on 22/5/2019
public interface GraphImporter {

	/**
	 * A GraphImporter returns a Graph read from any kind of source (stream, file, String...)
	 * @return a Graph build from the previous list of Nodes
	 */
    public NodeSet<?> getGraph();
    
    // static methods to use
    
    public static Logger log = Logger.getLogger(GraphImporter.class.getName());
    
	private static List<String> preprocess(List<String> lines) {
		List<String> result = new ArrayList<>();
		String concat = "";
		/*
		 * Skip blank lines and comment only lines. This ensures graph type is the first
		 * token in the file.
		 * 
		 * Use key symbol "+" for string concatenation.
		 * 
		 * Note, preprocessing lines means line numbers are no longer reliable for error
		 * reporting. Instead, report entire line of tokens to provide error context.
		 */
		for (String line : lines) {
			String tmp = line.trim();
			if (!tmp.isEmpty()) {
				if (!tmp.startsWith((COMMENT.prefix()))) {
					// need to look ahead to concatenate strings
					if (tmp.endsWith("+")) {
						if (concat.isEmpty())
							concat += line.substring(0, line.indexOf("+"));						
						else 
							concat += tmp.substring(0, tmp.length() - 1);
					} else if (!concat.isEmpty()) {
						concat += tmp;
						result.add(concat);
						concat = "";
					} else
						result.add(line);
				}
			}
		}
		if (!concat.isEmpty())
			log.severe("File format error: Concatenation of file strings remains unclosed\n"+concat);
		return result;
	}

    private static NodeSet<?> importGraph(List<String> lines) {
    	lines = preprocess(lines);
		String s = lines.get(0).trim();
		GraphImporter importer = null;
		if (s.startsWith("graph"))
			importer = new OmugiGraphImporter(new GraphParser(new GraphTokenizer(lines)));
		else if (s.startsWith("treegraph"))
			importer = new OmugiGraphImporter(new TreeGraphParser(new TreeGraphTokenizer(lines)));
		else if (s.startsWith("tree"))
			importer = new OmugiGraphImporter(new TreeParser(new TreeTokenizer(lines)));
		else for (String l:lines)
			if (l.contains("http://graphml.graphdrawing.org/xmlns"))
				log.warning("GRAPHML importer not yet implemented");
		return importer.getGraph();
    }
    
    /**
     * This should be the prefered way to load a graph from a file or jar
     * @param resourceName the file name (absolute or relative to class path)
     * @return the graph
     */
    public static NodeSet<?> importGraph(String resourceName) {
    	return importGraph(Resources.getTextResource(resourceName));
    }

    /**
     * This should be the prefered way to load a graph from a file or jar
     * @param resourceName the file name 
     * @param associatedWithClass a class in which package the file is to be found
     * @return
     */
    public static NodeSet<?> importGraph(String resourceName, Class<?> associatedWithClass) {
    	return importGraph(Resources.getTextResource(resourceName,associatedWithClass));
    }

}

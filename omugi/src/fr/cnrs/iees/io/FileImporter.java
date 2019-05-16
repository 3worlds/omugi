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
package fr.cnrs.iees.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.graph.io.GraphImporter;

import static fr.cnrs.iees.io.GraphFileFormats.*;

/**
 * A class to read any text file containing graph or tree data. This should be the entry point to
 * any graph-related file reading.
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */
// Tested OK with version 0.0.4 on 21/12/2018
// Tested OK with version 0.0.10 on 31/1/2019
public class FileImporter {
	
	private GraphImporter importer = null;
	private static Logger log = Logger.getLogger(FileImporter.class.getName());
	
	public FileImporter(File infile) {
		super();
		if (infile.exists()) {
			String graphPath = infile.getName();
			String extension = graphPath.substring(graphPath.indexOf('.')+1,graphPath.length());
			GraphFileFormats fileFormat = null;
			importer = getFileImporter(format(extension),infile);
			if (importer==null) {
				fileFormat = guessFileFormat(infile);
				if (fileFormat==null)
					log.warning("Cannot load files with extension \"" + extension + "\"");
				else {
					log.info("File \""+graphPath+"\" found to be of the "+fileFormat.toString());
					importer = getFileImporter(fileFormat,infile);
				}
			}
		}
		else
			log.warning("file \""+infile.getName()+"\" not found");			
	}
	
	// utility to infer the file format by peeking into it
	private GraphFileFormats guessFileFormat(File infile) {
		try {
			List<String> lines = Files.readAllLines(infile.toPath());
			String s = lines.get(0).trim();
			if (s.startsWith("graph"))
				return GOMUGI;
			if (s.startsWith("tree"))
				return TOMUGI;
			if (s.startsWith("aot"))
				return AOT;
			for (String l:lines)
				if (l.contains("http://graphml.graphdrawing.org/xmlns"))
					return GRAPHML;
		} catch (IOException e) {
			// Reaching here probably means the file is not a text file
			log.severe("cannot read file \""+infile.getName()+"\" as text");
		}
		return null;
	}
	
	public NodeSet<?> getGraph() {
		if (importer!=null)
			return importer.getGraph();
		return null;
	}
	
	public static NodeSet<?> loadGraphFromFile(File f){
		return new FileImporter(f).getGraph();
	}

}

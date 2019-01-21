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
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.graph.io.impl.GraphmlExporter;
import fr.cnrs.iees.graph.io.impl.OmugiGraphExporter;
import fr.cnrs.iees.graph.io.impl.OmugiGraphImporter;

/**
 * List of all supported graph file formats for import / export 
 * plus some helper static methods
 * 
 * @author Jacques Gignoux - 24/11/2017
 *
 */
public enum GraphFileFormats {
	// NOTE: Long descriptions are no good in a file dialog box
//	AOT			(".dsl .aot",		"S. Flint's AOT DSL graph format with cross-references (formerly known as 'dsl')", ""),
// Strictly speaking, this is no longer Shayne's format is it?? - add cf if you disagree
	/*-	format      file extensions     format description				url*/
	XML			(".xml",/*             */"3Worlds graph (Tree with cross-references)", ""),
 	AOT			(".aot",/*             */"3Worlds graph (Tree with cross-references)", ""),
    // remove TWG before first release
	TWG			(".dsl .twg",/*        */"S. Flint's AOT DSL graph format with cross-references and 3Worlds compliance (formerly known as 'dsl')", ""),
	UML			(".xmi .uml",/*        */"UML graph format with cross-references", ""),
	TRE         (".tre", /*            */"3Worlds hierachical graph", ""),
	GOMUGI      (".ugg", /*            */"omugi 'any graph' format", ""),
	TOMUGI      (".ugt", /*            */"omugi tree format", ""),
	GRAPHML		(".graphml .xml",/*    */"GraphML file format", 	"http://graphml.graphdrawing.org/")
// others to come:
//		GML
//		DOT
//		CSV/GEPHI
//		GRAPH6 SPARSE6
//		MATRIX
//		DIMACS2 & 9
	;

	private final String extension;
	private final String description;
	private final String url;
	private static Logger log = Logger.getLogger(GraphFileFormats.class.getName());

	private GraphFileFormats(String extension, String description, String url) {
		this.extension = extension;
		this.description = description;
		this.url = url;
	}

	public String toString() {
		return description;
	}

	public String extension() {
		return extension;
	}
	
	public String[] extensions() {
		return extension.split(" ");
	}
	
	public String url() {
		return url;
	}
	
	public static GraphFileFormats format(String ext) {
		for (GraphFileFormats f: GraphFileFormats.values()) {
			if (f.extension.indexOf(ext)>=0)
				return f;
		}
		return null;
	}
	
	/**
	 * Utility returning the proper {@link GraphImporter} class instance for a given file format
	 * 
	 * @param gf the file format
	 * @param f the input file
	 * @return the ad hoc GraphImporter
	 */
	public static GraphImporter getFileImporter(GraphFileFormats gf, File f) {
		if (gf!=null)
			switch (gf) {
				case AOT:
				try {
//					Class<?> c = Class.forName("fr.cnrs.iees.graph.io.impl.AotGraphImporter");
					Class<?> c = Class.forName("au.edu.anu.rscs.aot.graph.io.AotGraphImporter");
					Constructor<?> cc = c.getConstructor(File.class);
					GraphImporter gi = (GraphImporter) cc.newInstance(f);
					return gi;
				} catch (Exception e) {
					log.warning("Unable to load AOT format - file cannot be loaded");
					return null;
				}
				case TRE:
				case TWG:
				case UML:
				case XML:
					log.warning("Deprecated graph format - file cannot be loaded");
					return null;
				case GRAPHML:
					log.warning("GraphML input not yet implemented - please contact developers in case of urgent need");
					return null;
				case GOMUGI:
				case TOMUGI:
					return new OmugiGraphImporter(f);
			}
		return null;
	}

	/**
	 * Utility returning the file format matching a {@link GraphExporter} - use this to find the appropriate
	 * file extension.
	 * @param ge the GraphExporter
	 * @return the file format
	 */
	public static GraphFileFormats format(GraphExporter ge) {
		if (OmugiGraphExporter.class.isAssignableFrom(ge.getClass()))
			return GOMUGI;
		else if (GraphmlExporter.class.isAssignableFrom(ge.getClass()))
			return GRAPHML;
		return null;
	}
}

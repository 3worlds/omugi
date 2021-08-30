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

import fr.cnrs.iees.OmugiClassLoader;
import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.graph.io.impl.GraphmlExporter;
import fr.cnrs.iees.graph.io.impl.OmugiGraphExporter;
import fr.cnrs.iees.graph.io.impl.OmugiGraphImporter;
import fr.ens.biologie.generic.utils.Logging;

/**
 * <p>List of all supported graph file formats for import / export,  
 * plus some helper static methods.</p>
 * 
 * <p>We plan to support most 'standard' graph (e.g. 
 * <a href="https://gephi.org/users/supported-graph-formats/gml-format/">GML</a>, 
 * <a href="https://www.graphviz.org/documentation/">DOT</a>, 
 * <a href="https://gephi.org/users/supported-graph-formats/csv-format/">CSV/GEPHI</a>,
 * <a href ="https://users.cecs.anu.edu.au/~bdm/data/formats.txt">Graph6, Sparse6, Digraph6</a>, 
 * <a href="http://www.dimacs.rutgers.edu/programs/challenge/">DIMACS 2 &amp; 9</a>[but where exactly is this format defined?] formats in the future.</p>
 * 
 * <p>In the meanwhile, <a href="https://gephi.org/users/supported-graph-formats/">this page</a> provides a good analysis of the pros and cons of the major
 * file formats for saving graphs.</p>
 * 
 * @author Jacques Gignoux - 24/11/2017
 *
 */
public enum GraphFileFormats {
	// NOTE: Long descriptions are no good in a file dialog box
//	AOT			(".dsl .aot",		"S. Flint's AOT DSL graph format with cross-references (formerly known as 'dsl')", ""),
// Strictly speaking, this is no longer Shayne's format is it?? - add cf if you disagree
	/*-	format      file extensions     format description				url*/
	/** deprecated format */
	XML			(".xml",/*             */"3Worlds graph (Tree with cross-references)", ""),
	/** S. Flint's AOT DSL graph format with cross-references */
 	AOT			(".aot",/*             */"3Worlds graph (Tree with cross-references)", ""),
    // remove TWG before first release
	/** deprecated format */
	TWG			(".dsl .twg",/*        */"S. Flint's AOT DSL graph format with cross-references and 3Worlds compliance (formerly known as 'dsl')", ""),
	/** deprecated format */
	UML			(".xmi .uml",/*        */"UML graph format with cross-references", ""),
	/** deprecated format */
	TRE         (".tre", /*            */"3Worlds hierachical graph", ""),
	/** omugi 'any graph' format */
	GOMUGI      (".ugg", /*            */"omugi 'any graph' format", ""),
	/** omugi tree format */
	TOMUGI      (".ugt", /*            */"omugi tree format", ""),
	/** omugi treegraph format tree with cross-links */
	TGOMUGI     (".utg", /*            */"omugi treegraph format tree with cross-links", ""),
	/** <a href="http://graphml.graphdrawing.org/">GraphML</a> file format (export only) */
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
	private static Logger log = Logging.getLogger(GraphFileFormats.class);

	private GraphFileFormats(String extension, String description, String url) {
		this.extension = extension;
		this.description = description;
		this.url = url;
	}

	@Override
	public String toString() {
		return description;
	}

	/**
	 * 
	 * @return the valid file extensions for this format
	 */
	public String extension() {
		return extension;
	}
	
	/**
	 * 
	 * @return the list of valid file extensions for this format
	 */
	public String[] extensions() {
		return extension.split(" ");
	}
	
	/**
	 * 
	 * @return the url where the format is defined
	 */
	public String url() {
		return url;
	}
	
	/**
	 * Get the file format matching a file extension.
	 * 
	 * @param ext the file name extension (including the initial dot, e.g. ".xml")
	 * @return the matching file format, {@code null} if no match is found
	 */
	public static GraphFileFormats format(String ext) {
		for (GraphFileFormats f: GraphFileFormats.values()) {
			if (f.extension.indexOf(ext)>=0)
				return f;
		}
		return null;
	}
	
	/**
	 * Utility returning the proper {@link GraphImporter} implementation instance for a given file format
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
					Class<?> c = Class.forName("au.edu.anu.rscs.aot.graph.io.AotGraphImporter",true,OmugiClassLoader.getAppClassLoader());
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
				case TGOMUGI:
					return new OmugiGraphImporter(f);
			}
		return null;
	}

	/**
	 * Utility returning the file format matching a {@link GraphExporter} - use this to find the appropriate
	 * file extension to save a graph in a given format.
	 * 
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

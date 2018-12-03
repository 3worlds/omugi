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

/**
 * List of all supported graph file formats for import / export 
 * @author Jacques Gignoux - 24/11/2017
 *
 */
public enum GraphFileFormats {
	//FileChooser.ExtensionFilter
	/*-	format      file extensions     format description*/
		XML			(".xml",			"xml AOT graph format with cross-references"),
		AOT			(".dsl .aot",		"S. Flint's AOT DSL graph format with cross-references (formerly known as 'dsl')"),
		TWG			(".dsl .twg",		"S. Flint's AOT DSL graph format with cross-references and 3Worlds compliance (formerly known as 'dsl')"),
		UML			(".xmi .uml",		"UML graph format with cross-references"),
		TRE         (".tre",            "Hierachical graph")
	// others to come:
//		GML
//		DOT
//		CSV/GEPHI
//		GRAPH6 SPARSE6
//		MATRIX
//		GRAPHML
//		DIMACS2 & 9
		;

		private final String extension;
		private final String description;

		private GraphFileFormats(String extension, String description) {
			this.extension = extension;
			this.description = description;
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
		
		public static GraphFileFormats format(String ext) {
			for (GraphFileFormats f: GraphFileFormats.values()) {
				if (f.extension.indexOf(ext)>=0)
					return f;
			}
			return null;
		}

}

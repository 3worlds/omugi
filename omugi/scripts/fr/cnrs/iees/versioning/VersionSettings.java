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
package fr.cnrs.iees.versioning;

public class VersionSettings {
	
	// Change these fields to suit the project ====================================================
	
	/** The organisation name as will appear in the ivy module specification - it is a good idea
	 * to keep it consistent with the project src directory (although not required).*/
	protected static String ORG = "au.edu.anu.rscs.aot";
	
	/** The name of the ivy module (this will be the name of the generated jar file for 
	 * dependent projects).*/
	protected static String MODULE = "omugi";
	
	/** The ivy status of the module: integration, milestone, or release are the ivy defaults
	 * But we can define ours like bronze, gold, silver, or crap, supercrap, ultracrap. */
	protected static String STATUS = "integration";
	
	/** The license under which this module (= jar) is distributed */
	protected static String LICENSE = "gpl3";
	
	/**The url to the text of the license */
	protected static String LICENSE_URL = "https://www.gnu.org/licenses/gpl-3.0.txt";
	
	/**A (long) description of the ivy module */
	protected static String DESCRIPTION = 
		"This module is 'one more ultimate graph implementation'. It implements various kinds of graph, from very"+
		"light weight to elaborate, dynamic, graphs that can be used for data management or simulation";
	
	/**
	 * <p>Dependencies on other modules (they will be integrated in the ivy script).</p>
	 * 
	 * <p>This is a (n * 3) table of Strings.<br/>
	 * Every line is a new dependency.
	 * On every line, the 3 Strings must match the ivy fields 'org' (for organisation), 
	 * 'name' (for the module name), and 'rev' (for the revision or version number). The '+' can
	 * be conveniently used to specify 'any version'.
	 * The field can be empty (just needs the external braces).<br/>
	 * Example value: 
	 * <pre>{{"org.galaxy.jupiter","crap","1.0.+"},
	 * {"org.ocean.lostIsland","strungk","3.12.254"}}</pre> </p>
	 * <p>Wildcards for revision numbers are indicated <a href="http://ant.apache.org/ivy/history/master/ivyfile/dependency.html">there</a>.</p>
	 * 
	 */
	protected static String[][] DEPS = { 
		{"fr.ens.biologie", "generics", "+"},
	};
	
	/** The name of the main class to put in the jar manifest, if any. This enables users to
	 * run the jar using this class as the entry point. Of course this must be a fully qualified
	 * valid java class name found in the jar. 
	 */
	protected static String MAINCLASS = null;

}

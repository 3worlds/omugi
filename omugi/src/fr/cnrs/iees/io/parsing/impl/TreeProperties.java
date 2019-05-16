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
package fr.cnrs.iees.io.parsing.impl;

/**
 * List of tree properties understood by {@link TreeParser} and their default
 * values.
 * 
 * @author Jacques Gignoux - 20 déc. 2018
 *
 */
public enum TreeProperties {

	// name			property name	property class				property default value
	CLASS			("type",					
									"fr.cnrs.iees.graph.Tree",	"fr.cnrs.iees.graph.impl.SimpleTree"),
	
	TREE_FACTORY	("tree_factory",			
									"fr.cnrs.iees.graph.TreeNodeFactory",	
																"fr.cnrs.iees.graph.impl.TreeFactory"),
	
	PROP_FACTORY	("property_list_factory",	
									"fr.cnrs.iees.properties.PropertyListFactory",	
																"fr.cnrs.iees.graph.impl.TreeFactory"),
	
	MUTABLE			("mutable",		"java.lang.Boolean",		"false"),
	SCOPE			("scope",		"java.lang.String",			"DTF"),
	// others to come ?
	;
	
	private final String propertyName;
	private final String propertyType;
	private final String defaultValue;
	
	private TreeProperties(String propertyName, String propertyType, String defaultValue) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.defaultValue = defaultValue;
	}
	
	public String defaultValue() {
		return defaultValue;
	}
	
	public String propertyName() {
		return propertyName;
	}
	
	public String propertyType() {
		return propertyType;
	}
	
	public static TreeProperties propertyForName(String name) {
		for (TreeProperties p: TreeProperties.values())
			if (p.propertyName.equals(name))
				return p;
		return null;
	}

}

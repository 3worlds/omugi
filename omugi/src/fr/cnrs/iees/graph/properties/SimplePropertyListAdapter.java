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
package fr.cnrs.iees.graph.properties;

/**
 * This to better separate concerns of property list management 
 * currently mixed in PropertyListInterface.
 * 
 * A trivial implementation of PropertyListAccess.
 * basically, a copy of au.edu.anu.rscs.aot.graph.property.PropertyList + KeyedPropertyList 
 * WITHOUT the label and name default properties.
 * not resizeable, local storage, no default properties.
 * 
 * optimised for speed: no checks on dimensions, names or whatever
 *  
 * @author J. Gignoux - 13 févr. 2017
 *
 */
public abstract class SimplePropertyListAdapter
		extends ReadOnlyPropertyListAdapter
		implements SimplePropertyList {

	
	// PropertyListSetters  methods
	//
		
//	@Override
//	public final SimplePropertyList setProperty(String key, String value, String className, NodeList nodeList) {
//		setProperty(key, PropertyType.toObject(className, value));
//		return this;
//	}
	
	// DataContainer  methods
	//
	@Override
	public abstract SimplePropertyList clone();


}

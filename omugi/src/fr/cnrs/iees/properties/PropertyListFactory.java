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
package fr.cnrs.iees.properties;

import au.edu.anu.rscs.aot.graph.property.Property;

/**
 * 
 * @author Jacques Gignoux - 20 déc. 2018
 *
 */
public interface PropertyListFactory {

	/**
	 * Create a read-only property list with property names and values 
	 * @param properties
	 * @return
	 */
	public ReadOnlyPropertyList makeReadOnlyPropertyList(Property... properties);
	
	/**
	 * Create a read-only property list with property names, but no values
	 * @param propertyKeys
	 * @return
	 */
	public ReadOnlyPropertyList makeReadOnlyPropertyList(String... propertyKeys);
	
	/**
	 * Create a read-write property list with property names and values 
	 * @param properties
	 * @return
	 */
	public SimplePropertyList makePropertyList(Property... properties);
	
	/**
	 * Create a read-write property list with property names, but no values
	 * @param propertyKeys
	 * @return
	 */
	public SimplePropertyList makePropertyList(String... propertyKeys);

}

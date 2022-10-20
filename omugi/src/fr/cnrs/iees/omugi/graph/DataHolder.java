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
package fr.cnrs.iees.omugi.graph;

import fr.cnrs.iees.omugi.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.omugi.properties.SimplePropertyList;

/**
 * For all {@link Graph} or {@link Tree} connected elements that have read-write properties.
 * 
 * @author Jacques Gignoux - 15 avr. 2019
 *
 */
public interface DataHolder extends ReadOnlyDataHolder {
	
	@Override
	public SimplePropertyList properties();
	
	/**
	 * Accessor to the list of attributes of this instance, as a immutable (read-only)
	 * property list. Use when you want to make sure that properties cannot be edited.
	 * 
	 * @return this instance read-only property list 
	 */
	public default ReadOnlyPropertyList readOnlyProperties() {
		return properties();
	}
	
}

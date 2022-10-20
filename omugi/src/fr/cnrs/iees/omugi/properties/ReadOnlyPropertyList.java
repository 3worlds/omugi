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
package fr.cnrs.iees.omugi.properties;

import fr.cnrs.iees.omhtk.*;

/**
 * A read-only property list, ie with getters but no setters.
 * 
 * @author gignoux - 15 juin 2017
 *
 */
public interface ReadOnlyPropertyList 
		extends PropertyListGetters, Sizeable, DataContainer {

	/**
	 * Compares the keys of two property lists
	 * 
	 * @param list another property list to cmopare to
	 * @return {@code true} if both lists have the same property names
	 */
	public default boolean hasTheSamePropertiesAs(ReadOnlyPropertyList list) {
		if (size()==list.size())
			if (getKeysAsSet().equals(list.getKeysAsSet())) {
				for (String key:getKeysAsSet()) 
					if (!getPropertyClass(key).equals(list.getPropertyClass(key)))
						return false;
				return true;
			}
		return false;
	}
	
	@Override
	public default DataContainer clear() {
		// do nothing, this is read-only
		return this;
	}
	
	public ReadOnlyPropertyList clone();
}

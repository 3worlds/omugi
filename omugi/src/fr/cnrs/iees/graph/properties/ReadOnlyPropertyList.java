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

import fr.ens.biologie.generic.DataContainer;
import fr.ens.biologie.generic.Sizeable;

/**
 * A read-only property list, ie with a size and getters but no setters.
 * @author gignoux - 15 juin 2017
 *
 */
// This is a temporary version - see what happens to isProperty(), which depends
// on PropertyType.java, which is a complete mess.
public interface ReadOnlyPropertyList 
		extends PropertyListGetters, Sizeable, DataContainer {
	
//	@Override
//	public default boolean isPropertyType(String key, String className) {
//		try {
////			String fullClassName = PropertyType.toJavaClassName(className);
//			String fullClassName = ValidPropertyTypes.getJavaClassName(className);
//			Object value = getPropertyValue(key);
//			return fullClassName.equals(value.getClass().getName());
//		} catch (Exception e) {
//			return false;
//		}
//	}

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
	
	public SimplePropertyList clone();
}

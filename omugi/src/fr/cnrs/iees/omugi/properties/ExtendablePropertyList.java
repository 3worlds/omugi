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

import fr.cnrs.iees.omhtk.Sealable;

/**
 * <p>A mutable {@linkplain SimplePropertyList}, i.e. which can grow and shrink.</p> 
 *
 * <p>The contract is as follows: {@code add...()} methods will add a new property to the list
 * while {@code set...()} methods will only set a value for an existing property. They should
 * return an error if the property does not exist.</p>
 * 
 * <p>Instances of this class are {@link fr.cnrs.iees.omhtk.Sealable Sealable}, i.e. 
 * they can be made 
 * immutable after some time by calling {@code seal()}. This enables, for example,
 * to read properties from a file without knowing how many are expected, and then
 * make the property list immutable after the file has been closed.</p>
 * 
 * @author Jacques Gignoux - 29-8-2017
 *
 */
public interface ExtendablePropertyList 
	extends SimplePropertyList, ResizeablePropertyList, Sealable {

}

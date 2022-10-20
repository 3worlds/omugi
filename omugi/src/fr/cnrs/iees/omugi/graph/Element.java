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

import fr.cnrs.iees.omhtk.Textable;
import fr.cnrs.iees.omugi.identity.Identity;

/**
 * <p>A basic interface for graph elements. Graph elements are connected (cf. the {@link Connected}
 * interface), but they also have other features captured in this interface:</p>
 * 
 * <ul>
 * <li>They must have a unique identifier for internal graph management ({@link Identity} interface)</li>
 * <li>They should display information about their state in a human-readable way
 * ({@link Textable} interface)</li>
 * <li>They may belong to groups of elements with a similar behaviour, indicated by a label or
 * <em>classId</em> ({@link Specialized} interface)</li>
 * </ul>
 * 
 * @author Jacques Gignoux - 9 mai 2019
 *
 */
public interface Element extends Specialized, Textable, Identity {

}

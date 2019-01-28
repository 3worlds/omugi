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
package fr.cnrs.iees.identity;

/**
 * A scope over which Identities are guaranteed to be unique.
 * 
 * @author Jacques Gignoux - 28 janv. 2019
 *
 */
public interface IdentityScope {

	/**
	 * makes a new instance of an Identity, unique over this scope.
	 * NOTE: implementations of this method should never return null
	 * 
	 * @return a new Identity instance, unique over this scope
	 */
	public Identity newId();
	
	/**
	 * makes a new instance of an Identity, unique over this scope. The default method
	 * ignores the argument.
	 * NOTE: implementations of this method should never return null
	 * 
	 * @param proposedId a base for the id to be computed
	 * @return a new Identity instance, unique over this scope
	 */
	public default Identity newId(String proposedId) {
		return newId();
	}
	
	/**
	 * makes a new instance of an Identity, unique over this scope. The default method
	 * ignores the arguments.
	 * NOTE: implementations of this method should never return null
	 * 
	 * @param proposedIdComponents a base for the id to be computed
	 * @return a new Identity instance, unique over this scope
	 */
	public default Identity newId(String... proposedIdComponents) {
		return newId();
	}

}

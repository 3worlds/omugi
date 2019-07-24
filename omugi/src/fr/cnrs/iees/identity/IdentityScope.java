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

import java.util.HashSet;
import java.util.Set;

/**
 * A scope over which Identities are guaranteed to be unique.
 * Note: every scope has a unique id over the application scope so that merging items
 * from different scopes is always possible
 * 
 * @author Jacques Gignoux - 28 janv. 2019
 *
 */
// I need this interface to provide for proposing an id given an id - IDD!
// That is, to employ the given alg but NOT add the id to the list
public interface IdentityScope {
	
	/**
	 * This field to make sure all scope instances have a different id
	 */
	static final Set<String> scopeIds = new HashSet<String>();
	
	/**
	 * returns
	 * @return the scope id within the application context
	 */
	public String id();

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
	public default Identity newId(boolean addToScope,String proposedId) {
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
	public default Identity newId(boolean addToScope,String... proposedIdComponents) {
		return newId();
	}

}

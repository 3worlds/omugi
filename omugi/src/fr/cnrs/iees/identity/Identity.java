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

import fr.ens.biologie.generic.SaveableAsText;

/**
 * A unique identity within a given scope, attached to some item.
 * Classes implementing this interface must override the object equals() and hashCode() 
 * methods. [not sure - check this]
 * 
 * @author Jacques Gignoux - 28 janv. 2019
 *
 */
public interface Identity {
	
	/** 
	 * returns
	 * @return a unique identifier, valid over a particular scope
	 */
	public String id();
	
	/**
	 * returns
	 * @return the scope within which this Identity is guaranteed to be unique.
	 */
	public IdentityScope scope();
	
	/**
	 * returns
	 * @return the universal id = scope id + item id
	 */
	public default String universalId() {
		return scope().id()+SaveableAsText.COLON+id();
	}
	
	public void rename(String oldId, String newId);

}

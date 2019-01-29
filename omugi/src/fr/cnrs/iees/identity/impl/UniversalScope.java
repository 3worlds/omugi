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
package fr.cnrs.iees.identity.impl;

import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;
import fr.ens.biologie.generic.utils.UniqueString;

/**
 * A 'universal' scope for unique ids. The ids are constructed from (1) the computer mac Address
 * (2) the time at instantiation in milliseconds (3) an integer rank for instances sharing
 * the same mac address and date. Resulting ids are unique over a network of computers within the
 * same running application.
 * 
 * This uses Shayne's Uid class.
 * 
 * @author Jacques Gignoux - 28 janv. 2019
 *
 */
public class UniversalScope implements IdentityScope {

	private String id;
	
	public UniversalScope() {
		this(UniversalScope.class.getSimpleName());
	}

	public UniversalScope(String name) {
		super();
		id = UniqueString.makeString(name,scopeIds);
		scopeIds.add(id);
	}
	
	@Override
	public Identity newId() {
		return new UidIdentity(this);
	}

	@Override
	public java.lang.String id() {
		return id;
	}

}

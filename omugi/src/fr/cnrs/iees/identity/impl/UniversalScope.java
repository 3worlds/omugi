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

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;
import fr.ens.biologie.generic.utils.UniqueString;

/**
 * <p>
 * A 'universal' scope for unique ids. The ids are constructed from (1) the
 * computer mac Address (2) the time at instantiation in milliseconds (3) an
 * integer rank for instances sharing the same mac address and date. Resulting
 * ids are unique over a network of computers within the same running
 * application.
 * </p>
 * 
 * <p>
 * Works with {@link UidIdentity}.
 * </p>
 * 
 * @author Jacques Gignoux - 28 janv. 2019
 *
 */
public class UniversalScope implements IdentityScope {

	private String id;

	/**
	 * Constructor using the UniversalScope simple class name as its Id.
	 */
	public UniversalScope() {
		this(UniversalScope.class.getSimpleName());
	}

	/**
	 * @param name proposed name of the scope. Its uniqueness will be enforced by
	 *             incrementing an appended number.
	 */
	public UniversalScope(String name) {
		super();
		id = UniqueString.makeString(name, scopeIds);
		scopeIds.add(id);
	}

	@Override
	public void removeId(String id) {
		throw new OmugiException(
				"removing an id from '" + this.getClass().getSimpleName() + "' is not implemented  [" + id + "]");
	}

	@Override
	public Identity newId() {
		return new UidIdentity(this);
	}

	@Override
	public java.lang.String id() {
		return id;
	}

	@Override
	public boolean contains(String id) {
		throw new OmugiException(
				"Querying ids in'" + this.getClass().getSimpleName() + "' is not implemented  [" + id + "]");
	}

	@Override
	public void addId(String newId) {
		throw new OmugiException(
				"Adding an id to '" + this.getClass().getSimpleName() + "' is not implemented  [" + newId + "]");
	}

}

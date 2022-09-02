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

import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;
import fr.ens.biologie.generic.utils.UniqueString;

/**
 * <p>
 * A local scope, keeping track of all its ids. Works with
 * {@link SimpleIdentity}.
 * </p>
 * <p>
 * Every time a new {@code Identity} instance is created, its identifier is
 * recorded in an internal list. All later instantiations check that the new
 * identifier does not yet exist in this list and modify it (by adding an
 * increasing number suffix) if it does before generating the new instance. As a
 * result, this scope implementation becomes slow with increasing numbers of
 * {@code Identity} instances and should only be used with small sets of
 * identifiers. The method used to modify proposed identifiers is
 * {@link fr.ens.biologie.generic.utils.UniqueString#makeString(String, Set)}.
 * </p>
 * 
 * <p>
 * Its advantage is that you can provide explicit identifiers through the
 * {@link LocalScope#newId(boolean, String) newId(proposedId)} method, thus
 * creating more user-friendly identifier than other scope implementations.
 * </p>
 *
 * @author Jacques Gignoux - 28 janv. 2019
 *
 */
public class LocalScope implements IdentityScope {

	private String id;
	Set<String> ids = new HashSet<String>();

	/**
	 * Constructor using the LocalScope simple class name as its Id.
	 */
	public LocalScope() {
		this(LocalScope.class.getSimpleName());
	}

	/**
	 * @param name proposed name of the scope. Its uniqueness will be enforced by
	 *             incrementing an appended number.
	 */
	public LocalScope(String name) {
		super();
		id = UniqueString.makeString(name, scopeIds);
		scopeIds.add(id);
	}

	@Override
	public void removeId(String id) {
		if (!ids.remove(id))
			throw new IllegalArgumentException("Attempt to remove an id which does not exist [" + id + "]");
	}

	@Override
	public Identity newId() {
		return newId(true, "");
	}

	@Override
	public Identity newId(boolean addToScope, String proposedId) {
		SimpleIdentity result = null;
		if (!ids.contains(proposedId)) {
			result = new SimpleIdentity(proposedId, this);
			if (addToScope)
				ids.add(proposedId);
		} else {
			String s = UniqueString.makeString(proposedId, ids);
			result = new SimpleIdentity(s, this);
			if (addToScope)
				ids.add(s);
		}
		return result;
	}

	@Override
	public Identity newId(boolean addToScope, String... proposedIdComponents) {
		StringBuilder sb = new StringBuilder();
		for (String s : proposedIdComponents)
			sb.append(s);
		return newId(addToScope, sb.toString());
	}

	@Override
	public java.lang.String id() {
		return id;
	}

	@Override
	public boolean contains(String id) {
		return ids.contains(id);
	}

	@Override
	public void addId(String newId) {
		ids.add(newId);
	}

}

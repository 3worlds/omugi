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
package fr.cnrs.iees.omugi.identity.impl;

import java.util.Objects;

import fr.cnrs.iees.omugi.identity.Identity;
import fr.cnrs.iees.omugi.identity.IdentityScope;

/**
 * <p>Implementation of {@link Identity} based on a String. Works with
 * {@link LocalScope}, {@link ResettableLocalScope} and {@link IntegerScope}.</p>
 * 
 * @author Ian Davies - 28 jan. 2019
 *
 */
public final class SimpleIdentity implements Identity {

	private String ids;
//	private long idi;
	private final IdentityScope scope;
	// hash code for fast indexing
	private int hash = 0;

	/**
	 * protected constructor, as all instantiations should be made through the
	 * scope.
	 * 
	 * @param scope the scope used to instantiate this class
	 */
	protected SimpleIdentity(String id, IdentityScope scope) {
		super();
		this.ids = id;
//		this.idi = Long.valueOf(ids);
		this.scope = scope;
	}

	@Override
	public String id() {
		return ids;
	}

	@Override
	public IdentityScope scope() {
		return scope;
	}

	@Override
	public String toString() {
		return ids;
	}
	/*
	 * renaming can only take if:
	 * 
	 * 1) the id is a SimpleIdentity;
	 * 
	 * 2) It is contained in a LocalScope;
	 * 
	 * 3) If the old id exists, and:
	 * 
	 * 4) if the new Id does not exist.
	 * 
	 * A better approach may be to implement an editable interface for these
	 * classes.
	 */

	@Override
	public void rename(String oldId, String newId) {
		if (!scope.contains(oldId))
			throw new IllegalArgumentException("Attempt to rename a non-existent id from '" + oldId + "' to '" + newId + "'");
		if (scope.contains(newId))
			throw new IllegalArgumentException(
					"Attempt to rename an id to one that already exists from '" + oldId + "' to '" + newId + "'");
		this.ids = newId;
		scope.removeId(oldId);
		scope.addId(newId);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hash==0)
			hash = Objects.hash(ids,scope);
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SimpleIdentity))
			return false;
		SimpleIdentity other = (SimpleIdentity) obj;
//		return idi==other.idi && Objects.equals(scope,other.scope);
		return ids.equals(other.ids) && Objects.equals(scope,other.scope);
	}

}

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
import java.util.UUID;

import fr.cnrs.iees.omugi.identity.Identity;
import fr.cnrs.iees.omugi.identity.IdentityScope;

/**
 * <p>Implementation of universally unique {@link fr.cnrs.iees.omugi.identity.Identity Identity} based on {@link java.util.UUID}.
 * Currently not associated to a {@link fr.cnrs.iees.omugi.identity.IdentityScope IdentityScope} implementation.</p>
 * 
 * @author Ian Davies - 28 jan. 2019
 *
 */
public final class GUIDIdentity implements Identity{
	
	private final String id;
	private final IdentityScope scope;
	// hash code for fast indexing
	private int hash = 0;
	
	/**
	 * protected constructor, as all instantiations should be made through the scope.
	 * @param scope
	 */
	protected GUIDIdentity(IdentityScope scope) {
		super();
		id = UUID.randomUUID().toString();
		this.scope = scope;
	}
	
	@Override
	public String id() {
		return id;
	}

	@Override
	public IdentityScope scope() {
		return scope;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public void rename(String oldId, String newId) {
		throw new UnsupportedOperationException("Renaming of '"+this.getClass().getSimpleName()+"' is not implemented.");
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hash==0)
			hash = Objects.hash(id, scope);
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof GUIDIdentity))
			return false;
		GUIDIdentity other = (GUIDIdentity) obj;
		return Objects.equals(id,other.id) && Objects.equals(scope,other.scope);
	}

}

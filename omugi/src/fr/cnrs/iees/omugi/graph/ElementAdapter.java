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
package fr.cnrs.iees.omugi.graph;

import java.util.Objects;

import fr.cnrs.iees.omugi.identity.Identity;
import fr.cnrs.iees.omugi.identity.IdentityScope;

/**
 * A base implementation of {@link Element} with the methods that should be universal 
 * in all descendants. Provides default methods for all te abstract methods of {@code Element}.
 * 
 * 
 * @author gignoux - 16 août 2017
 *
 */
public abstract class ElementAdapter implements Element {
	
	/* now protected to allow restricted id renaming.*/
	protected Identity id = null;

	// Constructors

	// this to prevent construction without an id
	protected ElementAdapter() {
		throw new UnsupportedOperationException("A Graph Element must be created with a valid id");
	}
	
	/**
	 * An {@code Element} always requires a valid identifier for instantiation.
	 * 
	 * @param id a valid (= unique within its {@linkplain IdentityScope scope}) identifier
	 */
	protected ElementAdapter(Identity id) {
		super();
		this.id = id;
	}
	
	// IDENTITY
	
	@Override
	public String id() {
		return id.id();
	}
	
	@Override
	public IdentityScope scope() {
		return id.scope();
	}
	
	// TEXTABLE

	@Override
	public final String toUniqueString() {
		return id.universalId();
	}
	
	@Override
	public String toDetailedString() {
		return toShortString();
	}
	
	@Override
	public String toShortString() {
		return classId()+":"+id();
	}
	
	// OBJECT
	
	@Override
	public final String toString() {
		return "["+toShortString()+"]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 *  <p>NOTE: graph elements ({@link Node} and {@link Edge} and all their descendants) all have
	 *  a unique identifier (within a given scope). Hence equality is only a matter of comparing their
	 *  unique ids.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object) 
	 * @see {@linkplain Identity}
	 * @see {@linkplain IdentityScope}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ElementAdapter))
			return false;
		ElementAdapter other = (ElementAdapter) obj;
		return Objects.equals(id, other.id);
	}
	
	


}

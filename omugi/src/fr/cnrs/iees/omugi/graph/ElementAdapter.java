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

	/**
	 * Two elements are equal if they have the same id within the same scope
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		if (!(obj instanceof Element))
			return false;
		Element e = (Element) obj;
		return (scope().id().equals(e.scope().id()) &&
				id().equals(e.id()));
	}
	
	// This is important when using HashSets or HahsMaps: to make sure graph elements are only
	// considered different if they differ by classId+UniqueId, their hashCode must be computed
	// based on classId+UniqueId. Otherwise Object.hashCode() is called to compute the Hash
	// for the Map/Set and no subsequent call to .equals() is made, so elements with other
	// differences than classId+uniqueId will be considered different even if they have the 
	// same classId+instanceId.
	@Override
	public int hashCode() {
		return id.universalId().hashCode();
	}
	
	


}

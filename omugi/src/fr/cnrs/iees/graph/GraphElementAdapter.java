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
package fr.cnrs.iees.graph;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;

/**
 * A base implementation of Element with the methods that should be universal in all descendants
 * @author gignoux - 16 août 2017
 *
 */
public abstract class GraphElementAdapter implements GraphElement {
	
	private Identity id = null;

	// Constructors

	// this to prevent construction without an id
	protected GraphElementAdapter() {
		throw new OmugiException("A Graph Element must be created with a valid id");
	}
	
	// this is the only constructor to use
	protected GraphElementAdapter(Identity id) {
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
	public String toUniqueString() {
		return id();
	}

	@Override
	public String toShortString() {
		return id();
	}
	
	@Override
	public String toDetailedString() {
		return toUniqueString();
	}
	
	// OBJECT
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

	// Two elements are equal if they have the same id within their scope
	@Override
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		if (!GraphElement.class.isAssignableFrom(obj.getClass()))
			return false;
		GraphElement e = (GraphElement) obj;
		return (scope().id().equals(e.scope().id()) &&
				id().equals(e.id()));
	}
	
	// This is important when using HashSets or HahsMaps: to make sure graph elements are only
	// considered different if they differ by classId+UniqueId, their hashCode must be computed
	// based on classId+UniqueId. Otherwise Object.hashCode() is called to compute the Hash
	// for the Map/Set and no subsequent call to .equals() is made, so elements with other
	// differences than classId+uniqueId will be considereed different even if they have the 
	// same classId+instanceId.
	@Override
	public int hashCode() {
		return id.universalId().hashCode();
	}


}

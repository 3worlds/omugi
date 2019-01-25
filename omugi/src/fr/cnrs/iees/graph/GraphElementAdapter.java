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

import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.OmugiException;

/**
 * A base implementation of Element with the methods that should be universal in all descendants
 * @author gignoux - 16 août 2017
 *
 */
public abstract class GraphElementAdapter implements GraphElement {
	
	private String instanceId = null;
	private String classId = null;

	// Constructors
	
	/**
	 * Default constructor: initialises an Element with a unique ID.
	 */
	public GraphElementAdapter() {
		super();
		instanceId = (new Uid()).toString();
		classId = this.getClass().getSimpleName();
	}
	
	/**
	 * Use this constructor with caution: there is no guarantee that the ID passed as argument
	 * is unique.
	 * @param instanceId
	 */
	public GraphElementAdapter(String instanceId) {
		super();
		if (instanceId==null)
			throw new OmugiException("Attempt to instantiate a graph element with a null id.");
		this.instanceId = instanceId;
		classId = this.getClass().getSimpleName();
	}
	
	public GraphElementAdapter(String classId, String instanceId) {
		this(instanceId);
		if (classId!=null)
			this.classId = classId;
	}
	
	// Identifiable

	@Override
	public String instanceId() {
		return instanceId;
	}

	@Override
	public String classId() {
		return classId;
	}
	
	// TEXTABLE

	@Override
	public String toUniqueString() {
		return uniqueId();
	}

	@Override
	public String toShortString() {
		return classId();
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

	// Two elements are equal if they have the same classId and instanceId
	@Override
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		if (!GraphElement.class.isAssignableFrom(obj.getClass()))
			return false;
		GraphElement e = (GraphElement) obj;
		return (instanceId().equals(e.instanceId()) &&
				classId().equals(e.classId()));
	}
	
	// This is important when using HashSets or HahsMaps: to make sure graph elements are only
	// considered different if they differ by classId+UniqueId, their hashCode must be computed
	// based on classId+UniqueId. Otherwise Object.hashCode() is called to compute the Hash
	// for the Map/Set and no subsequent call to .equals() is made, so elements with other
	// differences than classId+uniqueId will be considereed different even if they have the 
	// same classId+instanceId.
	@Override
	public int hashCode() {
		return uniqueId().hashCode();
	}


}

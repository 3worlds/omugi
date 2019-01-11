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

/**
 * A base implementation of Element with the methods that should be universal in all descendants
 * @author gignoux - 16 août 2017
 *
 */
public abstract class GraphElementAdapter implements GraphElement {
	
	private String id = null;

	// Constructors
	
	/**
	 * Default constructor: initialises an Element with a unique ID.
	 */
	public GraphElementAdapter() {
		super();
		id = (new Uid()).toString();
	}
	
	/**
	 * Use this constructor with caution: there is no guarantee that the ID passed as argument
	 * is unique.
	 * @param instanceId
	 */
	public GraphElementAdapter(String instanceId) {
		super();
		id = instanceId;
	}
	
	// Identifiable

	@Override
	public String instanceId() {
		return id;
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
	
	

}
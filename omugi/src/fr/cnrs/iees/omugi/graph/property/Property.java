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
package fr.cnrs.iees.omugi.graph.property;

import java.util.Objects;

/**
 * A property that can be attached to a graph element (node or edge). It is just a (key,value) pair.
 * 
 * @author Shayne Flint - long ago (2012?)
 *
 */
public class Property {

	protected String key;
	protected Object value;
	
	public Property(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	// used a lot
	/**
	 * Get the class name from the value. As a consequence, this will fail if value = {@code null}.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return value.getClass().getName();
	}
	
	/**
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	// JG added - for better readability of Exceptions
	// ID updated - for the same reason
	public String toString() {
//		return("["+super.toString()+":"+key+"="+value+"]");
		return("["+getClass().getSimpleName()+":"+key+"="+value+"]");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(key,value);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Property))
			return false;
		Property other = (Property) obj;
		return Objects.equals(key, other.key) && Objects.equals(value, other.value);
	}

}

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

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import fr.cnrs.iees.omhtk.Sizeable;

/**
 * An ordered list of property names. For use when a large set of graph elements have the same properties
 * (cf. {@link fr.cnrs.iees.omugi.properties.impl.SharedPropertyListImpl}).
 * 
 * @author Shayne Flint - looooong ago.
 *
 */
public class PropertyKeys implements Sizeable {

	private String[] keySet;
	// hash code for fast indexing
	private int hash = 0;

	/**
	 * 
	 * @param keys the names of the properties
	 */
	public PropertyKeys(String... keys) {
		int len = keys.length;
		keySet = new String[len];
		for (int i=0; i< keys.length; i++)
			keySet[i] = keys[i];
	}

	/**
	 * 
	 * @param keys the names of the properties
	 */
	public PropertyKeys(Set<String> keys) {
		keySet = new String[keys.size()];
		int i=0;
		for (String key:keys) {
			keySet[i]=key;
			i++;
		}
	}
	
	/**
	 * 
	 * @return a set of property names
	 */
	public Set<String> getKeysAsSet() {
		Set<String> result = new TreeSet<String>();
		for (String key : keySet)
			result.add(key);
		return result;
	}
	
	/**
	 * 
	 * @return an array of property names
	 */
	public String[] getKeysAsArray() {
		return keySet;
	}

	@Override
	public int size() {
		return keySet.length;
	}

	/**
	 * 
	 * @param key the name of a property
	 * @return the rank of this property in the list
	 */
	public int indexOf(String key) {
		for (int i=0; i< keySet.length; i++) {
			if (keySet[i].equals(key))
				return i;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hash==0) {
			final int prime = 31;
			int hash = 1;
			hash = prime * hash + Arrays.hashCode(keySet);
		}
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PropertyKeys))
			return false;
		PropertyKeys other = (PropertyKeys) obj;
		return Arrays.equals(keySet, other.keySet);
	}
	
	
}

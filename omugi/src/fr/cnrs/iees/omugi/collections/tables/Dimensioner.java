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
package fr.cnrs.iees.omugi.collections.tables;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>A class to set the dimensions of other objects (typically, multi-dimensional containers such
 * as {@link Table}). It will store or generate the relevant indexes to access the object(s) it
 * is associated with. It can use {@code int}egers or {@link String}s as indexes.</p>
 * 
 * @author Shayne Flint - long ago 
 *
 */
public class Dimensioner {

	private int last;
	private String[] names;
	// hash code for faster comparison in maps
	private int hash = 0;
	
	/**
	 * Simple constructor just defining the length of the dimension. The indexes will 
	 * consist in integers from 0 to length-1.
	 * 
	 * @param length the number of indexes to generate
	 */
	public Dimensioner(int length) {
		this.last  = length - 1;
		this.names = null;
	}
	
	/**
	 * Constructor using {@code String}s as indexes. The arguments will be the successive values of
	 * the indexes, i;e. the order in the call to the constructor will define the order of index
	 * values in this instance.
	 * 
	 * @param names the index values in indexing order
	 */
	public Dimensioner(String... names) {
		this.last  = names.length - 1;
		this.names = names;
	}
	
	/**
	 * 
	 * @return the last index value
	 */
	public int getLast() {
		return last;
	}
	
	/**
	 * Accessor to the length of this instance, also known as this <em>dimension size</em>.
	 * 
	 * @return the number of values in this dimensioner
	 */
	public int getLength() {
		return last + 1;
	}
	
	/**
	 * Accessor to the name associated with an integer index. Throws an {@code Exception} if this
	 * instance has no names.
	 * 
	 * @param index the int index
	 * @return the matching name, if any.
	 */
	public String getName(int index) {
		if ((names==null)||(names.length == 0))
			throw new IllegalStateException("Dimensioner.getName: there are no names associated with " + toString());
		else
			return names[index];
	}
	
	/**
	 * Accessor to the integer value associated with a String index value.Throws an {@code Exception} if this
	 * instance has no names.
	 * 
	 * @param name the name 
	 * @return the matching integer index value
	 */
	public int getIndex(String name) {
		if (names == null)
			throw new IllegalStateException("Dimensioner.getIndex: " + toString() + " has no names.");		
		for (int i=0; i< names.length; i++)
			if (names[i].equals(name))
				return i;
		throw new IllegalStateException("Dimensioner.getIndex: '" + name + "' not found in " + toString());		
	}
	
	@Override
	public String toString() {
		String result = "[Dimensioner " + "0.." + last;
		if (names != null && names.length > 0) {
			result = result + "(";
			for (String name : names)
				result = result + " " + name;
			result = result + ")";
		}
		result = result + "]";
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hash==0) {
			final int prime = 31;
			hash = 1;
			hash = prime * hash + Arrays.hashCode(names);
			hash = prime * hash + Objects.hash(last);
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
		if (!(obj instanceof Dimensioner))
			return false;
		Dimensioner other = (Dimensioner) obj;
		return last == other.last && Arrays.equals(names,other.names);
	}
	
}

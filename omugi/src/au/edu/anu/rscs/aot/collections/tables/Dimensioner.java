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
package au.edu.anu.rscs.aot.collections.tables;

import fr.cnrs.iees.OmugiException;

/**
 * 
 * @author Shayne Flint - long ago 
 *
 */
public class Dimensioner {

	private int last;
	private String[] names;
	
	public Dimensioner(int length) {
		this.last  = length - 1;
	}
	
	public Dimensioner(String... names) {
		this.last  = names.length - 1;
		this.names = names;
	}
	
	public int getLast() {
		return last;
	}
	
	public int getLength() {
		return last + 1;
	}
	
	public String getName(int index) {
		if (names.length == 0)
			throw new OmugiException("Dimensioner.getName: there are no names associated with " + toString());
		else
			return names[index];
	}
	
	public int getIndex(String name) {
		if (names == null)
			throw new OmugiException("Dimensioner.getIndex: " + toString() + " has no names.");		
		for (int i=0; i< names.length; i++)
			if (names[i].equals(name))
				return i;
		throw new OmugiException("Dimensioner.getIndex: '" + name + "' not found in " + toString());		
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

	public String toShortString() {
		return "[" + "0.." + last + "]";
	}
}

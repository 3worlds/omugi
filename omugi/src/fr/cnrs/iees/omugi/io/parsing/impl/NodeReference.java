/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
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
package fr.cnrs.iees.omugi.io.parsing.impl;

import fr.cnrs.iees.omugi.graph.TreeNode;

/**
 * Static methods to match TreeNodes to references
 *
 * @author Jacques Gignoux - 18 avr. 2019
 *
 */
public class NodeReference {

	/**
	 * Checks if a TreeNode location in a tree matches the reference passed as argument.
	 * 
	 * @param anode the TreeNode to check
	 * @param reference the location reference
	 * @return true if both arguments match
	 */
	public static boolean matchesRef(TreeNode anode, String reference) {
		if (reference == null)
			return false;
		ReferenceTokenizer tk = new ReferenceTokenizer(reference);
		ReferenceParser p = tk.parser();
		p.parse();
		return p.matches(anode);
	}

}

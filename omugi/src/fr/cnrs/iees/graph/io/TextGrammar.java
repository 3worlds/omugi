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
package fr.cnrs.iees.graph.io;

import fr.ens.biologie.generic.Textable;

/**
 * This interface defines the conventions used to save graph elements as text. 
 * For example, which delimiter for which kind of token, etc.
 * <p>CAUTION: change in this file will corrupt ALL SAVED configuration files.</p>
 * 
 * @author gignoux
 *
 */
public interface TextGrammar {

	// Tables
	
	/** delimiter for dimensions */
	public static char[] DIM_BLOCK_DELIMITERS = Textable.SQUARE_BRACKETS;
	/** separator for dimensions */
	public static char DIM_ITEM_SEPARATOR = Textable.COMMA;
	/** delimiter for tables */
	public static char[] TABLE_BLOCK_DELIMITERS = Textable.BRACKETS;
	/** separator for table items */
	public static char TABLE_ITEM_SEPARATOR = Textable.COMMA;

	// Property lists
	
	/** delimiter for property lists */
	public static char[] PROPERTY_LIST_DELIMITERS = Textable.BRACES;
	/** separator for property lists */
	public static char PROPERTY_LIST_SEPARATOR = Textable.BLANK;
	
	// Properties
	
	/** separator for property (between key and value) */
	public static char PROPERTY_SEPARATOR = Textable.EQUAL;
	
}

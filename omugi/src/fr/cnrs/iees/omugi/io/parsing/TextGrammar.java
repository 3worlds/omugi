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
package fr.cnrs.iees.omugi.io.parsing;

import fr.cnrs.iees.omhtk.SaveableAsText;

/**
 * This static class defines the conventions used to save graph elements as text. 
 * For example, which delimiter for which kind of token, etc.
 * <p>CAUTION: change in this file will corrupt ALL SAVED configuration files.</p>
 * 
 * @author gignoux
 *
 */
public class TextGrammar {
	private TextGrammar() {};

	// Tables
	
	/** delimiter for dimensions */
	public final static char[] DIM_BLOCK_DELIMITERS = SaveableAsText.SQUARE_BRACKETS;
	/** separator for dimensions */
	public final static char DIM_ITEM_SEPARATOR = SaveableAsText.COMMA;
	/** delimiter for tables */
	public final static char[] TABLE_BLOCK_DELIMITERS = SaveableAsText.BRACKETS;
	/** separator for table items */
	public final static char TABLE_ITEM_SEPARATOR = SaveableAsText.COMMA;

	// Property lists
	
	/** delimiter for property lists */
	public final static char[] PROPERTY_LIST_DELIMITERS = SaveableAsText.BRACES;
	/** separator for property lists */
	public final static char PROPERTY_LIST_SEPARATOR = SaveableAsText.BLANK;
	
	// Properties
	
	/** separator for property (between key and value) */
	public final static char PROPERTY_SEPARATOR = SaveableAsText.EQUAL;
	    
}

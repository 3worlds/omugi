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
package fr.cnrs.iees.omugi.io.parsing;

import fr.cnrs.iees.omugi.graph.NodeSet;

/**
 * An interface for parsers transforming a list of tokens built by a {@link Tokenizer} into
 * a graph.
 * 
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
public abstract class Parser {
	
	protected abstract void parse();
	
	/**
	 * Processes the list of tokens and builds the graph. Lazy method, i.e. will do the parsing
	 * the first time it is called, and then will always return the same graph.
	 * 
	 * @return the graph
	 */
	public abstract NodeSet<?> graph();

	/**
	 * A specific method for use with files referred to in 'import' statements of other files.
	 * 
	 * @param factory the factory of the outer graph
	 */
	public abstract void setFactory(Object factory);

}

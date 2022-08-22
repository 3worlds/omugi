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
package fr.cnrs.iees;

import fr.ens.biologie.generic.Textable;

/**
 * An {@code Exception} for errors occurring in this library.
 * 
 * 
 * @author shayne.flint@anu.edu.au
 *
 * 
 * 
 */
//NB: this was previously AotException
//Policy is to make an exception at least for each library
//The general advice for exceptions is to throw early and catch late.
public class OmugiException extends RuntimeException {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 4121451020638650287L;

	/**
	 * Instantiate an exception with a {@link Textable} item and message
	 * 
	 * @param item    A {@link Textable} item.
	 * @param message Message string.
	 */
	public OmugiException(Textable item, String message) {
		super("[on " + item + "]\n[" + message + "]");
	}

	/**
	 * Instantiate an exception with a message
	 * 
	 * @param message the error message
	 */
	public OmugiException(String message) {
		super("[" + message + "]");
	}

	/**
	 * Exception wrapper.
	 * 
	 * @param e the exception to wrap
	 */
	public OmugiException(Exception e) {
		super(e);
	}

	/**
	 * Exception wrapper with additional information
	 * 
	 * @param message the error message
	 * @param e       the exception to wrap
	 */

	public OmugiException(String message, Exception e) {
		super("[" + message + "]\n[original exception: " + e + "]");
		e.printStackTrace();
	}

}

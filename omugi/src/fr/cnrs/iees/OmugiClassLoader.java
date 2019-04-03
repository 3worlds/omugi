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

/**
 * Author Ian Davies
 * 
 * NOTE: it is important that when calling class.forName(name,initialize,loader), the 
 * 'initialize' argument is set to true (it means the class will be initialized if not yet
 * done). Otherwise, no matter if the classLoader knows about the class, it will not find it
 * and return an error.
 *
 * Date 16 Feb. 2019
 */
public class OmugiClassLoader {
	// JG - 2/4/2019 If this is static, it is set at compile time ? then it's going to be wrong
//	private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	
//	private static ClassLoader classLoader =ClassLoader.getSystemClassLoader();
	public static ClassLoader getClassLoader() {
//		return classLoader;
		return Thread.currentThread().getContextClassLoader();
	}

}

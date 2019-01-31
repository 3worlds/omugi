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
package fr.cnrs.iees.io.parsing.impl;

import fr.ens.biologie.generic.SaveableAsText;

/**
 * Token types used in node references. Based on the former IoConstants interface 
 * in package au.edu.anu.rscs.aot.graph.io.
 * 
 * @author Jacques Gignoux - 18 déc. 2018
 *
 */
public enum ReferenceTokens {

	// token type	prefix						content type	suffix
	NODE_REF		('\0',							"token",	SaveableAsText.SLASH),
	NODE_LABEL		('\0',							"String",	SaveableAsText.COLON),
	NODE_NAME		(SaveableAsText.COLON,			"String",	'\0'),
	PROPERTY_NAME	(SaveableAsText.PLUS,			"String",	SaveableAsText.EQUAL),
	PROPERTY_VALUE	(SaveableAsText.EQUAL,			"Object",	'\0'),
	;
	
	private final char prefix;
	private final String type;
	private final char suffix;
	
	private ReferenceTokens(char prefix, String type, char suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.type = type;
	}
	
	public String tokenType() {
		return type;
	}
	
	public String suffix() {
		return String.valueOf(suffix);
	}
	
	public String prefix() {
		return String.valueOf(prefix);
	}

}

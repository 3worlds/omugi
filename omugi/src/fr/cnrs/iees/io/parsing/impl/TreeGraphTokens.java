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

import java.util.EnumSet;

import fr.ens.biologie.generic.SaveableAsText;

/**
 * Token types used in graph and tree text files
 * 
 * @author Jacques Gignoux - 21 janv. 2019
 *
 */
public enum TreeGraphTokens {
	
	// token type	prefix		content type	suffix
	COMMENT			("//",		"String",		"eol"),
	PROPERTY_NAME	("",		"String",		String.valueOf(SaveableAsText.EQUAL)),
	PROPERTY_VALUE	(String.valueOf(SaveableAsText.BRACKETS[SaveableAsText.BLOCK_OPEN]),
								"any",			String.valueOf(SaveableAsText.BRACKETS[SaveableAsText.BLOCK_CLOSE])),
	PROPERTY_TYPE	(String.valueOf(SaveableAsText.EQUAL),
								"java",			String.valueOf(SaveableAsText.BRACKETS[SaveableAsText.BLOCK_OPEN])),
	LABEL			("",		"String",		String.valueOf(SaveableAsText.BLANK)),
	NAME			("",		"String",		""),
	NODE_REF		(String.valueOf(SaveableAsText.SQUARE_BRACKETS[SaveableAsText.BLOCK_OPEN]),
								"String",		String.valueOf(SaveableAsText.SQUARE_BRACKETS[SaveableAsText.BLOCK_CLOSE])),
	LEVEL			("\t", 		"Integer",		""),
	
	IMPORT_RESOURCE	("importResource", "String","eol"),
	IMPORT_FILE     ("importFile","String","eol")
	/*
	 * old tokens were: import <file type(dsl|etc)> <"fromFile|fromResource> :
	 * <filename> import dsl
	 * 
	 * There is no longer any need to use the ext as a surrogate for type.
	 * Therefore, we just need to know if its a resource or file and the location 
	 * i.e.:
	 * importResource  au.edu.crap.3WorldsArchetype.ugt
	 * importFile  ian/something/3WorldsArchetype.ugt
	 * 
	 * 
	 */
	
	

	;
	private final String prefix;
	private final String type;
	private final String suffix;

	private TreeGraphTokens(String prefix, String type, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.type = type;
	}
	
	public String tokenType() {
		return type;
	}

	public String suffix() {
		return suffix;
	}

	public String prefix() {
		return prefix;
	}

	/**
	 * returns 
	 * @return the list of valid tokens for trees
	 */
	public static EnumSet<TreeGraphTokens> treeTokens() {
		return EnumSet.of(COMMENT,PROPERTY_NAME,PROPERTY_VALUE,PROPERTY_TYPE,LABEL,NAME,LEVEL,IMPORT_RESOURCE,IMPORT_FILE);
	}
	
	/**
	 * returns 
	 * @return the list of valid tokens for graphs
	 */
	public static EnumSet<TreeGraphTokens> graphTokens() {
		return EnumSet.of(COMMENT,PROPERTY_NAME,PROPERTY_VALUE,PROPERTY_TYPE,LABEL,NAME,NODE_REF,IMPORT_RESOURCE,IMPORT_FILE);
	}

}

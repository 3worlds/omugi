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

import static fr.cnrs.iees.io.parsing.impl.TreeGraphTokens.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.io.parsing.FileTokenizer;
import fr.cnrs.iees.io.parsing.LineTokenizer;
import fr.cnrs.iees.io.parsing.impl.GraphTokenizer.graphToken;

/**
 * A crude tokenizer for trees
 * @author Jacques Gignoux - 20 déc. 2018
 *
 */
// Tested OK with version 0.0.4 on 20/12/2018
public class TreeTokenizer extends LineTokenizer {
	
	private Logger log = Logger.getLogger(TreeTokenizer.class.getName());
			
	//----------------------------------------------------
	public class treeToken extends token {
		public int level;
		
		public treeToken(TreeGraphTokens type, String value, int level) {
			super(type,value);
			if (!TreeGraphTokens.treeTokens().contains(type))
				throw new OmugiException("Error: "+type+" is not a valid tree token type");
			this.level = level;
		}
		@Override
		public String toString() {
			return level+" "+type+":"+value;
		}

	}
	//----------------------------------------------------
	
	private List<treeToken> tokenlist = new ArrayList<>(1000);
	private treeToken cttoken = null;
	private int tokenIndex = -1;
	private int ctDepth = 0;
	private int maxDepth = 0;

	public TreeTokenizer(FileTokenizer parent) {
		super(parent);
	}

	public TreeTokenizer(String[] lines) {
		super(lines);
	}
	
	public boolean hasNext() {
		if (tokenlist.size()>0)
			if (tokenIndex<tokenlist.size())
				return true;
		return false;
	}
	
	public treeToken getNextToken() {
		treeToken result = null;
		if (tokenIndex<tokenlist.size()) {
			if (tokenIndex == -1)
				tokenIndex = 0;
			result = tokenlist.get(tokenIndex);
			tokenIndex ++;
		}
		else result = null;
		return result;
	}
	
	private void processLine(String line) {
		String[] words = line.split(COMMENT.prefix());
		if (words.length>1) { // a comment was found
			// process the beginning of the line
			processLine(words[0]);
			// end of the line is comment text that may include more '//'s
			cttoken = new treeToken(COMMENT,"",0);
			for (int i=1; i<words.length; i++)
				cttoken.value += words[i];
			tokenlist.add(cttoken);
			cttoken = null;
			return;
		}
		// analyse indentation
		int indentLevel=0;
		if (line.trim().length()>0)
			if (line.startsWith(LEVEL.prefix())) {
				char indentChar = LEVEL.prefix().charAt(0); // means '\t'
				char c = line.charAt(0);
				while ((c==indentChar) & (indentLevel<line.length()-1)) {
					indentLevel++;
					c = line.charAt(indentLevel);
				}
				ctDepth = indentLevel;
				maxDepth = Math.max(maxDepth, ctDepth);
		}
		// get other tokens - remember: no edges in this format
		words = line.trim().split(PROPERTY_NAME.suffix());
		if (words.length>1)  { // a property name was found
			if (words.length==2) {
				tokenlist.add(new treeToken(PROPERTY_NAME,words[0].trim(),ctDepth));
				processLine(words[1]);
				return;
			}
			else
				log.severe("malformed property format");
		}
		words = line.trim().split("\\(");
		if (words.length>1)  { // a property type (and value) was found (but it may contain more '(')
			if (line.trim().endsWith(PROPERTY_VALUE.suffix())) {
				tokenlist.add(new treeToken(PROPERTY_TYPE,words[0].trim(),ctDepth));
				String s = line.trim().substring(line.trim().indexOf('(')+1, line.trim().length()-1);
//				tokenlist.add(new token(PROPERTY_VALUE,words[1].substring(0,words[1].indexOf(PROPERTY_VALUE.suffix())).trim()));
				tokenlist.add(new treeToken(PROPERTY_VALUE,s,ctDepth));
				return;
			}
			else
				log.severe("malformed property format");
		}
		words = line.trim().split("\\s"); // matches any whitespace character
		if (words.length>1) { 
			tokenlist.add(new treeToken(LABEL,words[0].trim(),ctDepth)); // first word is label
			cttoken = new treeToken(NAME,"",ctDepth);
			for (int i=1; i<words.length; i++) { // anything else is name
				cttoken.value += words[i]+" ";
			}
			cttoken.value = cttoken.value.trim();
			tokenlist.add(cttoken);
			return;
		}		
		else if (words.length==1) // there is only one label in this case
			if (!words[0].trim().isEmpty()) 
				if (!words[0].trim().equals("tree")) 		// I hate this - it's a flaw !
					if (!words[0].trim().equals("aot"))	{ 	// I hate this - it's a flaw !
			tokenlist.add(new treeToken(LABEL,words[0].trim(),ctDepth)); 
			tokenlist.add(new treeToken(NAME,"",ctDepth));
			return;
		}
	}
	
	public int maxDepth() {
		return maxDepth;
	}
	
	@Override
	public void tokenize() {
		if (lines!=null)
			for (String line:lines) {
				processLine(line);
			}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (treeToken t:tokenlist)
			sb.append(t.toString()).append('\n');
		return sb.toString();
	}

}

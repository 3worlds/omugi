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
package fr.cnrs.iees.io.parsing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import fr.cnrs.iees.io.parsing.impl.TreeGraphTokens;
import fr.ens.biologie.generic.utils.Logging;

import static fr.cnrs.iees.io.parsing.impl.TreeGraphTokens.*;

/**
 * An abstract ancestor for tokenizers based on multi-line text. This class assumes that
 * the token come in a list of Strings (lines), and that a token is always fully contained
 * in a single line String.
 * 
 * @author Jacques Gignoux - 7 déc. 2018
 *
 */
public abstract class LineTokenizer implements Tokenizer {
	
	private static Logger log = Logging.getLogger(LineTokenizer.class);
	//----------------------------------------------------
	public class token {
		public TreeGraphTokens type;
		public String value;
		
		public token(TreeGraphTokens type, String value) {
			super();
			this.type = type;
			this.value = value;
		}
		
		@Override
		public String toString() {
			return type+":"+value;
		}
	}
	//----------------------------------------------------
	
	protected List<String> lines;
	protected List<token> tokenlist = new ArrayList<>(1000);
	
	// This array lists the words that are placed at the top of a file according
	// to the graph type.
	private static String[] fileHeaders = {"graph","tree","treegraph","aot"};
	// this for expanding this list in descendants
	protected Set<String> fheaders = new HashSet<String>();
		
	public LineTokenizer(FileTokenizer parent) {
		super();
		lines = parent.lines();
		for (String s:fileHeaders)
			fheaders.add(s);
	}
	
	protected boolean isFileHeader(String s) {
		return fheaders.contains(s);
	}
	
	protected LineTokenizer(List<String> lines) {
		super();
		this.lines = lines;
		for (String s:fileHeaders)
			fheaders.add(s);
	}
	
	// for debugging only
	protected LineTokenizer(String[] lines) {
		super();
		this.lines = new ArrayList<>(lines.length);
		for (int i=0; i<lines.length; i++)
			this.lines.add(lines[i]);
		for (String s:fileHeaders)
			fheaders.add(s);
	}
	
	// NB: must be overriden in descendants
	protected token makeToken(TreeGraphTokens type, String value) {
		return new token(type,value);
	}
	
	private String[] getToken(String suffix, String line) {
		String[] result = new String[1];
		result[0] = line; // default behaviour: do nothing
		if (line.contains(suffix)) {
			int suf = line.indexOf(suffix);
			result = new String[3];
			result[0] = "";
			result[1] = line.substring(0,suf);
			result[2] = line.substring(suf+suffix.length());
		}
		return result;
	}
	
	private String[] getToken(String prefix, String suffix, String line) {
		String[] result = new String[1];
		result[0] = line; // default behaviour: do nothing
		if (line.contains(prefix))
			if (line.contains(suffix)) {
				int pref = line.indexOf(prefix);
				int suf = line.indexOf(suffix);
				if (pref<suf) {
					result = new String[3];
					result[0] = line.substring(0,pref);
					result[1] = line.substring(pref+prefix.length(),suf);
					result[2] = line.substring(suf+suffix.length());
				}
			}
		return result;
	}
	
	private String unquote(String s) {
		String ss = s.trim();
		int pref = STRING.prefix().length();
		int suf = STRING.suffix().length();
		if (ss.startsWith(STRING.prefix()) && ss.endsWith(STRING.suffix())) {
			return ss.substring(pref,ss.length()-suf);
		}
		else return ss;
	}

//	// extract quoted strings as whole words
//	private String[] getWord(String textLine) {
//		List<String> result = new ArrayList<>();
//		if (textLine.indexOf(STRING.prefix())<0)
//			result.add(textLine);
//		else {
//			String endString = textLine;
//			String s = "";
//			while (endString.indexOf(STRING.prefix())>=0) {
//				// get opening quote
//				int cutoff = endString.indexOf(STRING.prefix());
//				s = endString.substring(0,cutoff);
//				// this is the string before the opening quote
//				result.add(s);
//				endString = endString.substring(cutoff+1);
//				// get closing quote
//				cutoff = endString.indexOf(STRING.suffix());
//				if (cutoff>-1) { // in case closing quote is missing
//					s = endString.substring(0,cutoff);
//					result.add(s);
//					endString = endString.substring(cutoff+1);
//				}
//			}
//			result.add(endString);
//		}
//		String[] a = new String[result.size()];
//		return result.toArray(a);
//	}

	/**
	 * extracts from a line a property name, type, and value, and create the corresponding tokens
	 * 
	 * @param propertyLine
	 */
	protected boolean tokenizeProperty(String propertyLine) {
		String[] result = getToken(PROPERTY_NAME.suffix(),propertyLine);
		if (result.length==3) {
			tokenlist.add(makeToken(PROPERTY_NAME,result[1].trim().replace("\"","")));
			result = getToken(PROPERTY_TYPE.prefix(),PROPERTY_TYPE.suffix(),propertyLine);
			tokenlist.add(makeToken(PROPERTY_TYPE,result[1].trim().replace("\"","")));
			if (result[2].trim().endsWith(PROPERTY_VALUE.suffix()))
				tokenlist.add(makeToken(PROPERTY_VALUE,unquote(result[2].substring(0,result[2].trim().length()-1))));
			else { // there must be a comment after the last ')'
				result = getToken(COMMENT.prefix(),result[2].trim());
				if (result.length==3) {
					tokenlist.add(makeToken(PROPERTY_VALUE,unquote(result[1].trim())));
//					tokenlist.add(makeToken(COMMENT,result[2])); // actually we dont care about comments
				}
				else
					log.severe("Error processing line \""+propertyLine+"\"");
			}
			return true;
		}
		return false;
	}
	
	/**
	 * returns true if a line contains an '=' sign not which is not contained within a String
	 * (roughly).
	 * @param line
	 * @return
	 */
	protected boolean isPropertyLine(String line) {
		if (line.contains(PROPERTY_NAME.suffix())) {
			if (line.contains(STRING.prefix()))
				return (line.indexOf(PROPERTY_NAME.suffix())<line.indexOf(STRING.prefix()));
			else
				return true;
		}
		return false;
	}
	
	protected boolean isEdgeLine(String line) {
		return (line.trim().startsWith(NODE_REF.prefix()));
	}
	
	protected boolean tokenizeEdge(String edgeLine) {
		String[] result = getToken(NODE_REF.prefix(),NODE_REF.suffix(),edgeLine);
		if (result.length==3) {
			tokenlist.add(makeToken(NODE_REF,result[1].trim().replace("\"","")));			
			String[] res = getToken(NODE_REF.prefix(),NODE_REF.suffix(),result[2]);
			tokenizeNode(res[0]);
			tokenlist.add(makeToken(NODE_REF,res[1].trim().replace("\"","")));
			// anything else is comment: ignore it.
			return true;
		}
		return false;
	}
	
	protected boolean tokenizeNode(String nodeLine) {		
		// Since '//' are not allowed in labels or names, there shouldnt be any in a String
		// so we look for a comment first
		String[] result = getToken(COMMENT.prefix(),nodeLine);
		if (result.length==3)
			nodeLine = nodeLine.substring(0,nodeLine.indexOf(COMMENT.prefix()));
		// label must not be quoted
		result = getToken(LABEL.suffix(),nodeLine.trim());
		if (result.length==3) {
			if (result[0].equals(IMPORT_RESOURCE.prefix()))
				tokenlist.add(makeToken(IMPORT_RESOURCE, result[1].trim().replace("\"","")));
			else if (result[0].equals(IMPORT_FILE.prefix()))
				tokenlist.add(makeToken(IMPORT_FILE, result[1].trim().replace("\"","")));
			else {
				tokenlist.add(makeToken(LABEL,result[1].trim().replace("\"","")));
				tokenlist.add(makeToken(NAME,result[2].trim().replace("\"","")));
			}
			return true;
		}	
		else {
			if (!result[0].trim().isEmpty())
				if (!isFileHeader(result[0].trim())) { // remove file header from the possible labels
					tokenlist.add(makeToken(LABEL,result[0].trim().replace("\"","")));
					tokenlist.add(makeToken(NAME,""));
					return true;
			}
		}
		return false;
	}
	
}

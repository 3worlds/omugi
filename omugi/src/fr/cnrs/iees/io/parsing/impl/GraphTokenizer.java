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

import static fr.cnrs.iees.io.parsing.impl.GraphTokens.*;

import java.util.ArrayList;
import java.util.List;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.io.parsing.FileTokenizer;
import fr.cnrs.iees.io.parsing.LineTokenizer;

/**
 * <p>A crude tokenizer for graphs.</p>
 * <p>It assumes the following text file syntax to describe graphs:</p>
 * <pre>
 * graph = headline {line}
 * headline = "graph" [comment] NEWLINE
 * comment = "\\ [TEXT]"
 * line = [{property|node|edge}] [comment] NEWLINE
 * node = node_label node_name
 * node_label = WORD
 * node_name = TEXT
 * property = prop_name "=" prop_type "(" prop_value ")"
 * prop_name = TEXT 
 * prop_type = JAVACLASS
 * prop_type = LOADABLETEXT
 * edge = "[" node_id "]" edge_label edge_name "[" node_id "]" 
 * edge_label = WORD
 * edge_name = TEXT
 * node_id = node_label ":" node_name
 * </pre> 
 * <p>where:</p>
 * <ul>
 * <li>{@code NEWLINE} = the end-of-line character</li>
 * <li>{@code TEXT} = any text (including white space)</li>
 * <li>{@code WORD} = any text with no white space</li>
 * <li>{@code JAVACLASS} = any java class that has a static valueOf(...) method</li>
 * <li>{@code LOADABLETEXT} = any text compatible with the matching valueOf(...) method to instantiate the class</li>
 * </ul>
 * <p>Properties are attached to the item (graph, node or edge) after which they appear.</p>
 * <p>The label:name pair is assumed to uniquely represent a node or edge in the graph. 
 * Depending on implementation, they might represent a class name and instance unique id,
 * or a real label and name.</p>
 * <p>Little example of a valid graph text file:</p>
 * <pre>=====================
graph // this is a comment

//this is another comment

label1 name1
  prop1=Integer(1)
	prop2 =Double(2.0)
prop3= String("blabla")
		prop4 = Boolean(true)

label2 name2
label1 name3

[label1:name1] label4 name1 [label2:name2]
	[ label1:name1] label4 name2	 [label2:name2 ]
[ label2:name2 ] label4 name1   [label1:name3]
label2 name5
prop1 = Integer(0)
=====================</pre>
 * <p>Notice that white space (blanks, tabs) and empty lines are ignored.</p>
 * 
 * @author Jacques Gignoux - 7 déc. 2018
 *
 */
// tested OK with version 0.0.1 on 14/12/2018
public class GraphTokenizer extends LineTokenizer {
	//----------------------------------------------------
	protected class token {
		protected GraphTokens type;
		protected String value;
		
		protected token(GraphTokens type, String value) {
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
	
	private List<token> tokenlist = new ArrayList<token>(1000);
	private token cttoken = null;
	private int tokenIndex = -1;
	
	public GraphTokenizer(FileTokenizer parent) {
		super(parent);
	}
	
	protected GraphTokenizer(String[] lines) {
		super(lines);
	}
	
	public boolean hasNext() {
		if ((tokenIndex==-1)&&(tokenlist.size()>0))
			return true;
		else if (tokenIndex<tokenlist.size())
			return true;
		return false;
	}
	
	public token getNextToken() {
		token result = null;
		if (tokenIndex<tokenlist.size()) {
			if (tokenIndex == -1)
				tokenIndex = 0;
			result = tokenlist.get(tokenIndex);
			tokenIndex ++;
		}
		else result = null;
		return result;
	}
	
	private String[] trimEmptyWords(String[] list) {
		ArrayList<String> result = new ArrayList<>();
		for (int i=0; i<list.length; i++)
			if (list[i].length()>0)
				result.add(list[i]);
		String[] res = new String[result.size()];
		return result.toArray(res);
	}
	
	private void processLine(String line) {
		String[] words = line.trim().split(COMMENT.prefix());
		if (words.length>1) { // a comment was found
			// process the beginning of the line
			processLine(words[0].trim());
			// end of the line is comment text that may include more '//'s
			cttoken = new token(COMMENT,"");
			for (int i=1; i<words.length; i++)
				cttoken.value += words[i];
			tokenlist.add(cttoken);
			cttoken = null;
			return;
		}
		words = line.trim().split(PROPERTY_NAME.suffix());
		if (words.length>1)  { // a property name was found
			if (words.length==2) {
				tokenlist.add(new token(PROPERTY_NAME,words[0].trim()));
				processLine(words[1]);
				return;
			}
			else
				throw new OmugiException("GraphTokenizer: malformed property format");
		}
		words = line.trim().split("\\(");
		if (words.length>1)  { // a property type (and value) was found (but it may contain more '(')
			if (line.trim().endsWith(PROPERTY_VALUE.suffix())) {
				tokenlist.add(new token(PROPERTY_TYPE,words[0].trim()));
				String s = line.trim().substring(line.trim().indexOf('(')+1, line.trim().length()-1);
//				tokenlist.add(new token(PROPERTY_VALUE,words[1].substring(0,words[1].indexOf(PROPERTY_VALUE.suffix())).trim()));
				tokenlist.add(new token(PROPERTY_VALUE,s));
				return;
			}
			else
				throw new OmugiException("GraphTokenizer: malformed property format");
		}
		words = line.trim().split("[\\[\\]]"); // TODO: generate regular expression from GraphTokens.
		if (words.length>1) { // an edge definition was found
			words = trimEmptyWords(words);
			if (words.length==3) {
				tokenlist.add(new token(NODE_REF,words[0].trim()));
				processLine(words[1]);
				tokenlist.add(new token(NODE_REF,words[2].trim()));
				return;
			}
			else
				throw new OmugiException("GraphTokenizer: malformed edge format");
		}
		words = line.trim().split("\\s"); // matches any whitespace character
		if (words.length>1) { 
			tokenlist.add(new token(LABEL,words[0].trim())); // first word is label
			cttoken = new token(NAME,"");
			for (int i=1; i<words.length; i++) { // anything else is name
				cttoken.value += words[i];
			}
			tokenlist.add(cttoken);
			return;
		}		
	}

	@Override
	public void tokenize() {
		if (lines!=null)
			for (String line:lines) {
				processLine(line);
			}
	}
	
	@Override
	public boolean tokenized() {
		return !tokenlist.isEmpty();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (token t:tokenlist)
			sb.append(t.toString()).append('\n');
		return sb.toString();
	}

}

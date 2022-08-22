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

import java.util.List;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.io.parsing.PreTokenizer;
import fr.cnrs.iees.io.parsing.LineTokenizer;

/**
 * <p>A crude tokenizer for graphs.</p>
 * <p>It assumes the following text file syntax to describe graphs(<strong><em>GOMUGI</em></strong> 
 * data format in {@link fr.cnrs.iees.io.GraphFileFormats GraphFileFormats}):</p>
 * <pre>
 * graph = headline {line}
 * headline = "graph" [comment] NEWLINE
 * comment = "// [TEXT]"
 * line = [{property|node|edge}] [comment] NEWLINE
 * node = node_label [node_name]
 * node_label = WORD
 * node_name = TEXT
 * property = prop_name "=" prop_type "(" prop_value ")"
 * prop_name = TEXT 
 * prop_type = JAVACLASS
 * prop_value = LOADABLETEXT
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
// tested OK with version 0.2.16 on 1/7/2020
public class GraphTokenizer extends LineTokenizer {
	//----------------------------------------------------
		/**
		 * @author Jacques Gignoux - 7 déc. 2018
		 */
		public class graphToken extends token {
		
		/**
		 * @param type The {@link TreeGraphTokens} type to add.
		 * @param value The token value.
		 */
		public graphToken(TreeGraphTokens type, String value) {
			super(type,value);
			if (!TreeGraphTokens.graphTokens().contains(type))
				throw new OmugiException("Error: "+type+" is not a valid graph token type");
		}
	}
	//----------------------------------------------------
	
	private int tokenIndex = -1;
	
	/**
	 * Constructor from a {@link PreTokenizer}
	 * 
	 * @param parent the pretokenizer
	 */
	public GraphTokenizer(PreTokenizer parent) {
		super(parent);
	}
	
	/**
	 * Constructor from an array of text lines
	 * 
	 * @param lines the text lines to tokenize
	 */
	public GraphTokenizer(String[] lines) {
		super(lines);
	}
	
	/**
	 * Constructor from a list of text lines
	 * 
	 * @param lines the text lines to tokenize
	 */
	public GraphTokenizer(List<String> lines) {
		super(lines);
	}
	
	protected token makeToken(TreeGraphTokens type, String value) {
		return new graphToken(type,value);
	}

	/**
	 * Checks if the token list is empty.
	 * 
	 * @return true if the list still contains tokens
	 */
	public boolean hasNext() {
		if (tokenlist.size()>0)
			if (tokenIndex<tokenlist.size())
				return true;
		return false;
	}
	
	/**
	 * Gets the tokens from the list
	 * 
	 * @return the next token
	 */
	public graphToken getNextToken() {
		graphToken result = null;
		if (tokenIndex<tokenlist.size()) {
			if (tokenIndex == -1)
				tokenIndex = 0;
			result = (graphToken) tokenlist.get(tokenIndex);
			tokenIndex ++;
		}
		else result = null;
		return result;
	}
	
	private void processLine(String line) {
		if (isPropertyLine(line))
			tokenizeProperty(line);
		else if (isEdgeLine(line))
			tokenizeEdge(line);
		else
			tokenizeNode(line);
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
			if (t.type==STRING)
				sb.append('[').append(t.toString()).append(']');
			else
				sb.append(t.toString()).append('\n');
		return sb.toString();
	}

}

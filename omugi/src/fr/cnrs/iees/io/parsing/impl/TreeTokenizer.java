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
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.io.parsing.PreTokenizer;
import fr.cnrs.iees.io.parsing.LineTokenizer;

/**
 * <p>
 * A crude tokenizer for trees.
 * </p>
 * <p>
 * It assumes the following text file syntax to describe trees (<strong><em>TOMUGI</em></strong> 
 * data format in {@link fr.cnrs.iees.io.GraphFileFormats GraphFileFormats}):
 * </p>
 * 
 * <pre>
tree = headline {line}
headline = "tree" [comment] NEWLINE
comment = "// [TEXT]"
line = [{INDENT}] [{node|property}] [comment] NEWLINE
node = node_label [node_name]
node_label = WORD
node_name = TEXT
property = prop_name "=" prop_type "(" prop_value ")"
prop_name = TEXT 
prop_type = JAVACLASS
prop_value = LOADABLETEXT
 * </pre>
 * <p>
 * where:
 * </p>
 * <ul>
 * <li>{@code NEWLINE} = the end-of-line character</li>
 * <li>{@code TEXT} = any text (including white space)</li>
 * <li>{@code INDENT} = indentation using 0..* tab characters</li>
 * <li>{@code WORD} = any text with no white space</li>
 * <li>{@code JAVACLASS} = any java class that has a static valueOf(...)
 * method</li>
 * <li>{@code LOADABLETEXT} = any text compatible with the matching valueOf(...)
 * method to instantiate the class</li>
 * </ul>			

 * <p>
 * Indentation gives the level of a node in the tree hierarchy: the root node
 * must have zero indentation; increase of indentation of one tab indicates the
 * node is a child of the immediate previous node with one less tab, eg:
 * </p>
 * 
 * <pre>
node A
	node B
		node C
	node D
 * </pre>
 * <p>
 * means that A is the root node, B is a child of A, C is a child of B, and D is
 * a child of A, sibling of B as it has the same indentation level.
 * </p>
 * <p>
 * The same rule applies to properties: they must be indented 1 level more than
 * their owner node.
 * </p>
 * <p>
 * The label:name pair is assumed to uniquely represent a node in the graph.
 * Depending on implementation, they might represent a class name and instance
 * unique id, or a real label and name.
 * </p>
 * 
 * <p>
 * Little example of a valid tree text file:
 * </p>
 * 			

 * <pre>
=====================
tree // this is a STUPID comment 

// This is a VERY STUPID comment 
		
label1 node1 
	prop1 = Integer(3) 
	prop2 = Double(4.2) 
	label2 node2
		label3 node3 
			prop4 = String("coucou") 
		label4
	label5 node5 
		table = au.edu.anu.rscs.aot.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false)) 
		 
	label6 node6 
		label7 node7 
			label8 node8 
				label9 node9 
		label10 node10
	// This is one more comment 
		label11 node11
			label12 node12 			

			truc=String("machin") 
				plop = Integer(12)
=====================
 * </pre>
 * <p>
 * Notice that, contrary to the format for graphs, leading tabs are used to
 * describe the hierarchy of nodes and are thus of utmost importance. Other
 * white space (empty lines, trailing white space) are ignored.
 * </p>
 * <p>
 * In the above example, the application of the indentation rule will attach
 * property {@code truc} to node {@code label11:node11} and property
 * {@code plop} to node {@code label12:node12}.
 * 
 * @author Jacques Gignoux - 20 déc. 2018
 *
 */
// Tested OK with version 0.0.4 on 20/12/2018
// Tested OK with version 0.2.16 on 1/7/2020
public class TreeTokenizer extends LineTokenizer {

	// ----------------------------------------------------
	public class treeToken extends token {
		public int level;

		public treeToken(TreeGraphTokens type, String value, int level) {
			super(type, value);
			if (!TreeGraphTokens.treeTokens().contains(type))
				throw new IllegalArgumentException("Error: " + type + " is not a valid tree token type");
			this.level = level;
		}

		@Override
		public String toString() {
			return level + " " + type + ":" + value;
		}

	}
	// ----------------------------------------------------

	private int tokenIndex = -1;
	private int ctDepth = 0;
	private int maxDepth = 0;

	/**
	 * Constructor from a {@link PreTokenizer}
	 * 
	 * @param parent the pretokenizer
	 */
	public TreeTokenizer(PreTokenizer parent) {
		super(parent);
	}

	/**
	 * Constructor from an array of text lines
	 * 
	 * @param lines the text lines to tokenize
	 */
	public TreeTokenizer(String[] lines) {
		super(lines);
	}

	/**
	 * Constructor from a list of text lines
	 * 
	 * @param lines the text lines to tokenize
	 */
	public TreeTokenizer(List<String> lines) {
		super(GraphImporter.preprocess( lines));
	}
	
	protected token makeToken(TreeGraphTokens type, String value) {
		return new treeToken(type,value,ctDepth);
	}

	/**
	 * Checks if the token list is empty.
	 * 
	 * @return true if the list still contains tokens
	 */
	public boolean hasNext() {
		if (tokenlist.size() > 0)
			if (tokenIndex < tokenlist.size())
				return true;
		return false;
	}

	/**
	 * Gets the tokens from the list
	 * 
	 * @return the next token
	 */
	public treeToken getNextToken() {
		treeToken result = null;
		if (tokenIndex < tokenlist.size()) {
			if (tokenIndex == -1)
				tokenIndex = 0;
			result = (treeToken) tokenlist.get(tokenIndex);
			tokenIndex++;
		} else
			result = null;
		return result;
	}

	private void processLine(String line) {
		// analyse indentation
		int indentLevel = 0;
		if (line.trim().length() > 0) {
			if (line.startsWith(LEVEL.prefix())) {
				char indentChar = LEVEL.prefix().charAt(0); // means '\t'
				char c = line.charAt(0);
				while ((c == indentChar) & (indentLevel < line.length() - 1)) {
					indentLevel++;
					c = line.charAt(indentLevel);
				}
				ctDepth = indentLevel;
				maxDepth = Math.max(maxDepth, ctDepth);
			}
			else // text starts at first column 
				ctDepth = 0;
		}
		
		if (isPropertyLine(line))
			tokenizeProperty(line);
		else if (isEdgeLine(line))
			tokenizeEdge(line);
		else
			tokenizeNode(line);
	}
	
	/**
	 * Getter for tree depth (= max chain length from root node).
	 * 
	 * @return the max tree depth
	 */
	public int maxDepth() {
		return maxDepth;
	}

	@Override
	public void tokenize() {
		if (lines != null)
			for (String line : lines) {
				processLine(line);
			}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (token t : tokenlist)
			sb.append(t.toString()).append('\n');
		return sb.toString();
	}

}
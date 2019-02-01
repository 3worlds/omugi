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

import fr.cnrs.iees.io.parsing.impl.TreeGraphTokens;

/**
 * An abstract ancestor for tokenizers based on multi-line text. This class assumes that
 * the token come in a list of Strings (lines), and that a token is always fully contained
 * in a single line String.
 * 
 * @author Jacques Gignoux - 7 déc. 2018
 *
 */
public abstract class LineTokenizer implements Tokenizer {
	
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
	
	// for debugging only
	protected LineTokenizer(String[] lines) {
		super();
		this.lines = new ArrayList<>(lines.length);
		for (int i=0; i<lines.length; i++)
			this.lines.add(lines[i]);
		for (String s:fileHeaders)
			fheaders.add(s);

	}
	
}

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

import static fr.cnrs.iees.io.parsing.impl.ReferenceTokens.*;

import java.util.ArrayList;
import java.util.List;

import fr.cnrs.iees.io.parsing.Tokenizer;

/**
 * <p>A tokenizer for string references to nodes in a tree.
 * The syntax for references is as follows:</p>
 * <pre>
 * reference = nodeReference {'/' nodeReference} 
 * nodeReference = [classId ':'][instanceId]{'+' key '=' value}
 * </pre>
 * <p>where:<br/>
 * {@code classId} is the node type (formerly known as <em>label</em>)<br/>
 * {@code instanceId} is the node id (formerly known as <em>name</em>)<br/>
 * {@code key} is an optional property name<br/>
 * {@code value} is a property value
 * </p>
 * <p>Since {@code classId} and {@code instanceId} are optional, a reference may match to
 * a set of nodes.</p>
 * 
 * @author Jacques Gignoux - 18 déc. 2018true
 *
 */
// Tested OK with version 0.0.3 on 18/12/2018 
public class ReferenceTokenizer implements Tokenizer {
	//----------------------------------------------------
	protected class token {
		protected ReferenceTokens type;
		protected String value;
		
		protected token(ReferenceTokens type, String value) {
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
	
	private String reference = null;
	private List<token> tokenlist = new ArrayList<token>(100);
	private int tokenIndex = -1;
	
	// constructor
	public ReferenceTokenizer(String ref) {
		super();
		reference = ref;
	}

	private void tokenizeProperties(String[] props) {
		for (int i=1; i<props.length; i++) {
			String[] ss = props[i].split(PROPERTY_VALUE.prefix());
			tokenlist.add(new token(PROPERTY_NAME,ss[0]));
			tokenlist.add(new token(PROPERTY_VALUE,ss[1]));
		}
	}
	
	@Override
	public void tokenize() {
		// split on '/'
		String[] words = reference.split(NODE_REF.suffix());
		for (String tokenString:words) {
			// split on ':'
			String[] tkwords = tokenString.split(NODE_LABEL.suffix());
			if (tokenString.contains(NODE_LABEL.suffix()))
				tokenlist.add(new token(NODE_LABEL,tkwords[0]));
			else
				tokenlist.add(new token(NODE_LABEL,""));
			if (tkwords.length>1) {
				// split on '+' (replaced by '\+' because of regexp)
				String[] propwords = tkwords[1].split("\\"+PROPERTY_NAME.prefix());
				tokenlist.add(new token(NODE_NAME,propwords[0]));
				tokenizeProperties(propwords);
			}
			else {
				tokenlist.add(new token(NODE_NAME,""));
				// split on '+' (replaced by '\+' because of regexp)
				String[] propwords = tokenString.split("\\"+PROPERTY_NAME.prefix());
				tokenizeProperties(propwords);
			}
		}
	}
	
	@Override
	public boolean tokenized() {
		return !tokenlist.isEmpty();
	}
	
	/**
	 * Gets the parser able to parse this tokenizer's token list
	 * 
	 * @return a new instance of the parser.
	 */
	public ReferenceParser parser() {
		return new ReferenceParser(this);
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (token t:tokenlist)
			sb.append(t.toString()).append('\n');
		return sb.toString();
	}

}

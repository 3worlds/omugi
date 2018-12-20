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

import static fr.cnrs.iees.io.parsing.TextGrammar.DIM_BLOCK_DELIMITERS;
import static fr.cnrs.iees.io.parsing.TextGrammar.DIM_ITEM_SEPARATOR;
import static fr.cnrs.iees.io.parsing.TextGrammar.TABLE_BLOCK_DELIMITERS;
import static fr.cnrs.iees.io.parsing.TextGrammar.TABLE_ITEM_SEPARATOR;

import java.util.LinkedList;
import java.util.List;

import au.edu.anu.rscs.aot.collections.tables.Table;
import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.io.parsing.Parser;
import fr.cnrs.iees.io.parsing.impl.ReferenceTokenizer.token;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.tree.TreeNode;
import fr.ens.biologie.generic.SaveableAsText;

public class ReferenceParser extends Parser {
	
	//----------------------------------------------------
	// what is needed for a property match
	private class propMatch {
		protected String name;
		protected String value;
		@Override // for debugging only
		public String toString() {
			return name+"="+value;
		}
	}
	//----------------------------------------------------
	// what is needed for a node match
	private class nodeMatch {
		protected String label;
		protected String name;
		protected List<propMatch> props = new LinkedList<propMatch>();
		@Override // for debugging only
		public String toString() {
			return label+":"+name;
		}
	}
	//----------------------------------------------------
	
	private ReferenceTokenizer tokenizer;
	private nodeMatch lastNodeMatch = null;
	private propMatch lastPropMatch = null;
	private List<nodeMatch> matches = new LinkedList<nodeMatch>();
	
	public ReferenceParser(ReferenceTokenizer tokenizer) {
		super();
		this.tokenizer = tokenizer;
	}

	@Override
	protected void parse() {
		if (!tokenizer.tokenized())
			tokenizer.tokenize();
		// this is stupidly straightforward...
		while (tokenizer.hasNext()) {
			token tk = tokenizer.getNextToken();
			switch (tk.type) {
				case NODE_REF:
					break;
				case NODE_LABEL:
					lastNodeMatch = new nodeMatch();
					lastNodeMatch.label = tk.value;
					break;
				case NODE_NAME:
					lastNodeMatch.name = tk.value;
					// Note: insertion in reverse order
					matches.add(0,lastNodeMatch);
					break;
				case PROPERTY_NAME:
					lastPropMatch = new propMatch();
					lastPropMatch.name = tk.value;
					break;
				case PROPERTY_VALUE:
					lastPropMatch.value = tk.value;
					lastNodeMatch.props.add(lastPropMatch);
					break;
			}
		}
	}
	
	public boolean matches(TreeNode node) {
		if (lastNodeMatch==null)
			parse();
		// since node matches are stored in reverse order, the first one applies to the
		// node passed as argument, and the following ones to its parents
		TreeNode testNode = node;
		for (nodeMatch nm:matches) {
			if (testNode==null) // only reached if there is no more parent while there is still a match to do
				return false;
			// match node label
			if (nm.label.length()>0) // no label in ref means any node matches
				if (!testNode.classId().equals(nm.label))
					return false;
			// match node name
			if (nm.name.length()>0) // no name in ref means any node matches
				if (!testNode.instanceId().equals(nm.name))
					return false;
			// match property names and values
			for (propMatch pm:nm.props) {
				// node has no properties
				if (!ReadOnlyPropertyList.class.isAssignableFrom(testNode.getClass()))
					return false;
				ReadOnlyPropertyList pl = (ReadOnlyPropertyList) testNode;
				// property is not found
				if (!pl.hasProperty(pm.name))
					return false;
				// match String property values
				if (String.class.isAssignableFrom(pl.getPropertyClass(pm.name))) {
					if (!pl.getPropertyValue(pm.name).equals(pm.value))
						return false;
				}
				// match Table property values
				else if (SaveableAsText.class.isAssignableFrom(pl.getPropertyClass(pm.name))) {
					SaveableAsText prop = (SaveableAsText) pl.getPropertyValue(pm.name);
					char[][] bdel = new char[2][2];
					bdel[Table.DIMix] = DIM_BLOCK_DELIMITERS;
					bdel[Table.TABLEix] = TABLE_BLOCK_DELIMITERS;
					char[] isep = new char[2];
					isep[Table.DIMix] = DIM_ITEM_SEPARATOR;
					isep[Table.TABLEix] = TABLE_ITEM_SEPARATOR;
					if (!prop.toSaveableString(bdel,isep).equals(pm.value))
						return false;
				}
				// match Primitive type property values
				else if (!pl.getPropertyValue(pm.name).toString().equals(pm.value))
					return false;
			}
			testNode = testNode.getParent();
		}
		return true;
	}
	
	// for debugging only
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Reference to match\n");
		for (nodeMatch nm: matches) {
			sb.append(nm.toString()).append('\n');
			for (propMatch pm:nm.props)
				sb.append('\t').append(pm.toString()).append('\n');
		}
		return sb.toString();
	}

}

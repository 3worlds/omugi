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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TreeTokenizerTest {

	String[] test = {"tree // this is a STUPID comment\n", 
			"\n",
			"// This is a VERY STUPID comment\n", 
			"\n",
			"label1 node1\n", 
			"	prop1 = Integer(3)\n", 
			"	prop2 = Double(4.2)\n", 
			"	label2 node2\n",
			"		label3 node3\n", 
			"			prop4 = String(\"coucou\")\n", 
			"	label5 node5\n", 
			"		table = au.edu.anu.rscs.aot.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))\n", 
			"\n", 
			"	label6 node6\n", 
			"		label7 node7\n", 
			"			label8 node8\n", 
			"				label9 node9\n", 
			"		label10 node10\n",
			"	// This is one more comment\n", 
			"		label11 node11\n",
			"			label12 node12\n", 
			"			truc=String(\"machin\")\n", 
			"				plop = Integer(12)"};

	@Test
	void testTokenize() {
		TreeTokenizer tk = new TreeTokenizer(test);
		tk.tokenize();
		System.out.println(tk.toString());
		assertEquals(tk.toString(),"0 COMMENT: this is a STUPID comment\n" + 
				"\n" + 
				"0 COMMENT: This is a VERY STUPID comment\n" + 
				"\n" + 
				"0 LABEL:label1\n" + 
				"0 NAME:node1\n" + 
				"1 PROPERTY_NAME:prop1\n" + 
				"1 PROPERTY_TYPE:Integer\n" + 
				"1 PROPERTY_VALUE:3\n" + 
				"1 PROPERTY_NAME:prop2\n" + 
				"1 PROPERTY_TYPE:Double\n" + 
				"1 PROPERTY_VALUE:4.2\n" + 
				"1 LABEL:label2\n" + 
				"1 NAME:node2\n" + 
				"2 LABEL:label3\n" + 
				"2 NAME:node3\n" + 
				"3 PROPERTY_NAME:prop4\n" + 
				"3 PROPERTY_TYPE:String\n" + 
				"3 PROPERTY_VALUE:coucou\n" + 
				"1 LABEL:label5\n" + 
				"1 NAME:node5\n" + 
				"2 PROPERTY_NAME:table\n" + 
				"2 PROPERTY_TYPE:au.edu.anu.rscs.aot.collections.tables.BooleanTable\n" + 
				"2 PROPERTY_VALUE:([3,2]false,false,false,false,false,false)\n" + 
				"1 LABEL:label6\n" + 
				"1 NAME:node6\n" + 
				"2 LABEL:label7\n" + 
				"2 NAME:node7\n" + 
				"3 LABEL:label8\n" + 
				"3 NAME:node8\n" + 
				"4 LABEL:label9\n" + 
				"4 NAME:node9\n" + 
				"2 LABEL:label10\n" + 
				"2 NAME:node10\n" + 
				"0 COMMENT: This is one more comment\n" + 
				"\n" + 
				"2 LABEL:label11\n" + 
				"2 NAME:node11\n" + 
				"3 LABEL:label12\n" + 
				"3 NAME:node12\n" + 
				"3 PROPERTY_NAME:truc\n" + 
				"3 PROPERTY_TYPE:String\n" + 
				"3 PROPERTY_VALUE:machin\n" + 
				"4 PROPERTY_NAME:plop\n" + 
				"4 PROPERTY_TYPE:Integer\n" + 
				"4 PROPERTY_VALUE:12\n");
	}

	String[] test2 = {"	//------- specific to createOtherDecision\n", 
			"	// do we want to keep a parent link between offspring and parent?\n", 
			"	hasProperty relateToFunctionKeepLinkPropertySpec\n",
			"		hasName = String(\"relateToProduct\")\n",
			"		type = String(\"Boolean\")\n",
			"		multiplicity = IntegerRange(\"0..1\")\n", 
			"	mustSatisfyQuery FunctionTypePropertySpecQuery\n", 
			"		className = String(\"au.edu.anu.twcore.archetype.tw.RequirePropertyQuery\")\n", 
			"		conditions = StringTable(([3]\"relateToProduct\",\"type\",\"CreateOtherDecision\"))\n" 
	};
	
	@Test
	void testTokenize2() {
		TreeTokenizer tk = new TreeTokenizer(test2);
		tk.tokenize();
		System.out.println(tk.toString());
//		assertEquals(tk.toString(),"0 COMMENT: this is a STUPID comment\n" );
	}
	
}

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

/**
 * 
 * @author Jacques Gignoux - 20 déc. 2018
 *
 */
class TreeParserTest {

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
			"		label4\n",
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
	void testParse() {
		TreeParser tp = new TreeParser(new TreeTokenizer(test));
		tp.parse();
		System.out.println(tp.toString());
		assertEquals(tp.toString(),"Tree specification\n" + 
				"label1:node1\n" + 
				"	prop1:Integer=3\n" + 
				"	prop2:Double=4.2\n" + 
				"	ROOT NODE\n" + 
				"label2:node2\n" + 
				"	parent label1:node1\n" + 
				"label3:node3\n" + 
				"	prop4:String=\"coucou\"\n" + 
				"	parent label2:node2\n" + 
				"label5:node5\n" + 
				"	table:au.edu.anu.rscs.aot.collections.tables.BooleanTable=([3,2]false,false,false,false,false,false)\n" + 
				"	parent label1:node1\n" + 
				"label6:node6\n" + 
				"	parent label1:node1\n" + 
				"label7:node7\n" + 
				"	parent label6:node6\n" + 
				"label8:node8\n" + 
				"	parent label7:node7\n" + 
				"label9:node9\n" + 
				"	parent label8:node8\n" + 
				"label10:node10\n" + 
				"	parent label6:node6\n" + 
				"label11:node11\n" + 
				"	truc:String=\"machin\"\n" + 
				"	parent label6:node6\n" + 
				"label12:node12\n" + 
				"	plop:Integer=12\n" + 
				"	parent label11:node11\n");
	}

	@Test
	void testTree() {
		TreeParser tp = new TreeParser(new TreeTokenizer(test));
		System.out.println(tp.graph().toString());
	}

}

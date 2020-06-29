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

import fr.cnrs.iees.graph.Graph;

class GraphParserTest {

	String[] test = {"graph // this is a comment\n",
			"\n",
			"//this is another comment\n", 
			"\n" ,
			"label1 name1\n" , 
			"  prop1=Integer(1)\n" , 
			"	prop2 =Double(2.0)\n" , 
			"prop3= String(\"blabla\")\n" , 
			"		prop4 = Boolean(true)\n" , 
			"\n" ,
			"label2 name2\n" , 
			"label1 name3\n" , 
			"\n" ,
			"[label1:name1] label4 \"name1\" [label2:name2]\n" , 
			"	[ label1:name1] label4 name2	 [label2:name2 ]\n" , 
			"[ label2:name2 ] label4 name1   [label1:name3]\n" ,
			"label2 name5\n" ,
			"prop1 = Integer(0)"};

	String[] testWithErrors = {"graph // this is a comment\n",
			"\n",
			"//this is another comment\n", 
			"\n" ,
			"label1 name1\n" , 
			"  prop1=Integer(1.0)\n" , 			// property value incompatible with type
			"	prop2 =Double(2.0)\n" , 
			"prop3= String(\"blabla\")\n" , 
			"		prop4 = Boolkean(true)\n" ,  // wrong property type
			"\n" ,
			" name2\n" ,  					// missing label here
			"label1 name3\n" , 
			"\n" ,
			"[label1:name1] label4 name1 [label2:name2]\n" , 
			"	[ label1:name11] label4 name2	 [label2:name2 ]\n" ,  // non existant start node
			"[ label2:name2 ] label4 name1   [label1:name7]\n" ,  // non existant end node 
			"label2 name5\n" ,
			"prop1 = Integer(0)"};

	String[] otherTest = {"graph // checking the new ids\n", 
			"  scope=String(\"otherTest\")",
			"  node=String(\"fr.cnrs.iees.graph.impl.ALNode\")\n",
			"  pnode=String(\"fr.cnrs.iees.graph.impl.ALDataNode\")\n",
			"  edge=String(\"fr.cnrs.iees.graph.impl.ALEdge\")\n", 
			"  mutable=Boolean(true)\n",
			"\n",
			"// nodes\n", 
			"pnode\n",
			"  prop1=Integer(1)\n", 
			"  prop2 =Double(2.0)\n", 
			"  prop3= String(\"blabla\")\n", 
			"  prop4 = Boolean(true)\n",
			"node name2\n",
			"node name3\n", 
			"\n",
			"// edges\n", 
			"[pnode:] edge name1 [node:name2]\n", 
			"[pnode:] edge name2 [node:name2 ]\n", 
			"[node:name2 ] edge [node:name3]\n", 
			"// more nodes\n",
			"pnode name5\n",
			"  prop1 = Integer(0)"};
	
	// testing for Edge properties
	String[] edgePropTest = {"graph // checking the new ids\n", 
			"  scope=String(\"edgePropTest\")",
			"  node=String(\"fr.cnrs.iees.graph.impl.ALNode\")\n",
			"  pnode=String(\"fr.cnrs.iees.graph.impl.ALDataNode\")\n",
			"  edge=String(\"fr.cnrs.iees.graph.impl.ALEdge\")\n",
			"  pedge=String(\"fr.cnrs.iees.graph.impl.ALDataEdge\")\n", 
			"  mutable=Boolean(true)\n",
			"\n",
			"// nodes\n", 
			"pnode\n",
			"  prop1=Integer(1)\n", 
			"  prop2 =Double(2.0)\n", 
			"  prop3= String(\"blabla\")\n", 
			"  prop4 = Boolean(true)\n",
			"node name2\n",
			"node name3\n", 
			"\n",
			"// edges\n", 
			"[pnode:] edge name1 [node:name2]\n", 
			"[pnode:] pedge name2 [node:name2 ]\n",
			"  propE=Integer(17)\n",
			"[node:name2 ] edge [node:name3]\n", 
			"// more nodes\n",
			"pnode name5\n",
			"  prop1 = Integer(0)"};

	
	@Test
	void testParse() {
		GraphParser gp = new GraphParser(new GraphTokenizer(test));
		gp.parse();
//		System.out.println(gp.toString());
		assertEquals(gp.toString(),"Graph specification\n" + 
				"Nodes:\n" + 
				"	label1:name1\n" + 
				"		prop1:Integer=1\n" + 
				"		prop2:Double=2.0\n" + 
				"		prop3:String=blabla\n" + 
				"		prop4:Boolean=true\n" + 
				"	label2:name2\n" + 
				"	label1:name3\n" + 
				"	label2:name5\n" + 
				"		prop1:Integer=0\n" + 
				"Edges:\n" + 
				"	label4:name1 [label1:name1-->label2:name2]\n" + 
				"	label4:name2 [label1:name1-->label2:name2]\n" + 
				"	label4:name1 [label2:name2-->label1:name3]\n" + 
				"");
	}

	@Test
	void testGraph() {
		GraphParser gp = new GraphParser(new GraphTokenizer(test));
		Graph<?,?> g = gp.graph();
//		System.out.println(g.toString());
		assertEquals(g.nNodes(),4);
		gp = new GraphParser(new GraphTokenizer(testWithErrors));
		g = gp.graph();
//		System.out.println(g.toString());
		assertEquals(g.nNodes(),4);
	}

	@Test
	void testParse2() {
		GraphParser gp = new GraphParser(new GraphTokenizer(otherTest));
		gp.parse();
//		System.out.println(gp.toString());
		assertEquals(gp.toString(),"Graph specification\n" + 
				"Graph properties:\n" + 
				"	scope:String=otherTest\n" + 
				"	node:String=fr.cnrs.iees.graph.impl.ALNode\n" + 
				"	pnode:String=fr.cnrs.iees.graph.impl.ALDataNode\n" + 
				"	edge:String=fr.cnrs.iees.graph.impl.ALEdge\n" + 
				"	mutable:Boolean=true\n" + 
				"Nodes:\n" + 
				"	pnode:\n" + 
				"		prop1:Integer=1\n" + 
				"		prop2:Double=2.0\n" + 
				"		prop3:String=blabla\n" + 
				"		prop4:Boolean=true\n" + 
				"	node:name2\n" + 
				"	node:name3\n" + 
				"	pnode:name5\n" + 
				"		prop1:Integer=0\n" + 
				"Edges:\n" + 
				"	edge:name1 [pnode:-->node:name2]\n" + 
				"	edge:name2 [pnode:-->node:name2]\n" + 
				"	edge: [node:name2-->node:name3]\n");
	}
	
	@Test
	void testGraph2() {
		GraphParser gp = new GraphParser(new GraphTokenizer(otherTest));
		Graph<?,?> g = gp.graph();
		System.out.println(g.toString());
		assertEquals(g.nNodes(),4); // 4 nodes
		assertEquals(g.getClass().getSimpleName(),"ALGraph");
	}		

	@Test
	void testParse3() {
		GraphParser gp = new GraphParser(new GraphTokenizer(edgePropTest));
		gp.parse();
//		System.out.println(gp.toString());	
		assertEquals(gp.toString(),"Graph specification\n" + 
				"Graph properties:\n" + 
				"	scope:String=edgePropTest\n" + 
				"	node:String=fr.cnrs.iees.graph.impl.ALNode\n" + 
				"	pnode:String=fr.cnrs.iees.graph.impl.ALDataNode\n" + 
				"	edge:String=fr.cnrs.iees.graph.impl.ALEdge\n" + 
				"	pedge:String=fr.cnrs.iees.graph.impl.ALDataEdge\n" + 
				"	mutable:Boolean=true\n" + 
				"Nodes:\n" + 
				"	pnode:\n" + 
				"		prop1:Integer=1\n" + 
				"		prop2:Double=2.0\n" + 
				"		prop3:String=blabla\n" + 
				"		prop4:Boolean=true\n" + 
				"	node:name2\n" + 
				"	node:name3\n" + 
				"	pnode:name5\n" + 
				"		prop1:Integer=0\n" + 
				"Edges:\n" + 
				"	edge:name1 [pnode:-->node:name2]\n" + 
				"	pedge:name2 [pnode:-->node:name2]\n" + 
				"		propE:Integer=17\n" + 
				"	edge: [node:name2-->node:name3]\n");
	}

	@Test
	void testGraph3() {
		GraphParser gp = new GraphParser(new GraphTokenizer(edgePropTest));
		Graph<?,?> g = gp.graph();
		System.out.println(g.toString());
		assertEquals(g.nNodes(),4); // 4 nodes
		assertEquals(g.getClass().getSimpleName(),"ALGraph");
	}		

}

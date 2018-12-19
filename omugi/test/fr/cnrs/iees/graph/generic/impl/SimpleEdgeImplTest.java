/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
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
package fr.cnrs.iees.graph.generic.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.impl.DefaultGraphFactory;

class SimpleEdgeImplTest {

	Node n1;
	Node n2, n3, n4;
	Edge e1, e2, e3, e4, e5;
	Map<String,String> nodes;
	
	// little test graph:
	//
	//              e3
	//              ||
	//              v|
	//  n1 ---e1--> n2 ---e4--> n3 ---e5--> n4
	//     <--e2--- 
	
	@BeforeEach
	private void init() {
		DefaultGraphFactory f = new DefaultGraphFactory(2);
		nodes = new HashMap<String,String>();
		n1 = f.makeNode();
		nodes.put(n1.uniqueId(), "n1");
		n2 = f.makeNode();
		nodes.put(n2.uniqueId(), "n2");
		n3 = f.makeNode();
		nodes.put(n3.uniqueId(), "n3");
		n4 = f.makeNode();
		nodes.put(n4.uniqueId(), "n4");
		e1 = f.makeEdge(n1,n2);
		e2 = f.makeEdge(n2,n1);
		e3 = f.makeEdge(n2,n2);
		e4 = f.makeEdge(n2,n3);
		e5 = f.makeEdge(n3,n4);
	}

	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@Test
	void testSimpleEdgeImpl() {
		assertNotNull(e1);
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",e5.toDetailedString());
		assertTrue(e5.toDetailedString().contains(n4.uniqueId().toString()));
	}

	@Test
	void testDisconnect() {
		assertEquals(n2.degree(),5);
		e4.disconnect();
		assertEquals(n2.degree(),4);
	}

	@Test
	void testTraversalInt() {
//		for (Node n:e4.traversal(1))
//			show("testTraversalInt",nodes.get(n.getId()));
		assertTrue(e4.traversal(1).contains(n2));
		assertTrue(e4.traversal(1).contains(n3));
		assertFalse(e4.traversal(1).contains(n1));
		assertFalse(e4.traversal(1).contains(n4));

		assertTrue(e4.traversal(2).contains(n2));
		assertTrue(e4.traversal(2).contains(n3));
		assertTrue(e4.traversal(2).contains(n1));
		assertTrue(e4.traversal(2).contains(n4));

	}

	@Test
	void testTraversalIntDirection() {
//		for (Node n:e2.traversal(3,Direction.IN))
//			show("testTraversalIntDirection",nodes.get(n.getId()));
		assertTrue(e2.traversal(3,Direction.IN).contains(n1));
		assertTrue(e2.traversal(3,Direction.IN).contains(n2));
		assertFalse(e2.traversal(3,Direction.IN).contains(n3));

		assertTrue(e2.traversal(3,Direction.OUT).contains(n1));
		assertTrue(e2.traversal(3,Direction.OUT).contains(n2));
		assertTrue(e2.traversal(3,Direction.OUT).contains(n3));
	}

	@Test
	void testStartNode() {
		assertEquals(e1.startNode(),n1);
		assertEquals(e3.startNode(),n2);
	}

	@Test
	void testEndNode() {
		assertEquals(e1.endNode(),n2);
		assertEquals(e3.endNode(),n2);
	}

	@Test
	void testOtherNode() {
		assertEquals(e1.otherNode(n1),n2);
		assertEquals(e3.otherNode(n2),n2);
	}

	@Test
	void testSetStartNode() {
		e2.setStartNode(n1);
		assertEquals(e2.startNode(),n1);
	}

	@Test
	void testSetEndNode() {
		e2.setEndNode(n2);
		assertEquals(e2.endNode(),n2);
	}

	@Test
	void testToShortString() {
		show("testToShortString",e2.toShortString());
		assertTrue(e2.toShortString().contains(e2.uniqueId().toString()));
	}

	@Test
	void testToString() {
		show("testToString",e2.toString());
		assertTrue(e2.toString().contains(n2.uniqueId().toString()));
	}

}

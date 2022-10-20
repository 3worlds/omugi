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
package fr.cnrs.iees.omugi.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.omugi.graph.Direction;
import fr.cnrs.iees.omugi.graph.Edge;
import fr.cnrs.iees.omugi.graph.Node;

class ALEdgeTest {
	
	Node n1;
	Node n2, n3, n4;
	Edge e1, e2, e3, e4, e5;
	ALGraphFactory f = null;
	
	// little test graph:
	//
	//              e3
	//              ||
	//              v|
	//  n1 ---e1--> n2 ---e4--> n3 ---e5--> n4
	//     <--e2--- 

	@BeforeEach
	private void init() {
		f = new ALGraphFactory("bzt");
		n1 = f.makeNode("n1");
		n2 = f.makeNode("n1");
		n3 = f.makeNode("n1");
		n4 = f.makeNode("n1");
		e1 = f.makeEdge(n1,n2,"e1");
		e2 = f.makeEdge(n2,n1,"e1");
		e3 = f.makeEdge(n2,n2,"e1");
		e4 = f.makeEdge(n2,n3,"e1");
		e5 = f.makeEdge(n3,n4,"e1");
	}
	
	private void show(String method,String text) {
//		System.out.println(method+": "+text);
	}

	@Test
	void testDisconnect() {
		assertEquals(n2.degree(),5);
		e4.disconnect();
		assertEquals(n2.degree(),4);
	}

	@Test
	void testDisconnectFrom() {
		e2.disconnectFrom(n1);
		show("testDisconnectFrom",e2.toDetailedString());
		assertEquals(e2.toDetailedString(),"ALEdge:e2 [ALNode:n2]→[NULL]");
		e2.disconnectFrom(n2);
		show("testDisconnectFrom",e2.toDetailedString());
		assertEquals(e2.toDetailedString(),"ALEdge:e2 [free-floating edge]");
	}

	@Test
	void testTraversalInt() {
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
		assertEquals(e2.endNode(),n1);
		assertEquals(e3.endNode(),n2);
	}

	@Test
	void testConnect() {
		e4.connect(n2, n4);
		show("testConnect",e4.toDetailedString());
		show("testConnect",n2.toDetailedString());
		show("testConnect",n4.toDetailedString());
		assertEquals(e4.toDetailedString(),"ALEdge:e4 [ALNode:n2]→[ALNode:n4]");
		assertEquals(n4.degree(Direction.IN),2);
		assertEquals(n4.degree(Direction.OUT),0);
	}

	@Test
	void testFactory() {
		assertEquals(e2.factory(),f);
	}

	@Test
	void testConnectLike() {
		Edge e6 = f.makeEdge(n1, n1, "e1");
		e6.connectLike(e5);
		show("testConnectLike",e6.toDetailedString());
		show("testConnectLike",n3.toDetailedString());
		show("testConnectLike",n4.toDetailedString());
		assertEquals(e6.toDetailedString(),"ALEdge:e6 [ALNode:n3]→[ALNode:n4]");
		assertEquals(n3.degree(Direction.OUT),2);
		assertEquals(n4.degree(Direction.IN),2);
	}

	@Test
	void testReplace() {
		Edge e6 = f.makeEdge(n1, n1, "e1");
		e6.replace(e5);
		show("testReplace",e6.toDetailedString());
		show("testReplace",n3.toDetailedString());
		show("testReplace",n4.toDetailedString());
		assertEquals(e6.toDetailedString(),"ALEdge:e6 [ALNode:n3]→[ALNode:n4]");
		assertEquals(n3.toDetailedString(),"ALNode:n3 ←ALNode:n2 →ALNode:n4");
		assertEquals(n4.toDetailedString(),"ALNode:n4 ←ALNode:n3");
	}

	@Test
	void testOtherNode() {
		assertEquals(e1.otherNode(n1),n2);
		assertEquals(e3.otherNode(n2),n2);
		assertNull(e3.otherNode(n4));
	}

	@Test
	void testToUniqueString() {
		show("testToUniqueString",e3.toUniqueString());
		assertEquals(e3.toUniqueString(),"bzt1:e3");
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",e5.toDetailedString());
		assertTrue(e5.toDetailedString().contains(n4.id().toString()));
	}

	@Test
	void testToShortString() {
		show("testToShortString",e2.toShortString());
		assertTrue(e2.toShortString().contains(e2.id().toString()));
	}

	@Test
	void testToString() {
		show("testToString",e2.toString());
		assertTrue(e2.toString().contains(e2.id().toString()));
	}

}

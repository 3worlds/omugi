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
package fr.cnrs.iees.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.impl.DefaultGraphFactory;

class SimpleNodeImplTest {

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
		DefaultGraphFactory f = new DefaultGraphFactory();
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
	void testSimpleNodeImpl() {
		assertNotNull(n1);
	}

	@Test
	void testSimpleNodeImplInt() {
		assertNotNull(n2);
	}

	@Test
	void testGetEdges() {
		int n=0;
		for (Edge e:n1.getEdges()) { 
			show("testGetEdges",e.toDetailedString());
			n++;
		}
		assertEquals(n,2);
		n=0;
		for (Edge e:n2.getEdges()) { 
			show("testGetEdges",e.toShortString());
			n++;
		}
		assertEquals(n,4);
	}

	@Test
	void testTraversalInt() {
//		for (Node n:n2.traversal(5))
//			show("testTraversalInt",nodes.get(n.getId()));
		assertTrue(n1.traversal(1).contains(n1));
		
		assertTrue(n1.traversal(2).contains(n1));
		assertTrue(n1.traversal(2).contains(n2));
		
		assertEquals(n1.traversal(0).size(),0);

		assertTrue(n2.traversal(2).contains(n1));
		assertTrue(n2.traversal(2).contains(n2));
		assertTrue(n2.traversal(2).contains(n3));
		assertFalse(n2.traversal(2).contains(n4));
		
		assertTrue(n2.traversal(3).contains(n4));
	}

	@Test
	void testTraversalIntDirection() {
//		for (Node n:n2.traversal(2,Direction.OUT))
//			show("testTraversalIntDirection",nodes.get(n.getId()));
		assertTrue(n1.traversal(4,Direction.IN).contains(n2));
		assertFalse(n1.traversal(4,Direction.IN).contains(n3));
		assertTrue(n1.traversal(4,Direction.OUT).contains(n3));
		
		assertTrue(n4.traversal(2,Direction.IN).contains(n3));
		assertFalse(n4.traversal(2,Direction.IN).contains(n2));
		
		assertFalse(n2.traversal(2,Direction.IN).contains(n3));
		assertTrue(n2.traversal(2,Direction.OUT).contains(n3));
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",n3.toDetailedString());
		assertTrue(n3.toDetailedString().contains(n4.uniqueId().toString()));
		assertTrue(n3.toDetailedString().contains(n2.uniqueId().toString()));
		assertFalse(n3.toDetailedString().contains(n1.uniqueId().toString()));
	}

	@Test
	void testAddEdgeEdgeDirection() {
		// these are all false because a call to makeEge will addEdge and edge lists are now sets, ie do not
		// allow for duplicate edges to be stored in there
		assertFalse(n1.addEdge(e1,Direction.OUT));
		assertFalse(n1.addEdge(e1,Direction.IN));
		assertFalse(n1.addEdge(e5,Direction.OUT));
	}

	@Test
	void testAddEdgeEdge() {
		// these are all false because a call to makeEge will addEdge and edge lists are now sets, ie do not
		// allow for duplicate edges to be stored in there
		assertFalse(n1.addEdge(e1));
		assertFalse(n1.addEdge(e2));
		assertFalse(n1.addEdge(e5));
	}

	@Test
	void testRemoveEdge() {
		assertTrue(n2.removeEdge(e2));
		assertFalse(n2.removeEdge(e2));
		assertTrue(n2.removeEdge(e3));
		assertFalse(n2.removeEdge(e3));
	}

	@Test
	void testDegree() {
		assertEquals(n1.degree(),2);
		assertEquals(n2.degree(),5);
		assertEquals(n4.degree(),1);
		assertEquals(n1.degree(Direction.IN),1);
		assertEquals(n2.degree(Direction.IN),2);
		assertEquals(n2.degree(Direction.OUT),3);
		assertEquals(n4.degree(Direction.OUT),0);
	}

	@Test
	void testIsLeaf() {
		assertTrue(n4.isLeaf());
		assertFalse(n1.isLeaf());
	}

	@Test
	void testIsRoot() {
		assertFalse(n1.isRoot());
		n1.removeEdge(e2);
		assertTrue(n1.isRoot());
	}

	@Test
	void testGetEdgesDirection() {
		int n=0;
		for (Edge e:n2.getEdges(Direction.IN)) { 
			show("testGetEdgesDirection",e.toDetailedString());
			n++;
		}
		assertEquals(n,2);
		n=0;
		for (Edge e:n2.getEdges(Direction.OUT)) { 
			show("testGetEdgesDirection",e.toShortString());
			n++;
		}
		assertEquals(n,3);
	}

	@Test
	void testDisconnect() {
		assertEquals(n2.degree(),5);
		n2.disconnect();
		assertEquals(n2.degree(),0);
	}

	@Test
	void testToShortString() {
		show("testToShortString",n4.toShortString());
		assertFalse(n4.toShortString().contains(e5.uniqueId().toString()));
		assertTrue(n4.toShortString().contains(n4.uniqueId().toString()));
	}

	@Test
	void testToString() {
		show("testToString",n4.toString());
		assertTrue(n4.toString().contains(e5.uniqueId().toString()));
	}

}

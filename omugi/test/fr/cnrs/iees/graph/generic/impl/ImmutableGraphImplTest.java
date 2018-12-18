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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Node;

class ImmutableGraphImplTest {

	DefaultGraphFactory f = new DefaultGraphFactory(2);
	Node n1;
	Node n2, n3, n4;
	Edge e1, e2, e3, e4, e5;
	Map<String,String> nodes;
	ImmutableGraphImpl<Node,Edge> graph;
	
	// little test graph:
	//
	//              e3
	//              ||
	//              v|
	//  n1 ---e1--> n2 ---e4--> n3 ---e5--> n4
	//     <--e2--- 
	
	@BeforeEach
	private void init() {
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
		List<Node> l = new LinkedList<Node>();
		l.add(n1); l.add(n2);
		l.add(n3); l.add(n4);
		graph = new ImmutableGraphImpl<Node,Edge>(l);
	}

	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@Test
	void testImmutableGraphImplIterableOfN() {
		assertNotNull(graph);
	}

	@Test
	void testNodes() {
		int i=0;
		for (Node n:graph.nodes()) {
			show("testNodes",nodes.get(n.uniqueId()));
			i++;
		}
		assertEquals(i,4);
	}

	@Test
	void testEdges() {
		int i=0;
		for (Edge e:graph.edges()) {
			show("testEdges",e.uniqueId().toString());
			i++;
		}
		assertEquals(i,5);
	}

	@SuppressWarnings("unused")
	@Test
	void testRoots() {
		int i=0;
		for (Node n:graph.roots()) 
			i++;
		assertEquals(i,0);
	}

	@SuppressWarnings("unused")
	@Test
	void testLeaves() {
		int i=0;
		for (Node n:graph.leaves()) 
			i++;
		assertEquals(i,1);
	}

	@Test
	void testContains() {
		assertTrue(graph.contains(n3));
		Node n = f.makeNode();
		assertFalse(graph.contains(n));
	}

	@Test
	void testSize() {
		assertEquals(graph.size(),4);
	}

	@Test
	void testToUniqueString() {
		show("testToUniqueString",graph.toUniqueString());
	}

	@Test
	void testToShortString() {
		show("testToShortString",graph.toShortString());
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",graph.toDetailedString());
	}

	@Test
	void testToString() {
		show("testToString",graph.toString());
	}

}

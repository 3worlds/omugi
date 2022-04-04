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
package fr.cnrs.iees.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Node;

class ALGraphTest {

	ALNode n1;
	ALNode n2, n3, n4;
	Edge e1, e2, e3, e4, e5;
	ALGraphFactory f = null;
	ALGraph<ALNode,ALEdge> graph = null;
	
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
		graph = new ALGraph<ALNode,ALEdge>(f);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);
	}
	
	private void show(String method,String text) {
//		System.out.println(method+": "+text);
	}

	@Test
	void testNNodes() {
		assertEquals(graph.nNodes(),4);
	}

	@Test
	void testNodes() {
		int i=0;
		for (Node n:graph.nodes()) {
			show("testNodes",n.id());
			i++;
		}
		assertEquals(i,4);
	}

	@Test
	void testEdges() {
		int i=0;
		for (Edge e:graph.edges()) {
			show("testEdges",e.id());
			i++;
		}
		assertEquals(i,5);
	}

	@Test
	void testRoots() {
		int i=0;
		for (Node n:graph.roots()) {
			show("testRoots",n.toDetailedString());
			i++;
		}
		assertEquals(i,0);
	}

	@Test
	void testLeaves() {
		int i=0;
		for (Node n:graph.leaves()) {
			show("testLeaves",n.toDetailedString());
			i++;
		}
		assertEquals(i,1);
	}

	@Test
	void testContains() {
		assertTrue(graph.contains(n3));
		// since graph is listening to f, the new nodes are automatically inserted in it
		ALNode n = f.makeNode("n1");
		assertTrue(graph.contains(n));
	}

	@Test
	void testNEdges() {
		assertEquals(graph.nEdges(),5);
	}

	@Test
	void testNodeFactory() {
		assertEquals(graph.nodeFactory(),f);
	}

	@Test
	void testAddNode() {
		graph.addNode(n1);
		show("testAddNode",graph.toDetailedString());
		assertEquals(graph.nNodes(),4);
	}

	@Test
	void testRemoveNode() {
		graph.removeNode(n1);
		show("testRemoveNode",graph.toShortString());
		assertEquals(graph.nNodes(),3);
	}

	@Test
	void testCountEdges() {
		assertEquals(ALGraph.countEdges(graph.nodes()),5);
	}

	@Test
	void testEdgeFactory() {
		assertEquals(graph.edgeFactory(),f);
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

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

import fr.cnrs.iees.omugi.graph.Edge;
import fr.cnrs.iees.omugi.graph.Node;
import fr.cnrs.iees.omugi.graph.impl.ALEdge;
import fr.cnrs.iees.omugi.graph.impl.TreeGraph;
import fr.cnrs.iees.omugi.graph.impl.TreeGraphFactory;
import fr.cnrs.iees.omugi.graph.impl.TreeGraphNode;

/**
 * 
 * @author Jacques Gignoux - 20 mai 2019
 *
 */
class TreeGraphTest {

	private TreeGraphFactory f = null;
	private TreeGraphNode n1, n2, n3, n4;
	private TreeGraph<TreeGraphNode,ALEdge> graph = null;

	// simple little tree:
	//  n1
	//  ├─n2
	//  │  └─n4
	//  └─n3
	// with edges
	// e1: n2-->n3
	// e2: n4-->n1

	@BeforeEach
	private void init() {
		f = new TreeGraphFactory("Brf");
		n1 = f.makeNode("n1");
		n2 = f.makeNode("n1");
		n3 = f.makeNode("n1");
		n4 = f.makeNode("n1");
		n1.connectChildren(n2,n3);
		n4.connectParent(n2);
		f.makeEdge(n2, n3, "e1");
		f.makeEdge(n4, n1, "e1");
		graph = new TreeGraph<>(f);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);
	}

	private void show(String method,String text) {
//		System.out.println(method+": "+text);
	}

	@Test
	final void testNodes() {
		int i=0;
		for (Node n:graph.nodes()) {
			show("testNodes",n.id());
			i++;
		}
		assertEquals(i,4);
	}

	@Test
	final void testRoots() {
		int i=0;
		for (Node n:graph.roots()) {
			show("testRoots",n.toDetailedString());
			i++;
		}
		assertEquals(i,1); 
	}

	@Test
	void testLeaves() {
		int i=0;
		for (Node n:graph.leaves()) {
			show("testLeaves",n.toDetailedString());
			i++;
		}
		assertEquals(i,1); // the only leaf is n3
	}

	@Test
	final void testContains() {
		assertTrue(graph.contains(n3));
		// since graph is listening to f, the new nodes are automatically inserted in it
		TreeGraphNode n = f.makeNode("n1");
		assertTrue(graph.contains(n));
	}

	@Test
	final void testNodeFactory() {
		assertEquals(graph.nodeFactory(),f);
	}

	@Test
	final void testAddNode() {
		graph.addNode(n1);
		show("testAddNode",graph.toDetailedString());
		assertEquals(graph.nNodes(),4);
	}

	@Test
	final void testRemoveNode() {
		graph.removeNode(n1);
		show("testRemoveNode",graph.toShortString());
		assertEquals(graph.nNodes(),3);
	}

	@Test
	final void testNNodes() {
		assertEquals(graph.nNodes(),4);
	}

	@Test
	final void testEdges() {
		int i=0;
		for (Edge e:graph.edges()) {
			show("testEdges",e.id());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	final void testEdgeFactory() {
		assertEquals(graph.edgeFactory(),f);
	}

	@Test
	final void testNEdges() {
		assertEquals(graph.nEdges(),2);
	}

	@Test
	final void testRoot() {
		assertEquals(graph.root(),n1);
		n4.disconnect();
		assertNull(graph.root());
	}

	@Test
	final void testSubTree() {
		int i=0;
		for (Node n:graph.subTree(n2)) {
			show("testSubTree",n.toDetailedString());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	final void testOnParentChanged() {
		show("testOnParentChanged",graph.root().toDetailedString());
		n2.connectParent(n4);
		assertNull(graph.root());
		int i=0;
		for (Node n:graph.roots()) {
			show("testOnParentChanged",n.id());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	final void testToUniqueString() {
		show("testToUniqueString",graph.toUniqueString());
	}

	@Test
	final void testToShortString() {
		show("testToShortString",graph.toShortString());
	}

	@Test
	final void testToDetailedString() {
		show("testToDetailedString",graph.toDetailedString());
	}

	@Test
	final void testToString() {
		show("testToString",graph.toString());
	}

}

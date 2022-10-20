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

import fr.cnrs.iees.omugi.graph.Node;

/**
 * 
 * @author Jacques Gignoux - 17 mai 2019
 *
 */
class SimpleTreeTest {

	private SimpleTreeNode tn1, tn2, tn3, tn4;
	private SimpleTreeFactory f;
	private SimpleTree<SimpleTreeNode> tree = null;
	
	private void show(String method,String text) {
//		System.out.println(method+": "+text);
	}
	
	// simple little tree:
	//  tn1
	//  ├─tn2
	//  │  └─tn4
	//  └─tn3
	
	@BeforeEach
	private void init() {
		f = new SimpleTreeFactory("cuckoo");
		tn1 = f.makeNode("tn1");
		tn2 = f.makeNode("tn1");
		tn3 = f.makeNode("tn1");
		tn4 = f.makeNode("tn1");
		tn2.connectParent(tn1);
		tn3.connectParent(tn1);
		tn4.connectParent(tn2);
		tree = new SimpleTree<>(f);
		tree.addNode(tn1);
		tree.addNode(tn2);
		tree.addNode(tn3);
		tree.addNode(tn4);
	}

	@Test
	final void testNodes() {
		int i=0;
		for (Node n:tree.nodes()) {
			show("testNodes",n.id());
			i++;
		}
		assertEquals(i,4);
	}

	@Test
	final void testRoots() {
		int i=0;
		for (Node n:tree.roots()) {
			show("testRoots",n.id());
			i++;
		}
		assertEquals(i,1);
	}

	@Test
	void testLeaves() {
		int i=0;
		for (Node n:tree.leaves()) {
			show("testLeaves",n.toDetailedString());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	final void testContains() {
		assertTrue(tree.contains(tn2));
		// since tree is listening to f, the new nodes are automatically inserted in it
		SimpleTreeNode tn = f.makeNode("tn1");
		assertTrue(tree.contains(tn));
	}

	@Test
	final void testRoot() {
		assertTrue(tree.root().equals(tn1));
	}

	@Test
	final void testSubTree() {
		int i=0;
		for (Node n:tree.subTree(tn2)) {
			show("testSubTree",n.toDetailedString());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	final void testAddNode() {
		tree.addNode(tn3);
		assertEquals(tree.nNodes(),4);
		SimpleTreeNode tn = f.makeNode("tn");
		tree.addNode(tn);
		assertEquals(tree.nNodes(),5);
	}

	@Test
	final void testRemoveNode() {
		tree.removeNode(tn2);
		assertEquals(tree.nNodes(),3);
		tree.removeNode(tn2);
		assertEquals(tree.nNodes(),3);
	}

	@Test
	final void testOnParentChanged() {
		show("testOnParentChanged",tree.root().toDetailedString());
		tn2.connectParent(tn4);
		assertNull(tree.root());
		int i=0;
		for (Node n:tree.roots()) {
			show("testOnParentChanged",n.id());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	final void testNodeFactory() {
		assertEquals(tree.nodeFactory(),f);
	}

	@Test
	final void testNNodes() {
		assertEquals(tree.nNodes(),4);
	}

	@Test
	final void testToUniqueString() {
		show("testToUniqueString",tree.toUniqueString());
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",tree.toDetailedString());
	}

	@Test
	void testToString() {
		show("testToString",tree.toString());
	}

}

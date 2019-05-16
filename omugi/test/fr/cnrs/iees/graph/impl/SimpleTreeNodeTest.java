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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.TreeNode;

class SimpleTreeNodeTest {

	private TreeNode tn1, tn2, tn3, tn4;
	private SimpleTreeFactory f;
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	// simple little tree:
	//  tn1
	//  ├─tn2
	//  │  └─tn4
	//  └─tn3
	
	@BeforeEach
	private void init() {
		f = new SimpleTreeFactory("cuckoo");
		tn1 = f.makeNode();
		tn2 = f.makeNode();
		tn3 = f.makeNode();
		tn4 = f.makeNode();
		tn2.connectParent(tn1);
		tn3.connectParent(tn1);
		tn4.connectParent(tn2);
	}

	@Test
	void testAddConnectionsLike() {
		show("testAddConnectionsLike",tn4.toDetailedString());
		tn4.addConnectionsLike(tn3);
		show("testAddConnectionsLike",tn4.toDetailedString());
		show("testAddConnectionsLike",tn3.toDetailedString());
		show("testAddConnectionsLike",tn2.toDetailedString());
		assertEquals(tn4.toDetailedString(),"SimpleTreeNode:node3 ↑SimpleTreeNode:node0");
		assertEquals(tn2.toDetailedString(),"SimpleTreeNode:node1 ↑SimpleTreeNode:node0");
	}

	@Test
	void testIsLeaf() {
		assertTrue(tn3.isLeaf());
		assertFalse(tn2.isLeaf());
	}

	@Test
	void testIsRoot() {
		assertTrue(tn1.isRoot());
		assertFalse(tn2.isRoot());
	}

	@Test
	void testDegree() {
		assertEquals(tn2.degree(Direction.IN),1);
		assertEquals(tn1.degree(Direction.IN),0);
		assertEquals(tn1.degree(Direction.OUT),2);
	}

	@Test
	void testEdgesDirection() {
		assertThrows(OmugiException.class,()->tn2.edges(Direction.OUT));
	}

	@Test
	void testEdges() {
		assertThrows(OmugiException.class,()->tn2.edges());
	}

	@Test
	void testNodesDirection() {
		int i=0;
		for (Node n:tn1.nodes(Direction.IN)) {
			show("testNodesDirection",n.toShortString());
			i++;
		}
		assertEquals(i,0);
		i=0;
		for (Node n:tn1.nodes(Direction.OUT)) {
			show("testNodesDirection",n.toShortString());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	void testNodes() {
		int i=0;
		for (Node n:tn2.nodes()) {
			show("testNodes",n.toShortString());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	void testConnectToDirectionNode() {
		assertThrows(OmugiException.class,()->tn2.connectTo(tn3));
	}

	@Test
	void testConnectToDirectionIterableOfQextendsNode() {
		assertThrows(OmugiException.class,()->tn2.connectTo(tn2.nodes()));
	}

	@Test
	void testDisconnect() {
		tn2.disconnect();
		assertTrue(tn2.isRoot());
		assertTrue(tn4.isRoot());
	}

	@Test
	void testDisconnectFrom() {
		tn1.disconnectFrom(tn2);
		tn3.disconnectFrom(tn1);
		assertTrue(tn1.isLeaf());
		assertTrue(tn2.isRoot());
		assertTrue(tn3.isRoot());
	}

	@Test
	void testTraversalInt() {
		Collection<? extends Node> l = tn1.traversal(0);
		assertEquals(l.size(),1);
		l = tn1.traversal(1);
		assertEquals(l.size(),3);
		l = tn2.traversal(2);
		assertEquals(l.size(),4);
	}

	@Test
	void testTraversalIntDirection() {
		fail("Not yet implemented");
	}

	@Test
	void testGetParent() {
		assertEquals(tn1.getParent(),null);
		assertEquals(tn2.getParent(),tn1);
	}

	@Test
	void testGetChildren() {
		List<TreeNode> l = new LinkedList<TreeNode>();
		for (TreeNode n:tn1.getChildren()) {
			show("testGetChildren",n.toShortString());
			l.add(n);
		}
		assertTrue((l.contains(tn2))&&(l.contains(tn3)));
		assertFalse(l.contains(tn1));
		assertFalse(l.contains(tn4));
	}

	@Test
	void testHasChildren() {
		assertTrue(tn1.hasChildren());
		assertFalse(tn4.hasChildren());
	}

	@Test
	void testNChildren() {
		assertEquals(tn1.nChildren(),2);
	}

	@Test
	void testFactory() {
		assertEquals(tn3.factory(),f);
	}

	@Test
	void testConnectParent() {
		fail("Not yet implemented");
	}

	@Test
	void testConnectChild() {
		fail("Not yet implemented");
	}

	@Test
	void testConnectChildrenTreeNodeArray() {
		fail("Not yet implemented");
	}

	@Test
	void testConnectChildrenIterableOfQextendsTreeNode() {
		fail("Not yet implemented");
	}

	@Test
	void testConnectChildrenCollectionOfQextendsTreeNode() {
		fail("Not yet implemented");
	}

	@Test
	void testToUniqueString() {
		show("testToUniqueString",tn1.toUniqueString());
		assertEquals(tn1.toUniqueString(),"cuckoo:node0");
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",tn2.toDetailedString());
		assertEquals(tn2.toDetailedString(),"SimpleTreeNode:node1 ↑SimpleTreeNode:node0 ↓SimpleTreeNode:node3");
	}

	@Test
	void testToShortString() {
		show("testToShortString",tn1.toShortString());
		assertEquals(tn1.toShortString(),"SimpleTreeNode:node0");
	}

	@Test
	void testToString() {
		show("testToString",tn2.toString());
		assertEquals(tn2.toString(),"[SimpleTreeNode:node1 ↑SimpleTreeNode:node0 ↓SimpleTreeNode:node3]");
	}
	
	@Test
	void testEqualsObject() {
		show("testEqualsObject",tn2.toDetailedString());
		show("testEqualsObject",tn3.toDetailedString());
		assertFalse(tn2.equals(tn3));
		assertFalse(tn1.equals(f));
		assertFalse(tn1.equals(tn2));
		assertTrue(tn2.equals(tn2));
		// same parent, same children, different id, but equal treenodes.
		TreeNode tn5 = new SimpleTreeNode(f.scope.newId("coucou"),f);
		tn5.connectParent(tn1);
		assertTrue(tn5.equals(tn3));
	}

}

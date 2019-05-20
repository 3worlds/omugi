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

import java.util.ArrayList;
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
		tn1 = f.makeNode("tn1");
		tn2 = f.makeNode("tn1");
		tn3 = f.makeNode("tn1");
		tn4 = f.makeNode("tn1");
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
		assertEquals(tn4.toDetailedString(),"SimpleTreeNode:tn4 ↑SimpleTreeNode:tn1");
		assertEquals(tn2.toDetailedString(),"SimpleTreeNode:tn2 ↑SimpleTreeNode:tn1");
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
		assertFalse(tn3.getParent()==tn2);
		tn2.connectTo(tn3); // equivalent to set tn3 as a child of tn2
		assertTrue(tn3.getParent()==tn2);
	}

	@Test
	void testConnectToDirectionIterableOfQextendsNode() {
		assertThrows(OmugiException.class,()->tn2.connectTo(tn2.nodes()));
	}

	@Test
	void testDisconnect() {
		tn1.disconnect();
		assertTrue(tn2.isRoot());
		assertTrue(tn3.isRoot());
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
		for (Node n:l)
			show("testTraversalInt",n.toShortString());
		assertEquals(l.size(),3);
		l = tn2.traversal(2);
		assertEquals(l.size(),4);
	}

	@Test
	void testTraversalIntDirection() {
		Collection<? extends Node> l = tn2.traversal(0,Direction.IN);
		assertEquals(l.size(),1);
		l = tn2.traversal(1,Direction.IN);
		for (Node n:l)
			show("testTraversalIntDirection",n.toShortString());
		assertEquals(l.size(),2);
		l = tn2.traversal(2,Direction.OUT);
		for (Node n:l)
			show("testTraversalIntDirection",n.toShortString());
		assertEquals(l.size(),2);
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
		TreeNode tn5 = f.makeNode("tn1");
		tn5.connectParent(tn4);
		assertEquals(tn5.getParent(),tn4);
		for (Node n:tn4.getChildren())
			assertEquals(n,tn5);
		tn4.connectParent(tn5);
		show("testConnectParent",tn2.toDetailedString());
		show("testConnectParent",tn4.toDetailedString());
		show("testConnectParent",tn5.toDetailedString());
		assertEquals(tn2.toDetailedString(),"SimpleTreeNode:tn2 ↑SimpleTreeNode:tn1");
		assertEquals(tn4.toDetailedString(),"SimpleTreeNode:tn4 ↑SimpleTreeNode:tn5");
		assertEquals(tn5.toDetailedString(),"SimpleTreeNode:tn5 ROOT ↓SimpleTreeNode:tn4");
	}

	@Test
	void testConnectChild() {
		TreeNode tn5 = f.makeNode("tn1");
		tn3.connectChild(tn5);
		assertEquals(tn5.getParent(),tn3);
		for (Node n:tn3.getChildren())
			assertEquals(n,tn5);
		tn5.connectChild(tn3);
		show("testConnectChild",tn1.toDetailedString());
		show("testConnectChild",tn3.toDetailedString());
		show("testConnectChild",tn5.toDetailedString());
		assertEquals(tn1.toDetailedString(),"SimpleTreeNode:tn1 ROOT ↓SimpleTreeNode:tn2");
		assertEquals(tn3.toDetailedString(),"SimpleTreeNode:tn3 ↑SimpleTreeNode:tn5");
		assertEquals(tn5.toDetailedString(),"SimpleTreeNode:tn5 ROOT ↓SimpleTreeNode:tn3");
	}

	@Test
	void testConnectChildrenTreeNodeArray() {
		TreeNode tn5 = f.makeNode("tn1");
		TreeNode tn6 = f.makeNode("tn1");
		tn1.connectChildren(tn5,tn6);
		assertEquals(tn5.getParent(),tn1);
		assertEquals(tn6.getParent(),tn1);
		assertEquals(tn1.nChildren(),4);
	}

	@Test
	void testConnectChildrenIterableOfQextendsTreeNode() {
		TreeNode tn5 = f.makeNode("tn1");
		TreeNode tn6 = f.makeNode("tn1");
		List<TreeNode> l =new ArrayList<>(2);
		l.add(tn6); l.add(tn5);
		tn1.connectChildren((Iterable<TreeNode>)l);
		assertEquals(tn5.getParent(),tn1);
		assertEquals(tn6.getParent(),tn1);
		assertEquals(tn1.nChildren(),4);
	}

	@Test
	void testConnectChildrenCollectionOfQextendsTreeNode() {
		TreeNode tn5 = f.makeNode("tn1");
		TreeNode tn6 = f.makeNode("tn1");
		List<TreeNode> l =new ArrayList<>(2);
		l.add(tn6); l.add(tn5);
		tn1.connectChildren((Collection<TreeNode>)l);
		assertEquals(tn5.getParent(),tn1);
		assertEquals(tn6.getParent(),tn1);
		assertEquals(tn1.nChildren(),4);
	}

	@Test
	void testToUniqueString() {
		show("testToUniqueString",tn1.toUniqueString());
		assertEquals(tn1.toUniqueString(),"cuckoo:tn1");
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",tn2.toDetailedString());
		assertEquals(tn2.toDetailedString(),"SimpleTreeNode:tn2 ↑SimpleTreeNode:tn1 ↓SimpleTreeNode:tn4");
	}

	@Test
	void testToShortString() {
		show("testToShortString",tn1.toShortString());
		assertEquals(tn1.toShortString(),"SimpleTreeNode:tn1");
	}

	@Test
	void testToString() {
		show("testToString",tn2.toString());
		assertEquals(tn2.toString(),"[SimpleTreeNode:tn2 ↑SimpleTreeNode:tn1 ↓SimpleTreeNode:tn4]");
	}
	
	@Test
	void testEqualsObject() {
		show("testEqualsObject",tn2.toDetailedString());
		show("testEqualsObject",tn3.toDetailedString());
		assertFalse(tn2.equals(tn3));
		assertFalse(tn1.equals(f));
		assertFalse(tn1.equals(tn2));
		assertTrue(tn2.equals(tn2));
		// same parent, same children, different id, hence different treenodes.
		TreeNode tn5 = new SimpleTreeNode(f.scope.newId("coucou"),f);
		tn5.connectParent(tn1);
		assertFalse(tn5.equals(tn3));
	}

}

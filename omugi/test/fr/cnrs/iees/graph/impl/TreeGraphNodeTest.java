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

import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.TreeNode;

/**
 * 
 * @author Jacques Gignoux - 20 mai 2019
 *
 */
class TreeGraphNodeTest {

	private TreeGraphFactory f = null;
	private TreeGraphNode n1, n2, n3, n4;

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
	}

	private void show(String method,String text) {
//		System.out.println(method+": "+text);
	}
	
	@Test
	final void testFactory() {
		assertEquals(n4.factory(),f);
	}

	@Test
	final void testGetParent() {
		assertEquals(n4.getParent(),n2);
		assertEquals(n3.getParent(),n1);
		assertFalse(n3.getParent()==n2);
		assertFalse(n3.getParent().equals(n2));
	}

	@Test
	final void testConnectParent() {
		assertFalse(n4.hasChildren());
		TreeGraphNode n5 = f.makeNode("n1");
		n5.connectParent(n4);
		assertEquals(n5.getParent(),n4);
		assertTrue(n4.hasChildren());
	}

	@Test
	final void testGetChildren() {
		int i=0;
		for (Node n:n1.getChildren()) {
			show("testGetChildren",n.toShortString());
			i++;
		}
		assertEquals(i,2);
		i=0;
		for (Node n:n3.getChildren()) {
			show("testGetChildren",n.toShortString());
			i++;
		}
		assertEquals(i,0);
	}

	@Test
	final void testConnectChild() {
		assertFalse(n4.hasChildren());
		TreeGraphNode n5 = f.makeNode("n1");
		n4.connectChild(n5);
		assertEquals(n5.getParent(),n4);
		assertTrue(n4.hasChildren());
	}

	@Test
	final void testConnectChildrenTreeNodeArray() {
		assertFalse(n4.hasChildren());
		TreeGraphNode n5 = f.makeNode("n1");
		TreeGraphNode n6 = f.makeNode("n1");
		n4.connectChildren(n5,n6);
		assertEquals(n5.getParent(),n4);
		assertEquals(n6.getParent(),n4);
		assertEquals(n4.nChildren(),2);
	}

	@Test
	final void testConnectChildrenIterableOfQextendsTreeNode() {
		TreeGraphNode n5 = f.makeNode("n1");
		TreeGraphNode n6 = f.makeNode("n1");
		List<TreeNode> l = new LinkedList<>();
		l.add(n5);
		l.add(n6);
		n1.connectChildren((Collection<TreeNode>)l);
		assertEquals(n5.getParent(),n1);
		assertEquals(n6.getParent(),n1);
		assertEquals(n1.nChildren(),4);
	}

	@Test
	final void testConnectChildrenCollectionOfQextendsTreeNode() {
		TreeGraphNode n5 = f.makeNode("n1");
		TreeGraphNode n6 = f.makeNode("n1");
		List<TreeNode> l = new LinkedList<>();
		l.add(n5);
		l.add(n6);
		n1.connectChildren(l);
		assertEquals(n5.getParent(),n1);
		assertEquals(n6.getParent(),n1);
		assertEquals(n1.nChildren(),4);
	}

	@Test
	final void testHasChildren() {
		assertTrue(n2.hasChildren());
		assertFalse(n3.hasChildren());
	}

	@Test
	final void testNChildren() {
		assertEquals(n1.nChildren(),2);
		assertEquals(n4.nChildren(),0);
	}

	@Test
	final void testToDetailedString() {
		show("testToDetailedString",n1.toDetailedString());
		show("testToDetailedString",n2.toDetailedString());
		show("testToDetailedString",n3.toDetailedString());
		show("testToDetailedString",n4.toDetailedString());
		assertEquals(n2.toDetailedString(),"TreeGraphNode:n2=[↑TreeGraphNode:n1 ↓TreeGraphNode:n4 →TreeGraphNode:n3]");
		assertEquals(n3.toDetailedString(),"TreeGraphNode:n3=[↑TreeGraphNode:n1 ←TreeGraphNode:n2]");
		assertEquals(n4.toDetailedString(),"TreeGraphNode:n4=[↑TreeGraphNode:n2 →TreeGraphNode:n1]");
	}

	@Test
	final void testDisconnect() {
		n1.disconnect();
		show("testDisconnect",n1.toDetailedString());
		show("testDisconnect",n2.toDetailedString());
		show("testDisconnect",n3.toDetailedString());
		show("testDisconnect",n4.toDetailedString());
	}

	@Test
	final void testDisconnectFrom() {
		// child
		n1.disconnectFrom(n2);
		show("testDisconnectFrom",n1.toDetailedString());
		// cross-link  OUT
		n2.disconnectFrom(n3);
		show("testDisconnectFrom",n2.toDetailedString());
		show("testDisconnectFrom",n3.toDetailedString());
		// parent
		n4.disconnectFrom(n2);
		show("testDisconnectFrom",n4.toDetailedString());
		// cross-link IN
		n1.disconnectFrom(n4);
		show("testDisconnectFrom",n4.toDetailedString());
	}

	@Test
	final void testAddConnectionsLike() {
		show("testAddConnectionsLike",n2.toDetailedString());
		TreeGraphNode n5 = f.makeNode("n1");
		n5.addConnectionsLike(n2);
		show("testAddConnectionsLike",n5.toDetailedString());
		show("testAddConnectionsLike",n2.toDetailedString());
		show("testAddConnectionsLike",n4.toDetailedString());
		assertEquals(n5.getParent(),n1);
		assertEquals(n5.nChildren(),1);
		// since n5 has been connected to n2's children, these are no longer n2's
		assertEquals(n2.nChildren(),0);
	}

	@Test
	final void testIsLeaf() {
		assertTrue(n3.isLeaf());
		assertFalse(n4.isLeaf());
	}

	@Test
	final void testIsRoot() {
		// this is false because n1 has an IN edge
		assertFalse(n1.isRoot());
		n1.disconnectFrom(n4);
		show("testIsRoot",n1.toDetailedString());
		assertTrue(n1.isRoot());
	}

	@Test
	final void testDegree() {
		assertEquals(n2.degree(),3);
		assertEquals(n4.degree(),2);
	}

	@Test
	final void testEdgesDirection() {
		int i=0;
		for (Edge e:n1.edges(Direction.IN)) {
			show("testEdgesDirection",e.toDetailedString());
			i++;
		}
		assertEquals(i,1);
		i=0;
		for (Edge e:n1.edges(Direction.OUT)) {
			show("testEdgesDirection",e.toDetailedString());
			i++;
		}
		assertEquals(i,0);
	}

	@Test
	final void testEdges() {
		int i=0;
		for (Edge e:n1.edges()) {
			show("testEdges",e.toDetailedString());
			i++;
		}
		assertEquals(i,1);
	}

	@Test
	final void testNodesDirection() {
		int i=0;
		for (Node n:n1.nodes(Direction.IN)) {
			show("testNodesDirection",n.toShortString());
			i++;
		}
		assertEquals(i,1);
		i=0;
		for (Node n:n1.nodes(Direction.OUT)) {
			show("testNodesDirection",n.toShortString());
			i++;
		}
		assertEquals(i,2);

	}

	@Test
	final void testNodes() {
		int i=0;
		for (Node n:n4.nodes()) {
			show("testNodes",n.toShortString());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	final void testConnectToDirectionNode() {
		TreeGraphNode n5 = f.makeNode("n1");
		n2.connectTo(Direction.IN,n5);
		show("testConnectToDirectionNode",n2.toDetailedString());
		show("testConnectToDirectionNode",n5.toDetailedString());
		assertEquals(n2.degree(),4);
		assertEquals(n5.degree(),1);
	}

	@Test
	final void testConnectToDirectionIterableOfQextendsNode() {
		n3.connectTo(Direction.OUT,n1.getChildren());
		show("testConnectToDirectionIterableOfQextendsNode",n3.toDetailedString());
		show("testConnectToDirectionIterableOfQextendsNode",n2.toDetailedString());
		assertEquals(n2.degree(),4);
		assertEquals(n3.degree(),5);
	}

	@Test
	final void testToUniqueString() {
		show("testToUniqueString",n1.toUniqueString());
		show("testToUniqueString",n2.toUniqueString());
		show("testToUniqueString",n3.toUniqueString());
		show("testToUniqueString",n4.toUniqueString());
		assertEquals(n1.toUniqueString(),"Brf:n1");
	}

	@Test
	final void testToShortString() {
		show("testToShortString",n1.toShortString());
		show("testToShortString",n2.toShortString());
		show("testToShortString",n3.toShortString());
		show("testToShortString",n4.toShortString());
		assertEquals(n3.toShortString(),"TreeGraphNode:n3");
	}

	@Test
	final void testToString() {
		show("testToString",n1.toString());
		show("testToString",n2.toString());
		show("testToString",n3.toString());
		show("testToString",n4.toString());
		assertEquals(n4.toDetailedString(),"TreeGraphNode:n4=[↑TreeGraphNode:n2 →TreeGraphNode:n1]");
	}

}

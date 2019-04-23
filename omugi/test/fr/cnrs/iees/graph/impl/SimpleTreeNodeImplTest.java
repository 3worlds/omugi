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

import fr.cnrs.iees.graph.TreeNode;

/**
 * 
 * @author Jacques Gignoux - 9 janv. 2019
 *
 */
class SimpleTreeNodeImplTest {
	
	private TreeNode tn1, tn2, tn3, tn4;
	private TreeFactory f;
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@BeforeEach
	private void init() {
		f = new TreeFactory();
		tn1 = f.makeTreeNode(null);
		tn2 = f.makeTreeNode(tn1);
		tn3 = f.makeTreeNode(tn1);
		tn4 = f.makeTreeNode(tn2);
	}

	@Test
	void testGetParent() {
		assertEquals(tn1.getParent(),null);
		assertEquals(tn2.getParent(),tn1);
	}

	@Test
	void testSetParent() {
		// this would create a loop in the tree but is prevented because tn2 already has a parent
		assertEquals(tn2.getParent(),tn1);
		tn2.setParent(tn4);
		assertEquals(tn2.getParent(),tn1);
		// impossible to make itself a parent
		tn2.setParent(tn2);
		assertEquals(tn2.getParent(),tn1);
		// this works because it is a fresh node
		TreeNode tn5 = new SimpleTreeNodeImpl(f.scope().newId("coucou"),f);
		assertNull(tn5.getParent());
		tn5.setParent(tn4);
		assertEquals(tn5.getParent(),tn4);
		// however this node was not properly connected: no addChild !
		// this is because the node must be constructed with treeFactory.makeTreeNode(...)
		assertFalse(TreeFactory.isInSubTree(tn4,tn5));
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
	void testAddChild() {
		// this wont have any effect because tn3 doesnt have tn2 as parent
		tn2.addChild(tn3);
		assertFalse(tn2.hasChild(tn3));
		// this wont have any effect because tn4 is already child of tn2
		assertTrue(tn2.hasChild(tn4));
		tn2.addChild(tn4);
		assertTrue(tn2.hasChild(tn4));
		// this works because it is a fresh node
		TreeNode tn5 = new SimpleTreeNodeImpl(f.scope().newId("coucou"),f);
		assertFalse(tn2.hasChild(tn5));
		tn2.addChild(tn5);
		assertTrue(tn2.hasChild(tn5));
		// however this node was not properly connected: no setParent !
		// this is because the node must be constructed with treeFactory.makeTreeNode(...)
		assertFalse(tn5.getParent()==tn2);
	}

	@Test
	void testSetChildrenTreeNodeArray() {
		TreeNode tn5 = new SimpleTreeNodeImpl(f.scope().newId("coucou"),f);
		TreeNode tn6 = new SimpleTreeNodeImpl(f.scope().newId("coucou"),f);
		tn3.setChildren(tn5,tn6);
		assertTrue(tn3.hasChild(tn5));
		assertTrue(tn3.hasChild(tn6));
	}

	@Test
	void testSetChildrenIterableOfTreeNode() {
		TreeNode tn5 = new SimpleTreeNodeImpl(f.scope().newId("coucou"),f);
		TreeNode tn6 = new SimpleTreeNodeImpl(f.scope().newId("coucou"),f);
		List<TreeNode> l = new LinkedList<TreeNode>();
		l.add(tn6); l.add(tn5);
		Iterable<TreeNode> i = l;
		tn3.setChildren(i);
		assertTrue(tn3.hasChild(tn5));
		assertTrue(tn3.hasChild(tn6));
	}

	@Test
	void testSetChildrenCollectionOfTreeNode() {
		TreeNode tn5 = new SimpleTreeNodeImpl(f.scope().newId("coucou"),f);
		TreeNode tn6 = new SimpleTreeNodeImpl(f.scope().newId("coucou"),f);
		Collection<TreeNode> l = new LinkedList<TreeNode>();
		l.add(tn6); l.add(tn5);
		tn3.setChildren(l);
		assertTrue(tn3.hasChild(tn5));
		assertTrue(tn3.hasChild(tn6));
	}

	@Test
	void testHasChildren() {
		assertTrue(tn1.hasChildren());
		assertFalse(tn4.hasChildren());
	}

	@Test
	void testId() {
		assertEquals(tn2.id(),"treenode1");
	}

	@Test
	void testToUniqueString() {
		show("testToUniqueString",tn1.toUniqueString());
		assertEquals(tn1.toUniqueString(),"DTF:treenode0");
	}

	@Test
	void testToShortString() {
		show("testToShortString",tn1.toShortString());
		assertEquals(tn1.toShortString(),"SimpleTreeNodeImpl:treenode0");
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",tn2.toDetailedString());
		assertEquals(tn2.toDetailedString(),"SimpleTreeNodeImpl:treenode1 ↑SimpleTreeNodeImpl:treenode0 ↓SimpleTreeNodeImpl:treenode3");
	}

	@Test
	void testTreeNodeFactory() {
		assertEquals(tn3.treeNodeFactory(),f);
	}

	@Test
	void testToString() {
		show("testToString",tn2.toString());
		assertEquals(tn2.toString(),"[SimpleTreeNodeImpl:treenode1 ↑SimpleTreeNodeImpl:treenode0 ↓SimpleTreeNodeImpl:treenode3]");
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
		TreeNode tn5 = new SimpleTreeNodeImpl(f.scope().newId("coucou"),f);
		tn5.setParent(tn1);
		assertTrue(tn5.equals(tn3));
	}

	@Test
	void testNChildren() {
		assertEquals(tn1.nChildren(),2);
	}
	
	@Test
	void testScope() {
		assertEquals(tn4.scope(),f.scope());
	}
}

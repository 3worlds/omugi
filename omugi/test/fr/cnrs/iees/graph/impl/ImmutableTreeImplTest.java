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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;

class ImmutableTreeImplTest {

	private TreeNode tn1 = null;
	private TreeNode tn2 = null;
	private TreeNode tn3 = null;
	private Tree<TreeNode> tree = null;
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}

	@BeforeEach
	private void init() {
		TreeNodeFactory tnf = new TreeFactory();
		tn1 = tnf.makeTreeNode(null);
		tn2 = tnf.makeTreeNode(tn1);
		tn3 = tnf.makeTreeNode(tn1);
		List<TreeNode> l = new ArrayList<TreeNode>();
		l.add(tn3); l.add(tn2); l.add(tn1);
		tree = new ImmutableTreeImpl<TreeNode>(l);
	}
	
	@Test
	void testImmutableTreeImpl() {
		Tree<TreeNode> tree = new ImmutableTreeImpl<TreeNode>();
		assertNotNull(tree);
		assertEquals(tree.size(),0);
		show("testImmutableTreeImpl",tree.toString());
	}

	@Test
	void testImmutableTreeImplIterableOfN() {
		TreeNodeFactory tnf = new TreeFactory();
		TreeNode tn1 = tnf.makeTreeNode(null);
		TreeNode tn2 = tnf.makeTreeNode(tn1);
		TreeNode tn3 = tnf.makeTreeNode(tn1);
		List<TreeNode> l = new ArrayList<TreeNode>();
		l.add(tn3); l.add(tn2); l.add(tn1);
		Tree<TreeNode> tree = new ImmutableTreeImpl<TreeNode>(l);
		assertNotNull(tree);
		assertEquals(tree.size(),3);
		show("testImmutableTreeImpl",tree.toString());
	}

	@Test
	void testImmutableTreeImplN() {
		fail("Not yet implemented");
	}

	@Test
	void testSize() {
		fail("Not yet implemented");
	}

	@Test
	void testNodes() {
		fail("Not yet implemented");
	}

	@Test
	void testRoot() {
		assertTrue(tree.root().equals(tn1));
	}

	@Test
	void testLeaves() {
		fail("Not yet implemented");
	}

	@Test
	void testComputeDepths() {
		fail("Not yet implemented");
	}

	@Test
	void testMaxDepth() {
		fail("Not yet implemented");
	}

	@Test
	void testMinDepth() {
		fail("Not yet implemented");
	}

	@Test
	void testSubTree() {
		fail("Not yet implemented");
	}

	@Test
	void testFindNodesByReference() {
		fail("Not yet implemented");
	}

	@Test
	void testToUniqueString() {
		fail("Not yet implemented");
	}

	@Test
	void testToShortString() {
		fail("Not yet implemented");
	}

	@Test
	void testToDetailedString() {
		fail("Not yet implemented");
	}

	@Test
	void testToString() {
		fail("Not yet implemented");
	}

}

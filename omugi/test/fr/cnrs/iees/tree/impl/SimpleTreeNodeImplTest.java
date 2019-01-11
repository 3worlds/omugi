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
package fr.cnrs.iees.tree.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.tree.TreeNode;

/**
 * TODO: implement all tests
 * 
 * @author Jacques Gignoux - 9 janv. 2019
 *
 */
class SimpleTreeNodeImplTest {
	
	@SuppressWarnings("unused")
	private TreeNode tn1, tn2, tn3, tn4;
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@BeforeEach
	private void init() {
		DefaultTreeFactory f = new DefaultTreeFactory();
		tn1 = f.makeTreeNode(null);
		tn2 = f.makeTreeNode(tn1);
		tn3 = f.makeTreeNode(tn1);
		tn4 = f.makeTreeNode(tn2);
	}

	@Test
	void testGetParent() {
		fail("Not yet implemented");
	}

	@Test
	void testSetParent() {
	
		fail("Not yet implemented");
	}

	@Test
	void testGetChildren() {
		fail("Not yet implemented");
	}

	@Test
	void testAddChild() {
		fail("Not yet implemented");
	}

	@Test
	void testSetChildrenTreeNodeArray() {
		fail("Not yet implemented");
	}

	@Test
	void testSetChildrenIterableOfTreeNode() {
		fail("Not yet implemented");
	}

	@Test
	void testSetChildrenCollectionOfTreeNode() {
		fail("Not yet implemented");
	}

	@Test
	void testHasChildren() {
		fail("Not yet implemented");
	}

	@Test
	void testInstanceId() {
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
	void testTreeNodeFactory() {
		fail("Not yet implemented");
	}

	@Test
	void testToString() {
		fail("Not yet implemented");
	}

	@Test
	void testEqualsObject() {
		show("testEqualsObject",tn2.toDetailedString());
		show("testEqualsObject",tn3.toDetailedString());
		assertFalse(tn2.equals(tn3));
	}

}

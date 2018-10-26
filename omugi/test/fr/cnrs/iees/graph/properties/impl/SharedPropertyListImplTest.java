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
package fr.cnrs.iees.graph.properties.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.graph.property.PropertyKeys;
import fr.cnrs.iees.graph.properties.SimplePropertyList;

class SharedPropertyListImplTest {
	
	private SimplePropertyList sp1=null, sp2=null, sp3=null;
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@BeforeEach
	private void init() {
		PropertyKeys keys = new PropertyKeys("int1","double2","triple3");
		sp1 = new SharedPropertyListImpl(keys);
		sp2 = new SharedPropertyListImpl("int1","double2","triple3");
		sp3 = new SharedPropertyListImpl(keys);
	}

	@Test
	void testSharedPropertyListImplPropertyKeys() {
		show("testSharedPropertyListImplPropertyKeys",sp1.toString());
		assertNotNull(sp1);
	}

	@Test
	void testSharedPropertyListImplSimplePropertyList() {
		sp3 = new SharedPropertyListImpl(sp1);
		show("testSharedPropertyListImplSimplePropertyList",sp3.toString());
		assertNotNull(sp3);
	}

	@Test
	void testSharedPropertyListImplStringArray() {
		show("testSharedPropertyListImplStringArray",sp2.toString());
		assertNotNull(sp2);
	}

	@Test
	void testSetProperty() {
		sp1.setProperty("int1", 1);
		assertEquals(sp1.getPropertyValue("int1"),1);
		try {
			sp1.setProperty("int3",67);
			fail("Key not found exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
	}

	@Test
	void testGetPropertyValue() {
		sp1.setProperty("triple3", false);
		assertEquals(sp1.getPropertyValue("triple3"),false);
		try {
			sp1.getProperty("notThere");
			fail("Key not found exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
	}

	@Test
	void testHasProperty() {
		assertTrue(sp1.hasProperty("double2"));
		assertFalse(sp2.hasProperty("notThere"));
	}

	@Test
	void testGetKeysAsSet() {
		assertEquals(sp1.getKeysAsSet(),sp2.getKeysAsSet());
	}

	@Test
	void testGetKeysAsArray() {
		assertFalse(sp1.getKeysAsArray()==sp2.getKeysAsArray());
		assertTrue(sp1.getKeysAsArray()==sp3.getKeysAsArray());
	}

	
	@Test
	void testClone() {
		SimplePropertyList sp4 = sp1.clone();
		show("testClone",sp4.toString());
		assertTrue(sp1.getKeysAsArray()==sp4.getKeysAsArray());
	}

	@Test
	void testClear() {
		String[] keys = {"int1","double2","triple3"};
		Object[] values = {1,12.8,false};
		sp2.setProperties(keys, values);
		show("testClear",sp2.toString());
		sp2.clear();
		show("testClear",sp2.toString());
		assertEquals(sp2.getPropertyValue("triple3"),null);
	}

	@Test
	void testSize() {
		assertEquals(sp3.size(),3);
	}

}

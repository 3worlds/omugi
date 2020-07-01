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
package fr.cnrs.iees.properties.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.graph.property.PropertyKeys;
import fr.cnrs.iees.properties.SimpleWriteProtectablePropertyList;

class SharedWriteProtectablePropertyListImplTest {

	private SimpleWriteProtectablePropertyList sp1=null, sp2=null, sp3=null;
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@BeforeEach
	private void init() {
		PropertyKeys keys = new PropertyKeys("int1","double2","triple3");
		sp1 = new SharedWriteProtectablePropertyListImpl(keys);
		sp2 = new SharedWriteProtectablePropertyListImpl("int1","double2","triple3");
		sp3 = new SharedWriteProtectablePropertyListImpl(keys);
	}
	
	@Test
	void testSharedWriteProtectablePropertyListImplPropertyKeys() {
		show("testSharedPropertyListImplPropertyKeys",sp1.toString());
		assertNotNull(sp1);
	}

	@Test
	void testSharedWriteProtectablePropertyListImplSimplePropertyList() {
		sp3 = new SharedWriteProtectablePropertyListImpl(sp1);
		show("testSharedPropertyListImplSimplePropertyList",sp3.toString());
		assertNotNull(sp3);
	}

	@Test
	void testSharedWriteProtectablePropertyListImplStringArray() {
		show("testSharedPropertyListImplStringArray",sp2.toString());
		assertNotNull(sp2);
	}

	@Test
	void testSetPropertyStringObject() {
		sp1.setProperty("int1", 1);
		show("testSetPropertyStringObject",sp1.toString());
		assertEquals(sp1.getPropertyValue("int1"),1);
		sp1.writeDisable();
		sp1.setProperty("double2",67.84829479723);
		show("testSetPropertyStringObject",sp1.toString());
		assertEquals(sp1.getPropertyValue("double2"),null);
		sp1.writeEnable();
		sp1.setProperty("double2",67.84829479723);
		show("testSetPropertyStringObject",sp1.toString());
		assertEquals(sp1.getPropertyValue("double2"),67.84829479723);
	}

	@Test
	void testIsReadOnly() {
		assertFalse(sp1.isReadOnly());
	}

	@Test
	void testWriteEnable() {
		sp1.writeDisable();
		assertTrue(sp1.isReadOnly());
		sp1.writeEnable();
		assertFalse(sp1.isReadOnly());
	}

	@Test
	void testWriteDisable() {
		sp1.writeDisable();
		assertTrue(sp1.isReadOnly());
	}

}

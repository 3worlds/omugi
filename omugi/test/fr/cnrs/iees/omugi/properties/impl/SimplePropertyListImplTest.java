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
package fr.cnrs.iees.omugi.properties.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.omugi.graph.property.Property;
import fr.cnrs.iees.omugi.properties.SimplePropertyList;

class SimplePropertyListImplTest {

	private SimplePropertyList sp1 = null, sp2=null;

	private void show(String method,String text) {
//		System.out.println(method+": "+text);
	}

	@Test
	void testSimplePropertyListImplSimplePropertyList() {
		sp1 = new SimplePropertyListImpl("int1","int2","string1");
		sp2 = new SimplePropertyListImpl(sp1);
		show("testSimplePropertyListImplSimplePropertyList",sp1.toString());
		show("testSimplePropertyListImplSimplePropertyList",sp2.toString());
		assertNotNull(sp2);		
	}

	@Test
	void testSimplePropertyListImplPropertyArray() {
		Property p1 = new Property("int1",12);
		Property p2 = new Property("int2",13);
		Property p3 = new Property("string1","parrot");
		sp1 = new SimplePropertyListImpl(p1,p2,p3);
		show("testSimplePropertyListImplPropertyArray",sp1.toString());
		assertNotNull(sp1);
	}

	@Test
	void testSimplePropertyListImplListOfStringListOfObject() {
		List<String> set = new ArrayList<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		List<Object> list = new LinkedList<Object>();
		list.add(12); list.add(13);	list.add("parrot");
		sp1 = new SimplePropertyListImpl(set,list);
		show("testSimplePropertyListImplListOfStringListOfObject",sp1.toString());
		assertNotNull(sp1);
	}

	@Test
	void testSimplePropertyListImplCollectionOfString() {
		Set<String> set = new HashSet<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		sp1 = new SimplePropertyListImpl(set);
		show("testSimplePropertyListImplCollectionOfString",sp1.toString());
		assertNotNull(sp1);
	}

	@Test
	void testSimplePropertyListImplStringArray() {
		sp1 = new SimplePropertyListImpl("int1","int2","string1");
		sp2 = new SimplePropertyListImpl("double1","long1","string2");
		show("testSimplePropertyListImplStringArray",sp1.toString());
		show("testSimplePropertyListImplStringArray",sp2.toString());
		assertNotNull(sp1);
		assertNotNull(sp2);
	}

	@Test
	void testSimplePropertyListImpl() {
		sp1 = new SimplePropertyListImpl();
		show("testSimplePropertyListImpl",sp1.toString());
		assertNotNull(sp1);
	}

	@Test
	void testSetProperty() {
		sp1 = new SimplePropertyListImpl("int1","int2","string1");
		sp2 = new SimplePropertyListImpl("double1","long1","string2");
		sp1.setProperty("int2", 2451);
		show("testSetProperty",sp1.toString());
		assertEquals(sp1.getPropertyValue("int2"), 2451);
		Property prop = new Property("string2","coucou");
		sp2.setProperty(prop);
		show("testSetProperty",sp2.toString());
		assertEquals(sp2.getPropertyValue("string2"),"coucou");
	}

	@Test
	void testGetPropertyValue() {
		List<String> set = new ArrayList<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		List<Object> list = new LinkedList<Object>();
		list.add(12); list.add(13);	list.add("parrot");
		sp1 = new SimplePropertyListImpl(set,list);
		show("testGetPropertyValue",sp1.toString());
		assertEquals(sp1.getPropertyValue("string1"),"parrot");
		assertEquals(sp1.getPropertyValue("int1"),12);
		assertEquals(sp1.getPropertyValue("int2"),13);
	}

	@Test
	void testGetKeysAsSet() {
		sp1 = new SimplePropertyListImpl("int1","int2","string1");
		Set<String> set1 = sp1.getKeysAsSet();
		show("testGetKeysAsSet",set1.toString());
		List<String> set = new ArrayList<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		assertTrue(set1.containsAll(set));
	}

	@Test
	void testClone() {
		sp2 = new SimplePropertyListImpl("double1","long1","string2");
		sp2.setProperty("long1", Long.MAX_VALUE);
		sp1 = (SimplePropertyList) sp2.clone();
		show("testClone",sp1.toString());
		assertEquals(sp1.getPropertyValue("long1"),Long.MAX_VALUE);
	}

	@Test
	void testHasProperty() {
		sp2 = new SimplePropertyListImpl("double1","long1","string2");
		assertTrue(sp2.hasProperty("double1"));
		assertFalse(sp2.hasProperty("double2"));
	}

	@Test
	void testSize() {
		sp2 = new SimplePropertyListImpl("double1","long1","string2");
		assertEquals(sp2.size(),3);
	}
	
	@Test
	void testClear() {
		List<String> set = new ArrayList<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		List<Object> list = new LinkedList<Object>();
		list.add(12); list.add(13);	list.add("parrot");
		sp1 = new SimplePropertyListImpl(set,list);
		sp1.clear();
		show("testClear",sp1.toString());
		assertEquals(sp1.toString(),"int1=null int2=null string1=null");
	}

	@Test
	void testToString() {
		List<String> set = new ArrayList<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		List<Object> list = new LinkedList<Object>();
		list.add(12); list.add(13);	list.add("parrot");
		sp1 = new SimplePropertyListImpl(set,list);
		show("testToString",sp1.toString());
		assertEquals(sp1.toString(),"int1=12 int2=13 string1=parrot");
	}

}

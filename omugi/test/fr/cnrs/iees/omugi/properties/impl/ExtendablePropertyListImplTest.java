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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.omugi.graph.property.Property;
import fr.cnrs.iees.omugi.properties.ExtendablePropertyList;

class ExtendablePropertyListImplTest {
	
	private ExtendablePropertyList ep1=null, ep2=null;
	
	private void show(String method,String text) {
//		System.out.println(method+": "+text);
	}
	
	@BeforeEach
	private void init() {
		List<String> set = new ArrayList<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		List<Object> list = new LinkedList<Object>();
		list.add(12); list.add(13);	list.add("parrot");
		ep1 = new ExtendablePropertyListImpl(set,list);
		ep2 = new ExtendablePropertyListImpl("double1","long1","string2");
	}

	@Test
	void testExtendablePropertyListImpl() {
		ep1 = new ExtendablePropertyListImpl();
		show("testExtendablePropertyListImpl",ep1.toString());
		assertNotNull(ep1);
	}

	@Test
	void testExtendablePropertyListImplStringArray() {
		ep1 = new ExtendablePropertyListImpl("int1","int2","string1");
		ep2 = new ExtendablePropertyListImpl("double1","long1","string2");
		show("testExtendablePropertyListImplStringArray",ep1.toString());
		show("testExtendablePropertyListImplStringArray",ep2.toString());
		assertNotNull(ep1);
		assertNotNull(ep2);
	}

	@Test
	void testExtendablePropertyListImplListOfStringListOfObject() {
		List<String> set = new ArrayList<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		List<Object> list = new LinkedList<Object>();
		list.add(12); list.add(13);	list.add("parrot");
		ep1 = new ExtendablePropertyListImpl(set,list);
		show("testExtendablePropertyListImplListOfStringListOfObject",ep1.toString());
		assertNotNull(ep1);
	}

	@Test
	void testExtendablePropertyListImplCollectionOfString() {
		Set<String> set = new HashSet<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		ep1 = new ExtendablePropertyListImpl(set);
		show("testExtendablePropertyListImplCollectionOfString",ep1.toString());
		assertNotNull(ep1);
	}

	@Test
	void testExtendablePropertyListImplPropertyArray() {
		Property p1 = new Property("int1",12);
		Property p2 = new Property("int2",13);
		Property p3 = new Property("string1","parrot");
		ep1 = new ExtendablePropertyListImpl(p1,p2,p3);
		show("testExtendablePropertyListImplPropertyArray",ep1.toString());
		assertNotNull(ep1);
	}

	@Test
	void testExtendablePropertyListImplSimplePropertyList() {
		ep1 = new ExtendablePropertyListImpl("int1","int2","string1");
		ep2 = new ExtendablePropertyListImpl(ep1);
		show("testExtendablePropertyListImplSimplePropertyList",ep1.toString());
		show("testExtendablePropertyListImplSimplePropertyList",ep2.toString());
		assertNotNull(ep2);		
	}

	@Test
	void testSetPropertyStringObject() {
		// correct setting
		ep1.setProperty("int2", 853920);
		show("testSetPropertyStringObject",ep1.toString());
		assertEquals(ep1.getPropertyValue("int2"),853920);
		// wrong setting (key doesnt exist in property list) - should fail.
		try {
			ep1.setProperty("int3",67);
			fail("Key not found exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
	}

	@Test
	void testAddPropertyProperty() {
		Property prop = new Property("stuff", new ArrayList<String>());
		ep1.addProperty(prop);
		show("testAddPropertyProperty",ep1.toString());
		assertEquals(ep1.size(),4);
		assertNotNull(ep1.getPropertyValue("stuff"));
		ep1.seal();
		try {
			ep1.addProperty(new Property("int4",68));
			fail("Sealed property list exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
	}

	@Test
	void testAddPropertyStringObject() {
		ep1.addProperty("stuff",true);
		show("testAddPropertyStringObject",ep1.toString());
		assertEquals(ep1.size(),4);
		assertEquals(ep1.getPropertyValue("stuff"),true);
		ep1.seal();
		try {
			ep1.addProperty("int4",68);
			fail("Sealed property list exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
	}

	@Test
	void testAddPropertyString() {
		ep1.addProperty("stuff");
		show("testAddPropertyString",ep1.toString());
		assertEquals(ep1.size(),4);
		assertNotNull(ep1.getPropertyValue("stuff"));
		ep1.seal();
		try {
			ep1.addProperty("int4");
			fail("Sealed property list exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
	}

	@Test
	void testAddPropertiesListOfString() {
		List<String> ss = new ArrayList<>();
		ss.add("un"); ss.add("deux"); ss.add("trois");
		ep2.addProperties(ss);
		show("testAddPropertiesListOfString",ep2.toString());
		assertEquals(ep2.size(),6);
		ep2.seal();
		try {
			ep2.addProperty("int4");
			fail("Sealed property list exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
	}

	@Test
	void testAddPropertiesStringArray() {
		ep2.addProperties("un","deux","trois");
		show("testAddPropertiesStringArray",ep2.toString());
		assertEquals(ep2.size(),6);
		ep2.seal();
		try {
			ep2.addProperty("int4");
			fail("Sealed property list exception not raised");
		}
		catch (Exception e) {
			// test OK
		}		
	}

	@Test
	void testAddPropertiesReadOnlyPropertyList() {
		ep1.addProperties(ep2);
		show("testAddPropertiesReadOnlyPropertyList",ep1.toString());
		assertEquals(ep1.size(),6);
		ep1.seal();
		try {
			ep1.addProperty("int4");
			fail("Sealed property list exception not raised");
		}
		catch (Exception e) {
			// test OK
		}		
	}

	@Test
	void testGetPropertyValueStringObject() {
		int x = (int) ep1.getPropertyValue("int1",12);
		assertEquals(x,12);
		assertEquals(ep1.getPropertyValue("string1","birdy"),"parrot");
	}

	@Test
	void testRemoveProperty() {
		ep1.removeProperty("int1");
		show("testRemoveProperty",ep1.toString());
		assertEquals(ep1.size(),2);
		ep2.removeProperty("lambda");
		show("testRemoveProperty",ep2.toString());
		assertEquals(ep2.size(),3);
		ep2.seal();
		try {
			ep2.removeProperty("double1");
			fail("Sealed property list exception not raised");
		}
		catch (Exception e) {
			// test OK
		}		
	}

	@Test
	void testRemoveAllProperties() {
		ep1.removeAllProperties();
		show("testRemoveAllProperties",ep1.toString());
		ep1.seal();
		try {
			ep1.addProperty("double1");
			fail("Sealed property list exception not raised");
		}
		catch (Exception e) {
			// test OK
		}		
	}

	@Test
	void testSeal() {
		ep1.seal();
		try {
			ep1.addProperties(ep2);
			fail("Sealed property list exception not raised");
		}
		catch (Exception e) {
			// test OK
		}		
	}

	@Test
	void testIsSealed() {
		assertFalse(ep1.isSealed());
		ep2.seal();
		assertTrue(ep2.isSealed());
	}

	@Test
	void testClone() {
		ep2 = (ExtendablePropertyList) ep1.clone();
		show("testClone",ep2.toString());
		assertEquals(ep1.getPropertyValue("int2"),ep2.getPropertyValue("int2"));
	}

}

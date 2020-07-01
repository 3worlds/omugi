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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.properties.ExtendablePropertyList;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

class CompoundPropertyListImplTest {
	
	// need a readonly property list to do the tests properly
	
	private ExtendablePropertyList ep1=null, ep2=null;
	private SimplePropertyList sp1=null, cp=null;
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@BeforeEach
	private void init() {
		List<String> set = new ArrayList<String>();
		set.add("int1"); set.add("int2"); set.add("string1");
		List<Object> list = new LinkedList<Object>();
		list.add(12); list.add(13);	list.add("parrot");
		ep1 = new ExtendablePropertyListImpl(set,list);
		ep2 = new ExtendablePropertyListImpl("double1","long1","string2");
		sp1 = new SimplePropertyListImpl("int3","int4","string3");
		ReadOnlyPropertyList[] pls = {ep1,ep2,sp1};
		String[] names = {"list1","list2","list3"};
		cp = new CompoundPropertyListImpl(pls,names) ;
	}

	@Test
	void testCompoundPropertyListImpl() {
		show("testCompoundPropertyListImpl",cp.toString());
		assertNotNull(cp);
		assertEquals(cp.toString(),"list1:int1=12 list1:int2=13 list1:string1=parrot "
			+ "list3:int3=null list3:int4=null list3:string3=null "
			+ "list2:double1=null list2:long1=null list2:string2=null");		
	}

	@Test
	void testSetProperty() {
		cp.setProperty("list2:long1",12909798623982986L);
		assertEquals(cp.getPropertyValue("list2:long1"),12909798623982986L);
		ep2.setProperty("double1",0.0);
		assertEquals(cp.getPropertyValue("list2:double1"),0.0);
	}

	@Test
	void testSetProperties() {
		sp1.setProperty("int3", 26);
		sp1.setProperty("int4", 87);
		sp1.setProperty("string3", "eighty-three");
		cp.setProperties(sp1);
		show("testSetProperties",cp.toString());
		assertEquals(cp.getPropertyValue("list3:string3"),"eighty-three");
	}

	@Test
	void testGetProperty() {
		Property prop = cp.getProperty("list1:string1");
		assertEquals(prop.getValue(),"parrot");
	}

	@Test
	void testGetPropertyValue() {
		assertEquals(cp.getPropertyValue("list1:string1"),"parrot");
		assertEquals(cp.getPropertyValue("string1"),null);
		assertEquals(cp.getPropertyValue("list1"),null);
	}

	@Test
	void testHasProperty() {
		assertTrue(cp.hasProperty("list3:int3"));
		assertFalse(cp.hasProperty("int3"));
	}

	@Test
	void testGetKeysAsSet() {
		Set<String> set = cp.getKeysAsSet();
		show("testGetKeysAsSet",set.toString());
		assertTrue(set.contains("list2:double1"));
	}

	@Test
	void testClone() {
		SimplePropertyList cp2 = cp.clone();
		show("testClone",cp2.toString());
		assertEquals(cp2.getPropertyValue("list2:double1"),null);
		assertEquals(cp.getKeysAsSet(),cp2.getKeysAsSet());
	}

	@Test
	void testClear() {
		cp.clear();
		assertEquals(cp.getPropertyValue("list1:string1"),null);
	}

	@Test
	void testSize() {
		assertEquals(cp.size(),9);
	}

	@Test
	void testHasTheSamePropertiesAs() {
		SimplePropertyList cp2 = cp.clone();
		assertFalse(cp.hasTheSamePropertiesAs(ep1));
		assertTrue(cp2.hasTheSamePropertiesAs(cp));		
	}

}

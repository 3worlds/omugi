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
package fr.cnrs.iees.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.DataNode;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.impl.DataNodeImpl;
import fr.cnrs.iees.graph.impl.GraphFactory;
import fr.cnrs.iees.graph.impl.SimpleNodeImpl;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;

class DataNodeImplTest {
	
	private DataNode n1, n3;
	private Node n2;
	private SimplePropertyList p;
	private NodeFactory f = new GraphFactory();
	
	@BeforeEach
	private void init() {
		p = new SimplePropertyListImpl("prop1","prop2");
		p.setProperty("prop1", "coucou");
		n1 = (DataNode) f.makeNode(p);
		n2 = f.makeNode();
		n3 = (DataNode) f.makeNode(p);
	}
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}

	@Test
	void testDataNodeImplGraphElementFactorySimplePropertyList() {
		assertNotNull(n1);
		assertEquals(n1.getClass(),DataNodeImpl.class);		
	}

	@Test
	void testDataNodeImplIntGraphElementFactorySimplePropertyList() {
		assertNotNull(n2);
		assertNotNull(n3);
		assertEquals(n2.getClass(),SimpleNodeImpl.class);
		assertEquals(n3.getClass(),DataNodeImpl.class);
	}

	@Test
	void testSetProperty() {
		n1.properties().setProperty("prop1", 12);
		n1.properties().setProperty("prop2",25.34);
		show("testSetProperty",n1.toDetailedString());
		assertEquals(n1.properties().getPropertyValue("prop1"),12);
		assertEquals(n1.properties().getPropertyValue("prop2"),25.34);
	}

	@Test
	void testClone() {
//		Node n = n1.clone();
	}

	@Test
	void testGetPropertyValue() {
		assertEquals(n3.properties().getPropertyValue("prop1"),"coucou");
	}

	@Test
	void testHasProperty() {
		assertTrue(n3.properties().hasProperty("prop1"));
		assertFalse(n3.properties().hasProperty("prop3"));
	}

	@Test
	void testGetKeysAsSet() {
		show("testGetKeysAsSet",n1.properties().getKeysAsSet().toString());
		assertEquals(n1.properties().getKeysAsSet().toString(),"[prop1, prop2]");
	}

	@Test
	void testSize() {
		assertEquals(n1.properties().size(),2);
	}

	@Test
	void testClear() {
		assertEquals(n3.properties().getPropertyValue("prop1"),"coucou");
		n3.properties().clear();
		assertEquals(n3.properties().getPropertyValue("prop1"),null);
	}

}

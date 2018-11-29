package fr.cnrs.iees.graph.generic.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.generic.DataNode;
import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.properties.SimplePropertyList;
import fr.cnrs.iees.graph.properties.impl.SimplePropertyListImpl;

class DataNodeImplTest {
	
	private DataNode n1, n3;
	private Node n2;
	private SimplePropertyList p;
	private GraphElementFactory f = new DefaultGraphFactory(3);
	
	@BeforeEach
	private void init() {
		p = new SimplePropertyListImpl("prop1","prop2");
		p.setProperty("prop1", "coucou");
		n1 = f.makeNode(p);
		n2 = f.makeNode();
		n3 = f.makeNode(p);
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
		n1.setProperty("prop1", 12);
		n1.setProperty("prop2",25.34);
		show("testSetProperty",n1.toDetailedString());
		assertEquals(n1.getPropertyValue("prop1"),12);
		assertEquals(n1.getPropertyValue("prop2"),25.34);
	}

	@Test
	void testClone() {
		Node n = n1.clone();
	}

	@Test
	void testGetPropertyValue() {
		assertEquals(n3.getPropertyValue("prop1"),"coucou");
	}

	@Test
	void testHasProperty() {
		assertTrue(n3.hasProperty("prop1"));
		assertFalse(n3.hasProperty("prop3"));
	}

	@Test
	void testGetKeysAsSet() {
		show("testGetKeysAsSet",n1.getKeysAsSet().toString());
		assertEquals(n1.getKeysAsSet().toString(),"[prop1, prop2]");
	}

	@Test
	void testSize() {
		assertEquals(n1.size(),2);
	}

	@Test
	void testClear() {
		assertEquals(n3.getPropertyValue("prop1"),"coucou");
		n3.clear();
		assertEquals(n3.getPropertyValue("prop1"),null);
	}

}

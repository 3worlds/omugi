package fr.cnrs.iees.graph.generic.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.graph.generic.Direction;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Node;

class SimpleEdgeImplTest {

	Node n1;
	SimpleNodeImpl n2, n3, n4;
	SimpleEdgeImpl e1, e2, e3, e4, e5;
	Map<Uid,String> nodes;
	
	// little test graph:
	//
	//              e3
	//              ||
	//              v|
	//  n1 ---e1--> n2 ---e4--> n3 ---e5--> n4
	//     <--e2--- 
	
	@BeforeEach
	private void init() {
		SimpleGraphFactory f = new SimpleGraphFactory();
		nodes = new HashMap<Uid,String>();
		n1 = f.makeNode();
		nodes.put(n1.getId(), "n1");
		n2 = f.makeNode(2);
		nodes.put(n2.getId(), "n2");
		n3 = f.makeNode(2);
		nodes.put(n3.getId(), "n3");
		n4 = f.makeNode(1);
		nodes.put(n4.getId(), "n4");
		e1 = f.makeEdge(n1,n2);
		e2 = f.makeEdge(n2,n1);
		e3 = f.makeEdge(n2,n2);
		e4 = f.makeEdge(n2,n3);
		e5 = f.makeEdge(n3,n4);
	}

	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@Test
	void testSimpleEdgeImpl() {
		assertNotNull(e1);
	}

	@Test
	void testClone() {
		Edge e = e2.clone();
		assertNotNull(e);
		assertEquals(e.startNode().getId(),e2.startNode().getId());
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",e5.toDetailedString());
		assertTrue(e5.toDetailedString().contains(n4.getId().toString()));
	}

	@Test
	void testDisconnect() {
		assertEquals(n2.degree(),5);
		e4.disconnect();
		assertEquals(n2.degree(),4);
	}

	@Test
	void testTraversalInt() {
//		for (Node n:e4.traversal(1))
//			show("testTraversalInt",nodes.get(n.getId()));
		assertTrue(e4.traversal(1).contains(n2));
		assertTrue(e4.traversal(1).contains(n3));
		assertFalse(e4.traversal(1).contains(n1));
		assertFalse(e4.traversal(1).contains(n4));

		assertTrue(e4.traversal(2).contains(n2));
		assertTrue(e4.traversal(2).contains(n3));
		assertTrue(e4.traversal(2).contains(n1));
		assertTrue(e4.traversal(2).contains(n4));

	}

	@Test
	void testTraversalIntDirection() {
//		for (Node n:e2.traversal(3,Direction.IN))
//			show("testTraversalIntDirection",nodes.get(n.getId()));
		assertTrue(e2.traversal(3,Direction.IN).contains(n1));
		assertTrue(e2.traversal(3,Direction.IN).contains(n2));
		assertFalse(e2.traversal(3,Direction.IN).contains(n3));

		assertTrue(e2.traversal(3,Direction.OUT).contains(n1));
		assertTrue(e2.traversal(3,Direction.OUT).contains(n2));
		assertTrue(e2.traversal(3,Direction.OUT).contains(n3));
	}

	@Test
	void testStartNode() {
		assertEquals(e1.startNode(),n1);
		assertEquals(e3.startNode(),n2);
	}

	@Test
	void testEndNode() {
		assertEquals(e1.endNode(),n2);
		assertEquals(e3.endNode(),n2);
	}

	@Test
	void testOtherNode() {
		assertEquals(e1.otherNode(n1),n2);
		assertEquals(e3.otherNode(n2),n2);
	}

	@Test
	void testSetStartNode() {
		e2.setStartNode(n1);
		assertEquals(e2.startNode(),n1);
	}

	@Test
	void testSetEndNode() {
		e2.setEndNode(n2);
		assertEquals(e2.endNode(),n2);
	}

	@Test
	void testToShortString() {
		show("testToShortString",e2.toShortString());
		assertTrue(e2.toShortString().contains(e2.getId().toString()));
	}

	@Test
	void testSetId() {
		e1.setId(Uid.nullUid());
		show("testSetId",e1.getId().toString());
		assertEquals(e1.getId().toString(),"000000000000-0000000000000000-0000");
	}

	@Test
	void testGetId() {
		show("testGetId",e1.getId().toString());
		e1.setId(Uid.nullUid());
		assertEquals(e1.getId().toString(),"000000000000-0000000000000000-0000");
	}

	@Test
	void testToString() {
		show("testToString",e2.toString());
		assertTrue(e2.toString().contains(n2.getId().toString()));
	}

}

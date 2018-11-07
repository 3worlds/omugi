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

class SimpleNodeImplTest {

	Node n1;
	SimpleNodeImpl n2, n3, n4;
	Edge e1, e2, e3, e4, e5;
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
	void testSimpleNodeImpl() {
		assertNotNull(n1);
	}

	@Test
	void testSimpleNodeImplInt() {
		assertNotNull(n2);
	}

	@Test
	void testGetEdges() {
		int n=0;
		for (Edge e:n1.getEdges()) { 
			show("testGetEdges",e.toDetailedString());
			n++;
		}
		assertEquals(n,2);
		n=0;
		for (Edge e:n2.getEdges()) { 
			show("testGetEdges",e.toShortString());
			n++;
		}
		assertEquals(n,4);
	}

	@Test
	void testTraversalInt() {
//		for (Node n:n2.traversal(5))
//			show("testTraversalInt",nodes.get(n.getId()));
		assertTrue(n1.traversal(1).contains(n1));
		
		assertTrue(n1.traversal(2).contains(n1));
		assertTrue(n1.traversal(2).contains(n2));
		
		assertEquals(n1.traversal(0).size(),0);

		assertTrue(n2.traversal(2).contains(n1));
		assertTrue(n2.traversal(2).contains(n2));
		assertTrue(n2.traversal(2).contains(n3));
		assertFalse(n2.traversal(2).contains(n4));
		
		assertTrue(n2.traversal(3).contains(n4));
	}

	@Test
	void testTraversalIntDirection() {
//		for (Node n:n2.traversal(2,Direction.OUT))
//			show("testTraversalIntDirection",nodes.get(n.getId()));
		assertTrue(n1.traversal(4,Direction.IN).contains(n2));
		assertFalse(n1.traversal(4,Direction.IN).contains(n3));
		assertTrue(n1.traversal(4,Direction.OUT).contains(n3));
		
		assertTrue(n4.traversal(2,Direction.IN).contains(n3));
		assertFalse(n4.traversal(2,Direction.IN).contains(n2));
		
		assertFalse(n2.traversal(2,Direction.IN).contains(n3));
		assertTrue(n2.traversal(2,Direction.OUT).contains(n3));
	}

	@Test
	void testClone() {
		Node n = n2.clone();
		show("testClone",n2.toShortString()+" cloned as "+n.toShortString());
		assertNotNull(n);
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",n3.toDetailedString());
		assertTrue(n3.toDetailedString().contains(n4.getId().toString()));
		assertTrue(n3.toDetailedString().contains(n2.getId().toString()));
		assertFalse(n3.toDetailedString().contains(n1.getId().toString()));
	}

	@Test
	void testAddEdgeEdgeDirection() {
		assertTrue(n1.addEdge(e1,Direction.OUT));
		assertFalse(n1.addEdge(e1,Direction.IN));
		assertFalse(n1.addEdge(e5,Direction.OUT));
	}

	@Test
	void testAddEdgeEdge() {
		assertTrue(n1.addEdge(e1));
		assertTrue(n1.addEdge(e2));
		assertFalse(n1.addEdge(e5));
	}

	@Test
	void testRemoveEdge() {
		assertTrue(n2.removeEdge(e2));
		assertFalse(n2.removeEdge(e2));
		assertTrue(n2.removeEdge(e3));
		assertFalse(n2.removeEdge(e3));
	}

	@Test
	void testDegree() {
		assertEquals(n1.degree(),2);
		assertEquals(n2.degree(),5);
		assertEquals(n4.degree(),1);
		assertEquals(n1.degree(Direction.IN),1);
		assertEquals(n2.degree(Direction.IN),2);
		assertEquals(n2.degree(Direction.OUT),3);
		assertEquals(n4.degree(Direction.OUT),0);
	}

	@Test
	void testIsLeaf() {
		assertTrue(n4.isLeaf());
		assertFalse(n1.isLeaf());
	}

	@Test
	void testIsRoot() {
		assertFalse(n1.isRoot());
		n1.removeEdge(e2);
		assertTrue(n1.isRoot());
	}

	@Test
	void testGetEdgesDirection() {
		int n=0;
		for (Edge e:n2.getEdges(Direction.IN)) { 
			show("testGetEdgesDirection",e.toDetailedString());
			n++;
		}
		assertEquals(n,2);
		n=0;
		for (Edge e:n2.getEdges(Direction.OUT)) { 
			show("testGetEdgesDirection",e.toShortString());
			n++;
		}
		assertEquals(n,3);
	}

	@Test
	void testDisconnect() {
		assertEquals(n2.degree(),5);
		n2.disconnect();
		assertEquals(n2.degree(),0);
	}

	@Test
	void testToShortString() {
		show("testToShortString",n4.toShortString());
		assertFalse(n4.toShortString().contains(e5.getId().toString()));
		assertTrue(n4.toShortString().contains(n4.getId().toString()));
	}

	@Test
	void testSetId() {
		n1.setId(Uid.nullUid());
		show("testSetId",n1.getId().toString());
		assertEquals(n1.getId().toString(),"000000000000-0000000000000000-0000");
	}

	@Test
	void testGetId() {
		show("testGetId",n1.getId().toString());
		n1.setId(Uid.nullUid());
		assertEquals(n1.getId().toString(),"000000000000-0000000000000000-0000");
	}

	@Test
	void testToString() {
		show("testToString",n4.toString());
		assertTrue(n4.toString().contains(e5.getId().toString()));
	}

}

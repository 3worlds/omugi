package fr.cnrs.iees.graph.generic.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.util.Uid;
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

	@Test
	void testSimpleEdgeImpl() {
		assertNotNull(e1);
	}

	@Test
	void testClone() {
		Edge e = e2.clone();
	}

	@Test
	void testNewInstance() {
		fail("Not yet implemented");
	}

	@Test
	void testToDetailedString() {
		fail("Not yet implemented");
	}

	@Test
	void testDisconnect() {
		fail("Not yet implemented");
	}

	@Test
	void testTraversalInt() {
		fail("Not yet implemented");
	}

	@Test
	void testTraversalIntDirection() {
		fail("Not yet implemented");
	}

	@Test
	void testStartNode() {
		fail("Not yet implemented");
	}

	@Test
	void testEndNode() {
		fail("Not yet implemented");
	}

	@Test
	void testOtherNode() {
		fail("Not yet implemented");
	}

	@Test
	void testSetStartNode() {
		fail("Not yet implemented");
	}

	@Test
	void testSetEndNode() {
		fail("Not yet implemented");
	}

	@Test
	void testToShortString() {
		fail("Not yet implemented");
	}

	@Test
	void testSetId() {
		fail("Not yet implemented");
	}

	@Test
	void testGetId() {
		fail("Not yet implemented");
	}

	@Test
	void testToString() {
		fail("Not yet implemented");
	}

}

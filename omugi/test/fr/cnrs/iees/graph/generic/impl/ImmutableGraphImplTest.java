package fr.cnrs.iees.graph.generic.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Node;

class ImmutableGraphImplTest {

	Node n1;
	SimpleNodeImpl n2, n3, n4;
	Edge e1, e2, e3, e4, e5;
	Map<Uid,String> nodes;
	ImmutableGraphImpl<Node,Edge> graph;
	
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
		List<Node> l = new LinkedList<Node>();
		l.add(n1); l.add(n2);
		l.add(n3); l.add(n4);
		graph = new ImmutableGraphImpl<Node,Edge>(l);
	}

	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@Test
	void testImmutableGraphImplIterableOfN() {
		assertNotNull(graph);
	}

	@Test
	void testImmutableGraphImplGraphImporterOfNE() {
		fail("Not yet implemented");
	}

	@Test
	void testNodes() {
		int i=0;
		for (Node n:graph.nodes()) {
			show("testNodes",nodes.get(n.getId()));
			i++;
		}
		assertEquals(i,4);
	}

	@Test
	void testEdges() {
		int i=0;
		for (Edge e:graph.edges()) {
			show("testEdges",e.getId().toString());
			i++;
		}
		assertEquals(i,5);
	}

	@SuppressWarnings("unused")
	@Test
	void testRoots() {
		int i=0;
		for (Node n:graph.roots()) 
			i++;
		assertEquals(i,0);
	}

	@SuppressWarnings("unused")
	@Test
	void testLeaves() {
		int i=0;
		for (Node n:graph.leaves()) 
			i++;
		assertEquals(i,1);
	}

	@Test
	void testContains() {
		assertTrue(graph.contains(n3));
		Node n = n2.clone();
		assertFalse(graph.contains(n));
	}

	@Test
	void testAdjacencyMatrix() {
		fail("Not yet implemented");
	}

	@Test
	void testIncidenceMatrix() {
		fail("Not yet implemented");
	}

	@Test
	void testFindNode() {
		Node n = graph.findNode(n2.getId());
		assertEquals(n.getId(),n2.getId());
		n = graph.findNode(Uid.nullUid());
		assertNull(n);
	}

	@Test
	void testFindEdge() {
		Edge e = graph.findEdge(e5.getId());
		assertEquals(e.getId(),e5.getId());
		e = graph.findEdge(Uid.nullUid());
		assertNull(e);
	}

	@Test
	void testSize() {
		assertEquals(graph.size(),4);
	}

	@Test
	void testToUniqueString() {
		show("testToUniqueString",graph.toUniqueString());
	}

	@Test
	void testToShortString() {
		show("testToShortString",graph.toShortString());
	}

	@Test
	void testToDetailedString() {
		show("testToDetailedString",graph.toDetailedString());
	}

	@Test
	void testToString() {
		show("testToString",graph.toString());
	}

}

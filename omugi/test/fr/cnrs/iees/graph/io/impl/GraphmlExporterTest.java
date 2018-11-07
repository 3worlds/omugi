package fr.cnrs.iees.graph.io.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.generic.impl.ImmutableGraphImpl;
import fr.cnrs.iees.graph.generic.impl.SimpleGraphFactory;
import fr.cnrs.iees.graph.generic.impl.SimpleNodeImpl;

class GraphmlExporterTest {
	
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
	void testGraphmlExporterFile() {
		File f = new File(System.getProperty("user.home")+File.separator+"bidon.xml");
		GraphmlExporter<Node,Edge> ge = new GraphmlExporter<>(f);
		assertNotNull(ge);
	}

	@Test
	void testGraphmlExporterString() {
		String s = System.getProperty("user.home")+File.separator+"bidon.xml";
		show("testGraphmlExporterString",s);
		GraphmlExporter<Node,Edge> ge = new GraphmlExporter<>(s);
		assertNotNull(ge);
	}

	@Test
	void testExportGraph() {
		String s = System.getProperty("user.home")+File.separator+"bidon.xml";
		GraphmlExporter<Node,Edge> ge = new GraphmlExporter<>(s);
		ge.exportGraph(graph);
	}

}

package fr.cnrs.iees.graph.io.impl;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.collections.tables.BooleanTable;
import au.edu.anu.rscs.aot.collections.tables.Dimensioner;
import au.edu.anu.rscs.aot.collections.tables.Table;
import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.graph.generic.DataEdge;
import fr.cnrs.iees.graph.generic.DataNode;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.generic.impl.DefaultGraphFactory;
import fr.cnrs.iees.graph.generic.impl.ImmutableGraphImpl;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.graph.properties.SimplePropertyList;
import fr.cnrs.iees.graph.properties.impl.SimplePropertyListImpl;

class OmugiGraphExporterTest {

	DefaultGraphFactory f = new DefaultGraphFactory(2);
	// first, a simple graph with no properties
	Node n1;
	Node n2, n3, n4;
	Edge e1, e2, e3, e4, e5;
	Map<Uid,String> nodes;
	ImmutableGraphImpl<Node,Edge> graph;
	// second, a graph with properties
	DataNode dn1, dn2, dn3, dn4;
	DataEdge de1, de2, de3, de4, de5;
	SimplePropertyList props = new SimplePropertyListImpl("one","two","three");
	SimplePropertyList prop2 = new SimplePropertyListImpl("four","five","one");
	ImmutableGraphImpl<DataNode,DataEdge> graph2;
	
	// little test graph:
	//
	//              e3
	//              ||
	//              v|
	//  n1 ---e1--> n2 ---e4--> n3 ---e5--> n4
	//     <--e2--- 
	
	@BeforeEach
	private void init() {
		// simple graph
		nodes = new HashMap<Uid,String>();
		n1 = f.makeNode();
		nodes.put(n1.getId(), "n1");
		n2 = f.makeNode();
		nodes.put(n2.getId(), "n2");
		n3 = f.makeNode();
		nodes.put(n3.getId(), "n3");
		n4 = f.makeNode();
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
		// property graph
		props.setProperty("one", 1.0F);
		props.setProperty("two", 2000L);
		prop2.setProperty("four", (short)4);
		Table t = new BooleanTable(new Dimensioner(3),new Dimensioner(2));
		prop2.setProperty("five", t);
		dn1 = f.makeNode(props);
		dn2 = f.makeNode(props);
		dn3 = f.makeNode(props);
		dn4 = f.makeNode(props);
		de1 = f.makeEdge(dn1,dn2,prop2);
		de2 = f.makeEdge(dn2,dn1,prop2);
		de3 = f.makeEdge(dn2,dn2,prop2);
		de4 = f.makeEdge(dn2,dn3,prop2);
		de5 = f.makeEdge(dn3,dn4,prop2);
		List<DataNode> l2 = new LinkedList<DataNode>();
		l2.add(dn1); l2.add(dn2);
		l2.add(dn3); l2.add(dn4);
		graph2 = new ImmutableGraphImpl<DataNode,DataEdge>(l2);
	}
	
	@Test
	void testExportGraph() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon.ugg";
		File f = new File(testfile);
		OmugiGraphExporter ge = new OmugiGraphExporter(f);
		ge.exportGraph(graph);
		testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon2.ugg";
		f = new File(testfile);
		ge = new OmugiGraphExporter(f);
		ge.exportGraph(graph2);
		// attempt to reimport graph
		GraphImporter gi = new OmugiGraphImporter(f);
		System.out.println(gi.getGraph().toString()); // doesnt work because of valueOf() arguments !
	}
	

}

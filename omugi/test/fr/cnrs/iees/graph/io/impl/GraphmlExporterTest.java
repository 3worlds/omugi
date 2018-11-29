package fr.cnrs.iees.graph.io.impl;

import static org.junit.jupiter.api.Assertions.*;

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
import fr.cnrs.iees.graph.generic.impl.ImmutableGraphImpl;
import fr.cnrs.iees.graph.properties.SimplePropertyList;
import fr.cnrs.iees.graph.properties.impl.SimplePropertyListImpl;
import fr.cnrs.iees.graph.generic.impl.DefaultGraphFactory;

class GraphmlExporterTest {

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
		s = System.getProperty("user.home")+File.separator+"bidon2.xml";
		GraphmlExporter<DataNode,DataEdge> ge2 = new GraphmlExporter<>(s);
		ge2.exportGraph(graph2);

	}
	
//	bidon.xml should be like that:
	
//	<?xml version="1.0" encoding="UTF-8"?>
//	<graphml xmlns="http://graphml.graphdrawing.org/xmlns"
//	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//	    xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
//	    http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
//	  <graph id="G" edgedefault="directed">
//	    <node id="1"/>
//	    <node id="2"/>
//	    <node id="3"/>
//	    <node id="4"/>
//	    <edge id="5" source="1" target="4"/>
//	    <edge id="6" source="1" target="1"/>
//	    <edge id="7" source="1" target="3"/>
//	    <edge id="8" source="3" target="2"/>
//	    <edge id="9" source="4" target="1"/>
//	  </graph>
//	</graphml>
	
	
//	bidon2.xml should be like that:
	
//	<?xml version="1.0" encoding="UTF-8"?>
//	<graphml xmlns="http://graphml.graphdrawing.org/xmlns"
//	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//	    xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
//	    http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
//	  <graph id="G" edgedefault="directed">
//	    <node id="1">
//	      <data key="one">1.0</data>
//	      <data key="three">""</data>
//	      <data key="two">2000</data>
//	    </node>
//	    <node id="2">
//	      <data key="one">1.0</data>
//	      <data key="three">""</data>
//	      <data key="two">2000</data>
//	    </node>
//	    <node id="3">
//	      <data key="one">1.0</data>
//	      <data key="three">""</data>
//	      <data key="two">2000</data>
//	    </node>
//	    <node id="4">
//	      <data key="one">1.0</data>
//	      <data key="three">""</data>
//	      <data key="two">2000</data>
//	    </node>
//	    <edge id="5" source="2" target="3">
//	      <data key="five">
//	        ([3,2]false,false,false,false,false,false)
//	      </data>
//	      <data key="four">4</data>
//	      <data key="one">""</data>
//	    </edge>
//	    <edge id="6" source="3" target="2">
//	      <data key="five">
//	        ([3,2]false,false,false,false,false,false)
//	      </data>
//	      <data key="four">4</data>
//	      <data key="one">""</data>
//	    </edge>
//	    <edge id="7" source="3" target="3">
//	      <data key="five">
//	        ([3,2]false,false,false,false,false,false)
//	      </data>
//	      <data key="four">4</data>
//	      <data key="one">""</data>
//	    </edge>
//	    <edge id="8" source="3" target="4">
//	      <data key="five">
//	        ([3,2]false,false,false,false,false,false)
//	      </data>
//	      <data key="four">4</data>
//	      <data key="one">""</data>
//	    </edge>
//	    <edge id="9" source="4" target="1">
//	      <data key="five">
//	        ([3,2]false,false,false,false,false,false)
//	      </data>
//	      <data key="four">4</data>
//	      <data key="one">""</data>
//	    </edge>
//	  </graph>
//	  <key id="one" for="all" attr.name="one" attr.type="float">
//	    <default>0.0</default>
//	  </key>
//	  <key id="three" for="node" attr.name="three" attr.type="string">
//	    <default></default>
//	  </key>
//	  <key id="two" for="node" attr.name="two" attr.type="long">
//	    <default>0</default>
//	  </key>
//	  <!-- GraphML mapping for type Short -->
//	  <key id="four" for="edge" attr.name="four" attr.type="int">
//	    <default>0</default>
//	  </key>
//	  <!-- GraphML mapping for type BooleanTable -->
//	  <key id="five" for="edge" attr.name="five" attr.type="string">
//	    <default>{[1],false}</default>
//	  </key>
//	</graphml>		

}

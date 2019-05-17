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
package fr.cnrs.iees.graph.io.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.collections.tables.BooleanTable;
import au.edu.anu.rscs.aot.collections.tables.Dimensioner;
import au.edu.anu.rscs.aot.collections.tables.Table;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;
import fr.cnrs.iees.graph.impl.ALEdge;
import fr.cnrs.iees.graph.impl.ALGraph;
import fr.cnrs.iees.graph.impl.ALGraphFactory;
import fr.cnrs.iees.graph.impl.ALNode;
/**
 * An exporter to test we can save as GraphML.
 * 
 * NOTE: wrong export format - the keys must appear before the graph nodes
 * 
 * @author Jacques Gignoux - 18 déc. 2018
 *
 */
class GraphmlExporterTest {

	ALGraphFactory f = new ALGraphFactory("ZZ");
	ALGraphFactory f2 = new ALGraphFactory("KK");
	// first, a simple graph with no properties
	ALNode n1;
	ALNode n2, n3, n4;
	ALEdge e1, e2, e3, e4, e5;
	ALGraph<ALNode,ALEdge> graph;
	// second, a graph with properties
	ALNode dn1, dn2, dn3, dn4;
	ALEdge de1, de2, de3, de4, de5;
	SimplePropertyList props = new SimplePropertyListImpl("one","two","three");
	SimplePropertyList prop2 = new SimplePropertyListImpl("four","five","one");
	ALGraph<ALNode,ALEdge> graph2;
	
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
		n1 = f.makeNode("n1");
		n2 = f.makeNode("n1");
		n3 = f.makeNode("n1");
		n4 = f.makeNode("n1");
		e1 = (ALEdge) f.makeEdge(n1,n2,"e1");
		e2 = (ALEdge) f.makeEdge(n2,n1,"e1");
		e3 = (ALEdge) f.makeEdge(n2,n2,"e1");
		e4 = (ALEdge) f.makeEdge(n2,n3,"e1");
		e5 = (ALEdge) f.makeEdge(n3,n4,"e1");
		List<ALNode> l = new LinkedList<>();
		l.add(n1); l.add(n2);
		l.add(n3); l.add(n4);
		graph = new ALGraph<ALNode,ALEdge>(f);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);
		// property graph
		props.setProperty("one", 1.0F);
		props.setProperty("two", 2000L);
		prop2.setProperty("four", (short)4);
		Table t = new BooleanTable(new Dimensioner(3),new Dimensioner(2));
		prop2.setProperty("five", t);
		dn1 = f2.makeNode("dn1",props);
		dn2 = f2.makeNode("dn1",props);
		dn3 = f2.makeNode("dn1",props);
		dn4 = f2.makeNode("dn1",props);
		de1 = (ALEdge) f2.makeEdge(dn1,dn2,"de1",prop2);
		de2 = (ALEdge) f2.makeEdge(dn2,dn1,"de1",prop2);
		de3 = (ALEdge) f2.makeEdge(dn2,dn2,"de1",prop2);
		de4 = (ALEdge) f2.makeEdge(dn2,dn3,"de1",prop2);
		de5 = (ALEdge) f2.makeEdge(dn3,dn4,"de1",prop2);
		List<ALNode> l2 = new LinkedList<ALNode>();
		l2.add(dn1); l2.add(dn2);
		l2.add(dn3); l2.add(dn4);
		graph2 = new ALGraph<ALNode,ALEdge>(f2);
		graph2.addNode(dn1);
		graph2.addNode(dn2);
		graph2.addNode(dn3);
		graph2.addNode(dn4);
	}

	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}


	@Test
	void testGraphmlExporterFile() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon.xml";
		File f = new File(testfile);
		GraphmlExporter ge = new GraphmlExporter(f);
		assertNotNull(ge);
	}

	@Test
	void testGraphmlExporterString() {
		String s = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon.xml";
		show("testGraphmlExporterString",s);
		GraphmlExporter ge = new GraphmlExporter(s);
		assertNotNull(ge);
	}

	@Test
	void testExportGraph() {
		String s = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon.xml";
		GraphmlExporter ge = new GraphmlExporter(s);
		ge.exportGraph(graph);
		s = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon2.xml";
		GraphmlExporter ge2 = new GraphmlExporter(s);
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
//  <key id="one" for="all" attr.name="one" attr.type="float">
//    <default>0.0</default>
//  </key>
//  <key id="three" for="node" attr.name="three" attr.type="string">
//    <default></default>
//  </key>
//  <key id="two" for="node" attr.name="two" attr.type="long">
//    <default>0</default>
//  </key>
//  <!-- GraphML mapping for type Short -->
//  <key id="four" for="edge" attr.name="four" attr.type="int">
//    <default>0</default>
//  </key>
//  <!-- GraphML mapping for type BooleanTable -->
//  <key id="five" for="edge" attr.name="five" attr.type="string">
//    <default>{[1],false}</default>
//  </key>
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
//	</graphml>		

}

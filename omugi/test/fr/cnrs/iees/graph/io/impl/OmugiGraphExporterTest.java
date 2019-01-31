/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.impl.DefaultGraphFactory;
import fr.cnrs.iees.graph.impl.ImmutableGraphImpl;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.io.FileImporter;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;

class OmugiGraphExporterTest {

	DefaultGraphFactory f = new DefaultGraphFactory();
	// first, a simple graph with no properties
	Node n1;
	Node n2, n3, n4;
	Edge e1, e2, e3, e4, e5;
	Map<String,String> nodes;
	ImmutableGraphImpl<Node,Edge> graph;
	// second, a graph with properties
	Node dn1, dn2, dn3, dn4;
	Edge de1, de2, de3, de4, de5;
	SimplePropertyList props = new SimplePropertyListImpl("one","two","three");
	SimplePropertyList prop2 = new SimplePropertyListImpl("four","five","one");
	ImmutableGraphImpl<Node,Edge> graph2;
	
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
		nodes = new HashMap<String,String>();
		n1 = f.makeNode();
		nodes.put(n1.id(), "n1");
		n2 = f.makeNode();
		nodes.put(n2.id(), "n2");
		n3 = f.makeNode();
		nodes.put(n3.id(), "n3");
		n4 = f.makeNode();
		nodes.put(n4.id(), "n4");
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
		List<Node> l2 = new LinkedList<Node>();
		l2.add(dn1); l2.add(dn2);
		l2.add(dn3); l2.add(dn4);
		graph2 = new ImmutableGraphImpl<Node,Edge>(l2);
	}
	
	@Test
	void testExportGraph1() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon.ugg";
		File f = new File(testfile);
		OmugiGraphExporter ge = new OmugiGraphExporter(f);
		ge.exportGraph(graph);
	}
	
/* bidon.ugg should look like this:

graph // saved by OmugiGraphExporter on Fri Jan 11 15:35:55 CET 2019

// 4 NODES
node D89EF3043496-000001683D5742F9-0000
node D89EF3043496-000001683D5742F9-0001
node D89EF3043496-000001683D5742F7-0001
node D89EF3043496-000001683D5742F8-0000

// 5 EDGES
[node:D89EF3043496-000001683D5742F9-0000] edge D89EF3043496-000001683D5742FB-0002 [node:D89EF3043496-000001683D5742F9-0001]
[node:D89EF3043496-000001683D5742F7-0001] edge D89EF3043496-000001683D5742FA-0000 [node:D89EF3043496-000001683D5742F8-0000]
[node:D89EF3043496-000001683D5742F8-0000] edge D89EF3043496-000001683D5742FB-0001 [node:D89EF3043496-000001683D5742F9-0000]
[node:D89EF3043496-000001683D5742F8-0000] edge D89EF3043496-000001683D5742FA-0001 [node:D89EF3043496-000001683D5742F7-0001]
[node:D89EF3043496-000001683D5742F8-0000] edge D89EF3043496-000001683D5742FB-0000 [node:D89EF3043496-000001683D5742F8-0000]

*/
	
	@Test
	void testExportGraph2() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon2.ugg";
		File f = new File(testfile);
		OmugiGraphExporter ge = new OmugiGraphExporter(f);
		ge = new OmugiGraphExporter(f);
		ge.exportGraph(graph2);
		// attempt to reimport graph
		GraphImporter gi = new OmugiGraphImporter(f);
		Graph<?,?> g = (Graph<?, ?>) gi.getGraph();
		assertEquals(graph.size(),4);
//		System.out.println(gi.getGraph().toString());
		// re-save to bidon3.ugg - should contain the same thing as bidon2.ugg
		testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon3.ugg";
		f = new File(testfile);
		ge = new OmugiGraphExporter(f);
		ge.exportGraph(g);
	}
	
/* 	bidon2.ugg should look like this:

graph // saved by OmugiGraphExporter on Fri Jan 11 15:35:55 CET 2019

// 4 NODES
node D89EF3043496-000001683D574316-0003
	one = java.lang.Float(1.0)
	three = java.lang.Object(null)
	two = java.lang.Long(2000)
node D89EF3043496-000001683D574317-0000
	one = java.lang.Float(1.0)
	three = java.lang.Object(null)
	two = java.lang.Long(2000)
node D89EF3043496-000001683D574316-0001
	one = java.lang.Float(1.0)
	three = java.lang.Object(null)
	two = java.lang.Long(2000)
node D89EF3043496-000001683D574316-0002
	one = java.lang.Float(1.0)
	three = java.lang.Object(null)
	two = java.lang.Long(2000)

// 5 EDGES
[node:D89EF3043496-000001683D574316-0003] edge D89EF3043496-000001683D574318-0003 [node:D89EF3043496-000001683D574317-0000]
	five = au.edu.anu.rscs.aot.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	four = java.lang.Short(4)
	one = java.lang.Object(null)
[node:D89EF3043496-000001683D574316-0001] edge D89EF3043496-000001683D574317-0001 [node:D89EF3043496-000001683D574316-0002]
	five = au.edu.anu.rscs.aot.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	four = java.lang.Short(4)
	one = java.lang.Object(null)
[node:D89EF3043496-000001683D574316-0002] edge D89EF3043496-000001683D574318-0001 [node:D89EF3043496-000001683D574316-0002]
	five = au.edu.anu.rscs.aot.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	four = java.lang.Short(4)
	one = java.lang.Object(null)
[node:D89EF3043496-000001683D574316-0002] edge D89EF3043496-000001683D574318-0000 [node:D89EF3043496-000001683D574316-0001]
	five = au.edu.anu.rscs.aot.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	four = java.lang.Short(4)
	one = java.lang.Object(null)
[node:D89EF3043496-000001683D574316-0002] edge D89EF3043496-000001683D574318-0002 [node:D89EF3043496-000001683D574316-0003]
	five = au.edu.anu.rscs.aot.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	four = java.lang.Short(4)
	one = java.lang.Object(null)
 
 */

/* 	bidon3.ugg should be exactly like bidon2.ugg except the ids are different
*/
	@Test
	void testExportGraph3() {
		// tree loading / saving
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon.ugt";
		File f = new File(testfile);
		FileImporter gi = new FileImporter(f);
		MinimalGraph<?> tree = gi.getGraph();
		testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon2.ugt";
		f = new File(testfile);
		OmugiGraphExporter ge = new OmugiGraphExporter(f);
		ge.exportGraph(tree);
	}
	
/* 	bidon2.ugt should look like this:

tree // saved by OmugiGraphExporter on Fri Jan 11 15:35:55 CET 2019

// 11 NODES
DataTreeNodeImpl 436e852b
	prop1 = java.lang.Integer(3)
	prop2 = java.lang.Double(4.2)
	DataTreeNodeImpl c46bcd4
		table = au.edu.anu.rscs.aot.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	SimpleTreeNodeImpl 631330c
		DataTreeNodeImpl 42f93a98
			prop4 = java.lang.String("coucou")
	SimpleTreeNodeImpl 3234e239
		SimpleTreeNodeImpl 3d921e20
			SimpleTreeNodeImpl 36b4cef0
				SimpleTreeNodeImpl fad74ee
		SimpleTreeNodeImpl 1a1d6a08
		DataTreeNodeImpl 37d31475
			truc = java.lang.String("machin")
			DataTreeNodeImpl 27808f31
				plop = java.lang.Integer(12)
	
*/	
}

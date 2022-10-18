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
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.omugi.collections.tables.BooleanTable;
import au.edu.anu.omugi.collections.tables.Dimensioner;
import au.edu.anu.omugi.collections.tables.Table;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.graph.impl.ALEdge;
import fr.cnrs.iees.graph.impl.ALGraph;
import fr.cnrs.iees.graph.impl.ALGraphFactory;
import fr.cnrs.iees.graph.impl.ALNode;
import fr.cnrs.iees.graph.impl.TreeGraph;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.io.FileImporter;
import fr.cnrs.iees.io.parsing.impl.TreeGraphParser;
import fr.cnrs.iees.io.parsing.impl.TreeGraphTokenizer;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;

/**
 * 
 * @author Jacques Gignoux - 17 mai 2019
 *
 */
class OmugiGraphExporterTest {

	ALGraphFactory f = new ALGraphFactory();
	ALGraphFactory f2 = new ALGraphFactory();
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
		assertEquals(graph.nNodes(),4);
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
	five = au.edu.anu.omugi.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	four = java.lang.Short(4)
	one = java.lang.Object(null)
[node:D89EF3043496-000001683D574316-0001] edge D89EF3043496-000001683D574317-0001 [node:D89EF3043496-000001683D574316-0002]
	five = au.edu.anu.omugi.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	four = java.lang.Short(4)
	one = java.lang.Object(null)
[node:D89EF3043496-000001683D574316-0002] edge D89EF3043496-000001683D574318-0001 [node:D89EF3043496-000001683D574316-0002]
	five = au.edu.anu.omugi.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	four = java.lang.Short(4)
	one = java.lang.Object(null)
[node:D89EF3043496-000001683D574316-0002] edge D89EF3043496-000001683D574318-0000 [node:D89EF3043496-000001683D574316-0001]
	five = au.edu.anu.omugi.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
	four = java.lang.Short(4)
	one = java.lang.Object(null)
[node:D89EF3043496-000001683D574316-0002] edge D89EF3043496-000001683D574318-0002 [node:D89EF3043496-000001683D574316-0003]
	five = au.edu.anu.omugi.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
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
		NodeSet<?> tree = gi.getGraph();
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
		table = au.edu.anu.omugi.collections.tables.BooleanTable(([3,2]false,false,false,false,false,false))
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
	
	String[] test = {"treegraph // saved by AotGraphExporter on Mon Jan 21 11:31:07 CET 2019\n", 
			"\n",
			"// TREE\n", 
			"node 3Worlds \n", 
			"	node ecology\n", 
			"		a = java.lang.Object(null)\n", 
			"		b = java.lang.Object(null)\n", 
			"		category animal\n",
			"			x = java.lang.Object(null)\n", 
			"			y = java.lang.Object(null)\n", 
			"			z = java.lang.Object(null)\n", 
			"		system entity\n",
			"			i = java.lang.Object(null)\n", 
			"			j = java.lang.Object(null)\n", 
			"			k = java.lang.Object(null)\n", 
			"			l = java.lang.Object(null)\n", 
			"		category plant\n",
			"			x = java.lang.Object(null)\n", 
			"			y = java.lang.Object(null)\n", 
			"			z = java.lang.Object(null)\n", 
			"		engine my simulator\n",
			"		process growth\n",
			"	node codeSource \n",
			"		function some computation\n", 
			"			a = java.lang.Object(null)\n", 
			"			b = java.lang.Object(null)\n", 
			"		bidon D89EF3043496-000001686FF6BA12-0000\n", 
			"	experiment my experiment\n",
			"\n",
			"// CROSS-LINKS\n", 
			"[system:entity] belongsTo random name [category:animal]\n", 
			"[process:growth] appliesTo  [category:animal]\n",
			"[process:growth] appliesTo  [category:plant]\n", 
			"	prop = Integer(4)\n",
			"[process:growth] function  [function:some computation]\n"};

	
	@Test
	void testExportTreeGraph() {
		TreeGraphParser p = new TreeGraphParser(new TreeGraphTokenizer(test));
		TreeGraph<?, ?> g = p.graph();
//		System.out.println(g.toDetailedString());
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "treegraph.utg";
		File f = new File(testfile);
		OmugiGraphExporter ge = new OmugiGraphExporter(f);
		ge.exportGraph(g);
	}
	
}

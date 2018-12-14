package fr.cnrs.iees.io.graph;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.io.graph.GraphParser;
import fr.cnrs.iees.io.graph.GraphTokenizer;

class GraphParserTest {

	String[] test = {"graph // this is a comment\n",
			"\n",
			"//this is another comment\n", 
			"\n" ,
			"label1 name1\n" , 
			"  prop1=Integer(1)\n" , 
			"	prop2 =Double(2.0)\n" , 
			"prop3= String(\"blabla\")\n" , 
			"		prop4 = Boolean(true)\n" , 
			"\n" ,
			"label2 name2\n" , 
			"label1 name3\n" , 
			"\n" ,
			"[label1:name1] label4 name1 [label2:name2]\n" , 
			"	[ label1:name1] label4 name2	 [label2:name2 ]\n" , 
			"[ label2:name2 ] label4 name1   [label1:name3]\n" ,
			"label2 name5\n" ,
			"prop1 = Integer(0)"};

	String[] testWithErrors = {"graph // this is a comment\n",
			"\n",
			"//this is another comment\n", 
			"\n" ,
			"label1 name1\n" , 
			"  prop1=Integer(1.0)\n" , 			// property value incompatible with type
			"	prop2 =Double(2.0)\n" , 
			"prop3= String(\"blabla\")\n" , 
			"		prop4 = Boolkean(true)\n" ,  // wrong property type
			"\n" ,
			" name2\n" ,  					// missing label here
			"label1 name3\n" , 
			"\n" ,
			"[label1:name1] label4 name1 [label2:name2]\n" , 
			"	[ label1:name11] label4 name2	 [label2:name2 ]\n" ,  // non existant start node
			"[ label2:name2 ] label4 name1   [label1:name7]\n" ,  // non existant end node 
			"label2 name5\n" ,
			"prop1 = Integer(0)"};

	
	@Test
	void testParse() {
		GraphParser gp = new GraphParser(new GraphTokenizer(test));
		gp.parse();
		assertEquals(gp.toString(),"Graph specification\n" + 
				"Nodes:\n" + 
				"	label1:name1\n" + 
				"		prop1:Integer=1\n" + 
				"		prop2:Double=2.0\n" + 
				"		prop3:String=\"blabla\"\n" + 
				"		prop4:Boolean=true\n" + 
				"	label2:name2\n" + 
				"	label1:name3\n" + 
				"	label2:name5\n" + 
				"		prop1:Integer=0\n" + 
				"Edges:\n" + 
				"	label4:name1 [label1:name1-->label2:name2]\n" + 
				"	label4:name2 [label1:name1-->label2:name2]\n" + 
				"	label4:name1 [label2:name2-->label1:name3]\n");
	}

	@Test
	void testGraph() {
		GraphParser gp = new GraphParser(new GraphTokenizer(test));
		Graph<?,?> g = gp.graph();
//		System.out.println(g.toString());
		assertEquals(g.size(),4);
		gp = new GraphParser(new GraphTokenizer(testWithErrors));
		g = gp.graph();
//		System.out.println(g.toString());
		assertEquals(g.size(),3);
	}

}

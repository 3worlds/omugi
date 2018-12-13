package fr.cnrs.iees.io;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
		fail("Not yet implemented");
	}

}

package fr.cnrs.iees.io;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GraphTokenizerTest {

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
	void testTokenize() {
		GraphTokenizer tk = new GraphTokenizer(test);
		tk.tokenize();
		assertEquals(tk.toString(),"COMMENT: this is a comment\n" + 
				"COMMENT:this is another comment\n" + 
				"LABEL:label1\n" + 
				"NAME:name1\n" + 
				"PROPERTY_NAME:prop1\n" + 
				"PROPERTY_TYPE:Integer\n" + 
				"PROPERTY_VALUE:1\n" + 
				"PROPERTY_NAME:prop2\n" + 
				"PROPERTY_TYPE:Double\n" + 
				"PROPERTY_VALUE:2.0\n" + 
				"PROPERTY_NAME:prop3\n" + 
				"PROPERTY_TYPE:String\n" + 
				"PROPERTY_VALUE:\"blabla\"\n" + 
				"PROPERTY_NAME:prop4\n" + 
				"PROPERTY_TYPE:Boolean\n" + 
				"PROPERTY_VALUE:true\n" + 
				"LABEL:label2\n" + 
				"NAME:name2\n" + 
				"LABEL:label1\n" + 
				"NAME:name3\n" + 
				"NODE_REF:label1:name1\n" + 
				"LABEL:label4\n" + 
				"NAME:name1\n" + 
				"NODE_REF:label2:name2\n" + 
				"NODE_REF:label1:name1\n" + 
				"LABEL:label4\n" + 
				"NAME:name2\n" + 
				"NODE_REF:label2:name2\n" + 
				"NODE_REF:label2:name2\n" + 
				"LABEL:label4\n" + 
				"NAME:name1\n" + 
				"NODE_REF:label1:name3\n" + 
				"LABEL:label2\n" + 
				"NAME:name5\n" + 
				"PROPERTY_NAME:prop1\n" + 
				"PROPERTY_TYPE:Integer\n" + 
				"PROPERTY_VALUE:0\n");
	}

}

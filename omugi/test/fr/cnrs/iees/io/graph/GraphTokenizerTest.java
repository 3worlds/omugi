package fr.cnrs.iees.io.graph;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.io.graph.GraphTokenizer;

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

	// this doesnt cause any problem to the Tokenizer
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
	
	// this text has nothing to do with a graph
	String[] testWithMoreErrors = {"Murs, ville,\n" + 
			"Et port,\n" + 
			"Asile\n" + 
			"De mort,\n" + 
			"Mer grise\n" + 
			"Où brise\n" + 
			"La brise,\n" + 
			"Tout dort.\n" + 
			"\n" + 
			"Dans la plaine\n" + 
			"Naît un bruit.\n" + 
			"C'est l'haleine\n" + 
			"De la nuit.\n" + 
			"Elle brame\n" + 
			"Comme une âme\n" + 
			"Qu'une flamme\n" + 
			"Toujours suit !\n" + 
			"\n" + 
			"La voix plus haute\n" + 
			"Semble un grelot.\n" + 
			"D'un nain qui saute\n" + 
			"C'est le galop.\n" + 
			"Il fuit, s'élance,\n" + 
			"Puis en cadence\n" + 
			"Sur un pied danse\n" + 
			"Au bout d'un flot.\n" + 
			"\n" + 
			"La rumeur approche.\n" + 
			"L'écho la redit.\n" + 
			"C'est comme la cloche\n" + 
			"D'un couvent maudit ;\n" + 
			"Comme un bruit de foule,\n" + 
			"Qui tonne et qui roule,\n" + 
			"Et tantôt s'écroule,\n" + 
			"Et tantôt grandit,"};

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
		tk = new GraphTokenizer(testWithErrors);
		tk.tokenize();
		assertEquals(tk.toString(),"COMMENT: this is a comment\n" + 
				"COMMENT:this is another comment\n" + 
				"LABEL:label1\n" + 
				"NAME:name1\n" + 
				"PROPERTY_NAME:prop1\n" + 
				"PROPERTY_TYPE:Integer\n" + 
				"PROPERTY_VALUE:1.0\n" + 
				"PROPERTY_NAME:prop2\n" + 
				"PROPERTY_TYPE:Double\n" + 
				"PROPERTY_VALUE:2.0\n" + 
				"PROPERTY_NAME:prop3\n" + 
				"PROPERTY_TYPE:String\n" + 
				"PROPERTY_VALUE:\"blabla\"\n" + 
				"PROPERTY_NAME:prop4\n" + 
				"PROPERTY_TYPE:Boolkean\n" + 
				"PROPERTY_VALUE:true\n" + 
				"LABEL:label1\n" + 
				"NAME:name3\n" + 
				"NODE_REF:label1:name1\n" + 
				"LABEL:label4\n" + 
				"NAME:name1\n" + 
				"NODE_REF:label2:name2\n" + 
				"NODE_REF:label1:name11\n" + 
				"LABEL:label4\n" + 
				"NAME:name2\n" + 
				"NODE_REF:label2:name2\n" + 
				"NODE_REF:label2:name2\n" + 
				"LABEL:label4\n" + 
				"NAME:name1\n" + 
				"NODE_REF:label1:name7\n" + 
				"LABEL:label2\n" + 
				"NAME:name5\n" + 
				"PROPERTY_NAME:prop1\n" + 
				"PROPERTY_TYPE:Integer\n" + 
				"PROPERTY_VALUE:0\n");
		// this one is read as a label and a name.
		tk = new GraphTokenizer(testWithMoreErrors);
		tk.tokenize();
		assertEquals(tk.toString(),"LABEL:Murs,\n" + 
				"NAME:ville,Etport,AsileDemort,MergriseOùbriseLabrise,Toutdort.DanslaplaineNaîtunbruit.C'estl'haleineDelanuit.EllebrameCommeuneâmeQu'uneflammeToujourssuit!LavoixplushauteSembleungrelot.D'unnainquisauteC'estlegalop.Ilfuit,s'élance,PuisencadenceSurunpieddanseAuboutd'unflot.Larumeurapproche.L'écholaredit.C'estcommelaclocheD'uncouventmaudit;Commeunbruitdefoule,Quitonneetquiroule,Ettantôts'écroule,Ettantôtgrandit,\n");
	}

	@Test
	void testGetNextToken() {
		GraphTokenizer tk = new GraphTokenizer(test);
		tk.tokenize();
		String s = "";
		while (tk.hasNext())
			s = s+tk.getNextToken().toString()+"\n";
//		System.out.println(s);
		assertEquals(s,"COMMENT: this is a comment\n" + 
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

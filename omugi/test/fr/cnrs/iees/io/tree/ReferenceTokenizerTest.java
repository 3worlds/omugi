package fr.cnrs.iees.io.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Jacques Gignoux - 18 déc. 2018
 *
 */
class ReferenceTokenizerTest {
	
	String ref;
	ReferenceTokenizer tk;

	@Test
	void testTokenize1() {
		ref = "";
		tk = new ReferenceTokenizer(ref);
		tk.tokenize();
		assertEquals(tk.toString(),"NODE_LABEL:\n" + 
				"NODE_NAME:\n");
	}

	@Test
	void testTokenize2() {
		ref = "label1:name1";
		tk = new ReferenceTokenizer(ref);
		tk.tokenize();
		assertEquals(tk.toString(),"NODE_LABEL:label1\n" + 
				"NODE_NAME:name1\n");
	}

	@Test
	void testTokenize3() {
		ref = "label2:";
		tk = new ReferenceTokenizer(ref);
		tk.tokenize();
		assertEquals(tk.toString(),"NODE_LABEL:label2\n" + 
				"NODE_NAME:\n");
	}

	@Test
	void testTokenize4() {
		ref = ":name3";
		tk = new ReferenceTokenizer(ref);
		tk.tokenize();
		assertEquals(tk.toString(),"NODE_LABEL:\n" + 
				"NODE_NAME:name3\n");
	}

	@Test
	void testTokenize5() {
		ref = "label1:name1+prop1=3.4";
		tk = new ReferenceTokenizer(ref);
		tk.tokenize();
		assertEquals(tk.toString(),"NODE_LABEL:label1\n" + 
				"NODE_NAME:name1\n" + 
				"PROPERTY_NAME:prop1\n" + 
				"PROPERTY_VALUE:3.4\n");
	}

	@Test
	void testTokenize6() {
		ref = "label4:+prop2=true";
		tk = new ReferenceTokenizer(ref);
		tk.tokenize();
		assertEquals(tk.toString(),"NODE_LABEL:label4\n" + 
				"NODE_NAME:\n" + 
				"PROPERTY_NAME:prop2\n" + 
				"PROPERTY_VALUE:true\n");
	}

	@Test
	void testTokenize7() {
		ref = ":+prop3=12";
		tk = new ReferenceTokenizer(ref);
		tk.tokenize();
		assertEquals(tk.toString(),"NODE_LABEL:\n" + 
				"NODE_NAME:\n" + 
				"PROPERTY_NAME:prop3\n" + 
				"PROPERTY_VALUE:12\n");
	}

	@Test
	void testTokenize8() {
		ref = "+prop4=\"blabla\"";
		tk = new ReferenceTokenizer(ref);
		tk.tokenize();
		assertEquals(tk.toString(),"NODE_LABEL:\n" + 
				"NODE_NAME:\n" + 
				"PROPERTY_NAME:prop4\n" + 
				"PROPERTY_VALUE:\"blabla\"\n");
	}

	@Test
	void testTokenize9() {
		ref = "+prop4=\"blabla\"+prop5=28.96542/label12:node15/labelDeCadix:/+prop8=false";
		tk = new ReferenceTokenizer(ref);
		tk.tokenize();
		assertEquals(tk.toString(),"NODE_LABEL:\n" + 
				"NODE_NAME:\n" + 
				"PROPERTY_NAME:prop4\n" + 
				"PROPERTY_VALUE:\"blabla\"\n" + 
				"PROPERTY_NAME:prop5\n" + 
				"PROPERTY_VALUE:28.96542\n" + 
				"NODE_LABEL:label12\n" + 
				"NODE_NAME:node15\n" + 
				"NODE_LABEL:labelDeCadix\n" + 
				"NODE_NAME:\n" + 
				"NODE_LABEL:\n" + 
				"NODE_NAME:\n" + 
				"PROPERTY_NAME:prop8\n" + 
				"PROPERTY_VALUE:false\n");
		System.out.println(tk.toString());
	}

}

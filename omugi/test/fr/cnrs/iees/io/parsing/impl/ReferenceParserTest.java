package fr.cnrs.iees.io.parsing.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.io.parsing.impl.ReferenceParser;
import fr.cnrs.iees.io.parsing.impl.ReferenceTokenizer;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;
import fr.cnrs.iees.tree.DataTreeNode;
import fr.cnrs.iees.tree.impl.DataTreeNodeImpl;

/**
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
class ReferenceParserTest {
	
	String ref;
	DataTreeNode node;
	SimplePropertyList props;

	@Test
	void testParse() {
		ref = "+prop4=\"blabla\"+prop5=28.96542/label12:node15/labelDeCadix:/+prop8=false";
		ReferenceTokenizer tk = new ReferenceTokenizer(ref);
		ReferenceParser p = tk.parser();
		assertEquals(p.toString(),"Reference to match\n");
		p.parse();
		assertEquals(p.toString(),"Reference to match\n" + 
				":\n" + 
				"	prop8=false\n" + 
				"labelDeCadix:\n" + 
				"label12:node15\n" + 
				":\n" + 
				"	prop4=\"blabla\"\n" + 
				"	prop5=28.96542\n");
	}

	@Test
	void testMatches() {
		ref = "label1:name1+prop1=3.4";
		ReferenceTokenizer tk = new ReferenceTokenizer(ref);
		ReferenceParser p = tk.parser();
		Property prop = new Property("prop1",3.4);
		props = new SimplePropertyListImpl(prop);
		node = new DataTreeNodeImpl(props);
		p.matches(node);
	}

}

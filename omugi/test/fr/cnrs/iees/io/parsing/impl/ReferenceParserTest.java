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
package fr.cnrs.iees.io.parsing.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.io.parsing.impl.ReferenceParser;
import fr.cnrs.iees.io.parsing.impl.ReferenceTokenizer;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;
import fr.cnrs.iees.tree.DataTreeNode;
import fr.cnrs.iees.tree.impl.DefaultTreeFactory;

/**
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
// TODO: more tests, with real names and labels
class ReferenceParserTest {
	
	String ref;
	DataTreeNode node;
	SimplePropertyList props;
	DefaultTreeFactory factory = new DefaultTreeFactory(); 

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
				"	prop4=blabla\n" + 
				"	prop5=28.96542\n");
	}

	@Test
	void testMatches1() {
		ref = "+prop1=3.4";
		ReferenceTokenizer tk = new ReferenceTokenizer(ref);
		ReferenceParser p = tk.parser();
		Property prop = new Property("prop1",3.4);
		props = new SimplePropertyListImpl(prop);
		node = factory.makeTreeNode(null,props);
		assertTrue(p.matches(node));
	}

	@Test
	void testMatches2() {
		ref = "+prop1=3.4/+prop8=false+prop4=\"blabla\"";
		ReferenceTokenizer tk = new ReferenceTokenizer(ref);
		ReferenceParser p = tk.parser();
		props = new SimplePropertyListImpl(
			new Property("prop8",false),
			new Property("prop4","blabla"));
		node = factory.makeTreeNode(null,props);
		props = new SimplePropertyListImpl(
			new Property("prop1",3.4));
		node.setParent(factory.makeTreeNode(null,props));
		assertTrue(p.matches(node));
	}

}

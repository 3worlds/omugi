/**************************************************************************
 *  AOT - Aspect-Oriented Thinking                                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  AOT is a method to generate elaborate software code from a series of  *
 *  independent domains of knowledge. It enables one to manage and        *
 *  maintain software from explicit specifications that can be translated *
 *  into any programming language.          							  *
 **************************************************************************                                       
 *  This file is part of AOT (Aspect-Oriented Thinking).                  *
 *                                                                        *
 *  AOT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  AOT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.io.parsing.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Jacques Gignoux - 23 janv. 2019
 *
 */
class TreeGraphTokenizerTest {

	String[] test = {"aot // saved by AotGraphExporter on Mon Jan 21 11:31:07 CET 2019\n", 
			"\n",
			"// TREE\n", 
			"3Worlds \n", 
			"	ecology my model\n", 
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
			"	codeSource \n",
			"		function some computation\n", 
			"			a = java.lang.Object(null)\n", 
			"			b = java.lang.Object(null)\n", 
			"		AOTNode D89EF3043496-000001686FF6BA12-0000\n", 
			"	experiment my experiment\n",
			"\n",
			"// CROSS-LINKS\n", 
			"[system:entity] belongsTo random name [category:animal]\n", 
			"[process:growth] appliesTo  [category:animal]\n",
			"[process:growth] appliesTo  [category:plant]\n", 
			"[process:growth] function  [function:some computation]\n"};
	
	@Test
	void testTokenize() {
		TreeGraphTokenizer tk = new TreeGraphTokenizer(test);
		tk.tokenize();
		System.out.println(tk.toString());
		assertEquals(tk.toString(),"0 COMMENT: saved by AotGraphExporter on Mon Jan 21 11:31:07 CET 2019\n" + 
				"\n" + 
				"0 COMMENT: TREE\n" + 
				"\n" + 
				"0 LABEL:3Worlds\n" + 
				"0 NAME:\n" + 
				"1 LABEL:ecology\n" + 
				"1 NAME:my model\n" + 
				"2 PROPERTY_NAME:a\n" + 
				"2 PROPERTY_TYPE:java.lang.Object\n" + 
				"2 PROPERTY_VALUE:null\n" + 
				"2 PROPERTY_NAME:b\n" + 
				"2 PROPERTY_TYPE:java.lang.Object\n" + 
				"2 PROPERTY_VALUE:null\n" + 
				"2 LABEL:category\n" + 
				"2 NAME:animal\n" + 
				"3 PROPERTY_NAME:x\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"3 PROPERTY_NAME:y\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"3 PROPERTY_NAME:z\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"2 LABEL:system\n" + 
				"2 NAME:entity\n" + 
				"3 PROPERTY_NAME:i\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"3 PROPERTY_NAME:j\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"3 PROPERTY_NAME:k\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"3 PROPERTY_NAME:l\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"2 LABEL:category\n" + 
				"2 NAME:plant\n" + 
				"3 PROPERTY_NAME:x\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"3 PROPERTY_NAME:y\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"3 PROPERTY_NAME:z\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"2 LABEL:engine\n" + 
				"2 NAME:my simulator\n" + 
				"2 LABEL:process\n" + 
				"2 NAME:growth\n" + 
				"1 LABEL:codeSource\n" + 
				"1 NAME:\n" + 
				"2 LABEL:function\n" + 
				"2 NAME:some computation\n" + 
				"3 PROPERTY_NAME:a\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"3 PROPERTY_NAME:b\n" + 
				"3 PROPERTY_TYPE:java.lang.Object\n" + 
				"3 PROPERTY_VALUE:null\n" + 
				"2 LABEL:AOTNode\n" + 
				"2 NAME:D89EF3043496-000001686FF6BA12-0000\n" + 
				"1 LABEL:experiment\n" + 
				"1 NAME:my experiment\n" + 
				"0 COMMENT: CROSS-LINKS\n" + 
				"\n" + 
				"NODE_REF:system:entity\n" + 
				"LABEL:belongsTo\n" + 
				"NAME:random name\n" + 
				"NODE_REF:category:animal\n" + 
				"NODE_REF:process:growth\n" + 
				"LABEL:appliesTo\n" + 
				"NAME:\n" + 
				"NODE_REF:category:animal\n" + 
				"NODE_REF:process:growth\n" + 
				"LABEL:appliesTo\n" + 
				"NAME:\n" + 
				"NODE_REF:category:plant\n" + 
				"NODE_REF:process:growth\n" + 
				"LABEL:function\n" + 
				"NAME:\n" + 
				"NODE_REF:function:some computation\n");
	}

}

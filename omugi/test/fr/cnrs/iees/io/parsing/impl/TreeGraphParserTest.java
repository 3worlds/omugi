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

import fr.cnrs.iees.graph.impl.TreeGraph;

/**
 * 
 * @author Jacques Gignoux - 23 janv. 2019
 *
 */
class TreeGraphParserTest {

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
			"		bidon D89EF3043496-000001686FF6BA12-0000\n", 
			"	experiment my experiment\n",
			"\n",
			"// CROSS-LINKS\n", 
			"[system:entity] belongsTo random name [category:animal]\n", 
			"[process:growth] appliesTo  [category:animal]\n",
			"[process:growth] appliesTo  [category:plant]\n", 
			"[process:growth] function  [function:some computation]\n"};
	
	String[] test2 = {"aot // saved by AotGraphExporter on Thu Jan 24 16:25:55 AEDT 2019\n", 
			"\n", 
			"// TREE\n", 
			"3Worlds myProject\n", 
			"\n",
			"// CROSS-LINKS"};
	
	@Test
	void testParse() {
		TreeGraphParser p = new TreeGraphParser(new TreeGraphTokenizer(test));
		p.parse();
//		System.out.println(p.toString());
		assertEquals(p.toString(),"Aot graph specification\n" + 
				"Nodes:\n" + 
				"3Worlds:\n" + 
				"	ROOT NODE\n" + 
				"ecology:my model\n" + 
				"	a:java.lang.Object=null\n" + 
				"	b:java.lang.Object=null\n" + 
				"	parent 3Worlds:\n" + 
				"category:animal\n" + 
				"	x:java.lang.Object=null\n" + 
				"	y:java.lang.Object=null\n" + 
				"	z:java.lang.Object=null\n" + 
				"	parent ecology:my model\n" + 
				"system:entity\n" + 
				"	i:java.lang.Object=null\n" + 
				"	j:java.lang.Object=null\n" + 
				"	k:java.lang.Object=null\n" + 
				"	l:java.lang.Object=null\n" + 
				"	parent ecology:my model\n" + 
				"category:plant\n" + 
				"	x:java.lang.Object=null\n" + 
				"	y:java.lang.Object=null\n" + 
				"	z:java.lang.Object=null\n" + 
				"	parent ecology:my model\n" + 
				"engine:my simulator\n" + 
				"	parent ecology:my model\n" + 
				"process:growth\n" + 
				"	parent ecology:my model\n" + 
				"codeSource:\n" + 
				"	parent 3Worlds:\n" + 
				"function:some computation\n" + 
				"	a:java.lang.Object=null\n" + 
				"	b:java.lang.Object=null\n" + 
				"	parent codeSource:\n" + 
				"bidon:D89EF3043496-000001686FF6BA12-0000\n" + 
				"	parent codeSource:\n" + 
				"experiment:my experiment\n" + 
				"	parent 3Worlds:\n" + 
				"Edges:\n" + 
				"	belongsTo:random name [system:entity-->category:animal]\n" + 
				"	appliesTo: [process:growth-->category:animal]\n" + 
				"	appliesTo: [process:growth-->category:plant]\n" + 
				"	function: [process:growth-->function:some computation]\n");
	}
	
	@Test
	void testParse2() {
		TreeGraphParser p = new TreeGraphParser(new TreeGraphTokenizer(test2));
		p.parse();
//		System.out.println(p.toString());
	}

	@Test
	void testGraph() {
		TreeGraphParser p = new TreeGraphParser(new TreeGraphTokenizer(test));
		TreeGraph<?, ?> g = p.graph();
		System.out.println(g.toDetailedString());
		assertTrue(g.toDetailedString().endsWith("(11 tree nodes / 4 cross-links) = {ecology:my model=[↑3Worlds: ↓category:animal ↓system:entity ↓category:plant ↓engine:my simulator ↓process:growth a=null b=null],codeSource:=[↑3Worlds: ↓function:some computation ↓bidon:D89EF3043496-000001686FF6BA12-0000],function:some computation=[↑codeSource: ←process:growth a=null b=null],category:animal=[↑ecology:my model ←system:entity ←process:growth x=null y=null z=null],system:entity=[↑ecology:my model →category:animal i=null j=null k=null l=null],experiment:my experiment=[↑3Worlds:],category:plant=[↑ecology:my model ←process:growth x=null y=null z=null],engine:my simulator=[↑ecology:my model],3Worlds:=[ROOT ↓ecology:my model ↓codeSource: ↓experiment:my experiment],process:growth=[↑ecology:my model →function:some computation →category:plant →category:animal],bidon:D89EF3043496-000001686FF6BA12-0000=[↑codeSource:]}"));
	}

}

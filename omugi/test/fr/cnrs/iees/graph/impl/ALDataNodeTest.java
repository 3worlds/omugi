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
package fr.cnrs.iees.graph.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.DataHolder;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * This tests could also apply to all DataHolder/ReadOnlyDataholder implementations, but I am lazy
 * @author Jacques Gignoux - 17 mai 2019
 *
 */
class ALDataNodeTest {

	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}

	@Test
	void testProperties() {
		ALGraphFactory f = new ALGraphFactory("sgrdt");
		SimplePropertyList p;
		p = f.makePropertyList("a","b","c");
		Node n = f.makeNode(p);
		show("testProperties",n.toDetailedString());
		((DataHolder)n).properties().setProperty("a", 12);
		((DataHolder)n).properties().setProperty("c", "blurp");
		show("testProperties",((DataHolder)n).properties().toString());
		assertEquals(((DataHolder)n).properties().getPropertyValue("a"),12);
	}

}

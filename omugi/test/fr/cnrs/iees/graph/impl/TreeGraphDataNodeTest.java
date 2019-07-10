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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.DataHolder;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * 
 * @author Jacques Gignoux - 20 mai 2019
 *
 */
class TreeGraphDataNodeTest {

	private TreeGraphFactory f = null;
	private TreeGraphNode n1;

	@BeforeEach
	private void init() {
		f = new TreeGraphFactory("Grtz");
		SimplePropertyList p;
		p = f.nodePropertyFactory().makePropertyList("a","b","c");
		p.setProperty("a", 12);
		p.setProperty("c", "blurp");
		n1 = f.makeNode("n1",p);
	}

	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}

	@Test
	final void testToDetailedString() {
		show("testToDetailedString",n1.toDetailedString());
	}

	@Test
	final void testProperties() {
		assertEquals(((DataHolder)n1).properties().getPropertyValue("a"),12);
	}

}

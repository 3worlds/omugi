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
package fr.cnrs.iees.graph.io;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.io.parsing.impl.TreeParser;

/**
 * 
 * @author Jacques Gignoux - 22 mai 2019
 *
 */
class GraphImporterTest {

	@Test
	final void testImportGraphString() {
		NodeSet<?> graph = GraphImporter.importGraph("fr/cnrs/iees/io/parsing/impl/importgraph.ugt");
//		System.out.println(graph.toDetailedString());
		assertNotNull(graph);
	}

	@Test
	final void testImportGraphStringClassOfQ() {
		NodeSet<?> graph = GraphImporter.importGraph("importgraph.ugt",TreeParser.class);
//		System.out.println(graph.toDetailedString());
		assertNotNull(graph);
	}

}

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
package fr.cnrs.iees.omugi.io;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.omugi.graph.NodeSet;

/**
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */
class FileImporterTest {

	@Test
	void testGetGraph1() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "test" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "testGraph.txt";
		File file = new File(testfile);
		assertTrue(file.exists());
		FileImporter fi = new FileImporter(file);
		NodeSet<?> g = fi.getGraph();
		assertNotNull(g);
		assertEquals(g.nNodes(),4);
	}

	@Test
	void testGetGraph2() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "test" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "bidon3.ugg";
		File file = new File(testfile);
		assertTrue(file.exists());
		FileImporter fi = new FileImporter(file);
		NodeSet<?> g = fi.getGraph();
		assertNotNull(g);
		assertEquals(g.nNodes(),4);
	}

	@Test
	void testGetGraph3() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "test" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "bidon.ugt";
		File file = new File(testfile);
		assertTrue(file.exists());
		FileImporter fi = new FileImporter(file);
		NodeSet<?> g = fi.getGraph();
		assertNotNull(g);
		assertEquals(g.nNodes(),12);
	}

	@Test
	void testGetGraph4() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "test" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "bidon.aot";
		File file = new File(testfile);
		assertTrue(file.exists());
		// this doesnt work because the graphImporter cannot be found by the classLoader in this library
		FileImporter fi = new FileImporter(file);
		NodeSet<?> g = fi.getGraph();
		assertNotNull(g);
		assertEquals(g.nNodes(),11);
	}
	
	@Test
	void testGraph5() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "test" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "treegraph.utg";
		File file = new File(testfile);
		assertTrue(file.exists());
		FileImporter fi = new FileImporter(file);
		NodeSet<?> g = fi.getGraph();
		assertNotNull(g);
		assertEquals(g.nNodes(),11);
	}
	
}

/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
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

import java.io.File;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.io.FileImporter;

class ImportParserTest {

	@Test
	void test() {
		String testfile = System.getProperty("user.dir") // 
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "master.ugt";
		File file = new File(testfile);
		FileImporter importer = new FileImporter(file);
		Tree<? extends TreeNode> specs = (Tree<? extends TreeNode>)importer.getGraph();
		// something wrong with parents/children?
		int count = 0;
		for (TreeNode n: specs.nodes()) {
			System.out.println(n);
			
		for (TreeNode c: n.getChildren())
			System.out.println("\t"+c.id());
		}
		
	}

}

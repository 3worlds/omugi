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
package fr.cnrs.iees.omugi.graph.io.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.omugi.graph.Tree;
import fr.cnrs.iees.omugi.graph.TreeNode;
import fr.cnrs.iees.omugi.graph.io.GraphImporter;

/**
 * 
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
class OmugiGraphImporterTest {

	@SuppressWarnings("unchecked")
	@Test
	void testGetGraph() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "test" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "treegraph.utg";
		File file = new File(testfile);
		assertTrue(file.exists());
		GraphImporter importer = new OmugiGraphImporter(file);
		Tree<? extends TreeNode> tree = (Tree<? extends TreeNode>)importer.getGraph();
		String indent = "";
		for (TreeNode node:tree.nodes()) {
			printTree(node,indent);
		}
		assertNotNull(tree);
	}

	// testing a multi-root tree
	@SuppressWarnings("unchecked")
	@Test
	void testGetGraph2() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "test" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "sameLevelTree.ugt";
		File file = new File(testfile);
		assertTrue(file.exists());
		GraphImporter importer = new OmugiGraphImporter(file);
		Tree<? extends TreeNode> tree = (Tree<? extends TreeNode>)importer.getGraph();
		String indent = "";
		for (TreeNode node:tree.nodes()) {
			printTree(node,indent);
		}
		assertNotNull(tree);
	}

	
	private void printTree(TreeNode parent,String indent) {
//		if (parent.getParent()!=null)
//			System.out.println(indent+parent.getParent().id()+"->"+parent.id());
//		else
//			System.out.println(indent+"null->"+parent.id());
//		for (TreeNode child:parent.getChildren())
//			printTree(child,indent+"  ");
	}

}

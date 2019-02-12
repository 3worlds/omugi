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
package fr.cnrs.iees.playground.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

import fr.cnrs.iees.io.parsing.Tokenizer;
import fr.cnrs.iees.playground.elements.IEdge;
import fr.cnrs.iees.playground.elements.INode;
import fr.cnrs.iees.playground.factories.IEdgeFactory;
import fr.cnrs.iees.playground.factories.INodeFactory;
import fr.cnrs.iees.playground.factories.ITreeNodeFactory;
import fr.cnrs.iees.playground.graphs.IGraph;
import fr.cnrs.iees.playground.graphs.IMinimalGraph;

public class FileTokenizer2 implements Tokenizer {
	
	private Logger log = Logger.getLogger(FileTokenizer2.class.getName());
	private List<String> lines = null;
	private LineTokenizer2 tokenizer = null;
	private IEdgeFactory edgeFactory;
	private INodeFactory nodeFactory;
	private ITreeNodeFactory treeNodeFactory;

	
	public FileTokenizer2(File f, IEdgeFactory edgeFactory, INodeFactory nodeFactory, ITreeNodeFactory treeNodeFactory) {
		super();
		this.edgeFactory=edgeFactory;
		this.nodeFactory = nodeFactory;
		this.treeNodeFactory = treeNodeFactory;
		try {
			lines = Files.readAllLines(f.toPath());
			if (nodeFactory!=null)
				tokenizer = new GraphTokenizer2(this);
			else if (edgeFactory == null)
				tokenizer = new TreeTokenizer2(this);
			else if (treeNodeFactory!=null)
				tokenizer = new TreeGraphTokenizer2(this);
			else
				log.severe("unrecognized file format - unable to load file \""+f.getName()+"\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected List<String> lines() {
		return lines;
	}
	
	public void tokenize() {
		tokenizer.tokenize();
	}

	/**
	 * Create an instance of {@link Parser} adapted for this tokenizer
	 * @return a new instance of Parser
	 */
	public AbstractParser parser() {
		if (GraphTokenizer2.class.isAssignableFrom(tokenizer.getClass()))
			return new GraphParser2((GraphTokenizer2) tokenizer, edgeFactory,nodeFactory);
		if (TreeTokenizer2.class.isAssignableFrom(tokenizer.getClass()))
			return new TreeParser2((TreeTokenizer2) tokenizer,treeNodeFactory);
		if (TreeGraphTokenizer2.class.isAssignableFrom(tokenizer.getClass()))
			return new TreeGraphParser2((TreeGraphTokenizer2) tokenizer,edgeFactory,treeNodeFactory);
		return null;
	}
	
}

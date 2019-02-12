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

import fr.cnrs.iees.playground.factories.IEdgeFactory;
import fr.cnrs.iees.playground.factories.INodeFactory;
import fr.cnrs.iees.playground.factories.ITreeNodeFactory;
import fr.cnrs.iees.playground.graphs.IMinimalGraph;

public class OmugiGraphImporter2 implements IGraphImporter {
	
	private final FileTokenizer2 tokenizer;
	private final AbstractParser parser;

	// All this is a mess!.
	public OmugiGraphImporter2(File infile, IEdgeFactory edgeFactory, INodeFactory nodeFactory,
			ITreeNodeFactory treeNodeFactory) {
		super();
		tokenizer = new FileTokenizer2(infile,edgeFactory, nodeFactory, treeNodeFactory);
		parser = tokenizer.parser();
	}


	@Override
	public IMinimalGraph<?> getGraph() {
		return parser.graph();

	}

}

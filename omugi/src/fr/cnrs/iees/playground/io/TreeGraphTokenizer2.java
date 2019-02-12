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
package fr.cnrs.iees.playground.io;

import java.util.LinkedList;
import java.util.List;

import fr.cnrs.iees.io.parsing.impl.GraphTokenizer;
import fr.cnrs.iees.io.parsing.impl.TreeTokenizer;

/**
 * A tokenizer for mixed tree graphs - first uses a tree tokenizer, then a graph tokenizer,
 * assuming the file comes into two parts matching those.
 * 
 * @author Jacques Gignoux - 22 janv. 2019
 *
 */
//tested OK with version 0.0.5 on 23/1/2019
public class TreeGraphTokenizer2 extends LineTokenizer2 {

	// the top of the file contains the tree structure
	private List<String> treeLines = new LinkedList<String>();
	// the end of the file contains the cross links between treenodes
	private List<String> crossLinkLines = new LinkedList<String>();
	private TreeTokenizer ttk = null;
	private GraphTokenizer gtk = null;
	
	protected TreeTokenizer treeTokenizer() {
		return ttk;
	}
	
	protected GraphTokenizer graphTokenizer() {
		return gtk;
	}
	
	private void splitLines() {
		for (String s:lines)
			if (s.trim().startsWith("["))
				crossLinkLines.add(s);
			else
				treeLines.add(s);
		String[] ss = new String[0];
		ttk = new TreeTokenizer(treeLines.toArray(ss));
		gtk = new GraphTokenizer(crossLinkLines.toArray(ss));
	}
	
	public TreeGraphTokenizer2(List<String> list) {
		super(list);
		splitLines();
	}
	
	public TreeGraphTokenizer2(String[] lines) {
		super(lines);
		splitLines();
	}
	
	public TreeGraphTokenizer2(FileTokenizer2 fileTokenizer2) {
	
	}

	@Override
	public void tokenize() {
		ttk.tokenize();
		gtk.tokenize();
	}

	@Override
	public String toString() {
		return ttk.toString()+gtk.toString();
	}

}

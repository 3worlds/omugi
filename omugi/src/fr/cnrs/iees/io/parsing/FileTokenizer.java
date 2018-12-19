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
package fr.cnrs.iees.io.parsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

import fr.cnrs.iees.io.parsing.impl.GraphParser;
import fr.cnrs.iees.io.parsing.impl.GraphTokenizer;

/**
 * 
 * @author Jacques Gignoux - 7 déc. 2018
 *
 */
public class FileTokenizer implements Tokenizer {
	
	private Logger log = Logger.getLogger(FileTokenizer.class.getName());
	private List<String> lines = null;
	private LineTokenizer tokenizer = null;
	
	public FileTokenizer(File f) {
		super();
		try {
			lines = Files.readAllLines(f.toPath());
			String s = lines.get(0).trim();
			if (s.startsWith("graph"))
				tokenizer = new GraphTokenizer(this);
			else if (s.startsWith("tree"))
//				tokenizer = new TreeTokenizer(this);
				;
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
	public Parser parser() {
		if (GraphTokenizer.class.isAssignableFrom(tokenizer.getClass()))
			return new GraphParser((GraphTokenizer) tokenizer);
//		if (TreeTokenizer.class.isAssignableFrom(tokenizer.getClass()))
//			return new TreeParser((TreeTokenizer) tokenizer);
		return null;
	}
	
}

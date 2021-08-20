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

import static fr.cnrs.iees.io.parsing.impl.TreeGraphTokens.COMMENT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.cnrs.iees.io.parsing.impl.GraphParser;
import fr.cnrs.iees.io.parsing.impl.GraphTokenizer;
import fr.cnrs.iees.io.parsing.impl.TreeGraphParser;
import fr.cnrs.iees.io.parsing.impl.TreeGraphTokenizer;
import fr.cnrs.iees.io.parsing.impl.TreeParser;
import fr.cnrs.iees.io.parsing.impl.TreeTokenizer;
import fr.ens.biologie.generic.utils.Logging;

/**
 * A Tokenizer for text files in the omugi format.
 * 
 * TODO: this class has a very, very bad name. It's NOT a tokenizer!
 * it's a preprocessor removing meaningless text and merging lines ending with '+'.
 * 
 * @author Jacques Gignoux - 7 déc. 2018
 *
 */
public class FileTokenizer implements Tokenizer {

	private static Logger log = Logging.getLogger(FileTokenizer.class);
	private List<String> lines = null;
	private LineTokenizer tokenizer = null;

	public FileTokenizer(File f) {
		super();
		try {
			lines = preprocess(Files.readAllLines(f.toPath()),log);
//			lines = Files.readAllLines(f.toPath());
			String s = lines.get(0);

			if (s.startsWith("graph"))
				tokenizer = new GraphTokenizer(this);
			else if (s.startsWith("treegraph"))
				tokenizer = new TreeGraphTokenizer(this);
			else if (s.startsWith("tree"))
				tokenizer = new TreeTokenizer(this);
			else
				log.severe("unrecognized file format - unable to load file \"" + f.getName() + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<String> preprocess(List<String> lines,Logger log) {
		List<String> result = new ArrayList<>();
		String concat = "";
		/*
		 * Skip blank lines and comment only lines. This ensures graph type is the first
		 * token in the file.
		 * 
		 * Use key symbol "+" for string concatenation.
		 * 
		 * Note, preprocessing lines means line numbers are no longer reliable for error
		 * reporting. Instead, report entire line of tokens to provide error context.
		 */

		for (String line : lines) {
			String tmp = line.trim();
			if (!tmp.isEmpty()) {
				if (!tmp.startsWith((COMMENT.prefix()))) {
					// need to look ahead to concatenate strings
					if (tmp.endsWith("+")) {
						if (concat.isEmpty())
							concat += line.substring(0, line.indexOf("+"));						
						else 
							concat += tmp.substring(0, tmp.length() - 1);
					} else if (!concat.isEmpty()) {
						concat += tmp;
						result.add(concat);
						concat = "";
					} else
						result.add(line);
				}
			}
		}
		if (!concat.isEmpty())
			log.severe("File format error: Concatenation of file strings remains unclosed\n"+concat);
		return result;
	}

	protected List<String> lines() {
		return lines;
	}

	public void tokenize() {
		tokenizer.tokenize();
	}

	/**
	 * Create an instance of {@link Parser} adapted for this tokenizer
	 * 
	 * @return a new instance of Parser
	 */
	public Parser parser() {
		if (GraphTokenizer.class.isAssignableFrom(tokenizer.getClass()))
			return new GraphParser((GraphTokenizer) tokenizer);
		if (TreeTokenizer.class.isAssignableFrom(tokenizer.getClass()))
			return new TreeParser((TreeTokenizer) tokenizer);
		if (TreeGraphTokenizer.class.isAssignableFrom(tokenizer.getClass()))
			return new TreeGraphParser((TreeGraphTokenizer) tokenizer);
		return null;
	}
	
	public static void main (String[] args) {
		List<String> lines = new ArrayList<>();
		lines.add("\t\thasProperty widgetClass");
		lines.add("\t\tmustSatisfyQuery widgetClassInValueSetQuery");
		lines.add("\t\t\tisOfClass = String(\"mustSatisfyQuery\")");
		lines.add("\t\t\tclassName = String(\"IsInValueSetQuery\")"); 
		lines.add("\t\t\tvalues = StringTable(\"[0],SingleGridWidget,+   ");
		lines.add("\t\t\tTimeDisplayWidgetfx,+");
		lines.add("\t\t\tSimpleSimCtrlWidget,+");
		lines.add("\t\t\tTimeSeriesPlotWidgetfx,+");
		lines.add("\t\t\tLabelValuePair\")");    
		lines = FileTokenizer.preprocess(lines,log);
		for (String line: lines)
			System.out.println(line);
	}

}

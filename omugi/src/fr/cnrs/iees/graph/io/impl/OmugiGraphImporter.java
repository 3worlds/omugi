package fr.cnrs.iees.graph.io.impl;

import java.io.File;

import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.io.Parser;
import fr.cnrs.iees.io.Tokenizer;

/**
 * Importer for graphs / trees in the omugi simple text format.
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
public class OmugiGraphImporter implements GraphImporter {

	private Tokenizer tokenizer = null;
	private Parser parser = null;
	
	public OmugiGraphImporter(File infile) {
		super();
		tokenizer = new Tokenizer(infile);
		parser = tokenizer.parser();
	}
	
	@Override
	public Graph<? extends Node, ? extends Edge> getGraph() {
		return parser.graph();
	}

}

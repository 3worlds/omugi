package fr.cnrs.iees.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

import fr.cnrs.iees.io.graph.GraphParser;
import fr.cnrs.iees.io.graph.GraphTokenizer;

/**
 * 
 * @author Jacques Gignoux - 7 déc. 2018
 *
 */
public class Tokenizer {
	
	private Logger log = Logger.getLogger(Tokenizer.class.getName());
	private List<String> lines = null;
	private LineTokenizer tokenizer = null;
	
	public Tokenizer(File f) {
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

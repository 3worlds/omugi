package fr.cnrs.iees.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * 
 * @author Jacques Gignoux - 7 déc. 2018
 *
 */
public class Tokenizer {
	
	private List<String> lines = null;
	private LineTokenizer tokenizer = null;
	
	public Tokenizer(File f) {
		super();
		try {
			lines = Files.readAllLines(f.toPath());
			String s = lines.get(0).trim();
			if (s.equals("graph"))
				tokenizer = new GraphTokenizer(this);
			else if (s.equals("tree"))
//				tokenizer = new TreeTokenizer(this);
				;
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

}

package fr.cnrs.iees.io;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jacques Gignoux - 7 déc. 2018
 *
 */
public abstract class LineTokenizer {
	
	protected List<String> lines;
		
	public LineTokenizer(Tokenizer parent) {
		super();
		lines = parent.lines();
	}
	
	// for debugging only
	protected LineTokenizer(String[] lines) {
		super();
		this.lines = new ArrayList<>(lines.length);
		for (int i=0; i<lines.length; i++)
			this.lines.add(lines[i]);
	}
	
	public abstract void tokenize();
	
	/**
	 * 
	 * @return true if this tokenizer has run
	 */
	protected boolean tokenized() {
		return false;
	}

}

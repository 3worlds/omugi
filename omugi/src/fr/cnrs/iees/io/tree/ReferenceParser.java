package fr.cnrs.iees.io.tree;

import fr.cnrs.iees.io.Parser;

public class ReferenceParser extends Parser {
	
	private ReferenceTokenizer tokenizer;
	
	public ReferenceParser(ReferenceTokenizer tokenizer) {
		super();
		this.tokenizer = tokenizer;
	}

	@Override
	public void parse() {
		if (!tokenizer.tokenized())
			tokenizer.tokenize();

	}

}

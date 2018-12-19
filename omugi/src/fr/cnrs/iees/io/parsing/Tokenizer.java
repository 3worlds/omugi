package fr.cnrs.iees.io.parsing;

/**
 * 
 * @author Jacques Gignoux - 18 déc. 2018
 *
 */
public interface Tokenizer {
	
	public void tokenize();

	
	/**
	 * 
	 * @return true if this tokenizer has run
	 */
	public default boolean tokenized() {
		return false;
	}

}

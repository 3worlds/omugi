package fr.cnrs.iees.io;

import static fr.cnrs.iees.io.GraphTokens.*;

import java.util.ArrayList;
import java.util.List;

import fr.cnrs.iees.OmugiException;

/**
 * A crude tokenizer for graphs
 * 
 * @author Jacques Gignoux - 7 déc. 2018
 *
 */
//token patterns for the parser:
//	label name = node
//	noderef label name noderef = edge
//	propnam proptype propvalue = property
//	property after node = node prop
//	property after edge = edge prop
//	property after nothing = graph property
//	comment = ignore/skip
// todo: import	
public class GraphTokenizer extends LineTokenizer {
	
	private class token {
		protected GraphTokens type;
		protected String value;
		
		protected token(GraphTokens type, String value) {
			super();
			this.type = type;
			this.value = value;
		}
		
		@Override
		public String toString() {
			return type+":"+value;
		}
	}
	
	private List<token> tokenlist = new ArrayList<token>(1000);
	private token cttoken = null;
	
	public GraphTokenizer(Tokenizer parent) {
		super(parent);
	}
	
	protected GraphTokenizer(String[] lines) {
		super(lines);
	}
	
	private String[] trimEmptyWords(String[] list) {
		ArrayList<String> result = new ArrayList<>();
		for (int i=0; i<list.length; i++)
			if (list[i].length()>0)
				result.add(list[i]);
		String[] res = new String[result.size()];
		return result.toArray(res);
	}
	
	private void processLine(String line) {
		String[] words = line.trim().split(COMMENT.prefix());
		if (words.length>1) { // a comment was found
			// process the beginning of the line
			processLine(words[0].trim());
			// end of the line is comment text that may include more '//'s
			cttoken = new token(COMMENT,"");
			for (int i=1; i<words.length; i++)
				cttoken.value += words[i];
			tokenlist.add(cttoken);
			cttoken = null;
			return;
		}
		words = line.trim().split(PROPERTY_NAME.suffix());
		if (words.length>1)  { // a property name was found
			if (words.length==2) {
				tokenlist.add(new token(PROPERTY_NAME,words[0].trim()));
				processLine(words[1]);
				return;
			}
			else
				throw new OmugiException("GraphTokenizer: malformed property format");
		}
		words = line.trim().split("\\(");
		if (words.length>1)  { // a property type (and value) was found
			if (words.length==2) {
				tokenlist.add(new token(PROPERTY_TYPE,words[0].trim()));
				tokenlist.add(new token(PROPERTY_VALUE,words[1].substring(0,words[1].indexOf(PROPERTY_VALUE.suffix())).trim()));
				return;
			}
			else
				throw new OmugiException("GraphTokenizer: malformed property format");
		}
		words = line.trim().split("[\\[\\]]"); // TODO: generate regular expression from GraphTokens.
		if (words.length>1) { // an edge definition was found
			words = trimEmptyWords(words);
			if (words.length==3) {
				tokenlist.add(new token(NODE_REF,words[0].trim()));
				processLine(words[1]);
				tokenlist.add(new token(NODE_REF,words[2].trim()));
				return;
			}
			else
				throw new OmugiException("GraphTokenizer: malformed edge format");
		}
		words = line.trim().split("\\s"); // matches any whitespace character
		if (words.length>1) { 
			tokenlist.add(new token(LABEL,words[0].trim())); // first word is label
			cttoken = new token(NAME,"");
			for (int i=1; i<words.length; i++) { // anything else is name
				cttoken.value += words[i];
			}
			tokenlist.add(cttoken);
			return;
		}		
	}

	@Override
	public void tokenize() {
		if (lines!=null)
			for (String line:lines) {
				processLine(line);
			}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (token t:tokenlist)
			sb.append(t.toString()).append('\n');
		return sb.toString();
	}

}

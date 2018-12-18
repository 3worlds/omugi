package fr.cnrs.iees.io.tree;

import java.util.ArrayList;
import java.util.List;

import fr.cnrs.iees.io.Tokenizer;
import static fr.cnrs.iees.io.tree.ReferenceTokens.*;

/**
 * <p>A tokenizer for string references to nodes in a tree.
 * The syntax for references is as follows:</p>
 * <pre>
 * reference = nodeReference {'/' nodeReference} 
 * nodeReference = [classId ':'][instanceId]{'+' key '=' value}
 * </pre>
 * <p>where:<br/>
 * {@code classId} is the node type (formerly known as <em>label</em>)<br/>
 * {@code instanceId} is the node id (formerly known as <em>name</em>)<br/>
 * {@code key} is an optional property name<br/>
 * {@code value} is a property value
 * </p>
 * <p>Since {@code classId} and {@code instanceId} are optional, a reference may match to
 * a set of nodes.</p>
 * 
 * @author Jacques Gignoux - 18 déc. 2018true
 *
 */
// Tested OK with version 0.0.3 on 18/12/2018 
public class ReferenceTokenizer implements Tokenizer {

	private class token {
		ReferenceTokens type;
		String value;
		
		private token(ReferenceTokens type, String value) {
			super();
			this.type = type;
			this.value = value;
		}
		
		@Override
		public String toString() {
			return type+":"+value;
		}
	}
	
	private String reference = null;
	private List<token> tokenlist = new ArrayList<token>(100);
	
	// constructor
	public ReferenceTokenizer(String ref) {
		super();
		reference = ref;
	}

	private void tokenizeProperties(String[] props) {
		for (int i=1; i<props.length; i++) {
			String[] ss = props[i].split(PROPERTY_VALUE.prefix());
			tokenlist.add(new token(PROPERTY_NAME,ss[0]));
			tokenlist.add(new token(PROPERTY_VALUE,ss[1]));
		}
	}
	
	@Override
	public void tokenize() {
		// split on '/'
		String[] words = reference.split(NODE_REF.suffix());
		for (String tokenString:words) {
			// split on ':'
			String[] tkwords = tokenString.split(NODE_LABEL.suffix());
			if (tokenString.contains(NODE_LABEL.suffix()))
				tokenlist.add(new token(NODE_LABEL,tkwords[0]));
			else
				tokenlist.add(new token(NODE_LABEL,""));
			if (tkwords.length>1) {
				// split on '+' (replaced by '\+' because of regexp)
				String[] propwords = tkwords[1].split("\\"+PROPERTY_NAME.prefix());
				tokenlist.add(new token(NODE_NAME,propwords[0]));
				tokenizeProperties(propwords);
			}
			else {
				tokenlist.add(new token(NODE_NAME,""));
				// split on '+' (replaced by '\+' because of regexp)
				String[] propwords = tokenString.split("\\"+PROPERTY_NAME.prefix());
				tokenizeProperties(propwords);
			}
		}
	}
	
	@Override
	public boolean tokenized() {
		return !tokenlist.isEmpty();
	}
	
	/**
	 * Gets the parser able to parse this tokenizer's token list
	 * 
	 * @return a new instance of the parser.
	 */
	public ReferenceParser parser() {
		return new ReferenceParser(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (token t:tokenlist)
			sb.append(t.toString()).append('\n');
		return sb.toString();
	}

}

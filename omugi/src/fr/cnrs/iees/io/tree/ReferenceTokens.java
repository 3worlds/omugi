package fr.cnrs.iees.io.tree;

import fr.cnrs.iees.graph.generic.Element;
import fr.ens.biologie.generic.SaveableAsText;

/**
 * Token types used in node references. Based on the former IoConstants interface 
 * in package au.edu.anu.rscs.aot.graph.io.
 * 
 * @author Jacques Gignoux - 18 déc. 2018
 *
 */
public enum ReferenceTokens {

	// token type	prefix						content type	suffix
	NODE_REF		('\0',							"token",	SaveableAsText.SLASH),
	NODE_LABEL		('\0',							"String",	Element.LABEL_NAME_SEPARATOR),
	NODE_NAME		(Element.LABEL_NAME_SEPARATOR,	"String",	'\0'),
	PROPERTY_NAME	(SaveableAsText.PLUS,			"String",	SaveableAsText.EQUAL),
	PROPERTY_VALUE	(SaveableAsText.EQUAL,			"Object",	'\0'),
	;
	
	private final char prefix;
	private final String type;
	private final char suffix;
	
	private ReferenceTokens(char prefix, String type, char suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.type = type;
	}
	
	public String tokenType() {
		return type;
	}
	
	public String suffix() {
		return String.valueOf(suffix);
	}
	
	public String prefix() {
		return String.valueOf(prefix);
	}

}
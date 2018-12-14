package fr.cnrs.iees.io.graph;

public enum GraphTokens {

	COMMENT			("//","String","eol"),
	PROPERTY_NAME	("","String","="),
	PROPERTY_VALUE	("(","any",")"),
	PROPERTY_TYPE	("=","java","("),
	LABEL			("","String",""),
	NAME			("","String",""),
	NODE_REF		("[","String","]"),
//	IMPORT			("import","filename","")
	;
	private final String prefix;
	private final String type;
	private final String suffix;

	private GraphTokens(String prefix, String type, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.type = type;
	}
	
	public String tokenType() {
		return type;
	}

	public String suffix() {
		return suffix;
	}

	public String prefix() {
		return prefix;
	}
}

package fr.cnrs.iees.io.graph;

/**
 * List of graph properties understood by {@link GraphParser} and their default
 * values.
 * 
 * @author Jacques Gignoux - 13 déc. 2018
 *
 */
public enum GraphProperties {
	// name		property name	property class										property default value
	CLASS		("type",		"fr.cnrs.iees.graph.generic.Graph",					"fr.cnrs.iees.graph.generic.impl.ImmutableGraphImpl"),
	FACTORY		("factory",		"fr.cnrs.iees.graph.generic.GraphElementFactory",	"fr.cnrs.iees.graph.generic.impl.DefaultGraphFactory"),
	DIRECTED	("directed",	"java.lang.Boolean",								"true"),
	MUTABLE		("mutable",		"java.lang.Boolean",								"false"),
	// others to come ?
	;
	
	private final String propertyName;
	private final String propertyType;
	private final String defaultValue;
	
	private GraphProperties(String propertyName, String propertyType, String defaultValue) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.defaultValue = defaultValue;
	}
	
	public String defaultValue() {
		return defaultValue;
	}
	
	public String propertyName() {
		return propertyName;
	}
	
	public String propertyType() {
		return propertyType;
	}
	
	public static GraphProperties propertyForName(String name) {
		for (GraphProperties p: GraphProperties.values())
			if (p.propertyName.equals(name))
				return p;
		return null;
	}
}

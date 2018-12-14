package fr.cnrs.iees.io.graph;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.generic.impl.MutableGraphImpl;
import fr.cnrs.iees.graph.io.ValidPropertyTypes;
import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.io.Parser;
import fr.cnrs.iees.io.graph.GraphTokenizer.token;
import fr.ens.biologie.generic.Labelled;
import fr.ens.biologie.generic.Named;

/**
 * <p>A replacement parser for Shayne's 'UniversalParser'. Simpler. Maybe Faster. 
 * Who am I to pretend it's better.</p>
 * 
 * <p>This parser is initialised with a {@link GraphTokenizer}, i.e. it gobbles a list of
 * tokens and spits out a {@link Graph} when asked for it. It is lazy, i.e. it will not do anything until
 * asked for a graph (i.e. invoking the {@code GraphParser.graph()} method, and it will parse only once after
 * initialisation. Further calls to {@code .graph()} return the already parsed graph.</p>
 * 
 * <p>Parsing is done in a single pass on the token list.</p>
 * 
 * <p>Options to setup the graph may be passed through graph-level properties in the file. These 
 * are found in {@link GraphProperties}. The best way to go is to implement a specific {@link GraphElementFactory}
 * which will implement which flavour of {@link Node}, {@link Edge} and property list
 * (cf. {@link PropertyListGetters} descendants) should be used to construct
 * the graph.</p> 
 * 
 * <p>Allowed property types are those listed in {@link ValidPropertyTypes}. They can be
 * specified as fully qualified java class names, or as simple strings as found in {@code ValidPropertyTypes}.</p>
 * 
 *  <p>Note that the best use of this class is to hide it inside a {@link GraphImporter}.</p>
 * 
 * @author Jacques Gignoux - 12 déc. 2018
 *
 */
//todo: import	
// add options at graph level for property list types, node types, edge types ???
public class GraphParser extends Parser {
	
	private Logger log = Logger.getLogger(GraphParser.class.getName());
	
	// which type of item is currently being constructed
	private enum itemType {
		GRAPH,
		NODE,
		EDGE
	}
	
	// specifications for a property
	private class propSpec {
		protected String name;
		protected String type;
		protected String value;
		@Override // for debugging only
		public String toString() {
			return name+":"+type+"="+value;
		}
	}

	// specifications for a node
	private class nodeSpec {
		protected String label;
		protected String name;
		protected List<propSpec> props = new LinkedList<propSpec>();
		@Override // for debugging only
		public String toString() {
			return label+":"+name;
		}
	}

	// specifications for an edge
	private class edgeSpec {
		protected String label;
		protected String name;
		protected String start;
		protected String end;
		protected List<propSpec> props = new LinkedList<propSpec>();
		@Override // for debugging only
		public String toString() {
			return label+":"+name+" ["+start+"-->"+end+"]";
		}
	}
	
	// the tokenizer used to read the file
	private GraphTokenizer tokenizer = null;
	
	// the factory used to build the graph
	private GraphElementFactory factory = null;
	
	// the list of specifications built from the token list
	private List<propSpec> graphProps = new LinkedList<propSpec>();
	private List<nodeSpec> nodeSpecs =  new LinkedList<nodeSpec>();
	private List<edgeSpec> edgeSpecs =  new LinkedList<edgeSpec>();
	
	// the last processed item
	private itemType lastItem = null;
	private propSpec lastProp = null;
	private nodeSpec lastNode = null;
	private edgeSpec lastEdge = null;
	
	// the result of this parsing
	private Graph<? extends Node,? extends Edge> graph = null;
	
	// lazy init: nothing is done before it's needed
	public GraphParser(GraphTokenizer tokenizer) {
		super();
		this.tokenizer =tokenizer;
	}
	
	/**
	 * <p>Construct the list of specifications for node and edge to build from the token list.</p>
	 * <p>Rules:</p>
	 * <ul>
	 * <li>There are three item types: {@code GRAPH, NODE, EDGE}
	 * <li>A sequence of {@code (NODE_REF, LABEL, NAME, NODE_REF)} tokens specifies an {@code EDGE}</li>
	 * <li>A sequence of {@code (LABEL, NAME)} tokens specifies a {@code NODE}</li>
	 * <li>A sequence of {@code (PROPERTY_NAME, PROPERTY_TYPE, PROPERTY_VALUE)} tokens specifies a property</li>
	 * <li>A property specification applies to the last item ({@code NODE} or {@code EDGE}) found</li>
	 * <li>Properties found at the top of the list, before any edge or node specification, apply to the graph</li>
	 * <li>Comments are skipped</li>
	 * </ul>
	 */
	protected void parse() {
		if (!tokenizer.tokenized())
			tokenizer.tokenize();
		lastItem = itemType.GRAPH;
		while (tokenizer.hasNext()) {
			token tk = tokenizer.getNextToken();
			switch (tk.type) {
				case COMMENT:
					break;
				case PROPERTY_NAME:
					lastProp = new propSpec();
					lastProp.name = tk.value;
					break;
				case PROPERTY_VALUE:
					lastProp.value = tk.value;
					switch (lastItem) {
						case GRAPH: // this is a graph property
							graphProps.add(lastProp);
							break;
						case NODE: // this is a node property
							lastNode.props.add(lastProp);
							break;
						case EDGE: // this is an edge property
							lastEdge.props.add(lastProp);
							break;
					}
					break;
				case PROPERTY_TYPE:	
					lastProp.type = tk.value;
					break;
				case LABEL:		
					switch (lastItem) {
						case GRAPH:
						case NODE:
							lastNode = new nodeSpec();
							lastNode.label = tk.value;
							lastItem = itemType.NODE;
							break;
						case EDGE:
							if (lastEdge.label==null)
								lastEdge.label = tk.value;
							else { // this is a node label
								lastNode = new nodeSpec();
								lastNode.label = tk.value;
								lastItem = itemType.NODE;
							}
							break;
					}
					break;
				case NAME:			
					switch (lastItem) {
						case GRAPH:
							log.severe("missing node label declaration");
							break;
//							throw new OmugiException("missing node label declaration");
						case NODE:
							lastNode.name = tk.value;
							nodeSpecs.add(lastNode);
							break;
						case EDGE:
							lastEdge.name = tk.value;
					}
					break;
				case NODE_REF:
					switch (lastItem) {
						case GRAPH:
						case NODE:
							lastEdge = new edgeSpec();
							lastEdge.start = tk.value;
							lastItem = itemType.EDGE;
							break;
						case EDGE:
							if (lastEdge.end==null) {
								lastEdge.end = tk.value;
								edgeSpecs.add(lastEdge);
							}
							else {
								lastEdge = new edgeSpec();
								lastEdge.start = tk.value;
							}
							break;
					}
					break;
			}
		}		
	}

	// gets a class from the graph properties
	private Class<?> getClass(GraphProperties gp, String value) {
		Class<?> result = null;
		if (value!=null)
			try {
				Class<?> c = Class.forName(value);
				if (Graph.class.isAssignableFrom(c))
					result = c;
				else
					log.severe("graph property \""+ gp.propertyName() +
						"\" does not refer to a valid type (" + gp.propertyType() +
						") - using default type (" + gp.defaultValue() +
						")");
			} catch (ClassNotFoundException e) {
				log.severe("graph property \""+ gp.propertyName() +
					"\" does not refer to a valid java class - using default type (" + gp.defaultValue() +
					")");
		}
		if (result==null)
			try {
				result = Class.forName(gp.defaultValue());
			} catch (ClassNotFoundException e) {
				// this is an error in GraphProperties.[...].defaultValue - fix code with a correct class name
				e.printStackTrace();
			}
		// this will always return a valid, non null class - if problems, it will throw an exception
		return result;
	}
	
	// gets a default class from the graph properties
	private Class<?> getClass(GraphProperties gp) {
		return getClass(gp,null);
	}
	
	// builds a propertyList from specs
	private ReadOnlyPropertyList makePropertyList(List<propSpec> props) {
		List<Property> pl = new LinkedList<Property>();
		for (propSpec p:props) {
			String className = ValidPropertyTypes.getJavaClassName(p.type);
			if (className==null)
				log.severe("unknown property type ("+p.type+")");
			else {
				Object o = null;
				try {
					Class<?> c = Class.forName(className);
					// if method present, instantiate object with valueOf(String)
					for (Method m:c.getMethods())
						if (m.getName().equals("valueOf")) {
							Class<?>[] pt = m.getParameterTypes();
							if (pt.length==1)
								if (pt[0].getSimpleName().equals("String") ) {
									o = m.invoke(null, p.value);
									break;
							}
					}
					// else must be a String
					if (o==null)
						o = p.value;
				} catch (ClassNotFoundException e) {
					// We should reach here only if there is an error in ValidPropertyTypes
					e.printStackTrace();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// this occurs if the value is not of the proper type
					o=null;
				}
				if (o==null)
					log.severe("could not instantiate property \""
						+p.name+":"+p.type+"\" with value \""+p.value+"\"");
				else
					pl.add(new Property(p.name,o));
			}
		}
		Property[] pp = new Property[pl.size()];
		int i=0;
		for (Property p:pl)
			pp[i++] = p;
		return factory.makePropertyList(pp);
	}
	
	// builds the graph from the parsed data
	@SuppressWarnings("unchecked")
	private void buildGraph() {
		// parse token if not yet done
		if (lastItem==null)
			parse();
		Class<? extends Graph<? extends Node, ? extends Edge>> graphClass = null;
		Class<? extends GraphElementFactory> factoryClass = null;
		// scan graph properties for graph building options
		for (propSpec p:graphProps) {
			switch (GraphProperties.propertyForName(p.name))  {
				case CLASS:
					graphClass = (Class<? extends Graph<? extends Node, ? extends Edge>>) 
						getClass(GraphProperties.CLASS,p.value);
					break;
				case FACTORY:
					factoryClass = (Class<? extends GraphElementFactory>) 
						getClass(GraphProperties.FACTORY,p.value);
					break;
				case DIRECTED:
					// TODO
					break;
				case MUTABLE:
					graphClass = (Class<? extends Graph<? extends Node, ? extends Edge>>) 
						getClass(GraphProperties.CLASS,MutableGraphImpl.class.getName());
					break;
				default:
					// other properties are ignored by the parser
					break;
			}
		}
		// use default settings if graph properties were absent
		if (graphClass==null)
			graphClass = (Class<? extends Graph<? extends Node, ? extends Edge>>) 
				getClass(GraphProperties.CLASS);
		if (factoryClass==null)
			factoryClass = (Class<? extends GraphElementFactory>) 
				getClass(GraphProperties.FACTORY);
		// setup the factory
		try {
			factory = factoryClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// There should not be any problem here given the previous checks
			// unless the factory class is flawed
			e.printStackTrace();
		}
		// make nodes and edges
		Map<String,Node> nodes = new HashMap<>();
		for (nodeSpec ns: nodeSpecs) {
			Node n = null;
			if (ns.props.isEmpty())
				n = factory.makeNode();
			else
				n = factory.makeNode(makePropertyList(ns.props));
			if (Labelled.class.isAssignableFrom(n.getClass())) 
				((Labelled)n).setLabel(ns.label);
			if (Named.class.isAssignableFrom(n.getClass())) 
				((Named)n).setName(ns.name);
			String nodeId = ns.label.trim()+":"+ns.name.trim();
			if (nodes.containsKey(nodeId))
				log.severe("duplicate node found ("+") - ignoring the second one");
			else
				nodes.put(nodeId,n);
		}
		for (edgeSpec es:edgeSpecs) {
			Edge e = null;
			String[] refs = es.start.split(":");
			String ref = refs[0].trim()+":"+refs[1].trim();
			Node start = nodes.get(ref);
			if (start==null)
				log.severe("start node "+ref+" not found for edge "+es.label+":"+es.name);
			refs = es.end.split(":");
			ref = refs[0].trim()+":"+refs[1].trim();
			Node end = nodes.get(ref);
			if (end==null)
				log.severe("end node "+ref+" not found for edge "+es.label+":"+es.name);
			if ((start!=null)&&(end!=null)) {
				if (es.props.isEmpty())
					e = factory.makeEdge(start, end);
				else 
					e = factory.makeEdge(start,end,makePropertyList(es.props));
				if (Labelled.class.isAssignableFrom(e.getClass())) 
					((Labelled)e).setLabel(es.label);
				if (Named.class.isAssignableFrom(e.getClass())) 
					((Named)e).setName(es.name);
			}
		}
		// make graph
		try {
			Constructor<?> cons = graphClass.getConstructor(Iterable.class);
			graph = (Graph<? extends Node, ? extends Edge>) cons.newInstance(nodes.values()); // pbs with non empty constructors
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the graph build from this parser
	 */
	public Graph<? extends Node,? extends Edge> graph() {
		if (graph==null)
			buildGraph();
		return graph;
	}
	
	// for debugging only
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graph specification\n");
		if (!graphProps.isEmpty())
			sb.append("Graph properties:\n");
		for (propSpec p:graphProps)
			sb.append('\t').append(p.toString()).append('\n');
		if (!nodeSpecs.isEmpty())
			sb.append("Nodes:\n");
		for (nodeSpec n:nodeSpecs) {
			sb.append('\t').append(n.toString()).append('\n');
			for (propSpec p:n.props)
				sb.append("\t\t").append(p.toString()).append('\n');
		}
		if (!edgeSpecs.isEmpty())
			sb.append("Edges:\n");
		for (edgeSpec e:edgeSpecs) {
			sb.append('\t').append(e.toString()).append('\n');
			for (propSpec p:e.props)
				sb.append("\t\t").append(p.toString()).append('\n');
		}
		return sb.toString();
	}
	
}

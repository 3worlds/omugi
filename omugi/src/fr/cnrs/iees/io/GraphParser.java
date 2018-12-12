package fr.cnrs.iees.io;

import java.util.LinkedList;
import java.util.List;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.generic.impl.DefaultGraphFactory;
import fr.cnrs.iees.io.GraphTokenizer.token;

/**
 * 
 * @author Jacques Gignoux - 12 déc. 2018
 *
 */
//todo: import	
public class GraphParser {
	
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
	private GraphElementFactory factory = new DefaultGraphFactory();
	
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
							throw new OmugiException("GraphParser: missing node label declaration");
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
	
	// builds the graph from the parsed data
	private void buildGraph() {
		if (lastItem==null)
			parse();
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

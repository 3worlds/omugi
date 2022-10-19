/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            *
 *                                                                        *
 *  OMUGI is an API to implement graphs, as described by graph theory,    *
 *  but also as more commonly used in computing - e.g. dynamic graphs.    *
 *  It interfaces with JGraphT, an API for mathematical graphs, and       *
 *  GraphStream, an API for visual graphs.                                *
 *                                                                        *
 **************************************************************************
 *  This file is part of OMUGI (One More Ultimate Graph Implementation).  *
 *                                                                        *
 *  OMUGI is free software: you can redistribute it and/or modify         *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  OMUGI is distributed in the hope that it will be useful,              *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with OMUGI.  If not, see <https://www.gnu.org/licenses/gpl.html>*
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.io.parsing.impl;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.util.Resources;
import fr.cnrs.iees.graph.*;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.io.parsing.impl.GraphTokenizer.graphToken;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.omhtk.utils.Logging;

/**
 * <p>A parser for plain graphs. </p>
 *
 * <p>This parser is initialised with a {@link GraphTokenizer}, i.e. it gobbles a list of
 * tokens and spits out a {@link Graph} when asked for it. It is lazy, i.e. it will not do anything until
 * asked for a graph, i.e. invoking the {@code GraphParser.graph()} method, and it will parse only once after
 * initialisation. Further calls to {@code .graph()} return the already parsed graph.</p>
 *
 * <p>Parsing is done in a single pass on the token list.</p>
 *
 * <p>Options to setup the graph may be passed through graph-level properties in the file. These
 * are found in {@link NodeSetParser} and {@link EdgeAndNodeSetParser}. The best way to go is to implement specific {@link NodeFactory},
 * {@link EdgeFactory} and {@link PropertyListFactory}
 * which will implement which flavour of {@link Node}, {@link Edge} and property list
 * (cf. {@link fr.cnrs.iees.properties.PropertyListGetters PropertyListGetters} descendants) should be used to construct
 * the graph.</p>
 *
 * <p>Allowed property types are those listed in {@link ValidPropertyTypes}. They can be
 * specified as fully qualified java class names, or as simple strings as found in {@code ValidPropertyTypes}.</p>
 *
 *  <p>Note that the best use of this class is to hide it inside a {@link fr.cnrs.iees.graph.io.GraphImporter GraphImporter}.</p>
 *
 * @author Jacques Gignoux - 12 déc. 2018
 *
 */
// Replacement parser for Shayne's 'UniversalParser'. Simpler. Maybe Faster.
// Who am I to pretend it's better.
//todo: import
// Tested OK with version 0.0.1 on 17/12/2018
// Tested OK with version 0.0.10 on 31/1/2019
// tested OK with version 0.2.0 on 20/5/2019
// Tested OK with version 0.2.1 on 27/5/2019
//Tested OK with version 0.2.16 on 1/7/2020
public class GraphParser extends EdgeAndNodeSetParser {

	private static Logger log = Logging.getLogger(GraphParser.class);
	static {
		log.setLevel(Level.OFF);
	}

	//----------------------------------------------------
	// which type of item is currently being constructed
	private enum itemType {
		GRAPH,
		NODE,
		EDGE
	}
	//----------------------------------------------------

	// the tokenizer used to read the file
	private GraphTokenizer tokenizer = null;

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

	/**
	 * Constructor from a GraphTokenizer. Lazy init: nothing is done before it's needed.
	 * 
	 * @param tokenizer The tokenizer {@link GraphTokenizer}
	 */
	public GraphParser(GraphTokenizer tokenizer) {
		super();
		this.tokenizer =tokenizer;
		// setup of default graph properties for this parser
		defaultGraphProperties.put(CLASS, 			"fr.cnrs.iees.graph.impl.ALGraph");
		defaultGraphProperties.put(NODE_FACTORY, 	"fr.cnrs.iees.graph.impl.ALGraphFactory");
		defaultGraphProperties.put(EDGE_FACTORY, 	"fr.cnrs.iees.graph.impl.ALGraphFactory");
//		defaultGraphProperties.put(PROP_FACTORY, 	"fr.cnrs.iees.graph.impl.ALGraphFactory");
		defaultGraphProperties.put(SCOPE, 			"DGF");
		graphPropertyTypes.put(CLASS,			Graph.class);
		graphPropertyTypes.put(NODE_FACTORY, 	NodeFactory.class);
		graphPropertyTypes.put(EDGE_FACTORY, 	EdgeFactory.class);
//		graphPropertyTypes.put(PROP_FACTORY, 	PropertyListFactory.class);
		graphPropertyTypes.put(SCOPE, 			String.class);
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
	@Override
	protected void parse() {
		if (!tokenizer.tokenized())
			tokenizer.tokenize();
		lastItem = itemType.GRAPH;
		while (tokenizer.hasNext()) {
			graphToken tk = tokenizer.getNextToken();
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
				case IMPORT_RESOURCE:
					lastNode.imports.add(
						new importGraph(
								new GraphParser(
									new GraphTokenizer(Resources.getTextResource(
										Resources.getPackagedFileName(tk.value))))));
					break;
			case LEVEL:
				throw new IllegalArgumentException("Invalid token type for a graph");
			default:
				break;
			}
		}
	}

	// builds the graph from the parsed data
	@SuppressWarnings("unchecked")
	private void buildGraph() {
		// parse tokens if not yet done
		if (lastItem==null)
			parse();
		// setup factories and graph
		processGraphProperties(graphProps,log);
		setupFactories(log);
		graph = (Graph<? extends Node, ? extends Edge>) setupGraph(log);
		// make nodes
		Map<String,Node> nodes = new HashMap<>();
		for (nodeSpec ns: nodeSpecs) {
			Node n = null;
			Class<? extends Node> nc = nodeFactory.nodeClass(ns.label);
			if (ns.props.isEmpty())
				if (nc==null)
					n = nodeFactory.makeNode(ns.name);
				else
					n = nodeFactory.makeNode(nc,ns.name);
			else
				if (nc==null)
					n = nodeFactory.makeNode(ns.name,
						makePropertyList(nodeFactory.nodePropertyFactory(),ns.props,log));
				else
					n = nodeFactory.makeNode(nc,ns.name,
						makePropertyList(nodeFactory.nodePropertyFactory(),ns.props,log));
			String nodeId = (ns.label+":"+ns.name).replaceAll("\\s","");
			if (nodes.containsKey(nodeId))
				log.severe(()->"duplicate node found ("+") - ignoring the second one");
			else
				nodes.put(nodeId,n);
			/*-
			 * Add in any imported graphs.
			 * The imported graph must use the node's factory instance
			 */
			for (importGraph ig : ns.imports) {
				Node parent = n;
				Graph<? extends Node,? extends Edge> importGraph =
					(Graph<? extends Node, ? extends Edge>) ig.getGraph(parent.factory());
				for (Node importNode : importGraph.nodes()) {
					// TODO: finish this!
					throw new UnsupportedOperationException("Import within a graph file not yet implemented: "+importNode.toDetailedString());
					
				}
			}

		}
		// make edges
		for (edgeSpec es:edgeSpecs) {
			String ref = es.start.replaceAll("\\s","");
			Node start = nodes.get(ref);
			if (start==null)
				log.severe("start node "+ref+" not found for edge "+es.label+":"+es.name);
			ref = es.end.replaceAll("\\s","");
			Node end = nodes.get(ref);
			if (end==null)
				log.severe("end node "+ref+" not found for edge "+es.label+":"+es.name);
			if ((start!=null)&&(end!=null)) {
				Class<? extends Edge> ec = edgeFactory.edgeClass(es.label);
				if (es.props.isEmpty())
					if (ec==null)
						edgeFactory.makeEdge(start,end,es.name);
					else
						edgeFactory.makeEdge(ec,start,end,es.name);
				else
					if (ec==null)
						edgeFactory.makeEdge(start,end,es.name,
							makePropertyList(edgeFactory.edgePropertyFactory(),es.props,log));
					else
						edgeFactory.makeEdge(ec,start,end,es.name,
							makePropertyList(edgeFactory.edgePropertyFactory(),es.props,log));
			}
		}

	}

	@Override
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

	@Override
	public void setFactory(Object factory) {
		// TODO Auto-generated method stub

	}

}

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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.impl.DefaultGraphFactory;
import fr.cnrs.iees.graph.impl.MutableGraphImpl;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.io.parsing.impl.GraphTokenizer.graphToken;
import fr.cnrs.iees.properties.PropertyListFactory;

/**
 * <p>A replacement parser for Shayne's 'UniversalParser'. Simpler. Maybe Faster. 
 * Who am I to pretend it's better.</p>
 * 
 * <p>This parser is initialised with a {@link GraphTokenizer}, i.e. it gobbles a list of
 * tokens and spits out a {@link Graph} when asked for it. It is lazy, i.e. it will not do anything until
 * asked for a graph, i.e. invoking the {@code GraphParser.graph()} method, and it will parse only once after
 * initialisation. Further calls to {@code .graph()} return the already parsed graph.</p>
 * 
 * <p>Parsing is done in a single pass on the token list.</p>
 * 
 * <p>Options to setup the graph may be passed through graph-level properties in the file. These 
 * are found in {@link GraphProperties}. The best way to go is to implement specific {@link NodeFactory},
 * {@link EdgeFactory} and {@link PropertyListFactory}
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
// Tested OK with version 0.0.1 on 17/12/2018
// Tested OK with version 0.0.10 on 31/1/2019
public class GraphParser extends MinimalGraphParser {
	
	private Logger log = Logger.getLogger(GraphParser.class.getName());

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
	
	// the factories used to build the graph
	private NodeFactory nodeFactory = null;
	private EdgeFactory edgeFactory = null;
	
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
			case LEVEL:
				throw new OmugiException("Invalid token type for a graph");
			default:
				break;
			}
		}		
	}

	// builds the graph from the parsed data
	@SuppressWarnings("unchecked")
	private void buildGraph() {
		// parse token if not yet done
		if (lastItem==null)
			parse();
		Class<? extends Graph<? extends Node, ? extends Edge>> graphClass = null;
		Class<? extends NodeFactory> nFactoryClass = null;
		Class<? extends EdgeFactory> eFactoryClass = null;
		Class<? extends PropertyListFactory> plFactoryClass = null;
		Map<String,String> labels = new HashMap<>();
		// scan graph properties for graph building options
		for (propSpec p:graphProps) {
			GraphProperties gp = GraphProperties.propertyForName(p.name);
			if (gp==null) { // all other properties are considered to be (label,class name) pairs
				if (p.type.contains("String"))
					labels.put(p.name,p.value);
			}
			else switch (gp)  {
				case CLASS:
					graphClass = (Class<? extends Graph<? extends Node, ? extends Edge>>) 
						getClass(GraphProperties.CLASS,p.value,log);
					break;
				case NODE_FACTORY:
					nFactoryClass = (Class<? extends NodeFactory>) 
						getClass(GraphProperties.NODE_FACTORY,p.value,log);
					break;
				case EDGE_FACTORY:
					eFactoryClass = (Class<? extends EdgeFactory>) 
						getClass(GraphProperties.EDGE_FACTORY,p.value,log);
					break;
				case PROP_FACTORY:
					plFactoryClass = (Class<? extends PropertyListFactory>) 
						getClass(GraphProperties.PROP_FACTORY,p.value,log);
					break;
				case DIRECTED:
					// TODO
					break;
				case MUTABLE:
					graphClass = (Class<? extends Graph<? extends Node, ? extends Edge>>) 
						getClass(GraphProperties.CLASS,MutableGraphImpl.class.getName(),log);
					break;
				default: 
					break;
			}
		}
		// use default settings if graph properties were absent
		if (graphClass==null)
			graphClass = (Class<? extends Graph<? extends Node, ? extends Edge>>) 
				getClass(GraphProperties.CLASS,log);
		if (nFactoryClass==null)
			nFactoryClass = (Class<? extends NodeFactory>) 
				getClass(GraphProperties.NODE_FACTORY,log);
		if (eFactoryClass==null)
			eFactoryClass = (Class<? extends EdgeFactory>) 
				getClass(GraphProperties.EDGE_FACTORY,log);
		if (plFactoryClass==null)
			plFactoryClass = (Class<? extends PropertyListFactory>) 
				getClass(GraphProperties.PROP_FACTORY,log);
		// setup the factories
		try {
			Constructor<? extends NodeFactory> c = nFactoryClass.getConstructor(Map.class);
			nodeFactory = c.newInstance(labels);
			edgeFactory = eFactoryClass.newInstance();
			if (eFactoryClass.equals(nFactoryClass))
				if (nodeFactory instanceof DefaultGraphFactory)
					edgeFactory = (EdgeFactory) nodeFactory;
			propertyListFactory = plFactoryClass.newInstance();
			if (plFactoryClass.equals(nFactoryClass))
				if (nodeFactory instanceof DefaultGraphFactory)
					propertyListFactory = (PropertyListFactory) nodeFactory;
		} catch (Exception e) {
			// There should not be any problem here given the previous checks
			// unless the factory class is flawed
			e.printStackTrace();
		}
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
					n = nodeFactory.makeNode(ns.name,makePropertyList(ns.props,log));
				else
					n = nodeFactory.makeNode(nc,ns.name,makePropertyList(ns.props,log));
			String nodeId = (ns.label+":"+ns.name).replaceAll("\\s","");
			if (nodes.containsKey(nodeId))
				log.severe(()->"duplicate node found ("+") - ignoring the second one");
			else
				nodes.put(nodeId,n);
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
						edgeFactory.makeEdge(start,end,es.name,makePropertyList(es.props,log));
					else
						edgeFactory.makeEdge(ec,start,end,es.name,makePropertyList(es.props,log));
			}
		}
		// make graph and fill it with nodes
		try {
			Constructor<?> cons = graphClass.getConstructor(Iterable.class);
			graph = (Graph<? extends Node, ? extends Edge>) cons.newInstance(nodes.values()); // pbs with non empty constructors
		} catch (Exception e) {
			log.severe("Graph constructor not found.");
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

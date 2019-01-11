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

import static fr.cnrs.iees.io.parsing.TextGrammar.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.collections.tables.Table;
import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.impl.MutableGraphImpl;
import fr.cnrs.iees.io.parsing.Parser;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.io.parsing.impl.GraphTokenizer.token;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
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
// Tested OK with version 0.0.1 on 17/12/2018
public class GraphParser extends Parser {
	
	private Logger log = Logger.getLogger(GraphParser.class.getName());

	//----------------------------------------------------
	// which type of item is currently being constructed
	private enum itemType {
		GRAPH,
		NODE,
		EDGE
	}
	//----------------------------------------------------
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
	//----------------------------------------------------
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
	//----------------------------------------------------
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
	//----------------------------------------------------
	
	// the tokenizer used to read the file
	private GraphTokenizer tokenizer = null;
	
	// the factories used to build the graph
	private NodeFactory nodeFactory = null;
	private EdgeFactory edgeFactory = null;
	private PropertyListFactory propertyListFactory = null;
	
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
					// if method present, instantiate object with valueOf()
					for (Method m:c.getMethods())
						if (m.getName().equals("valueOf")) {
							Class<?>[] pt = m.getParameterTypes();
							// first case, valueOf() only has a String argument --> primitive types
							if (pt.length==1) {
								if (String.class.isAssignableFrom(pt[0])) {
									o = m.invoke(null, p.value);
									break;
								}
							}
							// Second case, value of has 3 arguments --> Table type
							else if (pt.length==3) {
								if ((String.class.isAssignableFrom(pt[0])) &&
										(char[][].class.isAssignableFrom(pt[1])) &&
										(char[].class.isAssignableFrom(pt[2])) ) {
									char[][] bdel = new char[2][2];
									bdel[Table.DIMix] = DIM_BLOCK_DELIMITERS;
									bdel[Table.TABLEix] = TABLE_BLOCK_DELIMITERS;
									char[] isep = new char[2];
									isep[Table.DIMix] = DIM_ITEM_SEPARATOR;
									isep[Table.TABLEix] = TABLE_ITEM_SEPARATOR;
									o = m.invoke(null,p.value,bdel,isep);
								}
							}
					}
					// else must be a String
					if (o==null) {
						if (p.value.equals("null"))
							o = null;
						else
							o = p.value;
					}
				} catch (ClassNotFoundException e) {
					// We should reach here only if there is an error in ValidPropertyTypes
					e.printStackTrace();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// this occurs if the value is not of the proper type
					o=null;
				}
				pl.add(new Property(p.name,o));
			}
		}
		Property[] pp = new Property[pl.size()];
		int i=0;
		for (Property p:pl)
			pp[i++] = p;
		return propertyListFactory.makePropertyList(pp);
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
		// scan graph properties for graph building options
		for (propSpec p:graphProps) {
			switch (GraphProperties.propertyForName(p.name))  {
				case CLASS:
					graphClass = (Class<? extends Graph<? extends Node, ? extends Edge>>) 
						getClass(GraphProperties.CLASS,p.value);
					break;
				case NODE_FACTORY:
					nFactoryClass = (Class<? extends NodeFactory>) 
						getClass(GraphProperties.NODE_FACTORY,p.value);
					break;
				case EDGE_FACTORY:
					eFactoryClass = (Class<? extends EdgeFactory>) 
						getClass(GraphProperties.EDGE_FACTORY,p.value);
					break;
				case PROP_FACTORY:
					plFactoryClass = (Class<? extends PropertyListFactory>) 
						getClass(GraphProperties.PROP_FACTORY,p.value);
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
		if (nFactoryClass==null)
			nFactoryClass = (Class<? extends NodeFactory>) 
				getClass(GraphProperties.NODE_FACTORY);
		if (eFactoryClass==null)
			eFactoryClass = (Class<? extends EdgeFactory>) 
				getClass(GraphProperties.EDGE_FACTORY);
		if (plFactoryClass==null)
			plFactoryClass = (Class<? extends PropertyListFactory>) 
				getClass(GraphProperties.PROP_FACTORY);
		// setup the factories
		try {
			nodeFactory = nFactoryClass.newInstance();
			propertyListFactory = plFactoryClass.newInstance();
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
				n = nodeFactory.makeNode();
			else
				n = nodeFactory.makeNode(makePropertyList(ns.props));
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
					e = edgeFactory.makeEdge(start, end);
				else 
					e = edgeFactory.makeEdge(start,end,makePropertyList(es.props));
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

/**************************************************************************
 *  AOT - Aspect-Oriented Thinking                                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  AOT is a method to generate elaborate software code from a series of  *
 *  independent domains of knowledge. It enables one to manage and        *
 *  maintain software from explicit specifications that can be translated *
 *  into any programming language.          							  *
 **************************************************************************                                       
 *  This file is part of AOT (Aspect-Oriented Thinking).                  *
 *                                                                        *
 *  AOT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  AOT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.io.parsing.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.impl.ALEdge;
import fr.cnrs.iees.graph.impl.TreeGraph;
import fr.cnrs.iees.graph.impl.TreeGraphNode;
import fr.cnrs.iees.io.parsing.impl.GraphTokenizer.graphToken;
import fr.cnrs.iees.io.parsing.impl.TreeTokenizer.treeToken;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * 
 * @author Jacques Gignoux - 22 janv. 2019
 *
 */
// tested OK with version 0.0.5 on 23/1/2019
// tested OK with version 0.2.1 on 27/5/2019
public class TreeGraphParser extends EdgeAndNodeSetParser {

	// setup of default graph properties for this parser
	static {
		defaultGraphProperties.put(CLASS, 			"fr.cnrs.iees.graph.impl.TreeGraph");
		defaultGraphProperties.put(NODE_FACTORY, 	"fr.cnrs.iees.graph.impl.TreeGraphFactory");
		defaultGraphProperties.put(EDGE_FACTORY, 	"fr.cnrs.iees.graph.impl.TreeGraphFactory");
		defaultGraphProperties.put(PROP_FACTORY, 	"fr.cnrs.iees.graph.impl.TreeGraphFactory");
		defaultGraphProperties.put(SCOPE, 			"TGDF");
		graphPropertyTypes.put(CLASS,			TreeGraph.class);
		graphPropertyTypes.put(NODE_FACTORY, 	NodeFactory.class);
		graphPropertyTypes.put(EDGE_FACTORY, 	EdgeFactory.class);
		graphPropertyTypes.put(PROP_FACTORY, 	PropertyListFactory.class);
		graphPropertyTypes.put(SCOPE, 			String.class);
	}

	private Logger log = Logger.getLogger(TreeGraphParser.class.getName());

	// ----------------------------------------------------
	// which type of item is currently being constructed
	private enum itemType {
		GRAPH, NODE, EDGE
	}

	// the tokenizer used to read the file
	private TreeGraphTokenizer tokenizer = null;

	// the list of specifications built from the token list
	private List<propSpec> graphProps = new LinkedList<propSpec>();
	private List<treeNodeSpec> nodeSpecs = new LinkedList<treeNodeSpec>();
	private List<edgeSpec> edgeSpecs = new LinkedList<edgeSpec>();

	// the last processed item
	private itemType lastItem = null;
	private propSpec lastProp = null;
	private treeNodeSpec[] lastNodes = null;
	private edgeSpec lastEdge = null;

	// the result of this parsing
	// remind that an TreeGraph is its own Node, Edge and TreeNode factory
	private TreeGraph<TreeGraphNode,ALEdge> graph = null;

	// lazy init: nothing is done before it's needed
	/**
	 * Default constructor.
	 * 
	 * @param tokenizer
	 */
	public TreeGraphParser(TreeGraphTokenizer tokenizer) {
		super();
		this.tokenizer = tokenizer;
	}

	/*
	 * So how do we do imports? We need the graph, a parent node and we need to
	 * detect the 'import' key word - all this before the graph is complete!
	 */
	@SuppressWarnings("unchecked")
	private void buildGraph() {
		// parse tokens if not yet done
		if (lastItem == null)
			parse();
		processGraphProperties(graphProps,log);
		setupFactories(log);
		graph = (TreeGraph<TreeGraphNode, ALEdge>) setupGraph(log);
		// make tree nodes
		Map<String, TreeGraphNode> nodes = new HashMap<>();
		for (treeNodeSpec ns : nodeSpecs) {
			TreeGraphNode n = null;
			Class<? extends Node> nc = (Class<? extends Node>) nodeFactory.nodeClass(ns.label);
			if (ns.props.isEmpty())
				if (nc == null)
					n = (TreeGraphNode) nodeFactory.makeNode(ns.name);
				else
					n = (TreeGraphNode) nodeFactory.makeNode(nc, ns.name);
			else if (nc == null)
				n = (TreeGraphNode) nodeFactory.makeNode(ns.name, makePropertyList(ns.props, log));
			else
				n = (TreeGraphNode) nodeFactory.makeNode(nc, ns.name, makePropertyList(ns.props, log));
			if (ns.parent != null) {
				// the parent has always been set before
				TreeGraphNode parent = nodes.get(ns.parent.label.trim() + ":" + ns.parent.name.trim());
				n.connectParent(parent);
			}
			// this puts the node in the graph
			String nodeId = (ns.label + ":" + ns.name).replaceAll("\\s", "");
			if (nodes.containsKey(nodeId))
				log.severe(() -> "duplicate node found (" + ") - ignoring the second one");
			else
				nodes.put(nodeId, n);
		}
		// make cross links
		for (edgeSpec es : edgeSpecs) {
			SimplePropertyList pl = null;
			if (!es.props.isEmpty())
				pl = makePropertyList(es.props, log);
			String ref = es.start.replaceAll("\\s", "");
			Node start = nodes.get(ref);
			if (start == null)
				log.severe("start node \"" + ref + "\" not found for edge \"" + es.label + ":" + es.name + "\"");
			ref = es.end.replaceAll("\\s", "");
			Node end = nodes.get(ref);
			if (end == null)
				log.severe("end node \"" + ref + "\" not found for edge \"" + es.label + ":" + es.name + "\"");
			if ((start != null) && (end != null)) {
				Class<? extends Edge> ec = edgeFactory.edgeClass(es.label);
				if (pl == null)
					if (ec == null)
						edgeFactory.makeEdge(start, end, es.name);
					else
						edgeFactory.makeEdge(ec, start, end, es.name);
				else if (ec == null)
					edgeFactory.makeEdge(start, end, es.name, pl);
				else
					edgeFactory.makeEdge(ec, start, end, es.name, pl);
			}
		}
	}

	@Override
	public TreeGraph<? extends TreeGraphNode, ? extends Edge> graph() {
		if (graph == null)
			buildGraph();
		return graph;
	}

	@Override
	protected void parse() {
		if (!tokenizer.tokenized())
			tokenizer.tokenize();
		lastNodes = new treeNodeSpec[tokenizer.treeTokenizer().maxDepth() + 1];
		lastItem = itemType.GRAPH;
		// 1 analyse the tree part
		while (tokenizer.treeTokenizer().hasNext()) {
			treeToken tk = tokenizer.treeTokenizer().getNextToken();
			switch (tk.type) {
			case COMMENT:
				break;
			case LABEL:
				// System.out.println(tk.value);
				int level = tk.level;
				lastNodes[level] = new treeNodeSpec();
				lastNodes[level].label = tk.value;
				if (level > 0)
					lastNodes[level].parent = lastNodes[level - 1];
				lastItem = itemType.NODE;
				break;
			case LEVEL:
				// such tokens should never be created
				break;
			case NAME:
				// System.out.println(tk.value);
				level = tk.level;
				lastNodes[level].name = tk.value;
				nodeSpecs.add(lastNodes[level]);
				break;
			case PROPERTY_NAME:
				// System.out.println(tk.value);
				lastProp = new propSpec();
				lastProp.name = tk.value;
				break;
			case PROPERTY_TYPE:
				lastProp.type = tk.value;
				break;
			case PROPERTY_VALUE:
				lastProp.value = tk.value;
				if (lastItem == itemType.GRAPH)
					graphProps.add(lastProp);
				// i.e if not a graph property
				else if (lastNodes[tk.level - 1] != null)
					lastNodes[tk.level - 1].props.add(lastProp);
				break;
			case NODE_REF:
				throw new OmugiException("Invalid token type for a tree");
			default:
				break;
			}
		}
		// 2 analyse the cross-links
		while (tokenizer.graphTokenizer().hasNext()) {
			graphToken tk = tokenizer.graphTokenizer().getNextToken();
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
					// this is an error - there shouldnt be any node properties left here
					throw new OmugiException("There should not be any node property definition here.");
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
					throw new OmugiException("There should not be any node definition here.");
				case EDGE:
					if (lastEdge.label == null)
						lastEdge.label = tk.value;
					else // this is a node label
						throw new OmugiException("There should not be any node definition here.");
					break;
				}
				break;
			case NAME:
				switch (lastItem) {
				case GRAPH:
					log.severe("missing node label declaration");
					break;
				case NODE:
					throw new OmugiException("There should not be any node definition here.");
				case EDGE:
					lastEdge.name = tk.value;
				}
				break;
			case NODE_REF:
				switch (lastItem) {
				case GRAPH:
				case NODE:
					// System.out.println(tk.value);
					lastEdge = new edgeSpec();
					lastEdge.start = tk.value;
					lastItem = itemType.EDGE;
					break;
				case EDGE:
					if (lastEdge.end == null) {
						// System.out.println(tk.value);
						lastEdge.end = tk.value;
						edgeSpecs.add(lastEdge);
					} else {
						lastEdge = new edgeSpec();
						lastEdge.start = tk.value;
						// System.out.println(tk.value);
					}
					break;
				}
				break;
			case LEVEL:
				throw new OmugiException("Invalid token type for a cross-link only graph");
			default:
				break;
			}
		}
	}

	// for debugging only
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Aot graph specification\n");
		if (!graphProps.isEmpty())
			sb.append("Graph properties:\n");
		for (propSpec p : graphProps)
			sb.append('\t').append(p.toString()).append('\n');
		if (!nodeSpecs.isEmpty())
			sb.append("Nodes:\n");
		for (treeNodeSpec n : nodeSpecs) {
			sb.append(n.toString()).append('\n');
			for (propSpec p : n.props)
				sb.append("\t").append(p.toString()).append('\n');
			if (n.parent == null)
				sb.append("\tROOT NODE\n");
			else
				sb.append("\tparent ").append(n.parent.toString()).append('\n');
		}
		if (!edgeSpecs.isEmpty())
			sb.append("Edges:\n");
		for (edgeSpec e : edgeSpecs) {
			sb.append('\t').append(e.toString()).append('\n');
			for (propSpec p : e.props)
				sb.append("\t\t").append(p.toString()).append('\n');
		}
		return sb.toString();
	}

	@Override
	public void setFactory(Object factory) {
		// TODO Auto-generated method stub
		
	}

}

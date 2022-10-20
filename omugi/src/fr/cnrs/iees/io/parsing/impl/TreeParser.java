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

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

import au.edu.anu.omhtk.util.Resources;
import fr.cnrs.iees.graph.*;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.io.parsing.impl.TreeTokenizer.treeToken;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.omhtk.utils.Logging;

/**
 * <p>
 * A parser for tree data.
 * </p>
 * 
 * <p>
 * This parser is initialised with a {@link TreeTokenizer}, i.e. it gobbles a
 * list of tokens and spits out a {@link Tree} when asked for it. It is lazy,
 * i.e. it will not do anything until asked for a graph, i.e. invoking the
 * {@code TreeParser.graph()} method, and it will parse only once after
 * initialisation. Further calls to {@code .graph()} return the already parsed
 * tree.
 * </p>
 *
 * <p>
 * Parsing is done in a single pass on the token list.
 * </p>
 * 
 * <p>
 * Options to setup the tree may be passed through tree-level properties in the
 * file. These are found in {@link NodeSetParser}. The best way to go is to
 * implement specific {@link NodeFactory} and {@link PropertyListFactory}
 * which will implement which flavour of {@link TreeNode} and property list (cf.
 * {@link fr.cnrs.iees.properties.PropertyListGetters PropertyListGetters} descendants) should be used to construct the
 * tree.
 * </p>
 * 
 * <p>
 * Allowed property types are those listed in {@link ValidPropertyTypes}. They
 * can be specified as fully qualified java class names, or as simple strings as
 * found in {@code ValidPropertyTypes}.
 * </p>
 * 
 * <p>
 * Note that the best use of this class is to hide it inside a
 * {@link fr.cnrs.iees.graph.io.GraphImporter GraphImporter}.
 * </p>
 * 
 * @author Jacques Gignoux - 20 déc. 2018
 *
 */
//Tested OK with version 0.2.1 on 27/5/2019
//Tested OK with version 0.2.16 on 1/7/2020
public class TreeParser extends NodeSetParser {
	
	private static Logger log = Logging.getLogger(TreeParser.class);

	// ----------------------------------------------------
	// which type of item is currently being constructed
	private enum itemType {
		TREE, NODE,
	}

	// the tokenizer used to read the file
	private TreeTokenizer tokenizer;

	// the result of this parsing
	private Tree<? extends TreeNode> tree = null;

	// the list of specifications built from the token list
	private List<propSpec> treeProps = new LinkedList<propSpec>();
	private List<treeNodeSpec> nodeSpecs = new LinkedList<treeNodeSpec>();

	// the last processed item
	private itemType lastItem = null;
	private treeNodeSpec[] lastNodes = null;
	private propSpec lastProp = null;

	/**
	 * Constructor from a TreeTokenizer. Lazy init: nothing is done before it's needed.
	 * 
	 * @param tokenizer
	 */
	public TreeParser(TreeTokenizer tokenizer) {
		super();
		this.tokenizer = tokenizer;
		// setup of default tree properties and property types for this parser
		defaultGraphProperties.put(CLASS, 			"fr.cnrs.iees.graph.impl.SimpleTree");
		defaultGraphProperties.put(NODE_FACTORY, 	"fr.cnrs.iees.graph.impl.SimpleTreeFactory");
//		defaultGraphProperties.put(PROP_FACTORY, 	"fr.cnrs.iees.graph.impl.SimpleTreeFactory");
		defaultGraphProperties.put(SCOPE, 			"DTF");
		graphPropertyTypes.put(CLASS,			Tree.class);
		graphPropertyTypes.put(NODE_FACTORY, 	NodeFactory.class);
//		graphPropertyTypes.put(PROP_FACTORY, 	PropertyListFactory.class);
		graphPropertyTypes.put(SCOPE, 			String.class);
	}

	@Override
	protected void parse() {
		if (!tokenizer.tokenized())
			tokenizer.tokenize();
		lastNodes = new treeNodeSpec[tokenizer.maxDepth() + 1];
		lastItem = itemType.TREE;
		while (tokenizer.hasNext()) {
			treeToken tk = tokenizer.getNextToken();
			switch (tk.type) {
			case COMMENT:
				break;
			case LABEL:
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
				level = tk.level;
				lastNodes[level].name = tk.value;
				nodeSpecs.add(lastNodes[level]);
				break;
			case IMPORT_RESOURCE:
//				lastNodes[tk.level - 1].imports.add(new importGraph(Resources.getPackagedFile(tk.value)));
				// This is highly likely NOT to work because one must know the context: tree or treegraph ?
				// and what about edges in the case of a treegraph ?
				// assuming only a tree part is added here. But it's going to be false in many cases
				lastNodes[tk.level - 1].imports.add(
					new importGraph(
						new TreeParser(
							new TreeTokenizer(Resources.getTextResource(
								Resources.getPackagedFileName(tk.value))))));
				break;
			case IMPORT_FILE:
//				lastNodes[tk.level - 1].imports.add(new importGraph(new File(tk.value)));
				lastNodes[tk.level - 1].imports.add(new importGraph(new File(tk.value)));
				break;
			case PROPERTY_NAME:
				lastProp = new propSpec();
				lastProp.name = tk.value;
				break;
			case PROPERTY_TYPE:
				lastProp.type = tk.value;
				break;
			case PROPERTY_VALUE:
				lastProp.value = tk.value// Tested OK with version 0.2.1 on 27/5/2019
;
				if (lastItem == itemType.TREE)
					treeProps.add(lastProp);
				// i.e if not a graph property
				else if (lastNodes[tk.level - 1] != null)
					lastNodes[tk.level - 1].props.add(lastProp);
				break;
			case NODE_REF:
				throw new IllegalArgumentException("Invalid token type for a tree");
			default:
				break;
			}
		}
	}

	// builds the tree from the parsed data
	@SuppressWarnings("unchecked")
	private void buildTree() {
		// parse tokens if not yet done
		if (lastNodes == null)
			parse();
		// setup factories and tree
		processGraphProperties(treeProps,log);
		setupFactories(log);
		tree = (Tree<? extends TreeNode>) setupGraph(log);
		// make tree nodes
		Map<String, TreeNode> nodes = new HashMap<>();
		for (treeNodeSpec ns : nodeSpecs) {
			TreeNode n = null;
			Class<? extends Node> nc = nodeFactory.nodeClass(ns.label);
			if (ns.props.isEmpty())
				if (nc == null)
					n = (TreeNode) nodeFactory.makeNode(ns.name);
				else
					n = (TreeNode) nodeFactory.makeNode(nc, ns.name);
			else if (nc == null)
				n = (TreeNode) nodeFactory.makeNode(ns.name, 
					makePropertyList(nodeFactory.nodePropertyFactory(),ns.props, log));
			else
				n = (TreeNode) nodeFactory.makeNode(nc, ns.name, 
					makePropertyList(nodeFactory.nodePropertyFactory(),ns.props, log));
			String nodeId = ns.label.trim() + ":" + ns.name.trim();
			if (nodes.containsKey(nodeId))
				log.severe("duplicate node found (" + nodeId + ") - ignoring the second one");
			else
				nodes.put(nodeId, n);
			if (ns.parent != null) {
				String parentId = ns.parent.label.trim() + ":" + ns.parent.name.trim();
				TreeNode parent = nodes.get(parentId); // parent has always been treated before
				n.connectParent(parent);
			}
			/*-
			 * Add in any imported graphs.
			 * The imported graph must use the parent's factory instance
			 */
			for (importGraph ig : ns.imports) {
				TreeNode parent = n;
				Tree<? extends TreeNode> importTree = (Tree<? extends TreeNode>) ig.getGraph(parent.factory());
				for (TreeNode importNode : importTree.nodes()) {
					if (importNode.getParent() == null) {
						importNode.connectParent(parent);
//						importNode.setParent(parent);
//						parent.addChild(importNode);
					}
				}
			}
		}
	}


	@Override
	public Tree<? extends TreeNode> graph() {
		if (tree == null)
			buildTree();
		return tree;
	}

	// for debugging only
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tree specification\n");
		if (!treeProps.isEmpty())
			sb.append("Graph properties:\n");
		for (propSpec p : treeProps)
			sb.append('\t').append(p.toString()).append('\n');
		for (treeNodeSpec n : nodeSpecs) {
			sb.append(n.toString()).append('\n');
			for (propSpec p : n.props)
				sb.append("\t").append(p.toString()).append('\n');
			if (n.parent == null)
				sb.append("\tROOT NODE\n");
			else
				sb.append("\tparent ").append(n.parent.toString()).append('\n');
		}
		return sb.toString();
	}

	@Override
	public void setFactory(Object factory) {
		// Nasty!
		nodeFactory = (NodeFactory) factory;
	}

}

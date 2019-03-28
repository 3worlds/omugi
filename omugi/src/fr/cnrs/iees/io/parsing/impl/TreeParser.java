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
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.util.Resources;
import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.io.parsing.impl.TreeTokenizer.treeToken;
import fr.cnrs.iees.properties.PropertyListFactory;

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
 * file. These are found in {@link TreeProperties}. The best way to go is to
 * implement specific {@link TreeNodeFactory} and {@link PropertyListFactory}
 * which will implement which flavour of {@link TreeNode} and property list (cf.
 * {@link PropertyListGetters} descendants) should be used to construct the
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
 * {@link GraphImporter}.
 * </p>
 * 
 * @author Jacques Gignoux - 20 déc. 2018
 *
 */
public class TreeParser extends MinimalGraphParser {

	private Logger log = Logger.getLogger(TreeParser.class.getName());

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

	// factories for treenodes and properties
	private TreeNodeFactory treeFactory = null;

	public TreeParser(TreeTokenizer tokenizer) {
		super();
		this.tokenizer = tokenizer;
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
				lastNodes[tk.level - 1].imports.add(new importGraph(Resources.getPackagedFile(tk.value)));
				break;
			case IMPORT_FILE:
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
				lastProp.value = tk.value;
				if (lastItem == itemType.TREE)
					treeProps.add(lastProp);
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
	}

	// builds the tree from the parsed data
	@SuppressWarnings("unchecked")
	private void buildTree() {
		if (lastNodes == null)
			parse();
		Class<? extends Tree<? extends TreeNode>> treeClass = null;
		Class<? extends TreeNodeFactory> tFactoryClass = null;
		Class<? extends PropertyListFactory> plFactoryClass = null;
		// scan tree properties for tree building options
		for (propSpec p : treeProps) {
			switch (TreeProperties.propertyForName(p.name)) {
			case CLASS:
				treeClass = (Class<? extends Tree<? extends TreeNode>>) getClass(TreeProperties.CLASS, p.value, log);
				break;
			case MUTABLE:
// TODO: implement MutableTreeImpl				
//				treeClass = (Class<? extends Tree<? extends TreeNode>>) 
//					getClass(TreeProperties.CLASS,MutableTreeImpl.class.getName());
				break;
			case PROP_FACTORY:
				plFactoryClass = (Class<? extends PropertyListFactory>) getClass(TreeProperties.PROP_FACTORY, p.value,
						log);
				break;
			case TREE_FACTORY:
				tFactoryClass = (Class<? extends TreeNodeFactory>) getClass(TreeProperties.TREE_FACTORY, p.value, log);
				break;
			default:
				break;
			}
		}
		// use default settings if graph properties were absent
		if (treeClass == null)
			treeClass = (Class<? extends Tree<? extends TreeNode>>) getClass(TreeProperties.CLASS, log);
		if (tFactoryClass == null)
			tFactoryClass = (Class<? extends TreeNodeFactory>) getClass(TreeProperties.TREE_FACTORY, log);
		if (plFactoryClass == null)
			plFactoryClass = (Class<? extends PropertyListFactory>) getClass(TreeProperties.PROP_FACTORY, log);
		// setup the factories
		try {
			treeFactory = tFactoryClass.newInstance();
			propertyListFactory = plFactoryClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// There should not be any problem here given the previous checks
			// unless the factory class is flawed
			e.printStackTrace();
		}
		// make tree nodes
		Map<String, TreeNode> nodes = new HashMap<>();
		for (treeNodeSpec ns : nodeSpecs) {
			TreeNode n = null;
			Class<? extends TreeNode> nc = treeFactory.treeNodeClass(ns.label);
			if (ns.props.isEmpty())
				if (nc == null)
					n = treeFactory.makeTreeNode(null, ns.name);
				else
					n = treeFactory.makeTreeNode(nc, null, ns.name);
			else if (nc == null)
				n = treeFactory.makeTreeNode(null, ns.name, makePropertyList(ns.props, log));
			else
				n = treeFactory.makeTreeNode(nc, null, ns.name, makePropertyList(ns.props, log));
			String nodeId = ns.label.trim() + ":" + ns.name.trim();
			if (nodes.containsKey(nodeId))
				log.severe("duplicate node found (" + nodeId + ") - ignoring the second one");
			else
				nodes.put(nodeId, n);
			if (ns.parent != null) {
				String parentId = ns.parent.label.trim() + ":" + ns.parent.name.trim();
				TreeNode parent = nodes.get(parentId); // parent has always been treated before
				n.setParent(parent);
				parent.addChild(n);
			}
			/*-
			 * Add in any imported graphs.
			 * Factories should be the same so don't bother
			 * testing. If not, it will be a case of "crash now or crash later"
			 * The imported graph does not necwssarily have a root.
			 */
			for (importGraph ig : ns.imports) {
				TreeNode parent = n;
				Tree<? extends TreeNode> importTree = (Tree<? extends TreeNode>) ig.graph;
				for (TreeNode importNode : importTree.nodes()) {
					if (importNode.getParent() == null) {
						importNode.setParent(parent);
						parent.addChild(importNode);
					}
					if (nodes.containsKey(importNode.id())) {
						log.severe("duplicate node found (" + importNode.id() + ") - ignoring the second one");
					} else
						nodes.put(importNode.id(), importNode);
				}
			}
		}
		// make tree
		try {
			Constructor<?> cons = treeClass.getConstructor(Iterable.class);
			tree = (Tree<? extends TreeNode>) cons.newInstance(nodes.values());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return the tree built from this parser
	 */
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

}

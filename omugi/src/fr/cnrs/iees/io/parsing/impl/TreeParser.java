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
import fr.cnrs.iees.io.parsing.Parser;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.io.parsing.impl.TreeTokenizer.token;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.tree.Tree;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.TreeNodeFactory;
import fr.ens.biologie.generic.Labelled;
import fr.ens.biologie.generic.Named;

/**
 * <p>A parser for tree data</p>
 * 
 * @author Jacques Gignoux - 20 déc. 2018
 *
 */
public class TreeParser extends Parser {

	private Logger log = Logger.getLogger(TreeParser.class.getName());

	//----------------------------------------------------
	// which type of item is currently being constructed
	private enum itemType {
		TREE,
		NODE,
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
	// specifications for a tree node
	private class nodeSpec {
		protected nodeSpec parent = null;
		protected String label;
		protected String name;
		protected List<propSpec> props = new LinkedList<propSpec>();
		@Override // for debugging only
		public String toString() {
			return label+":"+name;
		}
	}

	// the tokenizer used to read the file
	private TreeTokenizer tokenizer;
	
	// the result of this parsing
	private Tree<? extends TreeNode> tree = null;

	// the list of specifications built from the token list
	private List<propSpec> treeProps = new LinkedList<propSpec>();
	private List<nodeSpec> nodeSpecs =  new LinkedList<nodeSpec>();
	
	// the last processed item
	private itemType lastItem = null;
	private nodeSpec[] lastNodes = null;
	private propSpec lastProp = null;

	// factories for treenodes and properties
	private TreeNodeFactory treeFactory = null;
	private PropertyListFactory propFactory = null;

	public TreeParser(TreeTokenizer tokenizer) {
		super();
		this.tokenizer =tokenizer;
	}

	@Override
	protected void parse() {
		if (!tokenizer.tokenized())
			tokenizer.tokenize();
		lastNodes = new nodeSpec[tokenizer.maxDepth()+1];
		lastItem = itemType.TREE;
		while (tokenizer.hasNext()) {
			token tk = tokenizer.getNextToken();
			switch (tk.type) {
				case COMMENT:
					break;
				case LABEL:
					int level = tk.level;
					lastNodes[level] = new nodeSpec();
					lastNodes[level].label = tk.value;
					if (level>0)
						lastNodes[level].parent = lastNodes[level-1];
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
				case PROPERTY_NAME:
					lastProp = new propSpec();
					lastProp.name = tk.value;
					break;
				case PROPERTY_TYPE:
					lastProp.type = tk.value;
					break;
				case PROPERTY_VALUE:
					lastProp.value = tk.value;
					if (lastItem==itemType.TREE)
						treeProps.add(lastProp);
					else
						lastNodes[tk.level-1].props.add(lastProp);
					break;
			}
		}
	}
	
	// gets a class from the tree properties
	private Class<?> getClass(TreeProperties gp, String value) {
		Class<?> result = null;
		if (value!=null)
			try {
				Class<?> c = Class.forName(value);
				if (Tree.class.isAssignableFrom(c))
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
	private Class<?> getClass(TreeProperties gp) {
		return getClass(gp,null);
	}

	// builds a propertyList from specs
	private SimplePropertyList makePropertyList(List<propSpec> props) {
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
		return propFactory.makePropertyList(pp);
	}

	
	// builds the tree from the parsed data
	@SuppressWarnings("unchecked")
	private void buildTree() {
		if (lastNodes==null)
			parse();
		Class<? extends Tree<? extends TreeNode>> treeClass = null;
		Class<? extends TreeNodeFactory> tFactoryClass = null;
		Class<? extends PropertyListFactory> plFactoryClass = null;
		// scan tree properties for tree building options
		for (propSpec p:treeProps) {
			switch (TreeProperties.propertyForName(p.name)) {
			case CLASS:
				treeClass = (Class<? extends Tree<? extends TreeNode>>) 
					getClass(TreeProperties.CLASS,p.value);
				break;
			case MUTABLE:
// TODO: implement MutableTreeImpl				
//				treeClass = (Class<? extends Tree<? extends TreeNode>>) 
//					getClass(TreeProperties.CLASS,MutableTreeImpl.class.getName());
				break;
			case PROP_FACTORY:
				plFactoryClass = (Class<? extends PropertyListFactory>) 
					getClass(TreeProperties.PROP_FACTORY,p.value);
				break;
			case TREE_FACTORY:
				tFactoryClass = (Class<? extends TreeNodeFactory>) 
					getClass(TreeProperties.TREE_FACTORY,p.value);
				break;
			default:
				break;
			}
		}
		// use default settings if graph properties were absent
		if (treeClass==null)
			treeClass = (Class<? extends Tree<? extends TreeNode>>) 
				getClass(TreeProperties.CLASS);
		if (tFactoryClass==null)
			tFactoryClass = (Class<? extends TreeNodeFactory>) 
				getClass(TreeProperties.TREE_FACTORY);
		if (plFactoryClass==null)
			plFactoryClass = (Class<? extends PropertyListFactory>) 
				getClass(TreeProperties.PROP_FACTORY);
		// setup the factories
		try {
			treeFactory = tFactoryClass.newInstance();
			propFactory = plFactoryClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// There should not be any problem here given the previous checks
			// unless the factory class is flawed
			e.printStackTrace();
		}
		// make tree nodes
		Map<String,TreeNode> nodes = new HashMap<>();
		for (nodeSpec ns:nodeSpecs) {
			TreeNode n = null;
			if (ns.props.isEmpty())
				n = treeFactory.makeTreeNode();
			else
				n = treeFactory.makeDataTreeNode(makePropertyList(ns.props));
			if (Labelled.class.isAssignableFrom(n.getClass())) 
				((Labelled)n).setLabel(ns.label);
			if (Named.class.isAssignableFrom(n.getClass())) 
				((Named)n).setName(ns.name);
			String nodeId = ns.label.trim()+":"+ns.name.trim();
			if (nodes.containsKey(nodeId))
				log.severe("duplicate node found ("+") - ignoring the second one");
			else
				nodes.put(nodeId,n);
			if (ns.parent!=null) {
				String parentId = ns.parent.label.trim()+":"+ns.parent.name.trim();
				TreeNode parent = nodes.get(parentId); // parent has always been treated before
				n.setParent(parent);
				parent.addChild(n);
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
		if (tree==null)
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
		for (propSpec p:treeProps)
			sb.append('\t').append(p.toString()).append('\n');
		for (nodeSpec n:nodeSpecs) {
			sb.append(n.toString()).append('\n');
			for (propSpec p:n.props)
				sb.append("\t").append(p.toString()).append('\n');
			if (n.parent==null)
				sb.append("\tROOT NODE\n");
			else
				sb.append("\tparent ").append(n.parent.toString()).append('\n');
		}
		return sb.toString();
	}
	
}

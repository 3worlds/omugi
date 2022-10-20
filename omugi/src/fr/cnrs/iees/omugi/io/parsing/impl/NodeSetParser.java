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
package fr.cnrs.iees.omugi.io.parsing.impl;

import static fr.cnrs.iees.omugi.io.parsing.TextGrammar.*;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import fr.cnrs.iees.omugi.OmugiClassLoader;
import fr.cnrs.iees.omugi.collections.tables.Table;
import fr.cnrs.iees.omugi.graph.Edge;
import fr.cnrs.iees.omugi.graph.Graph;
import fr.cnrs.iees.omugi.graph.Node;
import fr.cnrs.iees.omugi.graph.NodeFactory;
import fr.cnrs.iees.omugi.graph.NodeSet;
import fr.cnrs.iees.omugi.graph.io.impl.OmugiGraphImporter;
import fr.cnrs.iees.omugi.graph.property.Property;
import fr.cnrs.iees.omugi.io.parsing.Parser;
import fr.cnrs.iees.omugi.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.omugi.properties.PropertyListFactory;
import fr.cnrs.iees.omugi.properties.SimplePropertyList;

/**
 * <p>
 * This class groups the common grounds for graph and tree parsers. See
 * {@link GraphParser} and {@link TreeParser} for details.
 * </p>
 * 
 * @author Jacques Gignoux - 21 janv. 2019
 *
 */
public abstract class NodeSetParser extends Parser {
	
	// constants and defaults
	protected final static String CLASS = "type";
	protected final static String NODE_FACTORY = "node_factory";
//	protected final static String PROP_FACTORY = "property_list_factory";
	protected final static String SCOPE = "scope";
	protected final Map<String,String> defaultGraphProperties = new HashMap<>();
	protected final Map<String,Class<?>> graphPropertyTypes = new HashMap<>();
	
	// ----------------------------------------------------
	/**
	 * Specification of a property
	 * 
	 * @author Jacques Gignoux - 21 janv. 2019
	 *
	 */
	public class propSpec {
		public String name;
		public String type;
		public String value;

		@Override // for debugging only
		public String toString() {
			return name + ":" + type + "=" + value;
		}

		public propSpec() {
			super();
		}
	}
	
	// ----------------------------------------------------
	/**
	 * Specification for an import (NB: implementation not finished)
	 * 
	 * @author Jacques Gignoux - 21 janv. 2019
	 *
	 */
	public class importGraph {
		// public MinimalGraph<?> graph;
		private OmugiGraphImporter importer;

		public importGraph(File file) {
			importer = new OmugiGraphImporter(file);
		}
		
		public importGraph(Parser parser) {
			importer = new OmugiGraphImporter(parser);
		}

		public NodeSet<?> getGraph(Object factory) {
			// these files do not have their own scope or class statements but instead use
			// the parent node's factory
			importer.setFactory(factory);
			return importer.getGraph();
		}

		public importGraph(InputStream stream) {
			// TODO may need something different when reading from jars
		}
	}

	// ----------------------------------------------------
	/**
	 * Specification of a node
	 * 
	 * @author Jacques Gignoux - 21 janv. 2019
	 *
	 */
	public class nodeSpec {
		public String label;
		public String name;
		public List<propSpec> props = new LinkedList<propSpec>();
		public List<importGraph> imports = new LinkedList<importGraph>();

		@Override // for debugging only
		public String toString() {
			return label + ":" + name;
		}

		public nodeSpec() {
			super();
		}
	}

	// ----------------------------------------------------
	/**
	 * Specification of a tree node
	 * 
	 * @author Jacques Gignoux - 21 janv. 2019
	 *
	 */
	public class treeNodeSpec extends nodeSpec {
		public treeNodeSpec parent = null;

		public treeNodeSpec() {
			super();
		}
	}
	
	// ----------------------------------------------------
	// local fields
	Class<? extends NodeSet<? extends Node>> graphClass = null;
	Class<? extends NodeFactory> nFactoryClass = null;
	Class<? extends PropertyListFactory> plFactoryClass = null;
	Map<String,String> labels = new HashMap<>();
	String theScope = null;
	
	// ----------------------------------------------------
	
	// fields for descendants
//	protected PropertyListFactory propertyListFactory = null;
	protected NodeFactory nodeFactory = null;

	// builds a propertyList from specs
	protected SimplePropertyList makePropertyList(PropertyListFactory plf, List<propSpec> props, Logger log) {
		List<Property> pl = new LinkedList<Property>();
		for (propSpec p : props) {
			String className = ValidPropertyTypes.getJavaClassName(p.type);
			if (className == null)
				log.severe("unknown property type (" + p.type + ")");
			else {
				Object o = null;
				try {
					Class<?> c = Class.forName(className, false, OmugiClassLoader.getAppClassLoader());
					// if method present, instantiate object with valueOf()
					for (Method m : c.getMethods())
						if (m.getName().equals("valueOf")) {
							Class<?>[] pt = m.getParameterTypes();
							// first case, valueOf() only has a String argument --> primitive types
							if (pt.length == 1) {
								if (String.class.isAssignableFrom(pt[0])) {
									o = m.invoke(null, p.value);
									break;
								}
							}
							// Second case, value of has 3 arguments --> Table type
							else if (pt.length == 3) {
								if ((String.class.isAssignableFrom(pt[0])) && (char[][].class.isAssignableFrom(pt[1]))
										&& (char[].class.isAssignableFrom(pt[2]))) {
									char[][] bdel = new char[2][2];
									bdel[Table.DIMix] = DIM_BLOCK_DELIMITERS;
									bdel[Table.TABLEix] = TABLE_BLOCK_DELIMITERS;
									char[] isep = new char[2];
									isep[Table.DIMix] = DIM_ITEM_SEPARATOR;
									isep[Table.TABLEix] = TABLE_ITEM_SEPARATOR;
									o = m.invoke(null, p.value, bdel, isep);
								}
							}
						}
					// else must be a String
					if (o == null) {
						if (p.value.equals("null")) {
							o = null;
						}else {
							o = p.value;
						}
					}
				} catch (ClassNotFoundException e) {
					// We should reach here only if there is an error in ValidPropertyTypes
					e.printStackTrace();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// this occurs if the value is not of the proper type
					o = null;
				}
				pl.add(new Property(p.name, o));
			}
		}
		Property[] pp = new Property[pl.size()];
		int i = 0;
		for (Property p : pl)
			pp[i++] = p;
		return plf.makePropertyList(pp);
	}

	// gets a class from the graph properties
	protected Class<?> getClass(String gp, String value, Logger log) {
		Class<?> result = null;
		if (value != null)
			try {
				Class<?> superClass =Objects.requireNonNull(graphPropertyTypes.get(gp));
				Class<?> c = Objects.requireNonNull(Class.forName(value, true, OmugiClassLoader.getAppClassLoader()));
				if (superClass.isAssignableFrom(c))
					result = c;
				else
					log.severe("graph property \"" + gp + "\" does not refer to a valid type  ["+value+"]?-  ("
							+ graphPropertyTypes.get(gp) + ") - using default type (" + defaultGraphProperties.get(gp) + ")");
			} catch (ClassNotFoundException e) {
				log.severe("graph property \"" + gp
						+ "\" does not refer to a valid java class ['"+value+"']- using default type (" + defaultGraphProperties.get(gp) + ")");
			}
		if (result == null)
			try {
//				result = Class.forName(gp.defaultValue(), false, OmugiClassLoader.getClassLoader());
				result = Objects.requireNonNull(Class.forName(defaultGraphProperties.get(gp), false, OmugiClassLoader.getAppClassLoader()));
			} catch (ClassNotFoundException e) {
				// this is an error in GraphProperties.[...].defaultValue - fix code with a
				// correct class name
				e.printStackTrace();
			}
		// this will always return a valid, non null class - if problems, it will throw
		// an exception
		return result;
	}

	// gets a default class from the graph properties
	protected Class<?> getClass(String gp, Logger log) {
		return getClass(gp, null, log);
	}

	// setup the NodeFactory and PropertyList factory
	// NB: MUST be called AFTER processGraphProperties(...)
	@SuppressWarnings("unchecked")
	protected void setupFactories(Logger log) {
		if (nodeFactory==null) // else keep it !
			if (nFactoryClass==null)
				nFactoryClass =Objects.requireNonNull((Class<? extends NodeFactory>) 
					getClass(NODE_FACTORY,log));
//		if (propertyListFactory==null)
//			if (plFactoryClass==null)
//				plFactoryClass = (Class<? extends PropertyListFactory>) 
//					getClass(PROP_FACTORY,log);
		// setup the factories
		try {
			if (nodeFactory==null)
				if (labels.isEmpty()) {
					nodeFactory =Objects.requireNonNull( nFactoryClass.getDeclaredConstructor().newInstance());
				}
				else {
					Constructor<? extends NodeFactory> c = 
						nFactoryClass.getDeclaredConstructor(String.class,Map.class);
					nodeFactory = Objects.requireNonNull(c.newInstance(theScope,labels));
				}
//			if (propertyListFactory==null)
//				propertyListFactory = plFactoryClass.getDeclaredConstructor().newInstance();
//			if (plFactoryClass.equals(nFactoryClass))
//				if (nodeFactory instanceof PropertyListFactory)
//					propertyListFactory = (PropertyListFactory) nodeFactory;
		} catch (Exception e) {
			// There should not be any problem here given the previous checks
			// unless the factory class is flawed
			e.printStackTrace();
		}
		nodeFactory = Objects.requireNonNull(nodeFactory);
	}
	
	// setup the graph
	// NB: must be called AFTER setupFactories;
	@SuppressWarnings("unchecked")
	protected NodeSet<? extends Node> setupGraph(Logger log) {
		NodeSet<? extends Node> result = null;
		if (graphClass == null)
			graphClass = (Class<? extends NodeSet<? extends Node>>) getClass(CLASS, log);
		try {
			Constructor<?> cons = graphClass.getDeclaredConstructor(NodeFactory.class);
			result = (NodeSet<? extends Node>) cons.newInstance(nodeFactory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected void processGraphProperties(List<propSpec> graphProps, Logger log) {
		for (propSpec p:graphProps) {
			switch (p.name)  {
			case CLASS:
				graphClass =  Objects.requireNonNull((Class<? extends Graph<? extends Node, ? extends Edge>>) 
					getClass(CLASS,p.value,log));
				break;
			case NODE_FACTORY:
				nFactoryClass = Objects.requireNonNull((Class<? extends NodeFactory>) 
					getClass(NODE_FACTORY,p.value,log));
				break;
//			case PROP_FACTORY:
//				plFactoryClass = (Class<? extends PropertyListFactory>) 
//					getClass(PROP_FACTORY,p.value,log);
//				break;
			case SCOPE:
				theScope = p.value;
				break;
			// all other properties are considered to be (label,class name) pairs
			default: 
				if (p.type.contains("String"))
					labels.put(p.name,p.value);
				break;
			}
		}
	}
	
}

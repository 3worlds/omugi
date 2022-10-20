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

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.omugi.graph.EdgeFactory;
import fr.cnrs.iees.omugi.graph.GraphFactory;
import fr.cnrs.iees.omugi.graph.Node;
import fr.cnrs.iees.omugi.graph.NodeSet;

/**
 * An ancestor for {@link fr.cnrs.iees.omugi.graph.Graph Graph} and {@link fr.cnrs.iees.omugi.graph.impl.TreeGraph TreeGraph} parsers.
 * 
 * @author Jacques Gignoux - 27 mai 2019
 *
 */
public abstract class EdgeAndNodeSetParser extends NodeSetParser {

	// ----------------------------------------------------
	/**
	 * Specification of an edge
	 * 
	 * @author Jacques Gignoux - 27 mai 2019
	 *
	 */
	public class edgeSpec {
		/**
		 * The edge's label
		 */
		public String label;
		/**
		 * The edge's name
		 */
		public String name;
		/**
		 * The edge's start node (a label:name pair).
		 */
		public String start;
		/**
		 * The edge's end node (a label:name pair).
		 */
		public String end;
		/**
		 * List of edge property specifications {@link propSpec}.
		 */
		public List<propSpec> props = new LinkedList<propSpec>();

		@Override // for debugging only
		public String toString() {
			return label + ":" + name + " [" + start + "-->" + end + "]";
		}

		/**
		 * Default constructor.
		 */
		public edgeSpec() {
			super();
		}
	}
	// ----------------------------------------------------
	protected final static String EDGE_FACTORY = "edge_factory";
	protected EdgeFactory edgeFactory = null;
	private Class<? extends EdgeFactory> eFactoryClass;
	
	// setup all factories
	@Override
	@SuppressWarnings("unchecked")
	protected void setupFactories(Logger log) {
		super.setupFactories(log);
		if (edgeFactory==null)
			if (eFactoryClass==null)
				eFactoryClass = (Class<? extends EdgeFactory>) 
					getClass(EDGE_FACTORY,log);
		try {
			if (edgeFactory==null)
				if (labels.isEmpty()) {
					edgeFactory = eFactoryClass.getDeclaredConstructor().newInstance();
				}
				else {
					Constructor<? extends EdgeFactory> c2 = 
						eFactoryClass.getDeclaredConstructor(String.class,Map.class);
					edgeFactory = c2.newInstance(theScope,labels);
				}
			if (eFactoryClass.equals(nFactoryClass))
				if (nodeFactory instanceof GraphFactory)
					edgeFactory = (EdgeFactory) nodeFactory;
		} catch (Exception e) {
			// There should not be any problem here given the previous checks
			// unless the factory class is flawed
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void processGraphProperties(List<propSpec> graphProps, Logger log) {
		// scan for nodeset properties
		super.processGraphProperties(graphProps, log);
		// re-scan for edgeset properties
		for (propSpec p:graphProps) {
			switch (p.name)  {
			case EDGE_FACTORY:
				eFactoryClass = (Class<? extends EdgeFactory>) 
					getClass(EDGE_FACTORY,p.value,log);
				if (labels.containsKey(EDGE_FACTORY))
					labels.remove(EDGE_FACTORY);
				break;
			default: 
				break;
			}
		}
	}

	// setup the graph
	// NB: must be called AFTER setupFactories;
	// NB differs from ancestor by the constructor argument - assumes a GraphFactory
	@SuppressWarnings("unchecked")
	protected NodeSet<? extends Node> setupGraph(Logger log) {
		NodeSet<? extends Node> result = null;
		if (graphClass == null)
			graphClass = (Class<? extends NodeSet<? extends Node>>) getClass(CLASS, log);
		try {
			Constructor<?> cons = graphClass.getDeclaredConstructor(GraphFactory.class);
			result = (NodeSet<? extends Node>) cons.newInstance(nodeFactory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
}

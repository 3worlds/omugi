/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
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
package fr.cnrs.iees.graph.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.OmugiClassLoader;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.identity.impl.LocalScope;
import fr.cnrs.iees.properties.PropertyListFactory;

/**
 * A simple factory for graph elements - Can instantiate any descendant
 * of Node and Edge. Handles node and edge labels (must be passed in the constructor)
 * 
 * @author Jacques Gignoux - 7 nov. 2018
 *
 */
public class GraphFactory 
	implements PropertyListFactory, 
		DefaultEdgeFactory, 
		DefaultNodeFactory {
	
	private Map<String,Class<? extends Edge>> edgeLabels = new HashMap<>();
	private Map<Class<? extends Edge>,String> edgeClassNames = new HashMap<>();
	private Map<String,Class<? extends Node>> nodeLabels = new HashMap<>();
	private Map<Class<? extends Node>,String> nodeClassNames = new HashMap<>();
	private IdentityScope scope= new LocalScope("DGF");
	
	// constructors
	
	public GraphFactory() {
		super();
		scope= new LocalScope("DGF");
	}
	
	@SuppressWarnings("unchecked")
	public GraphFactory(String scopeId, Map<String,String> labels) {
		super();
		if (scopeId!=null)
			scope = new LocalScope(scopeId);
		else
			scope = new LocalScope("DGF");
		Logger log = Logger.getLogger(GraphFactory.class.getName());
		if (labels!=null)
			for (String label:labels.keySet()) {
				try {
					Class<?> c = Class.forName(labels.get(label),true,OmugiClassLoader.getClassLoader());
					if (Node.class.isAssignableFrom(c)) {
						nodeLabels.put(label,(Class<? extends Node>) c);
						nodeClassNames.put((Class<? extends Node>) c,label);
					}
					else if (Edge.class.isAssignableFrom(c)) {
						edgeLabels.put(label,(Class<? extends Edge>) c);
						edgeClassNames.put((Class<? extends Edge>) c, label);
					}
				} catch (ClassNotFoundException e) {
					log.severe(()->"Class \""+labels.get(label)+"\" for label \""+label+"\" not found");
				}
		}
	}

	// NodeFactory
	
	@Override
	public String nodeClassName(Class<? extends Node> nodeClass) {
		return nodeClassNames.get(nodeClass);
	}

	@Override
	public Class<? extends Node> nodeClass(String label) {
		return nodeLabels.get(label);
	}
		
	// EdgeFactory
	@Override
	public String edgeClassName(Class<? extends Edge> edgeClass) {
		return edgeClassNames.get(edgeClass);
	}

	public Class<? extends Edge> edgeClass(String label) {
		return edgeLabels.get(label);
	}
	
	// Scoped

	@Override
	public IdentityScope scope() {
		return scope;
	}
}

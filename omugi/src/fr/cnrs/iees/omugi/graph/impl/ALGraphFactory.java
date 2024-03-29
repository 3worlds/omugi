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
package fr.cnrs.iees.omugi.graph.impl;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.Logger;

import fr.cnrs.iees.omhtk.utils.Logging;
import fr.cnrs.iees.omugi.graph.*;
import fr.cnrs.iees.omugi.identity.*;
import fr.cnrs.iees.omugi.properties.*;

/**
 * <p>The factory for {@link ALGraph}s (node and edge factory).</p>
 * 
 * @author Jacques Gignoux - 13 mai 2019
 *
 */
public class ALGraphFactory extends GraphFactoryAdapter {

	private static Logger log = Logging.getLogger(ALGraphFactory.class);
	
	/** the list of graphs managed by this factory */
	protected Set<ALGraph<ALNode,ALEdge>> graphs = new HashSet<>();

	/**
	 * Basic constructor, only requires a scope
	 * 
	 * @param scopeName the scope identifier, e.g. "MyGraphFactory"
	 */
	public ALGraphFactory(String scopeName) {
		super(scopeName);
	}

	/**
	 * Basic constructor, only requires a scope
	 * 
	 * @param scope the scope instance
	 */
	public ALGraphFactory(IdentityScope scope) {
		super(scope);
	}
	
	/**
	 * Basic constructor with default scope "DGF" (for "default graph factory").
	 */
	public ALGraphFactory() {
		super();
	}

	/**
	 * Constructor with labels for sub-classes of {@link ALNode} and {@link ALEdge}
	 * 
	 * @param scopeName the scope identifier, e.g. "MyGraphFactory"
	 * @param labels a map of (labels,class names) associating a label to the valid java class name of a 
	 * descendant of {@code ALNode} or {@code ALEdge}
	 */
	public ALGraphFactory(String scopeName,Map<String,String> labels) {
		super(scopeName,labels);
	}

	/**
	 * Constructor with labels for sub-classes of {@link ALNode} and {@link ALEdge}
	 * 
	 * @param scope the scope instance
	 * @param labels a map of (labels,class names) associating a label to the valid java class name of a 
	 * descendant of {@code ALNode} or {@code ALEdge}
	 */
	public ALGraphFactory(IdentityScope scope,Map<String,String> labels) {
		super(scope,labels);
	}

	// NodeFactory

	@SuppressWarnings("unchecked")
	@Override
	public void manageGraph(NodeSet<? extends Node> graph) {
		if (graph instanceof ALGraph)
			graphs.add((ALGraph<ALNode, ALEdge>) graph);
	}
	
	@Override
	public void unmanageGraph(NodeSet<? extends Node> graph) {
		graphs.remove(graph);
	}

	private void addNodeToGraphs(ALNode node) {
		for (ALGraph<ALNode,ALEdge> g:graphs)
			g.addNode(node);
	}

	@Override
	public ALNode makeNode(String proposedId) {
		ALNode result = new ALNode(scope.newId(true,proposedId),this);
		addNodeToGraphs(result);
		return result;
	}

	@Override
	public ALNode makeNode(String proposedId, ReadOnlyPropertyList props) {
		ALNode result = null;
		if (props instanceof SimplePropertyList)
			result = new ALDataNode(scope.newId(true,proposedId),(SimplePropertyList) props,this);
		else
			result = new ALReadOnlyDataNode(scope.newId(true,proposedId),props,this);
		addNodeToGraphs(result);
		return result;
	}

	@Override
	public ALNode makeNode(Class<? extends Node> nodeClass, String proposedId, ReadOnlyPropertyList props) {
		ALNode result = null;
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,
				ReadOnlyPropertyList.class,
				GraphFactory.class);
		} catch (Exception e) {
			try {
				c = nodeClass.getDeclaredConstructor(Identity.class,
					SimplePropertyList.class,
					GraphFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope.newId(true,proposedId);
		try {
			result = (ALNode) c.newInstance(id,props,this);
			addNodeToGraphs(result);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return result;
	}

	@Override
	public ALNode makeNode(Class<? extends Node> nodeClass, String proposedId) {
		ALNode result = null;
		Constructor<? extends Node> c = null;
		try {
			c = nodeClass.getDeclaredConstructor(Identity.class,GraphFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+nodeClass.getName()+ "\" not found");
		}
		Identity id = scope.newId(true,proposedId);
		try {
			result = (ALNode) c.newInstance(id,this);
			addNodeToGraphs(result);
		} catch (Exception e) {
			log.severe(()->"Node of class \""+nodeClass.getName()+ "\" could not be instantiated");
		}
		return result;
	}


}

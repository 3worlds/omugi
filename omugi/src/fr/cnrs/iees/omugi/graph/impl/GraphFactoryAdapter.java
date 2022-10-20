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
import fr.cnrs.iees.omugi.OmugiClassLoader;
import fr.cnrs.iees.omugi.graph.*;
import fr.cnrs.iees.omugi.identity.*;
import fr.cnrs.iees.omugi.properties.*;

/**
 * A default abstract implementation of {@link GraphFactory}. Use it for further specialisation.
 * 
 * @author Jacques Gignoux - 16 mai 2019
 *
 */
public abstract class GraphFactoryAdapter 
		extends NodeFactoryAdapter 
		implements GraphFactory {

	private static Logger log = Logging.getLogger(GraphFactoryAdapter.class);
	protected Map<String,Class<? extends Edge>> edgeLabels = new HashMap<>();
	protected Map<Class<? extends Edge>,String> edgeClassNames = new HashMap<>();

	protected GraphFactoryAdapter(IdentityScope scope) {
		super(scope);
	}

	protected GraphFactoryAdapter(String scopeId) {
		super(scopeId);
	}

	protected GraphFactoryAdapter() {
		super("DGF"); // for "default graph factory"
	}

	@SuppressWarnings("unchecked")
	private void setupLabels(Map<String,String> labels) {
		if (labels!=null) {
			ClassLoader classLoader = OmugiClassLoader.getAppClassLoader();
			for (String label:labels.keySet()) {
				try {
					Class<?> c = Class.forName(labels.get(label),true,classLoader);
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
	}
	
	protected GraphFactoryAdapter(String scopeId, Map<String,String> labels) {
		super(scopeId);
		setupLabels(labels);
	}

	protected GraphFactoryAdapter(IdentityScope scope, Map<String,String> labels) {
		super(scope);
		setupLabels(labels);
	}

	// EdgeFactory
	
	@Override
	public Edge makeEdge(Node start, Node end, String proposedId) {
		return new ALEdge(scope.newId(true,proposedId),start,end,this);
	}

	@Override
	public Edge makeEdge(Node start, Node end, String proposedId, ReadOnlyPropertyList props) {
		if (props instanceof SimplePropertyList)
			return new ALDataEdge(scope.newId(true,proposedId),start,end,(SimplePropertyList)props,this);
		else
			return new ALReadOnlyDataEdge(scope.newId(true,proposedId),start,end,props,this);
	}

	@Override
	public Edge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, String proposedId,
			ReadOnlyPropertyList props) {
		Constructor<? extends Edge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				ReadOnlyPropertyList.class,EdgeFactory.class);
		} catch (Exception e) {
			try {
				c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
					SimplePropertyList.class,EdgeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope.newId(true,proposedId);
		try {
			return c.newInstance(id,start,end,props,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Edge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, String proposedId) {
		Constructor<? extends Edge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				EdgeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
		}
		Identity id = scope.newId(true,proposedId);
		try {
			return c.newInstance(id,start,end,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}
	
	@Override
	public final String edgeClassName(Class<? extends Edge> edgeClass) {
		return edgeClassNames.get(edgeClass);
	}

	@Override
	public final Class<? extends Edge> edgeClass(String label) {
		return edgeLabels.get(label);
	}
}

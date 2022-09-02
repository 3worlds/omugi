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
package fr.cnrs.iees.graph.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.OmugiClassLoader;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.identity.impl.LocalScope;
import fr.ens.biologie.generic.utils.Logging;

/**
 * A default abstract implementation of {@link NodeFactory}. Use it for further specialisation,
 * for example for {@link fr.cnrs.iees.graph.Tree Tree} factories.
 * 
 * @author Jacques Gignoux - 16 mai 2019
 *
 */
public abstract class NodeFactoryAdapter implements NodeFactory {

	private static Logger log = Logging.getLogger(NodeFactoryAdapter.class);
	protected Map<String, Class<? extends Node>> nodeLabels = new HashMap<>();
	protected Map<Class<? extends Node>, String> nodeClassNames = new HashMap<>();
	protected IdentityScope scope;
	
	protected NodeFactoryAdapter(IdentityScope scope) {
		super();
		if (scope!=null)
			this.scope = scope;
		else
			throw new NullPointerException("A factory requires a valid scope");
	}

	protected NodeFactoryAdapter(String scopeId) {
		super();
		if (scopeId != null)
			scope = new LocalScope(scopeId);
		else
			throw new NullPointerException("A factory requires a valid scope name");
	}

	@SuppressWarnings("unchecked")
	private void setupLabels(Map<String, String> labels) {
		ClassLoader classLoader = OmugiClassLoader.getAppClassLoader();
		if (labels != null) {
			for (String label : labels.keySet()) {
				try {
					Class<?> c = Class.forName(labels.get(label), true, classLoader);
					if (Node.class.isAssignableFrom(c)) {
						nodeLabels.put(label, (Class<? extends Node>) c);
						nodeClassNames.put((Class<? extends Node>) c, label);
					}
				} catch (ClassNotFoundException e) {
					log.severe(() -> "Class \"" + labels.get(label) + "\" for label \"" + label + "\" not found");
				}
			}
		}
	}
	
	protected NodeFactoryAdapter(String scopeId, Map<String, String> labels) {
		this(scopeId);
		setupLabels(labels);
	}
	
	protected NodeFactoryAdapter(IdentityScope scope, Map<String, String> labels) {
		this(scope);
		setupLabels(labels);
	}


	@Override
	public final String nodeClassName(Class<? extends Node> nodeClass) {
		return nodeClassNames.get(nodeClass);
	}

	@Override
	public final Class<? extends Node> nodeClass(String label) {
		return nodeLabels.get(label);
	}

}

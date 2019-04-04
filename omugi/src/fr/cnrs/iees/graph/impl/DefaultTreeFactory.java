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
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.identity.impl.LocalScope;
import fr.cnrs.iees.properties.PropertyListFactory;

/**
 * A simple factory for tree elements - mainly for testing purposes. Can instantiate any descendant
 * of TreeNode. Handles treenode labels (must be passed in the constructor)
 * 
 * @author Jacques Gignoux - 7 nov. 2018
 *
 */
public class DefaultTreeFactory implements PropertyListFactory, DefaultTreeNodeFactory {
	
	private Map<String,Class<? extends TreeNode>> treeNodeLabels = new HashMap<>();
	private Map<Class<? extends TreeNode>,String> treeNodeClassNames = new HashMap<>();
	private IdentityScope scope= new LocalScope("DTF");
	
	// constructors
	
	public DefaultTreeFactory() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public DefaultTreeFactory(Map<String,String> labels) {
		super();
		Logger log = Logger.getLogger(DefaultTreeFactory.class.getName());
		for (String label:labels.keySet()) {
			try {
				Class<?> c = Class.forName(labels.get(label),false,OmugiClassLoader.getClassLoader());
				if (TreeNode.class.isAssignableFrom(c)) {
					treeNodeLabels.put(label,(Class<? extends TreeNode>) c);
					treeNodeClassNames.put((Class<? extends TreeNode>) c,label);
				}
			} catch (ClassNotFoundException e) {
				log.severe(()->"Class \""+labels.get(label)+"\" for label \""+label+"\" not found");
			}
		}
	}

	// TreeNodeFactory

	public String treeNodeClassName(Class<? extends TreeNode> nodeClass) {
		return treeNodeClassNames.get(nodeClass);
	}
	
	public Class<? extends TreeNode> treeNodeClass(String label) {
		return treeNodeLabels.get(label);
	}
	
	// Scoped

	@Override
	public IdentityScope scope() {
		return scope;
	}
}

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
package fr.cnrs.iees.playground.graphs;

import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.identity.impl.LocalScope;
import fr.cnrs.iees.playground.elements.IEdge;
import fr.cnrs.iees.playground.elements.INode;
import fr.cnrs.iees.playground.elements.ITreeNode;
import fr.cnrs.iees.playground.factories.IEdgeFactory;
import fr.cnrs.iees.playground.factories.ITreeNodeFactory;
import fr.cnrs.iees.properties.ExtendablePropertyList;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.ExtendablePropertyListImpl;

public class AotGraph2 extends TreeGraph2<AotNode2, AotEdge2> implements ITreeNodeFactory, IEdgeFactory// ,
																										// ConfiguarableGraph,Textable
{
	private IdentityScope nodeScope;
	private IdentityScope edgeScope;//????????????????

	// Factories need a getScopeFunction
	public AotGraph2() {
		nodeScope = new LocalScope();
		edgeScope = new LocalScope();
	}

	// We don't need this except for Importers - probably the whole this will come unstuck there!!
	@Override
	public ReadOnlyPropertyList makeNodePropertyList() {
		return new ExtendablePropertyListImpl();
	}

	@Override
	public SimplePropertyList makeNodePropertyList(String... propertyKeys) {
		return null;
	}

	@Override
	public IEdge makeEdge(INode start, INode end, String proposedId) {
		Identity id = edgeScope.newId(proposedId);
		return new AotEdge2(id,start, end, (ExtendablePropertyList) makeEdgePropertyList(), this);
	}

	@Override
	public ReadOnlyPropertyList makeEdgePropertyList() {
		return new ExtendablePropertyListImpl();
	}

	@Override
	public SimplePropertyList makeEdgePropertyList(String... propertyKeys) {
		return null;
	}

	@Override
	public ITreeNode makeTreeNode(String proposedId) {
		Identity id = nodeScope.newId(proposedId);
		return new AotNode2(id, (ExtendablePropertyList) makeNodePropertyList(), this);
	}

}

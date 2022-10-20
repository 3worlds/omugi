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
package fr.cnrs.iees.omugi.graph;

/**
 * <p>A factory to create {@link Edge}s and {@link Node}s in an appropriate way to 
 * populate a {@link Graph}.</p>
 * 
 * <p>
 * Although nodes and edges could in theory exist without the context of a
 * graph, as soon as one starts to instantiate them a graph starts to exist. If
 * we want to put some constraints on this graph (e.g. directed/undirected
 * graph, acyclic graph, tree, multigraph, etc.) then we must be able to
 * constrain node and edge creation, and a generic public constructor for edges
 * and nodes does not allow this. Even worse, it could break the graph rules
 * unintentionnally. To secure this, nodes and edges must exist only within the
 * context of a graph.
 * </p>
 * 
 * <p>
 * But sometimes it makes sense that a node belongs to more than one graph. In
 * order to allow for this possibility, we separate the node creation ability
 * from the node addition into the graph. This way, a node made by one graph
 * could be added to another. Each Node or Edge will record which factory
 * created it, but not which graphs it belongs to. This way, other instances of
 * the same type can be made by calling the initial factory.
 * </p>
 *
 * <p>All graph elements (nodes and edges) are uniquely identified by a class ID / instance ID
 * pair (cf. {@link fr.cnrs.iees.omugi.identity.Identity Identity}). This pair is used for deciding if nodes / edges are 
 * equal (Object.equals(...) method). The unicity is guaranteed within a context, called 
 * a <em>scope</em>, that can be universal (=unicity within the universe) or local.
 * A {@code GraphFactory} implementation constructor requires a local scope to guarantee the unicity of all the
 * node / edge instances it constructs.</p>
 *  
 * @author Jacques Gignoux - 15 mai 2019
 *
 */
public interface GraphFactory extends NodeFactory, EdgeFactory {

}

package fr.cnrs.iees.graph.io;

import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Node;

/**
 * 
 * @author Shayne - long ago. 
 * Heavily refactored by J.Gignoux nov. 2017
 *
 */
public interface GraphImporter<N extends Node, E extends Edge> {

	/**
	 * A GraphImporter returns a Graph (JGraphT type) read from any kind of source (stream, file, String...)
	 * @return a Graph build from the previous list of Nodes
	 */
    public Graph<N,E> getGraph();
    
}

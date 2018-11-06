package fr.cnrs.iees.graph.io;

import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Node;

/**
 * 
 * @author Jacques Gignoux - 01-08-2018 
 *
 */
public interface GraphExporter<N extends Node, E extends Edge>
	extends TextGrammar {
	
	public void exportGraph(Graph<N,E> graph);

}

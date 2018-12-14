package fr.cnrs.iees.io;

import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Node;

/**
 * 
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
public abstract class Parser {
	
	protected abstract void parse();
	
	public abstract Graph<? extends Node,? extends Edge> graph();

}

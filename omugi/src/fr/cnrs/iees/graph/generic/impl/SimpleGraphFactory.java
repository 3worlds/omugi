package fr.cnrs.iees.graph.generic.impl;

import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.properties.SimplePropertyList;

/**
 * A simple factory for graph elements - mainly for testing purposes.
 * 
 * @author Jacques Gignoux - 7 nov. 2018
 *
 */
public class SimpleGraphFactory implements GraphElementFactory<SimpleNodeImpl, SimpleEdgeImpl> {

	@Override
	public SimpleNodeImpl makeNode() {
		return new SimpleNodeImpl(this);
	}

	@Override
	public SimpleNodeImpl makeNode(int capacity) {
		return new SimpleNodeImpl(capacity,this);
	}

	@Override
	public SimpleNodeImpl makeNode(SimplePropertyList props) {
		return new SimpleNodeImpl(this);
	}

	@Override
	public SimpleNodeImpl makeNode(int capacity, SimplePropertyList props) {
		return new SimpleNodeImpl(capacity,this);
	}

	@Override
	public SimpleEdgeImpl makeEdge(Node start, Node end) {
		return new SimpleEdgeImpl(start,end,this);
	}

	@Override
	public SimpleEdgeImpl makeEdge(Node start, Node end, SimplePropertyList props) {
		return new SimpleEdgeImpl(start,end,this);
	}

}

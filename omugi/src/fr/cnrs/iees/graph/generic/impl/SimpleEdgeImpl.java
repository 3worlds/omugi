package fr.cnrs.iees.graph.generic.impl;

import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.EdgeAdapter;
import fr.cnrs.iees.graph.generic.Node;

/**
 * <p>The simplest possible Edge implementation. This class only has:</p>
 * <ul>
 * <li>a unique id</li>
 * <li>a start Node</li> 
 * <li>an end Node</li>
 * </ul>
 * <p> Constructor is protected
 * and accessible only through EdgeFactory.</p>
  * @author gignoux - 30 août 2017
 *
 */
public class SimpleEdgeImpl extends EdgeAdapter {

	public SimpleEdgeImpl(Node start, Node end) {
		super(start,end);
	}

	@Override
	public Edge clone() {
		return new SimpleEdgeImpl(startNode(),endNode());
	}
	
	@Override 
	public Edge newInstance() {
		return new SimpleEdgeImpl(startNode(),endNode());
	}
		
}

package fr.cnrs.iees.graph.generic.impl;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.generic.DataEdge;
import fr.cnrs.iees.graph.generic.DataNode;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.generic.ReadOnlyDataEdge;
import fr.cnrs.iees.graph.generic.ReadOnlyDataNode;
import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.graph.properties.SimplePropertyList;

/**
 * A simple factory for graph elements - mainly for testing purposes. Nodes and Edges
 * have no properties.
 * Ideally this should be grouped with the Graph interface in a graph implementation (to
 * make node and edge creation consistent with graph constraints).
 * 
 * @author Jacques Gignoux - 7 nov. 2018
 *
 */
public class DefaultGraphFactory implements GraphElementFactory {
	
	private int capacity = 2;
	
	public DefaultGraphFactory() {
		super();
	}

	/**
	 * 
	 * @param cap the initial storage capacity for edge lists (inner and outer edges)
	 */
	public DefaultGraphFactory(int cap) {
		super();
		capacity = cap;
	}

	@Override
	public Node makeNode() {
		return new SimpleNodeImpl(capacity,this);
	}

	@Override
	public Edge makeEdge(Node start, Node end) {
		return new SimpleEdgeImpl(start,end,this);
	}

	@Override
	public DataNode makeNode(SimplePropertyList props) {
		if (props==null)
			throw new OmugiException("makeNode(SimplePropertyList): property list cannot be null. Use makeNode() if you want no properties.");
		return new DataNodeImpl(capacity,props,this);
	}

	@Override
	public DataEdge makeEdge(Node start, Node end, SimplePropertyList props) {
		if (props==null)
			throw new OmugiException("makeEdge(Node,Node,SimplePropertyList): property list cannot be null. Use makeEdge(Node,Node) if you want no properties.");
		return new DataEdgeImpl(start,end,props,this);
	}

	@Override
	public ReadOnlyDataNode makeNode(ReadOnlyPropertyList props) {
		if (props==null)
			throw new OmugiException("makeNode(ReadOnlyPropertyList): property list cannot be null. Use makeNode() if you want no properties.");
		return new ReadOnlyDataNodeImpl(capacity,props,this);
	}

	@Override
	public ReadOnlyDataEdge makeEdge(Node start, Node end, ReadOnlyPropertyList props) {
		if (props==null)
			throw new OmugiException("makeEdge(Node,Node,ReadOnlyPropertyList): property list cannot be null. Use makeEdge(Node,Node) if you want no properties.");
		return new ReadOnlyDataEdgeImpl(start,end,props,this);
	}


}

package fr.cnrs.iees.graph.generic.impl;

import java.util.Set;

import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.generic.ReadOnlyDataEdge;
import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;

/**
 * Straightforward implementation of {@linkplain ReadOnlyDataEdge} 
 * @author Jacques Gignoux - 29 nov. 2018
 *
 */
public class ReadOnlyDataEdgeImpl extends SimpleEdgeImpl implements ReadOnlyDataEdge {

	private ReadOnlyPropertyList propertyList = null;

	// SimpleEdgeImpl

	protected ReadOnlyDataEdgeImpl(Node start, Node end, ReadOnlyPropertyList props, GraphElementFactory factory) {
		super(start, end, factory);
		propertyList = props;
	}
	
	// SimplePropertyList

	@Override
	public ReadOnlyDataEdgeImpl clone() {
		return new ReadOnlyDataEdgeImpl(startNode(),endNode(),propertyList.clone(),factory());
	}

	@Override
	public Object getPropertyValue(String key) {
		return propertyList.getPropertyValue(key);
	}

	@Override
	public boolean hasProperty(String key) {
		return propertyList.hasProperty(key);
	}

	@Override
	public Set<String> getKeysAsSet() {
		return propertyList.getKeysAsSet();
	}

	@Override
	public int size() {
		return propertyList.size();
	}

}

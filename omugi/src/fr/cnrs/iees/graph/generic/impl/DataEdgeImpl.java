package fr.cnrs.iees.graph.generic.impl;

import java.util.Set;

import fr.cnrs.iees.graph.generic.DataEdge;
import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.properties.PropertyListSetters;
import fr.cnrs.iees.graph.properties.SimplePropertyList;
import fr.ens.biologie.generic.DataContainer;

public class DataEdgeImpl extends SimpleEdgeImpl implements DataEdge {

	private SimplePropertyList propertyList = null;

	// SimpleEdgeImpl

	protected DataEdgeImpl(Node start, Node end, SimplePropertyList props, GraphElementFactory factory) {
		super(start, end, factory);
		propertyList = props;
	}
	
	// SimplePropertyList
	
	@Override
	public DataEdgeImpl clone() {
		return new DataEdgeImpl(startNode(),endNode(),propertyList.clone(),factory());
	}

	@Override
	public PropertyListSetters setProperty(String key, Object value) {
		return propertyList.setProperty(key,value);
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

	@Override
	public DataContainer clear() {
		return propertyList.clear();
	}
	
}

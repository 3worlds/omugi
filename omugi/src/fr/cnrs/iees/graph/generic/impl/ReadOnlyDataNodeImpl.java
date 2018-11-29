package fr.cnrs.iees.graph.generic.impl;

import java.util.Set;

import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.generic.ReadOnlyDataNode;
import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;
import fr.ens.biologie.generic.DataContainer;

public class ReadOnlyDataNodeImpl extends SimpleNodeImpl implements ReadOnlyDataNode {
	
	private ReadOnlyPropertyList propertyList = null;
	
	// SimpleNodeImpl
	
	protected ReadOnlyDataNodeImpl(ReadOnlyPropertyList props, GraphElementFactory factory) {
		super(factory);
		propertyList = props;
	}

	public ReadOnlyDataNodeImpl(int capacity, ReadOnlyPropertyList props, GraphElementFactory factory) {
		super(capacity, factory);
		propertyList = props;
	}
	
	// ReadOnlyPropertyList
	
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
	public ReadOnlyDataNodeImpl clone() {
		return new ReadOnlyDataNodeImpl(propertyList.clone(),factory());
	}

	@Override
	public DataContainer clear() {
		// do nothing, this is read-only
		return this;
	}

}

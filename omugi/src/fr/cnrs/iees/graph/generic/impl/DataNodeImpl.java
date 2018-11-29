package fr.cnrs.iees.graph.generic.impl;

import java.util.Set;

import fr.cnrs.iees.graph.generic.DataNode;
import fr.cnrs.iees.graph.generic.GraphElementFactory;
import fr.cnrs.iees.graph.properties.PropertyListSetters;
import fr.cnrs.iees.graph.properties.SimplePropertyList;
import fr.ens.biologie.generic.DataContainer;

/**
 * <p>A node class with a list of properties.</p>
 * @author Jacques Gignoux - 27 nov. 2018
 *
 */
public class DataNodeImpl extends SimpleNodeImpl
		implements DataNode {

	private SimplePropertyList propertyList = null;

	// SimpleNodeImpl
	
	protected DataNodeImpl(SimplePropertyList props, GraphElementFactory factory) {
		super(factory);
		propertyList = props;
	}

	public DataNodeImpl(int capacity, SimplePropertyList props, GraphElementFactory factory) {
		super(capacity, factory);
		propertyList = props;
	}

	// SimplePropertyList
	
	@Override
	public PropertyListSetters setProperty(String key, Object value) {
		return propertyList.setProperty(key,value);
	}

	@Override
	public DataNodeImpl clone() {
		return new DataNodeImpl(propertyList.clone(),factory());
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
	
	// Textable

	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(super.toDetailedString());
		sb.append(' ');
		sb.append(propertyList.toString());
		return sb.toString();
	}

}

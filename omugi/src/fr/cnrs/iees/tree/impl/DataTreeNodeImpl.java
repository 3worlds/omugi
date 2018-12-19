package fr.cnrs.iees.tree.impl;

import java.util.Set;

import fr.cnrs.iees.properties.PropertyListSetters;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.tree.DataTreeNode;
import fr.ens.biologie.generic.DataContainer;

/**
 * Basic implementation of {@link TreeNode} with read-write properties.
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
public class DataTreeNodeImpl extends SimpleTreeNodeImpl 
		implements DataTreeNode {
	
	private SimplePropertyList propertyList = null;

	// Constructors
	
	protected DataTreeNodeImpl(SimplePropertyList props) {
		super();
		propertyList = props;
	}
	
	protected DataTreeNodeImpl(int capacity, SimplePropertyList props) {
		super(capacity);
		propertyList = props;
	}

	// SimplePropertyList
	
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
	public DataContainer clear() {
		return propertyList.clear();
	}
	
	@Override
	public int size() {
		return propertyList.size();
	}

	@Override
	public DataTreeNode clone() {
		return new DataTreeNodeImpl(propertyList.clone());
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

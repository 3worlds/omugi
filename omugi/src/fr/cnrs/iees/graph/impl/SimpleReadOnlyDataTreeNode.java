package fr.cnrs.iees.graph.impl;

import fr.cnrs.iees.graph.ReadOnlyDataHolder;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * 
 * @author Jacques Gignoux - 13 mai 2019
 *
 */
public class SimpleReadOnlyDataTreeNode extends SimpleTreeNode implements ReadOnlyDataHolder {

	private ReadOnlyPropertyList properties;

	public SimpleReadOnlyDataTreeNode(Identity id, 
			ReadOnlyPropertyList props, SimpleTreeFactory factory) {
		super(id, factory);
		properties = props;
	}

	@Override
	public ReadOnlyPropertyList properties() {
		return properties;
	}

}

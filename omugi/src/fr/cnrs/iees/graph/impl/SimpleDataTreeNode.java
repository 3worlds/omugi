package fr.cnrs.iees.graph.impl;

import fr.cnrs.iees.graph.DataHolder;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * 
 * @author Jacques Gignoux - 13 mai 2019
 *
 */
public class SimpleDataTreeNode extends SimpleTreeNode implements DataHolder {

	private SimplePropertyList properties;

	public SimpleDataTreeNode(Identity id, SimplePropertyList props, 
			SimpleTreeFactory factory) {
		super(id, factory);
		properties = props;
	}

	@Override
	public SimplePropertyList properties() {
		return properties;
	}

}

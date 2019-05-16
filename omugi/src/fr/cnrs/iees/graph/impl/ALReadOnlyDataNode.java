package fr.cnrs.iees.graph.impl;

import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.ReadOnlyDataHolder;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * 
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
public class ALReadOnlyDataNode extends ALNode implements ReadOnlyDataHolder {

	private ReadOnlyPropertyList properties;
	
	public ALReadOnlyDataNode(Identity id, 
			ReadOnlyPropertyList props, 
			GraphFactory<ALNode,? extends ALEdge> factory) {
		super(id, factory);
		properties = props;
	}

	@Override
	public ReadOnlyPropertyList properties() {
		return properties;
	}

}

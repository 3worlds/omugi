package fr.cnrs.iees.graph.impl;

import fr.cnrs.iees.graph.DataHolder;
import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * 
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
public class ALDataNode extends ALNode implements DataHolder {

	private SimplePropertyList properties;
	
	public ALDataNode(Identity id, 
			SimplePropertyList props, 
			GraphFactory factory) {
		super(id, factory);
		properties = props;
	}

	@Override
	public SimplePropertyList properties() {
		return properties;
	}

}

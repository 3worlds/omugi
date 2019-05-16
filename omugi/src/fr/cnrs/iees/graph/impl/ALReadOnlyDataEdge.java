package fr.cnrs.iees.graph.impl;

import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.ReadOnlyDataHolder;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * 
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
public class ALReadOnlyDataEdge extends ALEdge implements ReadOnlyDataHolder {

	private ReadOnlyPropertyList properties;
	
	public ALReadOnlyDataEdge(Identity id, Node start, Node end, 
			ReadOnlyPropertyList props, EdgeFactory graph) {
		super(id, start, end, graph);
		properties = props;
	}

	@Override
	public ReadOnlyPropertyList properties() {
		return properties;
	}

}

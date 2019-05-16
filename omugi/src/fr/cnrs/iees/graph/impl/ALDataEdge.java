package fr.cnrs.iees.graph.impl;

import fr.cnrs.iees.graph.DataHolder;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * 
 * @author Jacques Gignoux - 10 mai 2019
 *
 */
public class ALDataEdge extends ALEdge implements DataHolder {

	private SimplePropertyList properties;
	
	public ALDataEdge(Identity id, Node start, Node end, 
			SimplePropertyList props, EdgeFactory<ALEdge> graph) {
		super(id, start, end, graph);
		properties = props;
	}

	@Override
	public SimplePropertyList properties() {
		return properties;
	}

}

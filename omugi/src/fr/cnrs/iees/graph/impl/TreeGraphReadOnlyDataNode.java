package fr.cnrs.iees.graph.impl;

import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.ReadOnlyDataHolder;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * 
 * @author Jacques Gignoux - 15 mai 2019
 *
 */
public class TreeGraphReadOnlyDataNode extends TreeGraphNode implements ReadOnlyDataHolder {

	private ReadOnlyPropertyList properties;
	
	public TreeGraphReadOnlyDataNode(Identity id, GraphFactory gfactory,
			ReadOnlyPropertyList props) {
		super(id, gfactory);
		props = properties;
	}
	
	@Override
	public ReadOnlyPropertyList properties() {
		return properties;
	}

	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(super.toDetailedString());
		sb.append(' ');
		sb.append(properties.toString());
		return sb.toString();
	}

}

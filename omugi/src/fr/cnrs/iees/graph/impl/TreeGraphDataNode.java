package fr.cnrs.iees.graph.impl;

import fr.cnrs.iees.graph.DataHolder;
import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * 
 * @author Jacques Gignoux - 15 mai 2019
 *
 */
public class TreeGraphDataNode extends TreeGraphNode implements DataHolder {

	private SimplePropertyList properties;
	
	public TreeGraphDataNode(Identity id, GraphFactory gfactory, SimplePropertyList props) {
		super(id, gfactory);
		properties = props;
	}

	@Override
	public SimplePropertyList properties() {
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

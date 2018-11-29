package fr.cnrs.iees.graph.generic;

import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;

/**
 * An Edge with properties that can only be read, not set. Use this interface for 
 * edges with internally generated property values (like age, number of items, etc.)
 * 
 * @author Jacques Gignoux - 29 nov. 2018
 *
 */
public interface ReadOnlyDataEdge extends Edge, ReadOnlyPropertyList {

	@Override
	public ReadOnlyDataEdge clone();
	
}

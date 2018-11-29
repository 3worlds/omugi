package fr.cnrs.iees.graph.generic;

import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;

/**
 * A Node with properties that can only be read, not set. Use this interface for 
 * nodes with internally generated property values (like age, number of items, etc.)
 * 
 * @author Jacques Gignoux - 29 nov. 2018
 *
 */
public interface ReadOnlyDataNode extends Node, ReadOnlyPropertyList {
	
	@Override
	public ReadOnlyDataNode clone();

}

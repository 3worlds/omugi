package fr.cnrs.iees.graph.generic;

import fr.cnrs.iees.graph.properties.SimplePropertyList;

/**
 * An Edge with read/write data contained in it, accessible as properties.
 *  
 * @author Jacques Gignoux - 29 nov. 2018
 *
 */
public interface DataEdge extends Edge, SimplePropertyList {

	@Override
	public DataEdge clone();
	
}

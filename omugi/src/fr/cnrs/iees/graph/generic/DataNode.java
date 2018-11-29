package fr.cnrs.iees.graph.generic;

import fr.cnrs.iees.graph.properties.SimplePropertyList;

/**
 * A Node with read/write data contained in it, accessible as properties.
 *  
 * @author Jacques Gignoux - 29 nov. 2018
 *
 */
public interface DataNode extends Node, SimplePropertyList {
	
	@Override
	public DataNode clone();

}

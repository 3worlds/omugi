package fr.cnrs.iees.graph.generic;

import fr.cnrs.iees.graph.properties.SimplePropertyList;

/**
 * An element containing data as a property list
 * @author gignoux - 30 août 2017
 *
 */
public interface DataElement extends Element, SimplePropertyList {

	/**
	 * Makes a copy of the data contained in this element
	 * @return the cloned data
	 */
	public SimplePropertyList cloneData();

}

package fr.cnrs.iees;

import fr.ens.biologie.generic.SaveableAsText;

/**
 * How to uniquely identify items in a graph, tree or other system
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
public interface Identifiable {
	
    public static final char LABEL_NAME_SEPARATOR = SaveableAsText.COLON;

	/**
	 * Getter for
	 * @return this element's class id (eg 'node' or 'edge')
	 * <p>formerly known as <em>label</em></p>
	 */
	public String classId();
	
	/**
	 * Getter for
	 * @return this element's instance id
	 * <p>formerly known as <em>name</em></p>
	 */
	public String instanceId();

	/**
	 * Getter for
	 * @return this element's unique identifier
	 */
	public default String uniqueId() {
		return classId()+LABEL_NAME_SEPARATOR+instanceId();
	}

}

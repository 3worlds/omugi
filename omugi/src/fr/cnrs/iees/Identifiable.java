/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          * 
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  OMUGI is an API to implement graphs, as described by graph theory,    *
 *  but also as more commonly used in computing - e.g. dynamic graphs.    *
 *  It interfaces with JGraphT, an API for mathematical graphs, and       *
 *  GraphStream, an API for visual graphs.                                *
 *                                                                        *
 **************************************************************************                                       
 *  This file is part of OMUGI (One More Ultimate Graph Implementation).  *
 *                                                                        *
 *  OMUGI is free software: you can redistribute it and/or modify         *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  OMUGI is distributed in the hope that it will be useful,              *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with OMUGI.  If not, see <https://www.gnu.org/licenses/gpl.html>*
 *                                                                        *
 **************************************************************************/
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
    public static final String LABEL_NAME_STR_SEPARATOR = ""+LABEL_NAME_SEPARATOR;

	/**
	 * Getter for
	 * @return this element's class id (eg 'node' or 'edge')
	 * <p>formerly known as <em>label</em>. By default, this is the java class name.</p>
	 */
	public default String classId() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Getter for
	 * @return this element's instance id
	 * <p>formerly known as <em>name</em>. By default, this is the java instance hash code
	 * (i.e. the value returned by {@code Object.hashCode()}.</p>
	 */
	public default String instanceId() {
		return Integer.toHexString(hashCode());
	}

	/**
	 * Getter for
	 * @return this element's unique identifier
	 */
	public default String uniqueId() {
		return new StringBuilder().append(classId()+LABEL_NAME_SEPARATOR+instanceId()).toString();
	}

}

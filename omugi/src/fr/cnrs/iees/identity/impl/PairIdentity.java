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
package fr.cnrs.iees.identity.impl;

import fr.cnrs.iees.identity.*;
import fr.cnrs.iees.omhtk.SaveableAsText;

/**
 * <p>
 * Implementation of {@link Identity} based on a (label,name) String pair. Works
 * with {@link PairLocalScope}.
 * </p>
 * 
 * @author Jacques Gignoux - 28 janv. 2019
 *
 */
public class PairIdentity implements Identity {

	/**
	 * The label name separator as a char.
	 */
	public static final char LABEL_NAME_SEPARATOR = SaveableAsText.COLON;
	/**
	 * The label name separator as a String
	 */
	public static final String LABEL_NAME_STR_SEPARATOR = "" + LABEL_NAME_SEPARATOR;

	private final String label;
	private final String name;
	private final IdentityScope scope;

	/**
	 * protected constructor, as all instantiations should be made through the
	 * scope.
	 * 
	 * @param scope
	 */
	protected PairIdentity(String label, String name, IdentityScope scope) {
		super();
		this.label = label;
		this.name = name;
		this.scope = scope;
	}

	@Override
	public String id() {
		return label + LABEL_NAME_SEPARATOR + name;
	}

	@Override
	public IdentityScope scope() {
		return scope;
	}

	/**
	 * Getter for the Label part (first) of the Identity.
	 * 
	 * @return the label.
	 */
	public String label() {
		return label;
	}

	/**
	 * Getter for the name part (second) of the Identity. Names and unique with in
	 * the scope.
	 * 
	 * @return the name.
	 */
	public String name() {
		return name;
	}

	@Override
	public String toString() {
		return id();
	}

	@Override
	public void rename(String oldId, String newId) {
		throw new UnsupportedOperationException("Renaming of '" + this.getClass().getSimpleName() + "' is not implemented.");
	}

}

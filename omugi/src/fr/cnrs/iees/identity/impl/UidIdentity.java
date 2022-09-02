/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
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

import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;

/**
 * <p>Implementation of universally unique {@link Identity} based on {@link au.edu.anu.rscs.aot.util.Uid}.
 * Works with {@link UniversalScope}.</p>
 * 
 * <p>The identifier is constructed from:</p>
 * <ol>
 * <li>the host computer mac address - these are worldwide unique;</li>
 * <li>the current time in milliseconds (i.e. at the time of constructor call) - very likely
 * to be different unless successive calls to the constructor are done during the same millisecond
 * on the same computer;</li>
 * <li>a count (as a {@code short} integer) in case constructor calls are done during the same millisecond.</li>
 * </ol>
 * <p>When exported as a String, a Uid instance looks like:<br/>
 * D89EF3043496-00000167D04FC89F-0002<br/>
 * where the first part represents the mac address, the second the time stamp, and the third the count.
 * </p>
 * <p>Not very user friendly, but guaranteed unique.</p>
 * 
 * @author Ian Davies - 28 jan. 2019
 *
 */
public final class UidIdentity implements Identity{

	private final String id;
	private final IdentityScope scope;
	
	/**
	 * protected constructor, as all instantiations should be made through the scope.
	 * @param scope
	 */
	protected UidIdentity(IdentityScope scope) {
		id = new Uid().toHexString();	
		this.scope = scope;
	}
	
	@Override
	public String id() {
		return id;
	}
	
	@Override
	public IdentityScope scope() {
		return scope;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public void rename(String oldId, String newId) {
		throw new UnsupportedOperationException("Renaming of '"+this.getClass().getSimpleName()+"' is not implemented.");
	}

}

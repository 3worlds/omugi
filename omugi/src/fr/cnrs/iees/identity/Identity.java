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
package fr.cnrs.iees.identity;

import fr.cnrs.iees.OmugiException;
import fr.ens.biologie.generic.SaveableAsText;

/**
 * <p>A unique identity within a given scope, to attach to some item that requires a unique 
 * identifier - typically, a graph node or edge.</p>
 * <p>Rules for implementing classes:</p>
 * <ol>
 * <li>The constructor must be protected so that it can only be called from an 
 * {@link IdentityScope} instance.</li>
 * <li>They must override the {@link Object#equals(Object)}
 * and {@link Object#hashCode()} methods. [not sure - check this]</li>
 * </ol>
 * 
 * @author Jacques Gignoux - 28 janv. 2019
 *
 */
public interface Identity {
	
	/** 
	 * String version of this instance.
	 *  
	 * @return a unique identifier, valid over the scope it was instantiated with.
	 */
	public String id();
	
	/**
	 * The scope that was used to instantiate this instance, within which it 
	 * is guaranteed to be unique..
	 * 
	 * @return the scope 
	 */
	public IdentityScope scope();
	
	/**
	 * This identifier's unique identifier <em>within the application</em> it has been created. 
	 * This method returns a concatenation
	 * of the scope identifier (as returned by {@link IdentityScope#id()}) with this instance id. 
	 * The scope instance identifier is unique within the application that runs it.
	 * 
	 * @return the universal id = scope id + item id
	 */
	public default String universalId() {
		return scope().id()+SaveableAsText.COLON+id();
	}
	
	/**
	 * <p>Renames an id to another one - CAUTION: this is for very limited use cases (i.e. when editing the graph) and should be
	 * avoided as far as possible because it breaks the id immutability paradigm. The default
	 * implementation throws an Exception.
	 * </p>
	 * <p><strong>WARNING</strong>: This is a typical example of a hack we would like to remove. 
	 * Although there is no problem conceptually in having mutable identities, as long as their scope
	 * guarantees their uniqueness it makes implementation very difficult. Uniqueness is much easier to
	 * guarantee if identities are immutable. So don't rely on this method, don't use it, you have
	 * never seen it, ignore it, forget it if you didn't ignore it in the first time. It will certainly be
	 * removed one day.</p>
	 * 
	 * @param oldId The old Id string
	 * @param newId The new Id string
	 */
	public default void rename(String oldId, String newId) {
		throw new OmugiException("Renaming should only be used in exceptional circumstances");
	}

}

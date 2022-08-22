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

import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;
import fr.ens.biologie.generic.utils.UniqueString;

/**
 * <p>A local scope for an identity based on a pair label+name. Works with {@link PairIdentity}.
 * It works basically like {@link LocalScope}, except that it uses two Strings to construct its
 * identifiers.</p>
 * 
 * @author Jacques Gignoux - 28 janv. 2019
 *
 */

// TODO can this be removed now? (IDD)
public class PairLocalScope implements IdentityScope {
	
	private String id;
	private Set<String> names = new HashSet<String>();

	/**
	 * Constructor using the PairLocalScope simple class name as its Id.
	 */
	public PairLocalScope() {
		this(PairLocalScope.class.getSimpleName());
	}

	/**
	 * @param name proposed name of the scope. Its uniqueness will be enforced by
	 *             incrementing an appended number.
	 */
	public PairLocalScope(String name) {
		super();
		id = UniqueString.makeString(name,scopeIds);
		scopeIds.add(id);
	}
	
	/**
	 * returns a new identity with label and name modified to make the pair unique.
	 * The first argument is the label, all other arguments are concatenated into a name.
	 */
	@Override
	public Identity newId(boolean addToScope, String... proposedIdComponents) {
		PairIdentity result = null;
		String label = proposedIdComponents[0];
		String name = "";
		for (int i=1; i<proposedIdComponents.length; i++)
			name += proposedIdComponents[i];
		String id = label+PairIdentity.LABEL_NAME_SEPARATOR+name;
		if (!names.contains(name)) {
			result = new PairIdentity(label,name,this);
			if (addToScope) names.add(id);
		}
		else {
			String s = UniqueString.makeString(name,names);
			result = new PairIdentity(label,s,this);
			if (addToScope) names.add(s);
		}
		return result;
	}
	@Override
	public void removeId(String id) {
		throw new OmugiException("Removing an id from '"+this.getClass().getSimpleName()+"' is not implemented  ["+id+"]");		
	}

	
	/**
	 * returns a new identity based on label only - creates name with increasing numbers if required
	 */
	@Override
	public Identity newId(boolean addToScope,String proposedId) {
		String[] s = new String[2];
		s[0] = proposedId;
		s[1] = "";
		return newId(addToScope,s);
	}

	/**
	 * returns a new identity with empty name and label
	 */
	@Override
	public Identity newId() {
		return newId(true,"","");
	}

	@Override
	public java.lang.String id() {
		return id;
	}

	@Override
	public boolean contains(String id) {
		throw new OmugiException("Querying ids in'"+this.getClass().getSimpleName()+"' is not implemented  ["+id+"]");		
	}

	@Override
	public void addId(String newId) {
		throw new OmugiException("Adding an id to '"+this.getClass().getSimpleName()+"' is not implemented  ["+newId+"]");		
	}


}

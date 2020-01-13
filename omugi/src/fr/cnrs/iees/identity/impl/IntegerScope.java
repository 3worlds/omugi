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

import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.identity.IdentityScope;

/**
 * A very crude but fast scope to create unique ids as incremental long numbers, starting at 1.
 * 
 * @author Jacques Gignoux - 13 janv. 2020
 *
 */
public class IntegerScope implements IdentityScope {
	
	private String id;
	private long nextId = 0;

	public IntegerScope(String name) {
		super();
		id = name;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public Identity newId() {
		return new SimpleIdentity(String.valueOf(nextId++),this);
	}

	@Override
	public void removeId(String id) {
		throw new OmugiException("IntegerScope cannot modify an id");
	}

	@Override
	public boolean contains(String id) {
		return Long.parseLong(id)<=nextId;
	}

	@Override
	public void addId(String newId) {
		throw new OmugiException("IntegerScope does not store its ids");
	}

}

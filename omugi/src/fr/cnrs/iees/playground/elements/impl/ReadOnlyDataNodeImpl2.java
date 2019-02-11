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
package fr.cnrs.iees.playground.elements.impl;

import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.playground.elements.IReadOnlyProperties;
import fr.cnrs.iees.playground.factories.INodeFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;


public class ReadOnlyDataNodeImpl2 extends SimpleNodeImpl2 implements IReadOnlyProperties {
	
	private ReadOnlyPropertyList propertyList = null;
	

	protected ReadOnlyDataNodeImpl2(Identity id, ReadOnlyPropertyList props, INodeFactory factory) {
		super(id,factory);
		propertyList = props;
	}

	// DataNode

	@Override
	public ReadOnlyPropertyList properties() {
		return propertyList;
	}
	
	// Textable

	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(super.toDetailedString());
		sb.append(' ');
		sb.append(propertyList.toString());
		return sb.toString();
	}

}

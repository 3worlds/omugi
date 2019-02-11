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
package fr.cnrs.iees.playground.graphs;

import java.util.List;
import java.util.Set;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.playground.elements.INode;
import fr.cnrs.iees.playground.elements.impl.SimpleEdgeImpl2;
import fr.cnrs.iees.playground.factories.IEdgeFactory;
import fr.cnrs.iees.properties.ExtendablePropertyList;
import fr.cnrs.iees.properties.PropertyListSetters;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.ResizeablePropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.ens.biologie.generic.Sealable;

public class AotEdge2 extends SimpleEdgeImpl2  implements ExtendablePropertyList{

	private ExtendablePropertyList properties;

	// protected??
	protected AotEdge2(Identity id, INode start, INode end, ExtendablePropertyList props, IEdgeFactory factory) {
		super(id, start, end, factory);
		this.properties = props;
	}
	@Override
	public SimplePropertyList clone() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PropertyListSetters setProperty(String key, Object value) {
		return properties.setProperty(key,value);
	}

	@Override
	public Object getPropertyValue(String key) {
		return properties.getPropertyValue(key);
	}

	@Override
	public boolean hasProperty(String key) {
		return properties.hasProperty(key);
	}

	@Override
	public Set<String> getKeysAsSet() {
		return properties.getKeysAsSet();
	}

	@Override
	public int size() {
		return properties.size();
	}

	@Override
	public ResizeablePropertyList addProperty(Property property) {
		return properties.addProperty(property);
	}

	@Override
	public ResizeablePropertyList addProperty(String key) {
		return properties.addProperty(key);
	}

	@Override
	public ResizeablePropertyList addProperty(String key, Object value) {
		return properties.addProperty(key, value);
	}

	@Override
	public ResizeablePropertyList addProperties(List<String> keys) {
		return properties.addProperties(keys);
	}

	@Override
	public ResizeablePropertyList addProperties(String... keys) {
		return properties.addProperties(keys);
	}

	@Override
	public ResizeablePropertyList addProperties(ReadOnlyPropertyList plist) {
		return properties.addProperties(plist);
	}

	@Override
	public Object getPropertyValue(String key, Object defaultValue) {
		return properties.getPropertyValue(key, defaultValue);
	}

	@Override
	public ResizeablePropertyList removeProperty(String key) {
		return properties.removeProperty(key);
	}

	@Override
	public ResizeablePropertyList removeAllProperties() {
		return properties.removeAllProperties();
	}

	@Override
	public Sealable seal() {
		return properties.seal();
	}

	@Override
	public boolean isSealed() {
		return properties.isSealed();
	}

}

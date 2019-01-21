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
package fr.cnrs.iees.io.parsing.impl;

import static fr.cnrs.iees.io.parsing.TextGrammar.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.collections.tables.Table;
import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.io.parsing.Parser;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.properties.PropertyListFactory;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * Common grounds for graph and tree parsers. 
 * 
 * @author Jacques Gignoux - 21 janv. 2019
 *
 */
public abstract class MinimalGraphParser extends Parser {

	//----------------------------------------------------
	// specifications for a property
	protected class propSpec {
		protected String name;
		protected String type;
		protected String value;
		@Override // for debugging only
		public String toString() {
			return name+":"+type+"="+value;
		}
	}
	//----------------------------------------------------
	// specifications for a node
	protected class nodeSpec {
		protected String label;
		protected String name;
		protected List<propSpec> props = new LinkedList<propSpec>();
		@Override // for debugging only
		public String toString() {
			return label+":"+name;
		}
	}
	//----------------------------------------------------
	// specifications for a tree node
	protected class treeNodeSpec extends nodeSpec{
		protected treeNodeSpec parent = null;
	}
	//----------------------------------------------------
	// specifications for an edge
	protected class edgeSpec {
		protected String label;
		protected String name;
		protected String start;
		protected String end;
		protected List<propSpec> props = new LinkedList<propSpec>();
		@Override // for debugging only
		public String toString() {
			return label+":"+name+" ["+start+"-->"+end+"]";
		}
	}
	//----------------------------------------------------
	protected PropertyListFactory propertyListFactory = null;

	// builds a propertyList from specs
	protected SimplePropertyList makePropertyList(List<propSpec> props, Logger log) {
		List<Property> pl = new LinkedList<Property>();
		for (propSpec p:props) {
			String className = ValidPropertyTypes.getJavaClassName(p.type);
			if (className==null)
				log.severe("unknown property type ("+p.type+")");
			else {
				Object o = null;
				try {
					Class<?> c = Class.forName(className);
					// if method present, instantiate object with valueOf()
					for (Method m:c.getMethods())
						if (m.getName().equals("valueOf")) {
							Class<?>[] pt = m.getParameterTypes();
							// first case, valueOf() only has a String argument --> primitive types
							if (pt.length==1) {
								if (String.class.isAssignableFrom(pt[0])) {
									o = m.invoke(null, p.value);
									break;
								}
							}
							// Second case, value of has 3 arguments --> Table type
							else if (pt.length==3) {
								if ((String.class.isAssignableFrom(pt[0])) &&
										(char[][].class.isAssignableFrom(pt[1])) &&
										(char[].class.isAssignableFrom(pt[2])) ) {
									char[][] bdel = new char[2][2];
									bdel[Table.DIMix] = DIM_BLOCK_DELIMITERS;
									bdel[Table.TABLEix] = TABLE_BLOCK_DELIMITERS;
									char[] isep = new char[2];
									isep[Table.DIMix] = DIM_ITEM_SEPARATOR;
									isep[Table.TABLEix] = TABLE_ITEM_SEPARATOR;
									o = m.invoke(null,p.value,bdel,isep);
								}
							}
					}
					// else must be a String
					if (o==null) {
						if (p.value.equals("null"))
							o = null;
						else
							o = p.value;
					}
				} catch (ClassNotFoundException e) {
					// We should reach here only if there is an error in ValidPropertyTypes
					e.printStackTrace();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// this occurs if the value is not of the proper type
					o=null;
				}
				pl.add(new Property(p.name,o));
			}
		}
		Property[] pp = new Property[pl.size()];
		int i=0;
		for (Property p:pl)
			pp[i++] = p;
		return propertyListFactory.makePropertyList(pp);
	}



}

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
package au.edu.anu.rscs.aot.graph.types;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.util.Uid;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;

class ValidPropertyTypesTest {

	@Test
	void testRecordPropertyType() {
		ValidPropertyTypes.recordPropertyType("Uid", "au.edu.anu.rscs.aot.util", Uid.nullUid());
		assertEquals(ValidPropertyTypes.getJavaClassName("Uid"),"au.edu.anu.rscs.aot.util");
	}

	@Test
	void testGetJavaClassName() {
		assertEquals(ValidPropertyTypes.getJavaClassName("String"),"java.lang.String");
	}

	@Test
	void testGetDefaultValue() {
		assertEquals(ValidPropertyTypes.getDefaultValue("Long"),0L);
	}

	@Test
	void testIsValid() {
		assertTrue(ValidPropertyTypes.isValid("Double"));
		assertTrue(ValidPropertyTypes.isValid("double"));
	}

	@Test
	void testTypeOf() {
		assertEquals(ValidPropertyTypes.typeOf(12),"Integer");
	}

	@Test
	void testGetType() {
		assertEquals(ValidPropertyTypes.getType("au.edu.anu.rscs.aot.collections.tables.CharTable"),"CharTable");
	}

	@Test
	void testListTypes() {
//		ValidPropertyTypes.listTypes();
		assertTrue(true);
	}

}

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
package fr.cnrs.iees.io.parsing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.omugi.collections.tables.Dimensioner;
import fr.cnrs.iees.omugi.collections.tables.StringTable;
import fr.cnrs.iees.omugi.collections.tables.Table;

import static fr.cnrs.iees.io.parsing.ValidPropertyTypes.*;

/**
 * 
 * @author Jacques Gignoux - 23 mai 2019
 *
 */
class ValidPropertyTypesTest {

	private void show(String method,String text) {
//		System.out.println(method+": "+text);
	}

	@Test
	final void testGetJavaClassName() {
		assertEquals(getJavaClassName("java.lang.String"),"java.lang.String");
		assertEquals(getJavaClassName("String"),"java.lang.String");
		assertNull(getJavaClassName("string"));
		assertEquals(getJavaClassName("au.edu.anu.omugi.collections.tables.StringTable"),"au.edu.anu.omugi.collections.tables.StringTable");
		assertEquals(getJavaClassName("StringTable"),"au.edu.anu.omugi.collections.tables.StringTable");
		assertNull(getJavaClassName("List"));
		assertEquals(getJavaClassName("java.util.List"),"java.util.List");
	}

	@Test
	final void testGetDefaultValue() {
		assertEquals(getDefaultValue("StringTable").toString(),"([1]\"\")");
		assertEquals(getDefaultValue("Long"),0L);
		assertEquals(getDefaultValue("double"),0.0);
		assertEquals(getDefaultValue("IntegerRange").toString(),"MIN_INTEGER..*");
		assertNull(getDefaultValue("Object"));
		assertEquals(getDefaultValue("String"),"");
		assertEquals(getDefaultValue("java.lang.String"),"");
	}

	@Test
	final void testIsValid() {
		assertTrue(isValid("Double"));
		assertTrue(isValid("double"));
		assertTrue(isValid("java.lang.Double"));
		assertTrue(isValid("ShortTable"));
		assertTrue(isValid("au.edu.anu.omugi.collections.tables.ShortTable"));
		assertTrue(isValid("IntegerRange"));
		assertFalse(isValid("string"));
		assertFalse(isValid("aot.collections.tables.ShortTable"));
		assertFalse(isValid("fr.cnrs.iees.io.parsing.ValidPropertyTypes"));
	}

	@Test
	final void testTypeOf() {
		assertEquals(typeOf("a String"),"String");
		assertEquals(typeOf(2.0),"Double");
		assertNull(typeOf(null));
		Table t = new StringTable(new Dimensioner(3),new Dimensioner(4));
		assertEquals(typeOf(t),"StringTable");
		assertEquals(typeOf(89L),"Long");
	}

	@Test
	final void testGetType() {
		assertEquals(getType("java.lang.String"),"String");
		assertEquals(getType("String"),"String");
		assertNull(getType("string"));
		assertNull(getType("java.lang.string"));
		assertEquals(getType("au.edu.anu.omugi.collections.tables.IntTable"),"IntTable");
	}

	@Test
	final void testListTypes() {
//		listTypes();
		// should display something like that (order may change):
//
//		Valid property types currently recorded:
//		DoubleTable / au.edu.anu.omugi.collections.tables.DoubleTable / {[1],0.0}
//		ByteTable / au.edu.anu.omugi.collections.tables.ByteTable / {[1],0}
//		String / java.lang.String / 
//		float / java.lang.Float / 0.0
		
//		CharTable / au.edu.anu.omugi.collections.tables.CharTable / {[1],
		
		// for some unknown reason CharTable displays without a final "}". Why ?? it's generic code !
		
//		long / java.lang.Long / 0
//		Char / java.lang.Char /  
//		ShortTable / au.edu.anu.omugi.collections.tables.ShortTable / {[1],0}
//		Boolean / java.lang.Boolean / false
//		IntTable / au.edu.anu.omugi.collections.tables.IntTable / {[1],0}
//		Short / java.lang.Short / 0
//		FloatTable / au.edu.anu.omugi.collections.tables.FloatTable / {[1],0.0}
//		StringTable / au.edu.anu.omugi.collections.tables.StringTable / {[1],}
//		LongTable / au.edu.anu.omugi.collections.tables.LongTable / {[1],0}
//		IntegerRange / au.edu.anu.omhtk.util.IntegerRange / MIN_INTEGER..*
//		BooleanTable / au.edu.anu.omugi.collections.tables.BooleanTable / {[1],false}
//		byte / java.lang.Byte / 0
//		double / java.lang.Double / 0.0
//		Double / java.lang.Double / 0.0
//		int / java.lang.Integer / 0
//		Integer / java.lang.Integer / 0
//		Float / java.lang.Float / 0.0
//		boolean / java.lang.Boolean / false
//		Byte / java.lang.Byte / 0
//		Long / java.lang.Long / 0
//		char / java.lang.Char /  
//		short / java.lang.Short / 0
	}

	@Test
	final void testTypes() {
		show("testGetType",types().toString());
		// should display this (order may change):
//		
//		testGetType: [DoubleTable, ByteTable, String, float, CharTable, long, Char, 
//		ShortTable, Boolean, IntTable, Short, FloatTable, StringTable, LongTable, 
//		IntegerRange, BooleanTable, byte, double, Double, int, Integer, Float, boolean, 
//		Byte, Long, char, short]
	}

	@Test
	final void testIsPrimitiveType() {
		assertTrue(isPrimitiveType("float"));
		assertTrue(isPrimitiveType("Float"));
		assertTrue(isPrimitiveType("java.lang.Float"));
		assertTrue(isPrimitiveType("String"));
		assertFalse(isPrimitiveType("IntegerRange"));
		assertFalse(isPrimitiveType("DoubleRange"));
		assertFalse(isPrimitiveType("au.edu.anu.omugi.collections.tables.CharTable"));
		assertFalse(isPrimitiveType("java.util.List"));
	}

}

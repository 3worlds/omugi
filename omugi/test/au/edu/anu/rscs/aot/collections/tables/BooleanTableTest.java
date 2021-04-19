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
package au.edu.anu.rscs.aot.collections.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ens.biologie.generic.SaveableAsText;

// TODO: Since the code for tables is generated, the tests should also be generated.
// meanwhile, tests not written for other table types 
class BooleanTableTest {
	
	private BooleanTable tb;
	
	@BeforeEach
	private void init() {
		Dimensioner dim1 = new Dimensioner(5);
		Dimensioner dim2 = new Dimensioner(3);
		Dimensioner dim3 = new Dimensioner(2);
		tb = new BooleanTable(dim1,dim2,dim3);
		for (int i=0; i<5*3*2; i++)
			tb.setWithFlatIndex(false,i);
	}
//	
//	private void show(String method,String text) {
//		System.out.println(method+": "+text);
//	}
	
	@Test
	void testBooleanTable() {
		assertNotNull(tb);
	}

	@Test
	void testGetByInt() {
		int[] index = {3,2,2};
		assertFalse(tb.getByInt(index));
	}

	@Test
	void testSetByInt() {
		int[] index = {1,0,1};
		assertFalse(tb.getByInt(index));
		tb.setByInt(true, index);
		assertTrue(tb.getByInt(index));
	}

	@Test
	void testGetWithFlatIndex() {
		assertFalse(tb.getWithFlatIndex(7));
	}

	@Test
	void testSetWithFlatIndex() {
		assertFalse(tb.getWithFlatIndex(7));
		tb.setWithFlatIndex(true, 7);
		assertTrue(tb.getWithFlatIndex(7));
	}

	@Test
	void testClone() {
		BooleanTable tb2 = tb.clone();
		assertNotNull(tb2);
		assertEquals(tb.toString(),tb2.toString());
	}

	@Test
	void testFillWithBoolean() {
		tb.fillWith(true);
		for (int i=0; i<tb.size(); i++)
			assertTrue(tb.getWithFlatIndex(i));
	}

	@Test
	void testElementToString() {
		for (int i=0; i<tb.size(); i++)
			assertEquals(tb.elementToString(i),"false");
	}

	@Test
	void testClear() {
		tb.fillWith(true);
		for (int i=0; i<tb.size(); i++)
			assertTrue(tb.getWithFlatIndex(i));
		tb.clear();
		for (int i=0; i<tb.size(); i++)
			assertFalse(tb.getWithFlatIndex(i));		
	}

	@Test
	void testFillWithObject() {
		Boolean b = Boolean.valueOf(true);
		tb.fillWith(b);
		for (int i=0; i<tb.size(); i++)
			assertTrue(tb.getWithFlatIndex(i));
	}

	@Test
	void testCopy() {
		BooleanTable tb2 = tb.clone();
		tb2.fillWith(true);
		for (int i=0; i<tb.size(); i++)
			assertFalse(tb.getWithFlatIndex(i));		
		tb.copy(tb2);
		for (int i=0; i<tb.size(); i++)
			assertTrue(tb.getWithFlatIndex(i));		
	}

	@Test
	void testElementClassName() {
		assertEquals(tb.elementClassName(),"java.lang.Boolean");
	}

	@Test
	void testElementSimpleClassName() {
		assertEquals(tb.elementSimpleClassName(),"Boolean");
	}

	@Test
	void testContentType() {
		assertEquals(tb.contentType(),Boolean.class);
	}

	@Test
	void testValueOf() {
		String table = "([3,2]false,true,false,true,false,true)";
		char[][] bdel = {SaveableAsText.BRACKETS,SaveableAsText.SQUARE_BRACKETS};
		char[] isep = {SaveableAsText.COMMA,SaveableAsText.COMMA};
		BooleanTable tb2 = BooleanTable.valueOf(table, bdel, isep);
		assertNotNull(tb2);
		assertFalse(tb2.getWithFlatIndex(0));
		assertTrue(tb2.getWithFlatIndex(1));
		assertFalse(tb2.getWithFlatIndex(2));
		assertTrue(tb2.getWithFlatIndex(3));
		assertFalse(tb2.getWithFlatIndex(4));
		assertTrue(tb2.getWithFlatIndex(5));
	}

}

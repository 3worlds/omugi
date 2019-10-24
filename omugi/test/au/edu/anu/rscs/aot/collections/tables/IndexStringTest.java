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
package au.edu.anu.rscs.aot.collections.tables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.OmugiException;

/**
 * 
 * @author Jacques Gignoux - 23 oct. 2019
 *
 */
class IndexStringTest {
	
	Table table = new BooleanTable(new Dimensioner(4), new Dimensioner(5), new Dimensioner(3));
	int[][] result = null;
	
	private void show(String method,int[][] tab) {
		System.out.print(method+": [ ");
		for (int i=0; i<tab.length; i++)
			System.out.print(Arrays.toString(tab[i])+" ");
		System.out.println("]");
	}
	
	@Test
	final void testStringToIndex1() {
		result = IndexString.stringToIndex("",table);
		assertEquals(result.length,60);
		show("testStringToIndex1",result);
	}

	@Test
	final void testStringToIndex2() {
		result = IndexString.stringToIndex("[,,]",table);
		assertEquals(result.length,60);
		show("testStringToIndex2",result);
	}

	@Test
	final void testStringToIndex3() {
		result = IndexString.stringToIndex("[2,1,2]",table);
		assertEquals(result.length,1);
		show("testStringToIndex3",result);
	}

	@Test
	final void testStringToIndex4() {
		assertThrows(OmugiException.class,()->IndexString.stringToIndex("[1:3,,2:3]",table));
	}

	@Test
	final void testStringToIndex5() {
		result = IndexString.stringToIndex("[1:3,2:3,]",table);
		assertEquals(result.length,18);
		show("testStringToIndex5",result);
	}

	@Test
	final void testStringToIndex6() {
		result = IndexString.stringToIndex("[-2,-2:3,]",table);
		assertEquals(result.length,27);
		show("testStringToIndex6",result);
	}

	@Test
	final void testStringToIndex7() {
		result = IndexString.stringToIndex("[,,]",2,3,1);
		assertEquals(result.length,6);
		show("testStringToIndex7",result);
	}

	@Test
	final void testStringToIndex8() {
		result = IndexString.stringToIndex("[2,1,2]",4,3,3);
		assertEquals(result.length,1);
		show("testStringToIndex8",result);
	}

	@Test
	final void testStringToIndex9() {
		assertThrows(OmugiException.class,()->IndexString.stringToIndex("[1:3,,2:3]",4,3,3));
	}

	@Test
	final void testStringToIndex10() {
		result = IndexString.stringToIndex("[1:3,2:3,]",4,4,2);
		assertEquals(result.length,12);
		show("testStringToIndex10",result);
	}

	@Test
	final void testStringToIndex11() {
		result = IndexString.stringToIndex("[-2,-2:3,]",4,4,4);
		assertEquals(result.length,24);
		show("testStringToIndex11",result);
	}

	
	@Test
	final void testIndexToString() {
		fail("Not yet implemented");
	}

}

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
package fr.cnrs.iees.omugi.collections.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.omhtk.utils.*;
import fr.cnrs.iees.omugi.collections.tables.Dimensioner;
import fr.cnrs.iees.omugi.collections.tables.ObjectTable;

class ObjectTableTest {

	private Duple<String,Integer> dup1, dup2, dup3; // has no valueOf method
	private Interval inter, inter1, inter2, inter3; // has a valueOf method
	private ObjectTable<Interval> intert = null;
	private ObjectTable<Duple<String,Integer>> dupt = null;
	
//	private void show(String method, String s) {
//		System.out.println(method+": \""+s+"\"");
//	}
	
	@BeforeEach
	private void init() {
		inter = Interval.open(1.2,3.5);
		inter1 = Interval.openToPosInf(0.2);
		inter2 = Interval.halfOpenSup(12.8, 25.4);
		inter3 = Interval.toNegInf(0.0);
		intert = new ObjectTable<Interval>(new Dimensioner(4),new Dimensioner(2));
		intert.setByInt(inter,0,0);
		intert.setByInt(inter1,1,0);
		intert.setByInt(inter2,2,0);
		intert.setByInt(inter3,3,1);
		dup1 = new Duple<>("un",3);
		dup2 = new Duple<>("deux",2);
		dup3 = new Duple<>("trois",1);
		dupt = new ObjectTable<Duple<String,Integer>>(new Dimensioner(2),new Dimensioner(3));
		dupt.setByInt(dup1,1,0);
		dupt.setByInt(dup2,0,2);
		dupt.setByInt(dup3,0,1);
	}
	
	
	@Test
	void testObjectTable() {
		intert = new ObjectTable<Interval>(new Dimensioner(2),new Dimensioner(3));
//		show("testObjectTable",intert.toString());
		assertNotNull(intert);
		assertEquals(intert.flatSize,6);
		assertNotNull(dupt);
		assertEquals(dupt.flatSize,6);
	}

	@Test
	void testGetByInt() {
//		show("testGetByInt",dupt.getByInt(0,1).toString());
		assertEquals(dupt.getByInt(0,1).toString(),"trois|1");
	}

	@Test
	void testSetByInt() {
		dupt.setByInt(dup1,0,1);
		dupt.setByInt(dup2,0,2);
		dupt.setByInt(dup3,1,0);
//		show("testSetByInt",dupt.toString());
		assertEquals(dupt.toString(),"{[2,3],null,un|3,deux|2,trois|1,null,null}");
	}

	@Test
	void testGetWithFlatIndex() {
//		show("testGetWithFlatIndex",intert.getWithFlatIndex(2).toString());
		assertEquals(intert.getWithFlatIndex(2).toString(),"]0.2,+∞[");
	}

	@Test
	void testClone() {
		ObjectTable<Interval> blop = intert.clone();
//		show("testClone",blop.toString());
		assertEquals(blop.toString(),"{[4,2],]1.2,3.5[,null,]0.2,+∞[,null,[12.8,25.4[,null,null,]-∞,0.0]}");
	}

	@Test
	void testCloneStructure() {
		ObjectTable<?> blip = dupt.cloneStructure();
//		show("testCloneStructure",blip.toString());
		assertEquals(blip.toString(),"{[2,3],null,null,null,null,null,null}");
	}

	@Test
	void testFillWith() {
		intert.fillWith(inter);
//		show("testFillWith",intert.toString());
		assertEquals(intert.toString(),"{[4,2],]1.2,3.5[,]1.2,3.5[,]1.2,3.5[,]1.2,3.5[,]1.2,3.5[,]1.2,3.5[,]1.2,3.5[,]1.2,3.5[}");
	}

	@Test
	void testElementToString() {
//		show("testElementToString",intert.elementToString(0));
		assertEquals(intert.elementToString(0),"]1.2,3.5[");
	}

	@Test
	void testClear() {
		dupt.clear();
		assertEquals(dupt.toString(),"{[2,3],null,null,null,null,null,null}");
	}

//	@Test
//	void testCopy() {
//		ObjectTable<Interval> brzt = new ObjectTable<>(new Dimensioner(3),new Dimensioner(2));
//		// copy and resize data to fit the new table
//		brzt.copy(intert);
////		show("testCopy",brzt.toString());
//		assertEquals(brzt.toString(),"{[3,2],]1.2,3.5[,null,]0.2,+∞[,null,[12.8,25.4[,null}");
//	}

//	@Test
//	void testElementClassName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testElementSimpleClassName() {
//		fail("Not yet implemented");
//	}

	@Test
	void testContentType() {
		dupt.clear();
//		show("testContentType",intert.contentType().toString());
		assertEquals(dupt.contentType().toString(),"class java.lang.Object");
	}

	@Test
	void testValueOfString() {
		String s = "([4,2]]1.2,3.5[,null,]0.2,+∞[,null,[12.8,25.4[,null,null,]-∞,0.0])";
		assertThrows(UnsupportedOperationException.class,()->ObjectTable.valueOf(s));
	}

}

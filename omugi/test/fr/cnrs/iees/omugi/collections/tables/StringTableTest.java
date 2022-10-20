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

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.omugi.collections.tables.StringTable;

/**
 * Example of what could be done to parse StringTables using quotes
 * 
 * @author Jacques Gignoux - 29 oct. 2019
 *
 */
class StringTableTest {
private void show (String eg,String value, String saveable) {
//	System.out.println(eg+":\t\'"+value+"\' read as \'"+saveable+"\'");
}
	@Test
	final void testValueOf() {
		String value = "([4]\"a\", \"table[0,2:3][][0,2][-1]\", \"zer\" , \"12\")";
		StringTable st = StringTable.valueOf(value);
		assertNotNull(st);
		assertEquals("([4]\"a\",\"table[0,2:3][][0,2][-1]\",\"zer\",\"12\")",st.toSaveableString());
		show("example 1",value,st.toSaveableString());
		// round trip test
		value = st.toSaveableString();
		st = StringTable.valueOf(value);
		assertNotNull(st);
		show("example 1a",value,st.toSaveableString());
//		System.out.println("example 1a:\t\'"+value+"\' read as \'"+st.toSaveableString()+"\'");
		
		
		value = "";
		st = StringTable.valueOf(value);
//		show("example 2",value,st.toString());
//		System.out.println("example 2 : value \'"+value+"\' read as \'"+st+"\'");
		assertNull(st);

		// This throws an exception because the dimension is wrong
		assertThrows(IllegalArgumentException.class,()->StringTable.valueOf("([2]a,b,c,d)"));
		
		value="([3]a,\"b,c\",d)";
		st = StringTable.valueOf(value);
		show("example 3",value,st.toSaveableString());
//		System.out.println("example 3 : value \'"+value+"\' read as \'"+st.toSaveableString()+"\'");
		assertNotNull(st);
		
		value = st.toSaveableString();
		st = StringTable.valueOf(value);
		show("example 4",value,st.toSaveableString());
//		System.out.println("example 4 : value \'"+value+"\' read as \'"+st.toSaveableString()+"\'");
		assertNotNull(st);
		
		// This throws an exception because the table separators in the second element are not quoted
		assertThrows(IllegalArgumentException.class,()->StringTable.valueOf("([4]\"a\", table[0,2:3][][0,2][-1], \"zer\" , \"12\")"));
		
		value = "([4]a,b, \"c\" , \"d\")";
		st = StringTable.valueOf(value);
		show("example 5",value,st.toSaveableString());
//		System.out.println("example 5 : value \'"+value+"\' saveable as \'"+st.toSaveableString()+"\'");
		assertNotNull(st);
		
		value = "([2]\"\ta\t\",\" b \")";
		st = StringTable.valueOf(value);
		show("example 6",value,st.toSaveableString());
//		System.out.println("example 6 : value \'"+value+"\' saveable as \'"+st.toSaveableString()+"\'");
		assertNotNull(st);

		// tests with null content
		value = "([1]null)";
		st = StringTable.valueOf(value);
		show("example 7",value,st.toSaveableString());
//		System.out.println("example 7 : value \'"+value+"\' saveable as \'"+st.toSaveableString()+"\'");
		assertNotNull(st);
		String s = st.getWithFlatIndex(0);
		assertEquals(s,"");
		value = "([1]\"\")";
		st = StringTable.valueOf(value);
		show("example 7b",value,st.toSaveableString());
//		System.out.println("example 7b : value \'"+value+"\' saveable as \'"+st.toSaveableString()+"\'");
		assertNotNull(st);
		s = st.getWithFlatIndex(0);
		assertEquals(s,"");
		value = "([1])";
		st = StringTable.valueOf(value);
		show("example 7c",value,st.toSaveableString());
//		System.out.println("example 7c : value \'"+value+"\' saveable as \'"+st.toSaveableString()+"\'");
		assertNotNull(st);
		s = st.getWithFlatIndex(0);
		assertEquals(s,"");
		value = "([1]\"null\")";
		st = StringTable.valueOf(value);
		show("example 7d",value,st.toSaveableString());
//		System.out.println("example 7d : value \'"+value+"\' saveable as \'"+st.toSaveableString()+"\'");
		assertNotNull(st);
		s = st.getWithFlatIndex(0);
		assertEquals(s,"");
		
		value = "([3]null,douze,)";
		st = StringTable.valueOf(value);
		show("example 8",value,st.toSaveableString());
//		System.out.println("example 8 : value \'"+value+"\' saveable as \'"+st.toSaveableString()+"\'");
		assertNotNull(st);
		s = st.getWithFlatIndex(0);
		assertEquals(s,"");
		s = st.getWithFlatIndex(1);
		assertNotNull(s);
		// TODO: Is this correct or should an exception have been thrown?
		s = st.getWithFlatIndex(2);
		assertEquals(s,"");


		// Try this one day:
		value = "([1]\"System.out.println(\",\");\")";
		final String v = value;
		assertThrows(IllegalArgumentException.class,()->StringTable.valueOf(v));
		
	}

}

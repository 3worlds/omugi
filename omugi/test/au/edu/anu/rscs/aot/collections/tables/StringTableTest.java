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

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.OmugiException;

import static fr.cnrs.iees.io.parsing.TextGrammar.*;

/**
 * Example of what could be done to parse StringTables using quotes
 * 
 * @author Jacques Gignoux - 29 oct. 2019
 *
 */
class StringTableTest {

	@Test
	final void testValueOf() {
		String value = "([4]\"a\", \"table[0,2:3][][0,2][-1]\", \"zer\" , \"12\")";
		char[][] bdel = new char[2][2];
		bdel[Table.DIMix] = DIM_BLOCK_DELIMITERS;
		bdel[Table.TABLEix] = TABLE_BLOCK_DELIMITERS;
		char[] isep = new char[2];
		isep[Table.DIMix] = DIM_ITEM_SEPARATOR;
		isep[Table.TABLEix] = TABLE_ITEM_SEPARATOR;
		StringTable st = StringTable.valueOf(value,bdel,isep);
		System.out.println("example 1 : value \'"+value+"\' read as \'"+st+"\'");
		assertNotNull(st);
		
		value = "";
		st = StringTable.valueOf(value, bdel, isep);
		System.out.println("example 2 : value \'"+value+"\' read as \'"+st+"\'");
		assertNull(st);

		// This throws an exception because the dimension is wrong
		assertThrows(OmugiException.class,()->StringTable.valueOf("([2]a,b,c,d)", bdel, isep));
		
		value="([3]a,\"b,c\",d)";
		st = StringTable.valueOf(value, bdel, isep);
		System.out.println("example 3 : value \'"+value+"\' read as \'"+st+"\'");
		assertNotNull(st);
		
		value = st.toSaveableString(bdel,isep);
		st = StringTable.valueOf(value, bdel, isep);
		System.out.println("example 4 : value \'"+value+"\' read as \'"+st+"\'");
		assertNotNull(st);
		
		// This throws an exception because the table separators in the second element are not quoted
		assertThrows(OmugiException.class,()->StringTable.valueOf("([4]\"a\", table[0,2:3][][0,2][-1], \"zer\" , \"12\")", bdel, isep));
		
		value = "([4]a,b, \"c\" , \"d\")";
		st = StringTable.valueOf(value);
		System.out.println("example 5 : value \'"+value+"\' read as \'"+st+"\'");
		assertNotNull(st);
	}

}

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

class TableTest {

	private Table tb;
	
	@BeforeEach
	private void init() {
		Dimensioner dim1 = new Dimensioner(5);
		Dimensioner dim2 = new Dimensioner(3);
		Dimensioner dim3 = new Dimensioner(2);
		tb = new BooleanTable(dim1,dim2,dim3);
	}
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}
	
	@Test
	void testGetDimensioners() {
		assertEquals(tb.getDimensioners().length,3);
	}

	private String indexToString(int[] index) {
		StringBuilder sb = new StringBuilder(10);
		sb.append('[')
			.append(index[0]).append(',')
			.append(index[1]).append(',')
			.append(index[2]).append(']');
		return sb.toString();
	}
	
	@Test
	void testGetFlatIndexByInt() {
		assertEquals(tb.getFlatIndexByInt(1,0,1),7);
		int[] index = new int[3];
		for (int i=0; i<5; i++)
			for (int j=0; j<3; j++)
				for (int k=0; k<2; k++) {
					index[0] = i;
					index[1] = j;
					index[2] = k;
					show("testGetFlatIndexByInt",indexToString(index)+"="
						+String.valueOf(tb.getFlatIndexByInt(index)));
		}
	}

	@Test
	void testGetIndexes() {
		int[] index = {1,0,1};
		int[] index2 = tb.getIndexes(7);
		for (int i=0; i<index.length; i++)
			assertEquals(index[i],index2[i]);
		for (int i=0; i<5*3*2; i++) {
			index = tb.getIndexes(i);
			show("testGetIndexes",indexToString(index)+"="+i);
		}
	}

	@Test
	void testNdim() {
		assertEquals(tb.ndim(),3);
	}

	@Test
	void testSizeInt() {
		assertEquals(tb.size(0),5);
		assertEquals(tb.size(1),3);
		assertEquals(tb.size(2),2);
	}

	@Test
	void testSize() {
		assertEquals(tb.size(),5*3*2);
	}

	@Test
	void testToString() {
		show("testToString",tb.toString());
		assertEquals(tb.toString(),"{[5,3,2],false,false,false,false,false,false,false,false,false,false...}");
	}

	@Test
	void testGetBlockContent() {
		assertEquals(TableAdapter.getBlockContent("[5,3,2]",SaveableAsText.SQUARE_BRACKETS),"5,3,2");
	}

	@Test
	void testReadDimensioners() {
		Dimensioner[] dims = TableAdapter.readDimensioners("[5,3,2]", 
			SaveableAsText.SQUARE_BRACKETS, 
			SaveableAsText.COMMA);
		assertEquals(dims.length,3);
		for (int i=0; i<dims.length; i++) 
			assertEquals(dims[i],tb.getDimensioners()[i]);
	}

	@Test
	void testToSaveableString() {
		char[][] bdel = {SaveableAsText.BRACKETS,SaveableAsText.TRIANGULAR_BRACKETS};
		char[] isep = {SaveableAsText.BLANK,SaveableAsText.PLUS};
		show("testToSaveableString",tb.toSaveableString(bdel, isep));
		assertEquals(tb.toSaveableString(bdel, isep),"(<5+3+2>false false false false false "
			+ "false false false false false false false false false false false false false "
			+ "false false false false false false false false false false false false)");
	}
	
}

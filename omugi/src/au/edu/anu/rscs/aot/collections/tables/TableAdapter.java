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

import java.util.LinkedList;
import java.util.List;

import fr.cnrs.iees.OmugiException;
import fr.ens.biologie.generic.DataContainer;
import fr.ens.biologie.generic.Sizeable;
import fr.ens.biologie.generic.Textable;

public abstract class TableAdapter implements Table {
		
	// index for type of delimiters used in saving to files (cf. Textable interface)
	protected static int TABLE = 0;
	protected static int DIM = 1;

	protected Dimensioner[] dimensioners;
	
	// The following are pre-calculated in the constructor to improve performance later
	//
	protected int           dimensions;
	protected int           flatSize;
	private   int[]         offsets;
	
	// Constructors
	//

	public TableAdapter(Dimensioner... dimensioners) {
		dimensions = dimensioners.length;
		if (dimensions == 0)
			throw new OmugiException("TableAdapter: at least one dimension needs to be passed to constructor");
		this.dimensioners = dimensioners;

		offsets = new int[dimensions];

		for (int i=0; i<dimensions; i++) {
			offsets[i] = 1;
			for (int j=i+1; j<dimensions; j++)
				offsets[i] = offsets[i] * dimensioners[j].getLength();
		}

		flatSize = dimensioners[0].getLength();
		for (int i=1; i<dimensions; i++)
			flatSize = flatSize * dimensioners[i].getLength();
	}

	// static utilities (for reading from dsl file - used in tokenizer > token.asTable() )
	//
	
//	public static TableAdapter makeTable(NodeList nodeList, String value) {
//		AotList<Object> elements = new AotList<Object>();
//		
//		Tokenizer tokenizer = new Tokenizer(value, Tokenizer.DEFAULT_SYMBOLS + ",");
//		while (tokenizer.hasNext()) {
////			elements.add(tokenizer.getTypedValue(nodeList));
//			elements.add(tokenizer.getTypedValue());
//			if (tokenizer.isEndOfLine(+1))
//				return table(elements);;
//			if (tokenizer.hasNext())
//				tokenizer.getSymbol(",");
//		}
//		return table(elements);
//	}
	
//	// this is flawed ! may cause problems
//	private static TableAdapter table(AotList<Object> elements) {
////		JG - temporary fix - 1/10/2018
//		ObjectTable<Object> result = new ObjectTable<Object>(new Dimensioner(elements.size()));
////		StringTable result = new StringTable(new Dimensioner(elements.size()));
//		for (int i=0; i<elements.size(); i++)
//			result.set(elements.get(i), i);
//		return result;
//	}

	// TableAccess methods
	//
	
	@Override
	public final Dimensioner[] getDimensioners() {
		return dimensioners;
	}

	@Override
	public final int getFlatIndexByInt(int... indexes) {
		if (indexes.length != dimensioners.length)
			throw new OmugiException("TableAdapter.getFlatIndex expected " + dimensioners.length + " indexes (got " + indexes.length + ")");
		int index = 0;
		for (int dim=0; dim<dimensioners.length; dim++) {
			index = index + indexes[dim] * offsets[dim];
		}
		return index;
	}


	@Override
	public final int getFlatIndex(Object... indexes) {
		if (indexes.length != dimensioners.length)
			throw new OmugiException("TableAdapter.getFlatIndex expected " + dimensioners.length + " indexes (got " + indexes.length + ")");
		int index = 0;
		for (int dim=0; dim<dimensioners.length; dim++) {
			if (indexes[dim] instanceof Integer)
				index = index + (Integer)indexes[dim] * offsets[dim];
			else if (indexes[dim] instanceof String)
				index = index + dimensioners[dim].getIndex((String)indexes[dim]) * offsets[dim];				
		}
		return index;
	}


	@Override
	public final int[] getIndexes(int flatIndex) {
		int index = flatIndex;
		int[] result = new int[dimensions];
		result[0] = flatIndex / offsets[0];
		for (int dim=1; dim<dimensions; dim++) {
			index = index - result[dim-1] * offsets[dim-1];
			result[dim] = index / offsets[dim];
		}
		return result;
	}
	
	@Override
	public final int ndim() {
		return dimensions;
	}
	
	@Override
	public int size(int index) {
		return dimensioners[index].getLength();
	}

	// DataContainer methods
	//
	
	@Override
	public abstract TableAdapter clone();
	
	protected abstract TableAdapter cloneStructure();
	
	// Sizeable methods
	//
	
	@Override
	public final int size() {
		return flatSize;
	}
	
	// other methods
	//


	// CAUTION: this cannot be used to save files anymore since it is not
	// displaying more than 10 items. Just for debugging.
	@Override
	public String toString() {
		int MAXITEMS = 10;
		StringBuilder sb = new StringBuilder(1024);
		sb.append("{[").append(dimensioners[0].getLength());
		for (int i=1; i<dimensioners.length; i++)
			sb.append(",").append(dimensioners[i].getLength());
		sb.append("]");
		for (int i=0; i<Math.min(flatSize,MAXITEMS); i++)
			sb.append(",").append(elementToString(i));
		if (flatSize>MAXITEMS)
			sb.append("...");
		sb.append("}");
		return sb.toString();
	}
			
//	public String toString(int[] indexes) {
//		StringBuilder sb = new StringBuilder(1024);
////		String result = "[" + indexes[0];
//		sb.append("[").append(indexes[0]);
//		for (int i=1; i<indexes.length; i++)
//			sb.append(", ").append(indexes[i]);
////			result = result + ", " + indexes[i];
//		
////		return result + "]";
//		sb.append("]");
//		return sb.toString();
//	}

	public void show(String message) {
		System.out.println(message + " " + toString());
	}

	public void show() {
		System.out.println(toString());
	}
	
	
	// COMPONENTS OF valueOf() methods for descendants
	
	/**
	 * Utility for descendants - checks that a String passed as an argument to valueOf(...) has
	 * the correct start and end delimiters and returns that string stripped of these delimiters.
	 * will crash if argument value is empty or null
	 * @param value the String value to convert to a table block
	 * @param bdel the block delimiters to use (cf {@code TextGrammar})
	 */
	protected static String getBlockContent(String value, char[] bdel) {
		if ((value.charAt(0)!=bdel[BLOCK_OPEN]) ||
			(value.charAt(value.length()-1)!=bdel[BLOCK_CLOSE])) {
			StringBuilder sb = new StringBuilder();
			sb.append("valueOf: invalid input String \"")
				.append(value)
				.append("\" - String must start with '")
				.append(bdel[BLOCK_OPEN])
				.append("' and end with '")
				.append(bdel[BLOCK_CLOSE]);
			throw new OmugiException(sb.toString());
		}
		else
			return value.substring(1, value.indexOf(bdel[BLOCK_CLOSE]));
	}
	
	// utility for descendants, to read content from aString in valueOf()
	// the argument is supposed to be formatted as [dim1;dim2;...]
	// wil crash if argument is empty
	protected static Dimensioner[] readDimensioners(String value, char[] bdel, char isep) {
		String d = getBlockContent(value,bdel);
		List<Integer> dims = new LinkedList<Integer>();
		while (d.indexOf(isep)>0) {
			String s = d.substring(0,d.indexOf(isep));
			d = d.substring(d.indexOf(isep)+1);
			dims.add(Integer.valueOf(s));
		}
		dims.add(Integer.valueOf(d));
		Dimensioner[] dim = new Dimensioner[dims.size()];
		for (int i=0; i<dims.size(); i++) 
			dim[i] = new Dimensioner(dims.get(i));
		return dim;
	}
	
	@Override
	public String toSaveableString(char[][] bdel, char[] isep) {
		StringBuilder sb = new StringBuilder(1024);
		sb.append(bdel[TABLE][BLOCK_OPEN])
			.append(bdel[DIM][BLOCK_OPEN])
			.append(dimensioners[0].getLength());
		for (int i=1; i<dimensioners.length; i++)
			sb.append(isep[DIM]).append(dimensioners[i].getLength());
		sb.append(bdel[DIM][BLOCK_CLOSE]);
		if (flatSize>0) 
			sb.append(elementToString(0));
		for (int i=1; i<flatSize; i++)
			sb.append(isep[TABLE]).append(elementToString(i));
		sb.append(bdel[TABLE][BLOCK_CLOSE]);
		return sb.toString();
	}
	
	
	// methods specified here
	
	public abstract TableAdapter copy (TableAdapter from);
	
	public abstract Class<?> contentType();

	
	
}

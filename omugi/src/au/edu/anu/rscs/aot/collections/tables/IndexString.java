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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.OmugiException;

/**
 * <p>A Class to specify a set of multidimensional indexes using a string with a R-like syntax, i.e.:</p>
 * <p>example for a 3D table "TABLE" of dimensions 3,5,2:</p>
 * <ul>
 * <li>TABLE[||] or TABLE = the whole table (all indices)</li>
 * <li>TABLE[2|4|1] = cell (2,4,1) of the table</li>
 * <li>TABLE[|4|1] = cells (0,4,1) (1,4,1) and (2,4,1), ie all items in dimension 1</li>
 * <li>TABLE[3||] = all items in dimensions 2 and 3, cell 3 only in dimension 1</li>
 * <li>1:3 = specifies a range of indices in one dimension, in this case indices 1, 2 and 3</li>
 * <li>1,3,7 = specifies the three indexes in one dimension, in this case indices 1, 3 and 7</li>
 * <li>-2 = all indices in the dimension except index 2</li>
 * <li>-1:3 = all indicies in the dimension except 1,2 and 3</li>
 * </ul>
 * 
 * @author Jacques Gignoux - 23 oct. 2019
 *
 */
public class IndexString {
	
	// separator char for dimensions (possible alt.: "][") - caution, regexp
	private static String dimSep = "|";
	// separator char for cells in one dimension (possible alt.: ";")
	private static String cellSep = ";";
	// sequence character (possible alt.: "..")
	private static String seq = ":";
	// negation character
	private static String neg = "-";

	// this to prevent instantiation - only use static methods
	private IndexString() {	}
	
	// works out the indices to extract in a single dimension
	private static int[] extractDimIndices(int dim, String indexString, int tableDimLength) {
//		int tableDimLength = table.getDimensioners()[dim].getLength();
		int[] result = new int[tableDimLength];
		// if a minus sign is found, means indices must be excluded
		boolean exclude = indexString.contains(neg);
		// empty string, all indices are required
		if (indexString.isBlank() | indexString.isEmpty())
			for (int i=0; i<result.length; i++)
				result[i] = i;
		// a list of indices (exclude doesnt work here)
		else if (indexString.contains(cellSep)) {
			String[] s = indexString.split(Pattern.quote(cellSep));
			SortedSet<Integer> li = new TreeSet<>();
			for (int i=0; i<s.length; i++)
				// between commas there may be :'s
				if (s[i].contains(seq)) {
					String[] ss = s[i].split(Pattern.quote(seq));
					int min = Math.abs(Integer.valueOf(ss[0]));
					int max = Math.abs(Integer.valueOf(ss[1]));
					if ((min<0)|(max>tableDimLength-1))
						throw new OmugiException("Table index out of range "+indexString);
					for (int j=min; j<=max; j++)
						li.add(j);
				}
				// just a single index
				else {
					int index = Integer.valueOf(s[i]);
					if ((index<0)|(index>tableDimLength-1))
						throw new OmugiException("Table index out of range "+indexString);
					li.add(index);
				}
			result = new int[li.size()];
			int i=0;
			for (int n:li)
				result[i++] = n;
		}
		// a range of indices
		else if (indexString.contains(seq)) {
			String[] s = indexString.split(Pattern.quote(seq));
			int min = Math.abs(Integer.valueOf(s[0]));
			int max = Math.abs(Integer.valueOf(s[1]));
			if ((min<0)|(max>tableDimLength-1))
				throw new OmugiException("Table index out of range "+indexString);
			int range = max-min+1;
			if (exclude) {
				result = new int[tableDimLength-range];
				for (int i=0; i<min; i++)
					result[i] = i;
				for (int i=max+1; i<tableDimLength; i++)
					result[i-range] = i;
			}
			else {
				result = new int[range];
				for (int i=min; i<=max; i++)
					result[i-min] = i;
			}
		}
		// a single index
		else {
			int index = -1;
			try {
			    index = Math.abs(Integer.valueOf(indexString));
			} catch (NumberFormatException excpt) {
				throw new OmugiException("'"+indexString + "' is not an integer.");
			}
			if ((index<0)|(index>tableDimLength-1))
				throw new OmugiException("Table index out of range "+indexString);
			if (exclude) {
				result = new int[tableDimLength-1];
				for (int i=0; i<index; i++)
					result[i] = i;
				for (int i=index+1; i<tableDimLength; i++)
					result[i-1] = i;
			}
			else {
				result = new int[1];
				result[0] = index;
			}
		}
		return result;
	}
	
	private static void allIndices(int[] res,int depth, int[][] dimIndices, Collection<int[]> result) {
		for (int i=0; i<dimIndices[depth].length; i++) {
			res[depth] = dimIndices[depth][i];
			if (depth==dimIndices.length-1)
				result.add(res.clone());
			else
				allIndices(res,depth+1,dimIndices,result);
		}
	}
	
	private static Collection<int[]> allIndices(int[][] dimIndices) {
		int[] res = new int[dimIndices.length];
		int size = 1;
		for (int i=0; i<dimIndices.length; i++)
			size *= dimIndices[i].length;
		List<int[]> lr = new ArrayList<>(size);
		allIndices(res,0,dimIndices,lr);
		return lr;
	}
	
	/**
	 * Given a string index, returns the range of indices in each dimension
	 * 	
	 * @param indexString a String describing an index
	 * @param dim the dimensions of the table this string applies to 
	 * @return the min and max index values in every dimension
	 */
	public static IntegerRange[] stringIndexRanges(String indexString, int...dim) {
		IntegerRange[] result = new IntegerRange[dim.length];
		if ((indexString==null)||(indexString.isEmpty())||(indexString.isBlank())) {
			for (int i=0; i<dim.length; i++)
				result[i] = new IntegerRange(0,dim[i]-1);
		}
		else {
			String s = indexString.strip();
			s = s.substring(1,s.length()-1);
			String[] ds = s.split(Pattern.quote(dimSep),-1);
			if (ds.length != dim.length)
				throw new OmugiException("Index string " + indexString
					+ " has "+ ds.length +" dimensions but was expecting "+dim.length);
			for (int i=0; i<ds.length; i++) {
				int[] ixs = extractDimIndices(i,ds[i],dim[i]);
				result[i] = new IntegerRange(ixs[0],ixs[ixs.length-1]);
			}
		}
		return result;
	}
	
	/**
	 * This method converts a string describing an index range to a list of indices
	 * usable by the {@linkplain Table} class and its descendants.
	 * 
	 * @param indexString the String containing the index description - e.g. [1:3,2:3,]
	 * @param table the {@code Table} this string applies to - e.g. {@code new BooleanTable(new Dimensioner(4), 
	 * new Dimensioner(5), new Dimensioner(3))}
	 * @return an array of array of indices of the same dimension as the table - e.g.
	 * [[1,2,0] [1,2,1] [1,2,2] [1,3,0] [1,3,1] [1,3,2] [2,2,0] [2,2,1] [2,2,2] [2,3,0] [2,3,1]
	 *  [2,3,2] [3,2,0] [3,2,1] [3,2,2] [3,3,0] [3,3,1] [3,3,2]]
	 */
	public static int[][] stringToIndex(String indexString, Table table) {
		int[][] result = null; 
		// blank or null string = all indices 
		if ((indexString==null)||(indexString.isEmpty())||(indexString.isBlank())) {
			result = new int[table.size()][table.ndim()];
			for (int i=0; i<table.size(); i++)
				result[i] = table.getIndexes(i);
		}
		else {
			String s = indexString.strip();
			s = s.substring(1,s.length()-1);
			String[] ds = s.split(Pattern.quote(dimSep),-1);
			if (ds.length != table.ndim())
				throw new OmugiException("Index string " + indexString
					+ " has "+ ds.length +" dimensions while table has "+table.ndim());
			int[][] dimIndices = new int[ds.length][];
			for (int i=0; i<ds.length; i++)
				dimIndices[i] = extractDimIndices(i,ds[i],table.getDimensioners()[i].getLength());
			result = new int[1][];
			result = allIndices(dimIndices).toArray(result);
		}
		return result;
	}

	/**
	 * This method converts a string describing an index range to a list of indices
	 * usable by the {@linkplain Table} class and its descendants.
	 * 
	 * @param indexString the String containing the index description - e.g. [1:3,2:3,]
	 * @param dim the dimensions of the table this string applies to - e.g. 4,5,3
	 * @return an array of arrays of indices of the same dimension as the table - e.g.
	 * [[1,2,0] [1,2,1] [1,2,2] [1,3,0] [1,3,1] [1,3,2] [2,2,0] [2,2,1] [2,2,2] [2,3,0] [2,3,1]
	 *  [2,3,2] [3,2,0] [3,2,1] [3,2,2] [3,3,0] [3,3,1] [3,3,2]]
	 */
	public static int[][] stringToIndex(String indexString, int...dim) {
		int[][] result = null; 
		String s = null;
		// blank or null string = all indices 
		if ((indexString==null)||(indexString.isEmpty())||(indexString.isBlank())) {
			s = "[";
			for (int i=0; i<dim.length-1;i++)
				s +=",";
			s+="]";
		}
		else
			s = indexString.strip();
		if (s.length() > 1)//IDD
			s = s.substring(1, s.length() - 1);
		String[] ds = s.split(Pattern.quote(dimSep),-1);
		if (ds.length != dim.length)
			throw new OmugiException("Index string " + indexString
				+ " has "+ ds.length +" dimensions while table has "+dim.length);
		int[][] dimIndices = new int[ds.length][];
		for (int i=0; i<ds.length; i++)
			dimIndices[i] = extractDimIndices(i,ds[i],dim[i]);
		result = new int[1][];
		result = allIndices(dimIndices).toArray(result);
		return result;
	}

	/**
	 * CAUTION: this is NOT performing the reverse operation to stringToIndex(...) as the factoring out
	 * of a table of indices is not unique and tricky to work out. This uses a ndim arrays of indices
	 * selected in each of the dimensions independently and works out the global String matching
	 * these.
	 * Assumes indices are provided in proper order.
	 * 
	 * @param index a ndim-list of indices to search for in every dimension
	 * @param table ndim dimension sizes
	 * @return the matching String
	 */
	public static String indexToString(int[][] index, int...dim) {
		if ((index==null)&(dim==null))
			return ""; // whole table - whatever this means
		if (index.length!=dim.length)
			throw new OmugiException("Size of index list (" + index.length +
				")not matching number of dimensions (" + dim.length + ")");
		StringBuilder sb = new StringBuilder();
		boolean wholeTable = true;
		for (int i=0; i< dim.length; i++)
			if ((index[i]!=null) && (index[i].length != dim[i])) 
				wholeTable = false;
		if (wholeTable)
			return "";
		sb.append("[");
		for (int i=0; i< dim.length; i++)
			if (index[i]==null) { // means whole dimension
				if (i<dim.length-1)
					sb.append(dimSep);
			}
			else if (index[i].length == dim[i]) { // same
				if (i<dim.length-1)
					sb.append("][");
			}
			else {
				int last = index[i][0];
				sb.append(last);
				int j=1;
				String sep = seq;
				while (j<index[i].length) {
					if (index[i][j]!=index[i][j-1]+1) {
						if (last!=index[i][0])
							sb.append(sep).append(last);
						sep = cellSep;
					}
					else {
						if (sep==cellSep)
							if (last!=index[i][0])
								sb.append(sep).append(last);
						sep = seq;
					}
					last = index[i][j];
					j++;
				}
				if (last!=index[i][0])
					sb.append(sep).append(last);
				if (i<dim.length-1)
					sb.append(dimSep);
			}
		sb.append("]");
		return sb.toString();
	}

	public static String indexToString(int[][] index, Table table) {
		int[] dim = new int[table.ndim()];
		for (int i=0; i<dim.length; i++)
			dim[i] = table.getDimensioners()[i].getLength();
		return indexToString(index,dim);
	}
	
}

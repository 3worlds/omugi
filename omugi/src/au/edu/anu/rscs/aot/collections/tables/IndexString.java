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

import fr.cnrs.iees.OmugiException;

/**
 * <p>A Class to specify a set of multidimensional indexes using a string with a R-like syntax, i.e.:</p>
 * <p>example for a 3D table "TABLE" of dimensions 3,5,2:</p>
 * <ul>
 * <li>TABLE[,,] or TABLE = the whole table (all indices)</li>
 * <li>TABLE[2,4,1] = cell (2,4,1) of the table</li>
 * <li>TABLE[,4,1] = cells (0,4,1) (1,4,1) and (2,4,1), ie all items in dimension 1</li>
 * <li>TABLE[3,,] = all items in dimensions 2 and 3, cell 3 only in dimension 1</li>
 * <li>1:3 = specifies a range of indices in one dimension, in this case indices 1, 2 and 3</li>
 * <li>-2 = all indices in the dimension except index 2</li>
 * <li>-1:3 = all indicies in the dimension except 1,2 and 3</li>
 * </ul>
 * 
 * @author Jacques Gignoux - 23 oct. 2019
 *
 */
public class IndexString {

	// this to prevent instantiation - only use static methods
	private IndexString() {	}
	
	// works out the indices to extract in a single dimension
	private static int[] extractDimIndices(int dim, String indexString, Table table) {
		int tableDimLength = table.getDimensioners()[dim].getLength();
		int[] result = new int[tableDimLength];
		// if a minus sign is found, means indices must be excluded
		boolean exclude = indexString.contains("-");
		// empty string, all indices are required
		if (indexString.isBlank() | indexString.isEmpty())
			for (int i=0; i<result.length; i++)
				result[i] = i;
		// a range of indices
		else if (indexString.contains(":")) {
			String[] s = indexString.split(":");
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
			int index = Math.abs(Integer.valueOf(indexString));
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
	
	// takes only the index string as an argument, ie [...]
	public static int[][] stringToIndex(String indexString, Table table) {
		int[][] result = null; 
		// blank or null string = all indices 
		if ((indexString==null)|(indexString.isEmpty())|(indexString.isBlank())) {
			result = new int[table.size()][table.ndim()];
			for (int i=0; i<table.size(); i++)
				result[i] = table.getIndexes(i);
		}
		else {
			String s = indexString.strip();
			s = s.substring(1,s.length()-1);
			String[] ds = s.split(",",-1);
			if (ds.length != table.ndim())
				throw new OmugiException("Index string " + indexString
					+ " has "+ ds.length +" dimensions while table has "+table.ndim());
			int[][] dimIndices = new int[ds.length][];
			for (int i=0; i<ds.length; i++)
				dimIndices[i] = extractDimIndices(i,ds[i],table);
			result = new int[1][];
			result = allIndices(dimIndices).toArray(result);
		}
		return result;
	}
	
	public static String indexToString(int[][] index, Table table) {
		// TODO: implement this later...
		return null;
	}

}

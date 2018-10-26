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

import fr.ens.biologie.generic.DataContainer;
import fr.ens.biologie.generic.Sizeable;

/**
 * An interface for {@linkplain DataContainer}s indexd with {@linkplain Dimensioner}s, i.e.
 * typically multi-dimensional tables of items of the same type. The size (dimensions) of such
 * objects are set at construction time and immutable after.
 * 
 * @author gignoux
 *
 */
public interface MultiDimContainer extends DataContainer, Sizeable {
	
	/**
	 * Get the size of the container. <br/>NOTE: this is a safe-but-slow implementation.
	 * Descendants should rather return a constant field computed at construction time. 
	 * @return the total number of items in the container
	 */
	@Override
	public default int size() {
		int size = 1;
		for (int i=0; i<dims().length; i++)
			size *= size(i);
		return size;
	}
	
	/**
	 * Get the size of the container in the matching dimension
	 * @param index the dimension index
	 * @return the length of the table in this dimension
	 */
	public default int size(int index) {
		return dim(index).getLength();
	}
	
	/**
	 * Get the number of dimensions
	 * @return the number of dimensions
	 */
	public default int ndim() {
		return dims().length;
	}
	
	/** get the ith Dimensioner */
	public Dimensioner dim(int index);	
	
	/** get all dimensioners */
	public Dimensioner[] dims();
	
	/** get the content type of the DataContainer */
	public Class<?> contentType();
	
	/** get the content class name of the DataContainer */
	public default String contentClassName() {
		return contentType().getName();
	}

}

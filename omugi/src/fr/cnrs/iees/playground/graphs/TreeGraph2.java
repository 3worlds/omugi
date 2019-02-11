/**************************************************************************
 *  AOT - Aspect-Oriented Thinking                                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  AOT is a method to generate elaborate software code from a series of  *
 *  independent domains of knowledge. It enables one to manage and        *
 *  maintain software from explicit specifications that can be translated *
 *  into any programming language.          							  *
 **************************************************************************                                       
 *  This file is part of AOT (Aspect-Oriented Thinking).                  *
 *                                                                        *
 *  AOT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  AOT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.playground.graphs;

import fr.cnrs.iees.playground.elements.IEdge;
import fr.cnrs.iees.playground.elements.ITreeGraphNode;
import fr.ens.biologie.generic.Textable;


public class TreeGraph2<N extends ITreeGraphNode,E extends IEdge> 
	implements ITree<N>,	IGraph<N,E>,	Textable {

	@Override
	public Iterable<N> nodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<N> leaves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<N> findNodesByReference(String reference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<N> roots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(N node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterable<E> edges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public N root() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITree<N> subTree(N node) {
		// TODO Auto-generated method stub
		return null;
	}
}

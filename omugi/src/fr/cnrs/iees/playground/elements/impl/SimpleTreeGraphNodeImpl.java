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
package fr.cnrs.iees.playground.elements.impl;

import java.util.Collection;

import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.playground.elements.ITreeNode;
import fr.cnrs.iees.playground.elements.impl.NodeAdapter2;
import fr.cnrs.iees.playground.elements.impl.SimpleTreeNodeImpl2;
import fr.cnrs.iees.playground.factories.ITreeNodeFactory;

public class SimpleTreeGraphNodeImpl extends NodeAdapter2 implements ITreeNode {

	private ITreeNode treeNode;

	protected SimpleTreeGraphNodeImpl(Identity id, ITreeNodeFactory factory) {
		super(id);
		treeNode = new SimpleTreeNodeImpl2(id, factory);
	}

	// --------------------NodeAdapter2
	@Override
	public String classId() {
		// TODO Auto-generated method stub
		return null;
	}

	// ---------------------ITreeNode2
	@Override
	public ITreeNode getParent() {
		return treeNode.getParent();
	}

	@Override
	public void setParent(ITreeNode parent) {
		treeNode.setParent(parent);
	}

	@Override
	public Iterable<? extends ITreeNode> getChildren() {
		return treeNode.getChildren();
	}

	@Override
	public void addChild(ITreeNode child) {
		treeNode.addChild(child);
	}

	@Override
	public void setChildren(ITreeNode... children) {
		treeNode.setChildren(children);
	}

	@Override
	public void setChildren(Iterable<ITreeNode> children) {
		treeNode.setChildren(children);
	}

	@Override
	public void setChildren(Collection<ITreeNode> children) {
		treeNode.setChildren(children);
	}

	@Override
	public boolean hasChildren() {
		return treeNode.hasChildren();
	}

	@Override
	public ITreeNodeFactory treeNodeFactory() {
		return treeNode.treeNodeFactory();
	}

	@Override
	public int nChildren() {
		return treeNode.nChildren();
	}

}

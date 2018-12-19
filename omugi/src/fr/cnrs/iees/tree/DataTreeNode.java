package fr.cnrs.iees.tree;

import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * A tree node with data
 * 
 * @author Jacques Gignoux - 19 déc. 2018
 *
 */
public interface DataTreeNode extends TreeNode, SimplePropertyList {
	
	@Override
	public DataTreeNode clone();

}

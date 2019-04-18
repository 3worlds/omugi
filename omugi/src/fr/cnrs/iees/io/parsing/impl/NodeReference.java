package fr.cnrs.iees.io.parsing.impl;

import fr.cnrs.iees.graph.TreeNode;

/**
 * Static methods to match TreeNodes to references
 * 
 * @author Jacques Gignoux - 18 avr. 2019
 *
 */
public class NodeReference {

	public static boolean matchesRef(TreeNode anode, String reference) {
		if (reference == null)
			return false;
		ReferenceTokenizer tk = new ReferenceTokenizer(reference);
		ReferenceParser p = tk.parser();
		p.parse();
		return p.matches(anode);
	}

}

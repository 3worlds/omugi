package fr.cnrs.iees.identity.impl;

import fr.ens.biologie.generic.Resettable;

/**
 * A localscope that can forget all its id on reset.
 *
 * @author
 *
 */
public class ResettableLocalScope extends LocalScope implements Resettable {

	public ResettableLocalScope(String name) {
		super(name);
	}

	@Override
	public void postProcess() {
		ids.clear();
	}

}

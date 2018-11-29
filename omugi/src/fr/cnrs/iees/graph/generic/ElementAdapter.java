package fr.cnrs.iees.graph.generic;

import au.edu.anu.rscs.aot.util.Uid;

/**
 * A base implementation of Element with the methods that should be universal in all descendants
 * @author gignoux - 16 août 2017
 *
 */
public abstract class ElementAdapter implements Element {
	
	private Uid id = null;

	public ElementAdapter() {
		super();
		id = new Uid();
	}
	
	// ELEMENT

	@Override
	public Element setId(Uid id) {
		this.id = id;
		return this;
	}

	@Override
	public final Uid getId() {
		return id;
	}
	
	
	// TEXTABLE

	@Override
	public String toUniqueString() {
		return getClass().getSimpleName()+ " id=" + id.toString();
	}

	@Override
	public String toShortString() {
		return getClass().getSimpleName();
	}
	
	@Override
	public String toDetailedString() {
		return toUniqueString();
	}
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

}

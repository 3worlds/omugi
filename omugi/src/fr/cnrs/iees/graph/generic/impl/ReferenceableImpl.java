package fr.cnrs.iees.graph.generic.impl;

import fr.cnrs.iees.graph.io.TextGrammar;
import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;
import fr.ens.biologie.generic.Labelled;
import fr.ens.biologie.generic.Named;
import fr.ens.biologie.generic.NamedAndLabelled;
import fr.ens.biologie.generic.Referenceable;

/**
 * Implementation of a referenceable element
 * @author Jacques Gignoux - 27 nov. 2018
 *
 */
public class ReferenceableImpl implements Referenceable, NamedAndLabelled, TextGrammar {

	private String label = "";
	private String name = "";
	

	// utilities when used with PropertyLists
	
	protected final void checkProperty(String key, Object value) {
		if (key.equals(NAME))
			setName((String) value);
		else if (key.equals(LABEL))
			setLabel((String) value);
	}
	
	protected final void checkProperty(ReadOnlyPropertyList propertyList) {
		if (propertyList.hasProperty(LABEL))
			setLabel((String)propertyList.getPropertyValue(LABEL));
		else if (propertyList.hasProperty(NAME))
			setName((String)propertyList.getPropertyValue(NAME));
	}

	// NAMED
	
	@Override
	public final String getName() {
		return name;
	}

	@Override
	public NamedAndLabelled setName(String name) {
		this.name = name;
		return this;
	}
	
	@Override
	public boolean sameName(Named item) {
		String name1 = this.getName();
		String name2 = item.getName();
		return (name1 == null && name2 == null) || (name1 != null && name2 != null && name1.equals(name2));
	}

	// LABELLED

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public NamedAndLabelled setLabel(String label) {
		this.label = label;
		return this;
	}

	@Override
	public boolean sameLabel(Labelled item) {
		String label1 = this.getLabel();
		String label2 = item.getLabel();
		return (label1 == null && label2 == null) || (label1 != null && label2 != null && label1.equals(label2));
	}

	// REFERENCEABLE

	@Override
	public String getReference() {
		return getLabel()+LABEL_NAME_SEPARATOR+getName();
	}

	@Override
	public boolean hasName(String aName) {
		return ((name!=null) & (name.equals(aName)));
	}

	@Override
	public boolean hasLabel(String aLabel) {
		return ((label!=null) & (label.equals(aLabel)));
	}

}

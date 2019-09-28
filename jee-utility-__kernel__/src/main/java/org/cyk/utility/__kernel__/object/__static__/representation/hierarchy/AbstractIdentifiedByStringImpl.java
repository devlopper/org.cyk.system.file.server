package org.cyk.utility.__kernel__.object.__static__.representation.hierarchy;

import java.io.Serializable;

public abstract class AbstractIdentifiedByStringImpl<IDENTIFIED> extends AbstractIdentifiedImpl<String,IDENTIFIED> implements IdentifiedByString<IDENTIFIED>,Serializable {
	private static final long serialVersionUID = 1L;

	private String identifier;

	@Override
	public String getIdentifier() {
		return identifier;
	}
	
	@Override
	public IdentifiedByString<IDENTIFIED> setIdentifier(String identifier) {
		this.identifier = identifier;
		return this;
	}
	
}

package org.cyk.utility.system.action;

import java.io.Serializable;

public class SystemActionRemoveImpl extends AbstractSystemActionImpl implements SystemActionRemove, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		setIdentifier(IDENTIFIER);
	}
	
}

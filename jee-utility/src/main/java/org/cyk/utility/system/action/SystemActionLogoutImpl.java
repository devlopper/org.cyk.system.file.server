package org.cyk.utility.system.action;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

@Dependent
public class SystemActionLogoutImpl extends AbstractSystemActionImpl implements SystemActionLogout, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		setIdentifier(IDENTIFIER);
	}
	
}
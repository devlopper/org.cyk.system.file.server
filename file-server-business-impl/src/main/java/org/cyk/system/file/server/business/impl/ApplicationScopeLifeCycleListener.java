package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.business.Initializer;
import org.cyk.utility.business.Validator;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(org.cyk.system.file.server.persistence.impl.ApplicationScopeLifeCycleListener.class).initialize(null);
		__setQualifierClassTo__(org.cyk.system.file.server.annotation.System.class, Initializer.class,Validator.class);
	}
	
	@Override
	public void __destroy__(Object object) {}

	public void saveDataFromResources() {
		
	}
	
}

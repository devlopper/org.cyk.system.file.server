package org.cyk.system.file.server.persistence.impl;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.annotation.System;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.server.persistence.PersistableClassesGetter;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(org.cyk.utility.server.persistence.impl.ApplicationScopeLifeCycleListener.class).initialize(null);
		__inject__(org.cyk.system.file.server.persistence.entities.ApplicationScopeLifeCycleListener.class).initialize(null);
		__setQualifiersClasses__(PersistableClassesGetter.class, System.Class.class);
	}
	
	@Override
	public void __destroy__(Object object) {}
	
}
package org.cyk.system.file.server.representation.impl;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.representation.server.LinksGenerator;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		DependencyInjection.setQualifierClassTo(org.cyk.system.file.server.annotation.System.class, LinksGenerator.class);
		__inject__(org.cyk.system.file.server.business.impl.ApplicationScopeLifeCycleListener.class).initialize(null);
		__inject__(org.cyk.system.file.server.representation.api.ApplicationScopeLifeCycleListener.class).initialize(null);
	}
	 
	@Override
	public void __destroy__(Object object) {}
}
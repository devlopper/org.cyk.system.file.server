package org.cyk.system.file.server.representation.impl;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(org.cyk.system.file.server.business.impl.ApplicationScopeLifeCycleListener.class).initialize(null);
		__inject__(org.cyk.system.file.server.representation.api.ApplicationScopeLifeCycleListener.class).initialize(null);
	}
	 
	@Override
	public void __destroy__(Object object) {}
}
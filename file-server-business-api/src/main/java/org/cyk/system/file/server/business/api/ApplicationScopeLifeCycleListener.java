package org.cyk.system.file.server.business.api;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.assertion.AssertionsProviderClassMap;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(AssertionsProviderClassMap.class).set(File.class, FileAssertionsProvider.class);
	}
	
	@Override
	public void __destroy__(Object object) {}
	
}
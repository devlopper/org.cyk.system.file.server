package org.cyk.system.file.server.representation.impl.integration;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.business.impl.FileBusinessImpl;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.variable.VariableHelper;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener {

	@Override
	public void __initialize__(Object object) {
		org.cyk.system.file.server.persistence.impl.ApplicationScopeLifeCycleListener.initialize();
		VariableHelper.write(FileBusinessImpl.FILES_PATHS_NAMES
    			, System.getProperty("user.dir")+"/src/test/resources/org/cyk/system/file/server/representation/impl/integration");
	}
	
	@Override
	public void __destroy__(Object object) {
		
	}
}
package org.cyk.system.file.server.business.impl.integration;

import java.io.File;

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
				, new File("src/test/resources/org/cyk/system/file/server/business/impl").getAbsolutePath());
	}
	
	@Override
	public void __destroy__(Object object) {
		
	}
}
package org.cyk.system.file.server.business.impl.integration;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.impl.FilePersistenceImpl;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.variable.VariableHelper;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener {

	@Override
	public void __initialize__(Object object) {
		org.cyk.system.file.server.persistence.impl.ApplicationScopeLifeCycleListener.initialize();
		VariableHelper.write(FilePersistenceImpl.DIRECTORY, new File("src/test/resources/org/cyk/system/file/server/business/impl").getAbsolutePath());
		VariableHelper.write(FilePersistenceImpl.ACCEPTED_PATH_NAME_REGULAR_EXPRESSION, ".pdf");
	}
	
	@Override
	public void __destroy__(Object object) {
		
	}
}
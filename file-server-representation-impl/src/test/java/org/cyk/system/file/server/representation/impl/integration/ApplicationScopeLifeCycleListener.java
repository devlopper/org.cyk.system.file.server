package org.cyk.system.file.server.representation.impl.integration;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.impl.FilePersistenceImpl;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.variable.VariableHelper;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener {

	public static Boolean INTEGRATION = Boolean.TRUE;
	
	@Override
	public void __initialize__(Object object) {
		if(Boolean.TRUE.equals(INTEGRATION))
			org.cyk.system.file.server.persistence.impl.ApplicationScopeLifeCycleListener.initialize();
		VariableHelper.write(FilePersistenceImpl.DIRECTORY, System.getProperty("user.dir")+"/src/test/resources/org/cyk/system/file/server/representation/impl/integration");
		VariableHelper.write(FilePersistenceImpl.ACCEPTED_PATH_NAME_REGULAR_EXPRESSION, ".pdf|.txt");
	}
	
	@Override
	public void __destroy__(Object object) {
		
	}
}
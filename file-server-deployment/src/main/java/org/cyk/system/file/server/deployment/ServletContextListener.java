package org.cyk.system.file.server.deployment;

import java.io.Serializable;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

import org.cyk.system.file.server.business.impl.FileBusinessImpl;
import org.cyk.system.file.server.representation.impl.ApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.configuration.ConfigurationHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.server.deployment.AbstractServletContextListener;

@WebListener
public class ServletContextListener extends AbstractServletContextListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(ServletContext context) {
		super.__initialize__(context);
		org.cyk.system.file.server.persistence.impl.ApplicationScopeLifeCycleListener.initialize();
		
		__inject__(ApplicationScopeLifeCycleListener.class).initialize(null);
		
		String path = ConfigurationHelper.getValueAsString("cyk.file.root.folder.path");
		if(StringHelper.isNotBlank(path))
			FileBusinessImpl.ROOT_FOLDER_PATH = Paths.get(path);
		//QueryExecutor.AbstractImpl.LOG_LEVEL = Level.INFO;
	}	
}
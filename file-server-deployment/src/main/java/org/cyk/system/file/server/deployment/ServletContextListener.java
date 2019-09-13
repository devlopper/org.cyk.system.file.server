package org.cyk.system.file.server.deployment;

import java.io.Serializable;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

import org.cyk.system.file.server.business.impl.FileBusinessImpl;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.impl.ApplicationScopeLifeCycleListener;
import org.cyk.utility.clazz.ClassInstancesRuntime;
import org.cyk.utility.configuration.ConstantParameterName;
import org.cyk.utility.server.deployment.AbstractServletContextListener;
import org.cyk.utility.string.StringHelperImpl;

@WebListener
public class ServletContextListener extends AbstractServletContextListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(ServletContext context) {
		super.__initialize__(context);
		__inject__(ClassInstancesRuntime.class).get(File.class).setIsActionable(Boolean.FALSE);//FIXME
		__inject__(ApplicationScopeLifeCycleListener.class).initialize(null);
		String path = ConstantParameterName.get("cyk.file.root.folder.path");
		if(StringHelperImpl.__isNotBlank__(path))
			FileBusinessImpl.ROOT_FOLDER_PATH = Paths.get(path);
	}
	
}

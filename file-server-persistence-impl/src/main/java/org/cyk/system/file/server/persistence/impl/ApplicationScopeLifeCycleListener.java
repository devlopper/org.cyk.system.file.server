package org.cyk.system.file.server.persistence.impl;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.impl.query.FileQuerierImpl;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.persistence.query.QueryManager;
import org.cyk.utility.persistence.server.TransientFieldsProcessor;
import org.cyk.utility.persistence.server.hibernate.Initializer;
import org.cyk.utility.persistence.server.query.string.RuntimeQueryStringBuilder;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		DependencyInjection.setQualifierClassTo(org.cyk.system.file.server.annotation.System.class, RuntimeQueryStringBuilder.class,TransientFieldsProcessor.class);
		__inject__(org.cyk.utility.server.persistence.impl.ApplicationScopeLifeCycleListener.class).initialize(null);
		__inject__(org.cyk.system.file.server.persistence.api.ApplicationScopeLifeCycleListener.class).initialize(null);
	}
	
	@Override
	public void __destroy__(Object object) {}
	
	public static void initialize() {
		Initializer.initialize();
		QueryManager.getInstance().scan(List.of(FileQuerier.class.getPackage()));
		org.cyk.system.file.server.persistence.api.ApplicationScopeLifeCycleListener.initialize();	
		FileQuerierImpl.initialize();
	}
}
package org.cyk.system.file.server.representation.impl;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.annotation.File;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.annotation.Default;
import org.cyk.utility.__kernel__.annotation.Server;
import org.cyk.utility.field.FieldValueCopyFieldsGetter;
import org.cyk.utility.instance.InstanceBuilder;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(org.cyk.system.file.server.business.impl.ApplicationScopeLifeCycleListener.class).initialize(null);
		__inject__(org.cyk.system.file.server.representation.api.ApplicationScopeLifeCycleListener.class).initialize(null);
		
		__setQualifierClassTo__(Default.class,FieldValueCopyFieldsGetter.class);
		
		__setQualifiersClasses__(InstanceBuilder.class, File.class,Server.class);
	}
	 
	@Override
	public void __destroy__(Object object) {}
	
	/**/
	
	public static final Integer LEVEL = new Integer(org.cyk.system.file.server.representation.api.ApplicationScopeLifeCycleListener.LEVEL+1);
}
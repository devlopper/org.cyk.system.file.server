package org.cyk.system.file.server.representation.impl;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.file.server.annotation.System;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDtoMapper;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.annotation.Default;
import org.cyk.utility.__kernel__.annotation.Server;
import org.cyk.utility.field.FieldValueCopyFieldsGetter;
import org.cyk.utility.instance.InstanceBuilder;
import org.cyk.utility.server.representation.RepresentationEntity;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		FileDtoMapper.DOWNLOAD_UNIFORM_RESOURCE_FORMAT = StringUtils.replace(FileRepresentation.PATH+FileRepresentation.PATH_DOWNLOAD_ONE+"?isinline=%s"
				,RepresentationEntity.FORMAT_PARAMETER_IDENTIFIER,"%s");
		__inject__(org.cyk.system.file.server.business.impl.ApplicationScopeLifeCycleListener.class).initialize(null);
		__inject__(org.cyk.system.file.server.representation.api.ApplicationScopeLifeCycleListener.class).initialize(null);
		
		__setQualifierClassTo__(Default.class,FieldValueCopyFieldsGetter.class);
		
		__setQualifiersClasses__(InstanceBuilder.class, System.class,Server.class);
	}
	 
	@Override
	public void __destroy__(Object object) {}
	
	/**/
	
	public static final Integer LEVEL = new Integer(org.cyk.system.file.server.representation.api.ApplicationScopeLifeCycleListener.LEVEL+1);
}
package org.cyk.system.file.server.representation.api;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.file.server.representation.entities.FileDtoMapper;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.server.representation.RepresentationEntity;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		FileDtoMapper.DOWNLOAD_PATH_FORMAT = "%s/"+StringUtils.replace(FileRepresentation.PATH_DOWNLOAD_ONE+"?isinline=%s"
				,RepresentationEntity.FORMAT_PARAMETER_IDENTIFIER,"%s");
	}
	
	@Override
	public void __destroy__(Object object) {}
	
	/**/
	
	public static final Integer LEVEL = 1000;//new Integer(org.cyk.system.file.server.business.ApplicationScopeLifeCycleListener.LEVEL+1);
}
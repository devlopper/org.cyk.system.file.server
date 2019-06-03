package org.cyk.system.file.server.representation.impl;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.__kernel__.annotation.Server;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.identifier.resource.UniformResourceIdentifierStringBuilder;
import org.cyk.utility.instance.AbstractInstanceBuilderImpl;
import org.cyk.utility.server.representation.RepresentationEntity;
import org.cyk.utility.string.StringHelper;

@org.cyk.system.file.server.annotation.System @Server
public class InstanceBuilderImpl extends AbstractInstanceBuilderImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __copy__(Object source, Object destination, Properties properties) {
		super.__copy__(source, destination, properties);
		@SuppressWarnings("unchecked")
		List<String> fields = (List<String>) Properties.getFromPath(properties,Properties.FIELDS);
		if(source instanceof File && destination instanceof FileDto) {
			File persistence = (File) source;
			FileDto representation = (FileDto) destination;
			if(__inject__(StringHelper.class).isBlank(representation.getUniformResourceLocator()) 
					&& (fields == null || fields.contains("uniformResourceLocator"))) {
				HttpServletRequest request = __inject__(HttpServletRequest.class);
				String uri = __inject__(UniformResourceIdentifierStringBuilder.class).setRequest(request)
						.setPath(StringUtils.replace(DOWNLOAD_UNIFORM_RESOURCE_FORMAT, RepresentationEntity.FORMAT_PARAMETER_IDENTIFIER, persistence.getIdentifier()) )
						.execute().getOutput();
				representation.setUniformResourceLocator(uri);				
			}
		}
	}
	
	private static final String DOWNLOAD_UNIFORM_RESOURCE_FORMAT = FileRepresentation.PATH+FileRepresentation.PATH_DOWNLOAD_ONE+"?isinline=true";
	
}

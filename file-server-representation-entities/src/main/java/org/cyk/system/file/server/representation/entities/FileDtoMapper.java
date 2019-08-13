package org.cyk.system.file.server.representation.entities;

import javax.servlet.http.HttpServletRequest;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.object.__static__.representation.Action;
import org.cyk.utility.identifier.resource.UniformResourceIdentifierStringBuilder;
import org.cyk.utility.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class FileDtoMapper extends AbstractMapperSourceDestinationImpl<FileDto, File> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenGetSourceAfter__(File destination, FileDto source) {
		super.__listenGetSourceAfter__(destination, source);
		source.add__download__(DependencyInjection.inject(UniformResourceIdentifierStringBuilder.class)
				.setRequest(DependencyInjection.inject(HttpServletRequest.class))
				.setPath(String.format(DOWNLOAD_UNIFORM_RESOURCE_FORMAT, destination.getIdentifier(),Boolean.TRUE) ).execute().getOutput(),Action.METHOD_GET);
	}
	
	public static String DOWNLOAD_UNIFORM_RESOURCE_FORMAT;
}

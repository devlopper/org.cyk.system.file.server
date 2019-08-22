package org.cyk.system.file.server.representation.entities;

import javax.servlet.http.HttpServletRequest;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.object.__static__.representation.Action;
import org.cyk.utility.server.representation.AbstractMapperSourceDestinationImpl;
import org.cyk.utility.string.Strings;
import org.mapstruct.Mapper;

@Mapper
public abstract class FileDtoMapper extends AbstractMapperSourceDestinationImpl<FileDto, File> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Strings __getActionsIdentifiers__(File destination, FileDto source) {
		Strings identifiers = super.__getActionsIdentifiers__(destination, source);
		if(identifiers == null)
			identifiers = DependencyInjection.inject(Strings.class);
		identifiers.add(Action.IDENTIFIER_DOWNLOAD);
		return identifiers;
	}
	
	@Override
	protected String __getActionMethod__(String actionIdentifier, File destination, FileDto source) {
		if(Action.IDENTIFIER_DOWNLOAD.equals(actionIdentifier))
			return Action.METHOD_GET;
		return super.__getActionMethod__(actionIdentifier, destination, source);
	}
	
	@Override
	protected String __getPathFormat__(String actionIdentifier, File destination, FileDto source) {
		if(Action.IDENTIFIER_DOWNLOAD.equals(actionIdentifier))
			return DOWNLOAD_PATH_FORMAT;
		return super.__getPathFormat__(actionIdentifier, destination, source);
	}
	
	@Override
	protected Object[] __getPathFormatParameters__(String actionIdentifier, HttpServletRequest request, File destination, FileDto source) {
		if(Action.IDENTIFIER_DOWNLOAD.equals(actionIdentifier))
			return new Object[] {__resourcePath__,source.getIdentifier(),Boolean.TRUE};
		return super.__getPathFormatParameters__(actionIdentifier, request, destination, source);
	}
	
	public static String DOWNLOAD_PATH_FORMAT;
}

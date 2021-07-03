package org.cyk.system.file.server.representation.impl.openapi;

import java.io.Serializable;

import javax.ws.rs.core.Response;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.representation.Arguments;
import org.cyk.utility.representation.EntityReader;

public class FileOpenAPIImpl extends AbstractOpenAPIImpl implements FileOpenAPI,Serializable {

	public static final String PATH = "open/file";
	
	@Override
	public Response import_() {
		return __inject__(FileRepresentation.class).import_();
	}
	
	@Override
	public Response get() {
		Arguments arguments = new Arguments().setRepresentationEntityClass(FileDto.class).setPersistenceEntityClass(File.class);
		arguments.getQueryExecutorArguments(Boolean.TRUE).setQueryIdentifier(FileQuerier.QUERY_IDENTIFIER_READ_DYNAMIC);
		return EntityReader.getInstance().read(arguments);
	}
}
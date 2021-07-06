package org.cyk.system.file.server.representation.impl.openapi;

import java.io.Serializable;
import java.util.List;

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
	public Response import_(List<String> pathsNames,String acceptedPathNameRegularExpression) {
		return __inject__(FileRepresentation.class).import_(pathsNames, acceptedPathNameRegularExpression);
	}
	
	@Override
	public Response get(String filterAsString,Integer firstTupleIndex,Integer numberOfTuples) {
		Arguments arguments = new Arguments().setRepresentationEntityClass(FileDto.class).setPersistenceEntityClass(File.class);
		arguments.getQueryExecutorArguments(Boolean.TRUE).setQueryIdentifier(FileQuerier.QUERY_IDENTIFIER_READ_DYNAMIC);		
		arguments.getQueryExecutorArguments(Boolean.TRUE).addFilterFieldsValues(FileQuerier.PARAMETER_NAME_NAME,filterAsString);
		arguments.getQueryExecutorArguments(Boolean.TRUE).setFirstTupleIndex(firstTupleIndex).setNumberOfTuples(numberOfTuples);
		arguments.getResponseBuilderArguments(Boolean.TRUE).setHeadersCORS();
		return EntityReader.getInstance().read(arguments);
	}
}
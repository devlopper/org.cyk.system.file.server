package org.cyk.system.file.server.representation.impl.openapi;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.core.Response;

import org.cyk.system.file.server.representation.api.FileRepresentation;

public class FileOpenAPIImpl extends AbstractOpenAPIImpl implements FileOpenAPI,Serializable {

	public static final String PATH = "open/file";
	
	@Override
	public Response import_(List<String> pathsNames,String acceptedPathNameRegularExpression) {
		return __inject__(FileRepresentation.class).import_(pathsNames, acceptedPathNameRegularExpression);
	}
	
	@Override
	public Response get(String filterAsString,Boolean countable,Integer firstTupleIndex,Integer numberOfTuples) {
		return __inject__(FileRepresentation.class).get(filterAsString, countable, firstTupleIndex, numberOfTuples);
	}
	
	@Override
	public Response getInfos(String identifier) {
		return __inject__(FileRepresentation.class).getInfos(identifier);
	}
	
	@Override
	public Response download(String identifier, Boolean isInline) {
		return __inject__(FileRepresentation.class).download(identifier, isInline);
	}
	
	@Override
	public Response extractBytesOfAll() {
		return __inject__(FileRepresentation.class).extractBytesOfAll();
	}
	
	@Override
	public Response extractBytes(List<String> identifiers) {
		return __inject__(FileRepresentation.class).extractBytes(identifiers);
	}

	@Override
	public Response count(String filterAsString) {
		return __inject__(FileRepresentation.class).count(filterAsString);
	}

	@Override
	public Response countInDirectory() {
		return __inject__(FileRepresentation.class).countInDirectory();
	}
}
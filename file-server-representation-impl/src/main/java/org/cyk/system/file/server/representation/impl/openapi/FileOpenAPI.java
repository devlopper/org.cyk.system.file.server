package org.cyk.system.file.server.representation.impl.openapi;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.utility.representation.EntityReader;
import org.cyk.utility.representation.server.OpenAPI;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path(FileOpenAPI.PATH)
@Tag(name = "File")
public interface FileOpenAPI extends OpenAPI {
	
	public static final String PATH = "open/file";
	
	String OPERATION_IMPORT = "import";
	@POST
	@Path(OPERATION_IMPORT)
	@Produces({MediaType.TEXT_PLAIN})
	@Operation(description = "Import files into database from sources",operationId = "import_files")
	@APIResponses(value = {
			@APIResponse(description = "Files imported",responseCode = "201", content = @Content(mediaType = MediaType.TEXT_PLAIN))
			,@APIResponse(description = "Error while importing files",responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON))
	})
	Response import_(
			@Parameter(name = "Paths names") 
			@QueryParam(FileRepresentation.PARAMETER_PATHS_NAMES) List<String> pathsNames
			,@Parameter(name = "Accepted path name regular expression") 
			@QueryParam(FileRepresentation.PARAMETER_ACCEPTED_PATH_NAME_REGULAR_EXPRESSION) String acceptedPathNameRegularExpression);
	
	String OPERATION_GET = "get";
	@GET
	@Path(OPERATION_GET)
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(description = "Get files",operationId = "get_files")
	@APIResponses(value = {
			@APIResponse(description = "Files got",responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON))
			,@APIResponse(description = "Error while getting files",responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON))
	})
	Response get(
			@Parameter(name = "Filter as string")
			@QueryParam(EntityReader.PARAMETER_NAME_FILTER_AS_STRING) String filterAsString
			,@Parameter(name = "First tuple index")
			@QueryParam(EntityReader.PARAMETER_NAME_FIRST_TUPLE_INDEX) Integer firstTupleIndex
			,@Parameter(name = "Number of tuples")
			@QueryParam(EntityReader.PARAMETER_NAME_NUMBER_OF_TUPLES) Integer numberOfTuples
			);
	
	/**/	
}
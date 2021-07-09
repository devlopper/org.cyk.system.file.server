package org.cyk.system.file.server.representation.impl.openapi;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
			@Parameter(name = FileRepresentation.PARAMETER_PATHS_NAMES) 
			@QueryParam(FileRepresentation.PARAMETER_PATHS_NAMES) List<String> pathsNames
			,@Parameter(name = FileRepresentation.PARAMETER_ACCEPTED_PATH_NAME_REGULAR_EXPRESSION) 
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
			@Parameter(name = EntityReader.PARAMETER_NAME_FILTER_AS_STRING,description = "Filter")
			@QueryParam(EntityReader.PARAMETER_NAME_FILTER_AS_STRING) String filterAsString
			,@Parameter(name = EntityReader.PARAMETER_NAME_COUNTABLE,description = "Countable")
			@QueryParam(EntityReader.PARAMETER_NAME_COUNTABLE) Boolean countable
			,@Parameter(name = EntityReader.PARAMETER_NAME_FIRST_TUPLE_INDEX,description = "First file index")
			@QueryParam(EntityReader.PARAMETER_NAME_FIRST_TUPLE_INDEX) Integer firstTupleIndex
			,@Parameter(name = EntityReader.PARAMETER_NAME_NUMBER_OF_TUPLES,description = "Number of files")
			@QueryParam(EntityReader.PARAMETER_NAME_NUMBER_OF_TUPLES) Integer numberOfTuples
			);
	
	String OPERATION_COUNT = "count";
	@GET
	@Path(OPERATION_COUNT)
	@Produces({MediaType.TEXT_PLAIN})
	@Operation(description = "Count files",operationId = "count_files")
	Response count(@QueryParam(EntityReader.PARAMETER_NAME_FILTER_AS_STRING) String filterAsString);
	
	String OPERATION_COUNT_IN_DIRECTORY = "countindirectory";
	@GET
	@Path(OPERATION_COUNT_IN_DIRECTORY)
	@Produces({MediaType.TEXT_PLAIN})
	@Operation(description = "Count files in directory",operationId = "count_files_in_directory")
	Response countInDirectory();
	
	String OPERATION_DOWNLOAD = "{identifier}/download";
	@GET
	@Path(OPERATION_DOWNLOAD)
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	@Operation(description = "Download file",operationId = "download_file")
	@APIResponses(value = {
			@APIResponse(description = "File downloaded",responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM))
			,@APIResponse(description = "File not found",responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
			,@APIResponse(description = "Error while downloading file",responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON))
	})
	Response download(@PathParam(FileRepresentation.PARAMETER_IDENTIFIER) String identifier,@QueryParam(FileRepresentation.PARAMETER_IS_INLINE) Boolean isInline);
	
	String OPERATION_GET_INFOS = "{identifier}/getinfos";
	@HEAD
	@Path(OPERATION_GET_INFOS)
	@Operation(description = "Get file infos",operationId = "get_file_infos")
	@APIResponses(value = {
			@APIResponse(description = "File infos got",responseCode = "200")
			,@APIResponse(description = "File not found",responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
			,@APIResponse(description = "Error while getting file infos",responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON))
	})
	Response getInfos(@PathParam(FileRepresentation.PARAMETER_IDENTIFIER) String identifier);
	
	String OPERATION_EXTRACT_BYTES_OF_ALL = "extractbytesofall";
	@POST
	@Path(OPERATION_EXTRACT_BYTES_OF_ALL)
	@Produces({ MediaType.TEXT_PLAIN})
	@Operation(description = "Extract bytes of all file",operationId = "extract_bytes_of_all_files")
	@APIResponses(value = {
			@APIResponse(description = "Bytes of all files extracted",responseCode = "200", content = @Content(mediaType = MediaType.TEXT_PLAIN))
			,@APIResponse(description = "Error while extracting bytes of all files",responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON))
	})
	Response extractBytesOfAll();
	
	String OPERATION_EXTRACT_BYTES = "{identifier}/extractbytes";
	@POST
	@Path(OPERATION_EXTRACT_BYTES)
	@Produces({ MediaType.TEXT_PLAIN})
	@Operation(description = "Extract bytes of files",operationId = "extract_bytes_of_files")
	@APIResponses(value = {
			@APIResponse(description = "Bytes of files extracted",responseCode = "200", content = @Content(mediaType = MediaType.TEXT_PLAIN))
			,@APIResponse(description = "Error while extracting bytes of files",responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON))
	})
	Response extractBytes(
			@Parameter(name = FileRepresentation.PARAMETER_IDENTIFIERS) 
			@QueryParam(FileRepresentation.PARAMETER_IDENTIFIERS) List<String> identifiers
		);
	
	/**/	
}
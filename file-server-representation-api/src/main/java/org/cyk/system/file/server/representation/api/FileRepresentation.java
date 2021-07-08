package org.cyk.system.file.server.representation.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.__kernel__.constant.ConstantString;
import org.cyk.utility.representation.EntityReader;
import org.cyk.utility.representation.SpecificRepresentation;

@Path(FileRepresentation.PATH)
public interface FileRepresentation extends SpecificRepresentation<FileDto> {
	
	String PATH = "file";
	
	String PATH_GET = "get";
	@GET
	@Path(PATH_GET)
	@Produces({MediaType.APPLICATION_JSON})
	Response get(
			@QueryParam(EntityReader.PARAMETER_NAME_FILTER_AS_STRING) String filterAsString
			,@QueryParam(EntityReader.PARAMETER_NAME_COUNTABLE) Boolean countable
			,@QueryParam(EntityReader.PARAMETER_NAME_FIRST_TUPLE_INDEX) Integer firstTupleIndex
			,@QueryParam(EntityReader.PARAMETER_NAME_NUMBER_OF_TUPLES) Integer numberOfTuples
			);
	
	String PATH_IMPORT = "import";
	@POST
	@Path(PATH_IMPORT)
	@Produces({ MediaType.APPLICATION_JSON})
	Response import_(@QueryParam(PARAMETER_PATHS_NAMES) List<String> pathsNames
			,@QueryParam(PARAMETER_ACCEPTED_PATH_NAME_REGULAR_EXPRESSION) String acceptedPathNameRegularExpression);
	
	String PATH_EXTRACT_BYTES_OF_ALL = "extractbytesofall";
	@POST
	@Path(PATH_EXTRACT_BYTES_OF_ALL)
	@Produces({ MediaType.TEXT_PLAIN})
	Response extractBytesOfAll();
	
	String PATH_EXTRACT_BYTES = "{identifier}/extractbytes";
	@POST
	@Path(PATH_EXTRACT_BYTES)
	@Produces({ MediaType.TEXT_PLAIN})
	Response extractBytes(@QueryParam(PARAMETER_IDENTIFIERS) List<String> identifiers);
	
	String PATH_DOWNLOAD_ONE = "{identifier}/"+ConstantString.DOWNLOAD;
	@GET
	@Path(PATH_DOWNLOAD_ONE)
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	Response download(@PathParam(PARAMETER_IDENTIFIER) String identifier,@QueryParam(PARAMETER_IS_INLINE) Boolean isInline);
	
	/*
	String PATH_CREATE_FROM_DIRECTORIES = "create_from_directories";
	@POST
	@Path(PATH_CREATE_FROM_DIRECTORIES)
	@Produces({ MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML })
	Response createFromDirectories(@QueryParam(PARAMETER_DIRECTORY) List<String> directories,@QueryParam(PARAMETER_EXTENSION) List<String> extensions
			,@QueryParam(PARAMETER_SIZE) List<String> sizes,@QueryParam(PARAMETER_BATCH_SIZE) Integer batchSize,@QueryParam(PARAMETER_COUNT) Integer count);
	*/
	/*
	 * Upload is not yet standard. For now we will use specific application server functionality
	 */
	/*
	@POST
	@Path(PATH_UPLOAD)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	Response upload();
	*/
	/*
	@GET
	@Path(PATH_GET_MANY_BY_GLOBAL_FILTER)
	@Produces({ MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML })
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	Response getManyByGlobalFilter(@QueryParam(PARAMETER_IS_PAGEABLE) Boolean isPageable,@QueryParam(PARAMETER_FROM) Long from,@QueryParam(PARAMETER_COUNT) Long count
			,@QueryParam(PARAMETER_FIELDS) String fields,@PathParam("__filter__") String globalFilter,@QueryParam("loggableAsInfo")Boolean loggableAsInfo);
	*/
	
	/**/

	//String PATH = "file";
	
	String PATH_UPLOAD = ConstantString.UPLOAD;
	String PATH_UPLOAD__ = "/upload";
	String PATH_GET_MANY_BY_GLOBAL_FILTER = "/filter/{__filter__}";
	
	String PARAMETER_PATHS_NAMES = "paths";
	String PARAMETER_ACCEPTED_PATH_NAME_REGULAR_EXPRESSION = "accepted";
}
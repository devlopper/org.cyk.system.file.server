package org.cyk.system.file.server.representation.impl.openapi;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path(FileOpenAPI.PATH)
@Tag(name = "File")
public interface FileOpenAPI {
	
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
	Response import_();
	
	String OPERATION_GET = "get";
	@GET
	@Path(OPERATION_GET)
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(description = "Get files",operationId = "get_files")
	@APIResponses(value = {
			@APIResponse(description = "Files got",responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON))
			,@APIResponse(description = "Error while getting file list",responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON))
	})
	Response get();
	
	/**/
	
	public static final String PARAMETER_FILE_IDENTIFIER = "file";
	
}
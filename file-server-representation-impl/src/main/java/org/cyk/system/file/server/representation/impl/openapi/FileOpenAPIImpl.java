package org.cyk.system.file.server.representation.impl.openapi;

import java.io.Serializable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path(FileOpenAPIImpl.PATH)
@Tag(name = "File")
public class FileOpenAPIImpl extends AbstractOpenAPIImpl implements Serializable {

	public static final String PATH = "open/file";
	
	public static final String OPERATION_GET_LIST = "get_list";
	@GET
	@Path(OPERATION_GET_LIST)
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(description = "Get file list",operationId = "get_file_list")
	@APIResponses(value = {
			@APIResponse(description = "File list got",responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON))
			,@APIResponse(description = "Error while getting file list",responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON))
	})
	public Response getList() {		
		return null;
	}
	
	/**/
	
	public static final String PARAMETER_FILE_IDENTIFIER = "fichier";
}
package org.cyk.system.file.server.representation.api;

import java.util.List;

import javax.ws.rs.Consumes;
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
import org.cyk.utility.server.representation.RepresentationEntity;

@Path(FileRepresentation.PATH)
public interface FileRepresentation extends RepresentationEntity<FileDto> {
	
	@POST
	@Path(PATH_CREATE_FROM_DIRECTORIES)
	@Produces({ MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML })
	Response createFromDirectories(@QueryParam(PARAMETER_DIRECTORY) List<String> directories,@QueryParam(PARAMETER_EXTENSION) List<String> extensions
			,@QueryParam(PARAMETER_SIZE) List<String> sizes,@QueryParam(PARAMETER_BATCH_SIZE) Integer batchSize,@QueryParam(PARAMETER_COUNT) Integer count);
	
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
	
	@GET
	@Path(PATH_GET_MANY_BY_GLOBAL_FILTER)
	@Produces({ MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML })
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	Response getManyByGlobalFilter(@QueryParam(PARAMETER_IS_PAGEABLE) Boolean isPageable,@QueryParam(PARAMETER_FROM) Long from,@QueryParam(PARAMETER_COUNT) Long count
			,@QueryParam(PARAMETER_FIELDS) String fields,@PathParam("__filter__") String globalFilter);
	
	@GET
	@Path(PATH_DOWNLOAD_ONE)
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	Response download(@PathParam(PARAMETER_IDENTIFIER) String identifier,@QueryParam(PARAMETER_IS_INLINE) String isInline);
	
	/**/
	
	String PATH_CREATE_FROM_DIRECTORIES = PATH_ROOT+ConstantString.CREATE+__SLASH__+ConstantString.FROM+__SLASH__+ConstantString.DIRECTORIES;
	
	String PATH = "file";
	String PATH_UPLOAD = ConstantString.UPLOAD;
	String PATH_UPLOAD__ = PATH+__SLASH__+PATH_UPLOAD;
	String PATH_DOWNLOAD_ONE = PATH_IDENTIFIER+__SLASH__+ConstantString.DOWNLOAD;
	String PATH_GET_MANY_BY_GLOBAL_FILTER = "/filter/{__filter__}";
	
}

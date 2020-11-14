package org.cyk.system.file.server.representation.impl;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantString;
import org.cyk.utility.__kernel__.file.FileHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.representation.Arguments;
import org.cyk.utility.__kernel__.representation.EntityReader;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.string.Strings;
import org.cyk.utility.number.Intervals;
import org.cyk.utility.server.representation.AbstractRepresentationEntityImpl;

@ApplicationScoped
public class FileRepresentationImpl extends AbstractRepresentationEntityImpl<FileDto> implements FileRepresentation,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Response createFromDirectories(List<String> directories,/*List<String> mimeTypeTypes,List<String> mimeTypeSubTypes,List<String> mimeTypes,*/List<String> extensions
			,List<String> sizes,Integer batchSize,Integer count) {
		Intervals intervals = null;
		if(CollectionHelper.isNotEmpty(sizes)) {
			intervals = __inject__(Intervals.class);
			for(String index : sizes) {
				String[] extremities = index.split(";");
				if(extremities.length == 2) {
					intervals.add(NumberHelper.getInteger(extremities[0]), NumberHelper.getInteger(extremities[1]));
				}
			}
		}
		__inject__(FileBusiness.class).createFromDirectories(__inject__(Strings.class).add(directories),null,null,null
				,__inject__(Strings.class).add(extensions),intervals,batchSize,count == null || count == 0 ? null : count);
		return Response.ok("Files has been created from directories").build();
	}
	
	@Override
	public Response getManyByGlobalFilter(Boolean isPageable, Long from, Long count, String fields,String globalFilter,Boolean loggableAsInfo) {
		Arguments arguments = new Arguments().setRepresentationEntityClass(FileDto.class);
		arguments.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(FileQuerier.QUERY_IDENTIFIER_READ_VIEW_01)
				.addFilterField(File.FIELD_NAME, globalFilter)
				.setFirstTupleIndex(NumberHelper.getInteger(from))
				.setNumberOfTuples(NumberHelper.getInteger(count))
				).setCountable(Boolean.TRUE).setLoggableAsInfo(loggableAsInfo);
		return EntityReader.getInstance().read(arguments);
		//return getMany(null,isPageable, from, count, fields, new Filter.Dto().setValue(globalFilter));
	}
	
	@Override
	public Response download(String identifier,String isInline) {
		File file = __inject__(FileBusiness.class).findBySystemIdentifier(identifier);
		if(file == null)
			return Response.status(Status.NOT_FOUND).build();
		byte[] bytes = null;
		if(StringHelper.isBlank(file.getUniformResourceLocator())) {
			FileBytes fileBytes = __inject__(FileBytesBusiness.class).findByFile(file);
			bytes = fileBytes.getBytes();
		}else {
			try {
				URI uri = new URI(file.getUniformResourceLocator());
				bytes = IOUtils.toByteArray(uri.toURL());
			} catch (Exception exception) {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception.toString()).build();
			}
		}
		ResponseBuilder response = Response.ok(bytes);
	    response.header(HttpHeaders.CONTENT_TYPE, file.getMimeType());
	    String name = FileHelper.concatenateNameAndExtension(file.getName(), file.getExtension());
	    response.header(HttpHeaders.CONTENT_DISPOSITION, (Boolean.parseBoolean(isInline) ? ConstantString.INLINE : ConstantString.ATTACHMENT)+"; "+ConstantString.FILENAME
	    		+"="+name);
	    Long size = file.getSize();
	    if(size!=null && size > 0)
	    	response.header(HttpHeaders.CONTENT_LENGTH, size);
	    return response.build();
	}	
}
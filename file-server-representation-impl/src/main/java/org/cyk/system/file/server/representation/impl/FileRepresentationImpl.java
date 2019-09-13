package org.cyk.system.file.server.representation.impl;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.system.file.server.representation.entities.FileDtoCollection;
import org.cyk.utility.__kernel__.constant.ConstantString;
import org.cyk.utility.number.Intervals;
import org.cyk.utility.number.NumberHelper;
import org.cyk.utility.server.persistence.query.filter.FilterDto;
import org.cyk.utility.server.representation.AbstractRepresentationEntityImpl;
import org.cyk.utility.string.Strings;

@ApplicationScoped
public class FileRepresentationImpl extends AbstractRepresentationEntityImpl<File,FileBusiness,FileDto,FileDtoCollection> implements FileRepresentation,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Response createFromDirectories(List<String> directories,/*List<String> mimeTypeTypes,List<String> mimeTypeSubTypes,List<String> mimeTypes,*/List<String> extensions
			,List<String> sizes,Integer batchSize,Integer count) {
		Intervals intervals = null;
		if(__injectCollectionHelper__().isNotEmpty(sizes)) {
			intervals = __inject__(Intervals.class);
			for(String index : sizes) {
				String[] extremities = index.split(";");
				if(extremities.length == 2) {
					intervals.add(__inject__(NumberHelper.class).getInteger(extremities[0]), __inject__(NumberHelper.class).getInteger(extremities[1]));
				}
			}
		}
		__inject__(FileBusiness.class).createFromDirectories(__inject__(Strings.class).add(directories),null,null,null
				,__inject__(Strings.class).add(extensions),intervals,batchSize,count == null || count == 0 ? null : count);
		return Response.ok("Files has been created from directories").build();
	}
	
	@Override
	public Response getManyByGlobalFilter(Boolean isPageable, Long from, Long count, String fields,String globalFilter) {
		return getMany(isPageable, from, count, fields, new FilterDto().setValue(globalFilter));
	}
	
	@Override
	public Response download(String identifier,String isInline) {
		File file = __inject__(FileBusiness.class).findBySystemIdentifier(identifier);
		FileBytes fileBytes = __inject__(FileBytesBusiness.class).findByFile(file);
	    ResponseBuilder response = Response.ok(fileBytes.getBytes());
	    response.header(HttpHeaders.CONTENT_TYPE, file.getMimeType());
	    response.header(HttpHeaders.CONTENT_DISPOSITION, (Boolean.parseBoolean(isInline) ? ConstantString.INLINE : ConstantString.ATTACHMENT)+"; "+ConstantString.FILENAME
	    		+"="+file.getNameAndExtension());
	    Long size = file.getSize();
	    if(size!=null && size > 0)
	    	response.header(HttpHeaders.CONTENT_LENGTH, size);
	    return response.build();
	}
	
}

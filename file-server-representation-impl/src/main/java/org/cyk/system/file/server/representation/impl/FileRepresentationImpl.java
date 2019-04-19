package org.cyk.system.file.server.representation.impl;

import java.io.Serializable;

import javax.inject.Singleton;
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
import org.cyk.utility.server.representation.AbstractRepresentationEntityImpl;

@Singleton
public class FileRepresentationImpl extends AbstractRepresentationEntityImpl<File,FileBusiness,FileDto,FileDtoCollection> implements FileRepresentation,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Response download(String identifier,String isInline) {
		File file = __inject__(FileBusiness.class).findOneBySystemIdentifier(identifier);
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
	
	@Override
	public Class<File> getPersistenceEntityClass() {
		return File.class;
	}
	
}

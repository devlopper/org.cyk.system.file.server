package org.cyk.system.file.server.representation.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.__kernel__.constant.ConstantString;
import org.cyk.utility.__kernel__.object.dynamic.AbstractObject;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.map.MapHelper;
import org.cyk.utility.server.representation.Representation;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartInput;

@Path(FileRepresentation.PATH_UPLOAD__)
public class FileUploaderRestEasy extends AbstractObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@POST
	@Path(Representation.__SLASH__)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Response upload(MultipartInput multipartInput) {
		Response response = null;
		Collection<FileDto> fileDtos = null;
		for(InputPart inputPart : multipartInput.getParts()) {
			@SuppressWarnings("unchecked")
			String contentDisposition = (String) CollectionHelper.getFirst(__inject__(MapHelper.class).get(inputPart.getHeaders(), List.class, "Content-Disposition"));
			String nameAndExtension = StringUtils.substringBetween(contentDisposition, ConstantString.FILENAME+"=\"","\"");
			String mimeType = (String) CollectionHelper.getFirst(inputPart.getHeaders().get(HttpHeaders.CONTENT_TYPE));
			if(StringHelper.isNotBlank(nameAndExtension)) {
				//There is a file to upload
				try {
					if(fileDtos == null)
						fileDtos = new ArrayList<>();
					fileDtos.add(new FileDto().setNameAndExtension(nameAndExtension).setMimeType(mimeType).setBytes(inputPart.getBody(byte[].class, null)));
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(CollectionHelper.isNotEmpty(fileDtos))
			response = __inject__(FileRepresentation.class).createMany(fileDtos,null);
		return response;
	}
	
}

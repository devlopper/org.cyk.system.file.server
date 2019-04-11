package org.cyk.system.file.server.representation.api;

import javax.ws.rs.Path;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.system.file.server.representation.entities.FileDtoCollection;
import org.cyk.utility.server.representation.RepresentationEntity;

@Path(FileRepresentation.PATH)
public interface FileRepresentation extends RepresentationEntity<File,FileDto,FileDtoCollection> {
	
	String PATH = "/file";
	
}

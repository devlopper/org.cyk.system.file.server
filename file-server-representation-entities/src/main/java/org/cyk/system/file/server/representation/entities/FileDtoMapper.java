package org.cyk.system.file.server.representation.entities;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class FileDtoMapper extends AbstractMapperSourceDestinationImpl<FileDto, File> {
	private static final long serialVersionUID = 1L;

}

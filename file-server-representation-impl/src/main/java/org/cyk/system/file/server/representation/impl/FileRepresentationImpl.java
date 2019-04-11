package org.cyk.system.file.server.representation.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.system.file.server.representation.entities.FileDtoCollection;
import org.cyk.utility.server.representation.AbstractRepresentationEntityImpl;

@Singleton
public class FileRepresentationImpl extends AbstractRepresentationEntityImpl<File,FileBusiness,FileDto,FileDtoCollection> implements FileRepresentation,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Class<File> getPersistenceEntityClass() {
		return File.class;
	}
	
}

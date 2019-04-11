package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.business.AbstractBusinessEntityImpl;

@Singleton
public class FileBusinessImpl extends AbstractBusinessEntityImpl<File, FilePersistence> implements FileBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<File> __getPersistenceEntityClass__() {
		return File.class;
	}
	
}

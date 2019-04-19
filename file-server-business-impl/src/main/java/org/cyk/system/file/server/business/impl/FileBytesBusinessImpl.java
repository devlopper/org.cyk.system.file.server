package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.server.business.AbstractBusinessEntityImpl;

@Singleton
public class FileBytesBusinessImpl extends AbstractBusinessEntityImpl<FileBytes, FileBytesPersistence> implements FileBytesBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public FileBytes findByFile(File file) {
		return getPersistence().readByFile(file);
	}
	
	@Override
	protected Class<FileBytes> __getPersistenceEntityClass__() {
		return FileBytes.class;
	}

}

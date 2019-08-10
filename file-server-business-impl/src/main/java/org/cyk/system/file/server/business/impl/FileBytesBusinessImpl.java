package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.server.business.AbstractBusinessEntityImpl;

@ApplicationScoped
public class FileBytesBusinessImpl extends AbstractBusinessEntityImpl<FileBytes, FileBytesPersistence> implements FileBytesBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public FileBytes findByFile(File file) {
		return __persistence__.readByFile(file);
	}
	
}

package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.entities.FileBytes;

@ApplicationScoped
public class FileBytesPersistenceImpl extends AbstractFileDetailPersistenceImpl<FileBytes> implements FileBytesPersistence,Serializable {
	private static final long serialVersionUID = 1L;

	
}

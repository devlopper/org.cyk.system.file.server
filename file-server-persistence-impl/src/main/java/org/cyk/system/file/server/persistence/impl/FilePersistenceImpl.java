package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;

@Singleton
public class FilePersistenceImpl extends AbstractPersistenceEntityImpl<File> implements FilePersistence,Serializable {
	private static final long serialVersionUID = 1L;

}

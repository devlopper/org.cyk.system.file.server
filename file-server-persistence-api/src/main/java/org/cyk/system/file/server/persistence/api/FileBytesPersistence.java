package org.cyk.system.file.server.persistence.api;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.server.persistence.PersistenceEntity;

public interface FileBytesPersistence extends PersistenceEntity<FileBytes> {

	FileBytes readByFile(File file);
	
}

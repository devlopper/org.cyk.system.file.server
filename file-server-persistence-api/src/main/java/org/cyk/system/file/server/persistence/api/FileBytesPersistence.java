package org.cyk.system.file.server.persistence.api;

import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.server.persistence.PersistenceEntity;

public interface FileBytesPersistence extends PersistenceEntity<FileBytes> {

	Collection<FileBytes> readByFilesIdentifiers(Collection<String> filesIdentifiers);
	Collection<FileBytes> readByFilesIdentifiers(String...filesIdentifiers);
	FileBytes readByFileIdentifier(String fileIdentifier);
	FileBytes readByFile(File file);
	
}

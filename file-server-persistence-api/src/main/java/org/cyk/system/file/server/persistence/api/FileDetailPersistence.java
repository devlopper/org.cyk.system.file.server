package org.cyk.system.file.server.persistence.api;

import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.persistence.PersistenceEntity;

public interface FileDetailPersistence<T> extends PersistenceEntity<T> {

	Collection<T> readByFilesIdentifiers(Collection<String> filesIdentifiers);
	Collection<T> readByFilesIdentifiers(String...filesIdentifiers);
	T readByFileIdentifier(String fileIdentifier);
	
	Collection<T> readByFiles(Collection<File> files);
	Collection<T> readByFiles(File...files);
	T readByFile(File file);
}

package org.cyk.system.file.server.business.api;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.business.SpecificBusiness;

public interface FileBytesBusiness extends SpecificBusiness<FileBytes> {
	
	void createFromFilesIdentifiers(Collection<String> filesIdentifiers);
	
	void createFromFilesIdentifiers(String...filesIdentifiers);
	
	void createFromFiles(Collection<File> files,EntityManager entityManager);
	void createFromFiles(Collection<File> files);
	
	void createFromFiles(File...files);
}
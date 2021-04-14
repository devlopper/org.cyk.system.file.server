package org.cyk.system.file.server.business.api;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.business.SpecificBusiness;
import org.cyk.utility.business.TransactionResult;

public interface FileBytesBusiness extends SpecificBusiness<FileBytes> {
	
	TransactionResult createFromFilesIdentifiers(Collection<String> filesIdentifiers);
	
	TransactionResult createFromFilesIdentifiers(String...filesIdentifiers);
	
	TransactionResult createFromFiles(Collection<File> files,EntityManager entityManager);
	TransactionResult createFromFiles(Collection<File> files);
	
	TransactionResult createFromFiles(File...files);
}
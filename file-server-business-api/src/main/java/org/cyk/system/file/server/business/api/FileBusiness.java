package org.cyk.system.file.server.business.api;

import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.business.SpecificBusiness;
import org.cyk.utility.business.TransactionResult;

public interface FileBusiness extends SpecificBusiness<File> {

	String IMPORT = "IMPORT";
	/**
	 * Get file from some where (the file system, social network and so on) and store it in the database
	 */
	TransactionResult import_(Collection<String> pathsNames,String acceptedPathNameRegularExpression);	
	TransactionResult import_();
	
	String EXTRACT_BYTES = "EXTRACT_BYTES";
	TransactionResult extractBytes(Collection<File> files);
	TransactionResult extractBytes(File...files);
	TransactionResult extractBytesOfAll();
	TransactionResult extractBytesFromIdentifiers(Collection<String> identifiers);
	TransactionResult extractBytesFromIdentifiers(String...identifiers);
	
	String EXTRACT_TEXT = "EXTRACT_TEXT";
	TransactionResult extractText();
	
	String DOWNLOAD = "DOWNLOAD";
	File download(String identifier);
	
	String GET_INFOS = "GET_INFOS";
	File getInfos(String identifier);
}
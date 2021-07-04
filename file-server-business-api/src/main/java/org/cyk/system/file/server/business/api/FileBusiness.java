package org.cyk.system.file.server.business.api;

import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.string.Strings;
import org.cyk.utility.business.SpecificBusiness;
import org.cyk.utility.business.TransactionResult;
import org.cyk.utility.number.Intervals;

public interface FileBusiness extends SpecificBusiness<File> {

	String IMPORT = "IMPORT";
	/**
	 * Get file from some where (the file system, social network and so on) and store it in the database
	 */
	TransactionResult import_(Collection<String> pathsNames,String acceptedPathNameRegularExpression);	
	TransactionResult import_();
	
	String EXTRACT_BYTES = "EXTRACT_BYTES";
	TransactionResult extractBytes();
	
	String EXTRACT_TEXT = "EXTRACT_TEXT";
	TransactionResult extractText();
	
	FileBusiness createFromDirectories(Strings directories,Strings mimeTypeTypes,Strings mimeTypeSubTypes,Strings mimeTypes,Strings extensions,Intervals sizes
			,Integer batchSize,Integer count);
	
	Collection<File> findWhereNameContains(String string);
}
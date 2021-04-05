package org.cyk.system.file.server.business.api;

import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.string.Strings;
import org.cyk.utility.business.SpecificBusiness;
import org.cyk.utility.number.Intervals;

public interface FileBusiness extends SpecificBusiness<File> {

	FileBusiness createFromDirectories(Strings directories,Strings mimeTypeTypes,Strings mimeTypeSubTypes,Strings mimeTypes,Strings extensions,Intervals sizes
			,Integer batchSize,Integer count);
	
	Collection<File> findWhereNameContains(String string);
}

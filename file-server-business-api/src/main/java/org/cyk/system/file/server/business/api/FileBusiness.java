package org.cyk.system.file.server.business.api;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.number.Intervals;
import org.cyk.utility.server.business.BusinessEntity;
import org.cyk.utility.string.Strings;

public interface FileBusiness extends BusinessEntity<File> {

	FileBusiness createFromDirectories(Strings directories,Strings mimeTypeTypes,Strings mimeTypeSubTypes,Strings mimeTypes,Strings extensions,Intervals sizes
			,Integer batchSize,Integer count);
	
	//FileBusiness createFromDirectories(Collection<File> files);
}

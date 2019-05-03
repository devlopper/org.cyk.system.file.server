package org.cyk.system.file.server.business.api;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.business.BusinessEntity;
import org.cyk.utility.string.Strings;

public interface FileBusiness extends BusinessEntity<File> {

	FileBusiness createFromDirectories(Strings directories);
	
}

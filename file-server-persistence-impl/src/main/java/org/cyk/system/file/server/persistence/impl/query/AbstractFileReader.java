package org.cyk.system.file.server.persistence.impl.query;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractFileReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<File> implements Serializable {

	@Override
	protected Class<File> getEntityClass() {
		return File.class;
	}
	
}
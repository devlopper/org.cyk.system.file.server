package org.cyk.system.file.server.persistence.impl.query;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;

public class FileBytesReader extends AbstractFileReader implements Serializable {

	@Override
	protected String getQueryValue() {
		return "SELECT t.identifier,fb.bytes FROM File t LEFT JOIN FileBytes fb ON fb.file = t WHERE t.identifier IN :identifiers";
	}

	@Override
	protected void __set__(File file, Object[] array) {
		Integer index = 1;
		file.setBytes((byte[]) array[index++]);
	}
}
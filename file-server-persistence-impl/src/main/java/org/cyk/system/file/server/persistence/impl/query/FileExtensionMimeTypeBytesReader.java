package org.cyk.system.file.server.persistence.impl.query;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;

public class FileExtensionMimeTypeBytesReader extends AbstractFileReader implements Serializable {

	@Override
	protected String getQueryValue() {
		return String.format("SELECT t.%s,t.%s,t.%s,t.%s,fb.%s FROM File t LEFT JOIN FileBytes fb ON fb.file = t WHERE t.identifier IN :identifiers"
				,File.FIELD_IDENTIFIER,File.FIELD_EXTENSION,File.FIELD_MIME_TYPE,File.FIELD_SIZE,FileBytes.FIELD_BYTES);
	}

	@Override
	protected void __set__(File file, Object[] array) {
		Integer index = 0;
		file.setIdentifier((String)array[index++]);
		file.setExtension((String)array[index++]);
		file.setMimeType((String)array[index++]);
		file.setSize((Long)array[index++]);
		file.setBytes((byte[]) array[index++]);
	}
}
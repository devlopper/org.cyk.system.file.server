package org.cyk.system.file.server.persistence.impl.query;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.file.FileHelper;

public class FileNameAndExtensionReader extends AbstractFileReader implements Serializable {

	@Override
	protected String getQueryValue() {
		return "SELECT t.identifier,t.name,t.extension FROM File t WHERE t.identifier IN :identifiers";
	}

	@Override
	protected void __set__(File file, Object[] array) {
		super.__set__(file, array);
		Integer index = 1;
		file.setNameAndExtension(FileHelper.concatenateNameAndExtension((String)array[index++], (String)array[index++]));
	}
}
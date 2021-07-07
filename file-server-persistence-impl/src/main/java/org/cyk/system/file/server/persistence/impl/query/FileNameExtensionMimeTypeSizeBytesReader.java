package org.cyk.system.file.server.persistence.impl.query;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.persistence.query.Querier;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class FileNameExtensionMimeTypeSizeBytesReader extends AbstractFileReader implements Serializable {

	@Override
	protected String getQueryValue() {
		QueryStringBuilder.Arguments arguments = new QueryStringBuilder.Arguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",File.FIELD_IDENTIFIER,File.FIELD_NAME,File.FIELD_EXTENSION,File.FIELD_MIME_TYPE,File.FIELD_SIZE);
		arguments.getProjection(Boolean.TRUE).add("fb.bytes");
		arguments.getTuple(Boolean.TRUE).add("File t").addJoins("LEFT JOIN FileBytes fb ON fb.file = t");
		arguments.getPredicate(Boolean.TRUE).add("t.identifier IN :"+Querier.PARAMETER_NAME_IDENTIFIERS);
		return QueryStringBuilder.getInstance().build(arguments);
	}

	@Override
	protected void __set__(File file, Object[] array) {
		Integer index = 0;
		file.setIdentifier((String)array[index++]);
		file.setName((String)array[index++]);
		file.setExtension((String)array[index++]);
		file.setMimeType((String)array[index++]);
		file.setSize((Long)array[index++]);
		file.setBytes((byte[]) array[index++]);
	}
}
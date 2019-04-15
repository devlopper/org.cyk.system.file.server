package org.cyk.system.file.server.representation.impl;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.__kernel__.annotation.Default;
import org.cyk.utility.field.AbstractFieldValueCopyFieldsGetterImpl;
import org.cyk.utility.field.Fields;

@Default
public class FieldValueCopyFieldsGetterImpl extends AbstractFieldValueCopyFieldsGetterImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected Fields __execute__() throws Exception {
		Fields fields = super.__execute__();
		if(File.class.equals(getSourceClass()) && FileDto.class.equals(getDestinationClass())) {
			fields.removeByNames(File.FIELD_BYTES);
		}			
		return fields;
	}
	
}
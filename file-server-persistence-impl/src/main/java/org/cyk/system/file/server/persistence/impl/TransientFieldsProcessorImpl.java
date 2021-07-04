package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.impl.query.FileNameAndExtensionReader;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.query.Filter;

@org.cyk.system.file.server.annotation.System
public class TransientFieldsProcessorImpl extends org.cyk.utility.persistence.server.TransientFieldsProcessorImpl implements Serializable {

	@Override
	protected void __process__(Class<?> klass,Collection<?> objects,Filter filter, Collection<String> fieldsNames) {
		if(File.class.equals(klass))
			processFiles(CollectionHelper.cast(File.class, objects),fieldsNames);
		else
			super.__process__(klass,objects,filter, fieldsNames);
	}
	
	public void processFiles(Collection<File> files,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(File.FIELD_NAME_AND_EXTENSION.equals(fieldName))
				new FileNameAndExtensionReader().readThenSet(files, null);
		}		
	}
}
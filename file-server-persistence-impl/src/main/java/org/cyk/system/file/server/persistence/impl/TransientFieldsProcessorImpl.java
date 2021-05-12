package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.file.FileHelper;
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
			if(File.FIELD_NAME_AND_EXTENSION.equals(fieldName)) {
				Collection<Object[]> arrays = FileQuerier.getInstance().readNamesAndExtensions(files);
				if(CollectionHelper.isNotEmpty(arrays)) {
					for(File file : files) {
						for(Object[] array : arrays) {
							if(array[0].equals(file.getIdentifier()))
								file.setNameAndExtension(FileHelper.concatenateNameAndExtension((String)array[1], (String)array[2]));
						}
					}
				}
			}
		}		
	}
}
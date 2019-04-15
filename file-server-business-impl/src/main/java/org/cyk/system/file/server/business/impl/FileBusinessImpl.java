package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.server.business.AbstractBusinessEntityImpl;
import org.cyk.utility.server.business.BusinessFunctionCreator;

@Singleton
public class FileBusinessImpl extends AbstractBusinessEntityImpl<File, FilePersistence> implements FileBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenExecuteCreateOneBefore__(File file, Properties properties,BusinessFunctionCreator function) {
		super.__listenExecuteCreateOneBefore__(file, properties, function);
		function.try_().begin().addRunnables(new Runnable() {
			@Override
			public void run() {
				String nameAndExtension = file.getNameAndExtension();
				String extension = file.getExtension();
				byte[] bytes = file.getBytes();
				
				if(__injectStringHelper__().isBlank(file.getName())) {
					if(__injectStringHelper__().isNotBlank(nameAndExtension))
						file.setName(__inject__(FileHelper.class).getName(nameAndExtension));
				}
				
				if(__injectStringHelper__().isBlank(extension)) {
					if(__injectStringHelper__().isNotBlank(nameAndExtension))
						file.setExtension(__inject__(FileHelper.class).getExtension(nameAndExtension));
				}
				
				if(__injectStringHelper__().isBlank(file.getMimeType())) {
					if(__injectStringHelper__().isNotBlank(extension))
						file.setMimeType(__inject__(FileHelper.class).getMimeTypeByExtension(extension));
					else if(__injectStringHelper__().isNotBlank(nameAndExtension))
						file.setMimeType(__inject__(FileHelper.class).getMimeTypeByNameAndExtension(nameAndExtension));
				}
				
				if(file.getSize() == null) {
					if(bytes!=null)
						file.setSize(new Long(bytes.length));
				}
			}
		});
	}

	@Override
	protected Class<File> __getPersistenceEntityClass__() {
		return File.class;
	}
	
}

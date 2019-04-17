package org.cyk.system.file.server.business.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.__kernel__.constant.ConstantCharacter;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.server.business.AbstractBusinessEntityImpl;
import org.cyk.utility.server.business.BusinessFunctionCreator;
import org.cyk.utility.server.business.BusinessFunctionRemover;
import org.cyk.utility.string.Strings;

@Singleton
public class FileBusinessImpl extends AbstractBusinessEntityImpl<File, FilePersistence> implements FileBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenExecuteCreateOneBefore__(File file, Properties properties,BusinessFunctionCreator function) {
		super.__listenExecuteCreateOneBefore__(file, properties, function);
		function.addTryBeginRunnables(new Runnable() {
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
		
		function.try_().end().addRunnables(new Runnable() {
			@Override
			public void run() {
				byte[] bytes = file.getBytes();
				if(bytes!=null)
					__inject__(FileBytesBusiness.class).create(__inject__(FileBytes.class).setFile(file).setBytes(bytes));
			}
		});
	}
	
	@Override
	protected void __listenExecuteDeleteOneBefore__(File file, Properties properties,BusinessFunctionRemover function) {
		super.__listenExecuteDeleteOneBefore__(file, properties, function);
		function.addTryBeginRunnables(new Runnable() {
			@Override
			public void run() {
				FileBytes fileBytes = __inject__(FileBytesPersistence.class).readByFile(file);
				if(fileBytes!=null)
					__inject__(FileBytesBusiness.class).delete(fileBytes);
			}
		});
	}
	
	@Override
	public File findOne(Object identifier, Properties properties) {
		File file = super.findOne(identifier, properties);
		Object fieldsObject = Properties.getFromPath(properties, Properties.FIELDS);
		Strings fields = null;
		if(fieldsObject instanceof Strings)
			fields = (Strings) fieldsObject;
		else if(fieldsObject instanceof String) {
			fields = __inject__(Strings.class).add(StringUtils.split((String) fieldsObject,ConstantCharacter.COMA.toString()));
		}
		if(__injectCollectionHelper__().isNotEmpty(fields))
			fields.get().forEach(new Consumer<String>() {
				@Override
				public void accept(String field) {
					if(File.FIELD_BYTES.equals(field)) {
						FileBytes fileBytes = __inject__(FileBytesPersistence.class).readByFile(file);
						if(fileBytes!=null)
							file.setBytes(fileBytes.getBytes());
					}
				}
			});
		return file;
	}
	
	@Override
	public Collection<File> findMany(Properties properties) {
		Collection<File> files = super.findMany(properties);
		
		Object fieldsObject = Properties.getFromPath(properties, Properties.FIELDS);
		Strings fields = null;
		if(fieldsObject instanceof Strings)
			fields = (Strings) fieldsObject;
		else if(fieldsObject instanceof String) {
			fields = __inject__(Strings.class).add(StringUtils.split((String) fieldsObject,ConstantCharacter.COMA.toString()));
		}
		if(__injectCollectionHelper__().isNotEmpty(fields))
			fields.get().forEach(new Consumer<String>() {
				@Override
				public void accept(String field) {
					if(File.FIELD_BYTES.equals(field)) {
						if(__injectCollectionHelper__().isNotEmpty(files))
							files.forEach(new Consumer<File>() {
								@Override
								public void accept(File file) {
									FileBytes fileBytes = __inject__(FileBytesPersistence.class).readByFile(file);
									if(fileBytes!=null)
										file.setBytes(fileBytes.getBytes());
								}
							});
					}
				}
			});
		return files;
	}
	
	@Override
	protected Class<File> __getPersistenceEntityClass__() {
		return File.class;
	}
	
}

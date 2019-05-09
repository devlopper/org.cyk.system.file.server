package org.cyk.system.file.server.business.impl;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.__kernel__.constant.ConstantCharacter;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.file.Files;
import org.cyk.utility.file.FilesGetter;
import org.cyk.utility.file.Paths;
import org.cyk.utility.file.PathsGetter;
import org.cyk.utility.number.Intervals;
import org.cyk.utility.server.business.AbstractBusinessEntityImpl;
import org.cyk.utility.server.business.BusinessFunctionCreator;
import org.cyk.utility.server.business.BusinessFunctionRemover;
import org.cyk.utility.string.StringHelper;
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
				byte[] bytes = file.getBytes();
				if(__inject__(StringHelper.class).isBlank(file.getSha1())) {
					if(bytes==null) {
						//TODO get a way to compute sha1 : from given uniform resource locator
					}else
						file.setSha1(new String(new DigestUtils(MessageDigestAlgorithms.SHA_1).digestAsHex(bytes)));
				}
				/*
				if(__injectStringHelper__().isNotBlank(file.getSha1())) {
					File current = __inject__(FilePersistence.class).readBySha1(file.getSha1());
					if(current!=null)
						__inject__(ThrowableHelper.class).throwRuntimeException("File content already exist");
				}
				*/
				String nameAndExtension = file.getNameAndExtension();
				String extension = file.getExtension();
				
				
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
		
		function.addTryEndRunnables(new Runnable() {
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
	public FileBusiness createFromDirectories(Strings directories,Strings mimeTypeTypes,Strings mimeTypeSubTypes,Strings mimeTypes,Strings extensions,Intervals sizes
			,Integer batchSize,Integer count) {
		System.out.println("Creating file from directories");
		System.out.println("Directories : "+directories);
		System.out.println("Extensions : "+extensions);
		System.out.println("Sizes : "+sizes);
		System.out.println("Batch size : "+batchSize);
		System.out.println("Count : "+count);
		
		if(count!=null && count<1)
			count = 1;
		if(batchSize == null)
			batchSize = 200;
		if(count!=null && count < batchSize)
			batchSize = count;	
		
		Paths paths = __inject__(PathsGetter.class).addDirectories(directories).setIsDirectoryGettable(Boolean.FALSE).setIsFileGettable(Boolean.TRUE).execute().getOutput();
		System.out.println("Number of files paths : "+paths.getSize());
		
		Integer numberOfPages = paths.getSize() / batchSize + (paths.getSize() % batchSize == 0 ? 0 : 1);
		System.out.println("Page size : "+batchSize+" , number of pages : "+numberOfPages);
		for(Integer index = 0; index < numberOfPages; index = index + 1) {
			Integer from = index * batchSize;
			Integer to = from + batchSize - 1;
			if(to>paths.getSize())
				to = paths.getSize()-1;
			System.out.println("Page from "+from+" to "+to);
			FilesGetter filesGetter = __inject__(FilesGetter.class);
			filesGetter.getPaths(Boolean.TRUE).add( ((List<Path>)paths.get()).subList(from, to+1) );
			filesGetter.setIsFileChecksumComputable(Boolean.TRUE).setIsFilterByFileChecksum(Boolean.TRUE).setIsFileBytesComputable(Boolean.TRUE);
			filesGetter.getFileExtensions(Boolean.TRUE).add(extensions);
			filesGetter.getFileSizeIntervals(Boolean.TRUE).add(sizes);
			Files files = filesGetter.execute().getOutput();
			
			System.out.println("Number of file read : "+__inject__(CollectionHelper.class).getSize(files));
			if(__injectCollectionHelper__().isNotEmpty(files)) {
				Collection<File> persistences = new ArrayList<>();
				//System.out.println("FREE MEMORY 01 : "+Runtime.getRuntime().freeMemory());
				for(org.cyk.utility.file.File indexFile : files.get()) {
					File persistence = null;
					if(__injectStringHelper__().isNotBlank(indexFile.getMimeType()) && __injectNumberHelper__().isGreaterThanZero(indexFile.getSize())) {
						if(__injectStringHelper__().isNotBlank(indexFile.getChecksum()))
							persistence = getPersistence().readBySha1(indexFile.getChecksum());
						if(persistence == null) {
							persistence = __inject__(File.class);
							persistence.setExtension(indexFile.getExtension());
							persistence.setMimeType(indexFile.getMimeType());
							persistence.setName(indexFile.getName());
							persistence.setSha1(indexFile.getChecksum());
							persistence.setSize(indexFile.getSize());
							persistence.setUniformResourceLocator(indexFile.getUniformResourceLocator());
							persistence.setBytes(indexFile.getBytes());
							persistences.add(persistence);
							if(count!=null) {
								count--;
								if(count == 0)
									break;	
							}
							
						}
					}
				}
				
				System.out.println("Persisting "+persistences.size());
				createFromDirectories(persistences);
				persistences.stream().map(x -> x.setBytes(null));
				persistences.clear();
				files.get().stream().map(x -> x.setBytes(null));
				files.removeAll();
				System.gc();
				System.out.println("Done!!!");
				if(count!=null && count == 0)
					break;
				//System.out.println("FREE MEMORY 02 : "+Runtime.getRuntime().freeMemory());
			}
		}
		return this;
	}
	
	@Transactional(value=TxType.REQUIRES_NEW)
	private void createFromDirectories(Collection<File> files) {
		createMany(files);
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

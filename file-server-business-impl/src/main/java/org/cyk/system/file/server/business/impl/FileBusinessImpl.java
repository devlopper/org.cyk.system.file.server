package org.cyk.system.file.server.business.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.io.IOUtils;
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
import org.cyk.utility.file.Files;
import org.cyk.utility.file.FilesGetter;
import org.cyk.utility.server.business.AbstractBusinessEntityImpl;
import org.cyk.utility.server.business.BusinessFunctionCreator;
import org.cyk.utility.server.business.BusinessFunctionRemover;
import org.cyk.utility.server.persistence.Persistence;
import org.cyk.utility.string.StringHelper;
import org.cyk.utility.string.Strings;
import org.cyk.utility.time.TimeHelper;

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
	public FileBusiness createFromDirectories(Strings directories) {
		FilesGetter filesGetter = __inject__(FilesGetter.class);
		filesGetter.addDirectories(directories).setIsFileChecksumComputable(Boolean.TRUE).setIsFilterByFileChecksum(Boolean.TRUE);
		System.out.println("Reading file from directories : "+directories);
		Files files = filesGetter.execute().getOutput();
		System.out.println("Number of file found in directories () : "+__injectCollectionHelper__().getSize(files));
		Files filesNotToBeCreated = __inject__(Files.class);
		if(files!=null) {
			Collection<File> persistences = new ArrayList<>();
			files.get().forEach(new Consumer<org.cyk.utility.file.File>() {
				@Override
				public void accept(org.cyk.utility.file.File file) {
					File persistence = null;
					if(__injectStringHelper__().isNotBlank(file.getMimeType()) && file.getSize()!=null && file.getSize()>0 && file.getSize() < 1024 * 1024 * 1) {
						if(__injectStringHelper__().isNotBlank(file.getChecksum()))
							persistence = getPersistence().readBySha1(file.getChecksum());
						if(persistence == null) {
							persistence = __inject__(File.class);
							try {
								byte[] buffer = new byte[1024 * 8];
								byte[] bytes = null;
							    try (InputStream inputStream = java.nio.file.Files.newInputStream(Paths.get(file.getPathAndNameAndExtension()))) {
							        int numberOfBytesRead = inputStream.read(buffer);
							        if (numberOfBytesRead < buffer.length) {
							        	bytes = Arrays.copyOf(buffer, numberOfBytesRead);
							        }else {
							        	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 16);
								        while (numberOfBytesRead != -1) {
								        	outputStream.write(buffer, 0, numberOfBytesRead);
								        	numberOfBytesRead = inputStream.read(buffer);
								        }
								        bytes = outputStream.toByteArray();
								        outputStream.close();
							        }
							    }
							    persistence.setBytes(bytes);
							    buffer = null;
							    bytes = null;	    
							} catch (Exception exception) {
								exception.printStackTrace();
							}
							persistence.setExtension(file.getExtension());
							persistence.setMimeType(file.getMimeType());
							persistence.setName(file.getName());
							persistence.setSha1(file.getChecksum());
							persistence.setSize(file.getSize());
							persistence.setUniformResourceLocator(file.getUniformResourceLocator());
							//persistence.setUniformResourceLocator("url");
							persistences.add(persistence);
							if(persistences.size() == 300) {
								System.out.println("FileBusinessImpl.createFromDirectories(...).new Consumer() {...}.accept() FREE MEMORY 01 : "+Runtime.getRuntime().freeMemory());
								createFromDirectories(persistences);
								//__inject__(Persistence.class).flush();
								//__inject__(Persistence.class).clear();
								persistences.stream().map(x -> x.setBytes(null));
								persistences.clear();
								System.gc();
								//System.out.println("Garbage collector called. Waiting sometimes before continuing...");
								//__inject__(TimeHelper.class).pause(1000l * 10);
								System.out.println("FileBusinessImpl.createFromDirectories(...).new Consumer() {...}.accept() FREE MEMORY 02 : "+Runtime.getRuntime().freeMemory());
							}	
						}else {
							filesNotToBeCreated.add(file);
						}
					}else {
						filesNotToBeCreated.add(file);
					}
				}
			});
			if(persistences.size() > 0)
				createFromDirectories(persistences);
		}
		System.out.println("Number of file created from directories () : "+(__injectCollectionHelper__().getSize(files)-__injectCollectionHelper__().getSize(filesNotToBeCreated)));
		return this;
	}
	
	/*
	@Override @Transactional(value=TxType.REQUIRES_NEW)
	public FileBusiness createFromDirectories(Collection<File> files) {
		createMany(files);
		//__inject__(Persistence.class).flush();
		return this;
	}
	*/
	
	@Transactional(value=TxType.REQUIRES_NEW)
	private void createFromDirectories(Collection<File> files) {
		createMany(files);
		//__inject__(Persistence.class).flush();
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

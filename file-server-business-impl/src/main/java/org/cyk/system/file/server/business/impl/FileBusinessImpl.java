package org.cyk.system.file.server.business.impl;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.io.FileUtils;
import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.business.api.FileTextBusiness;
import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.system.file.server.persistence.entities.FileText;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.collection.CollectionHelperImpl;
import org.cyk.utility.file.FileHelperImpl;
import org.cyk.utility.file.Files;
import org.cyk.utility.file.Paths;
import org.cyk.utility.number.Intervals;
import org.cyk.utility.number.NumberHelperImpl;
import org.cyk.utility.server.business.AbstractBusinessEntityImpl;
import org.cyk.utility.server.business.BusinessFunctionCreator;
import org.cyk.utility.server.business.BusinessFunctionRemover;
import org.cyk.utility.string.StringHelperImpl;
import org.cyk.utility.string.Strings;

@ApplicationScoped
public class FileBusinessImpl extends AbstractBusinessEntityImpl<File, FilePersistence> implements FileBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	public static Path ROOT_FOLDER_PATH;
	
	@Override
	protected void __listenExecuteCreateBefore__(File file, Properties properties, BusinessFunctionCreator function) {
		super.__listenExecuteCreateBefore__(file, properties, function);
		function.addTryBeginRunnables(new Runnable() {
			@Override
			public void run() {
				byte[] bytes = file.getBytes();
				if(StringHelperImpl.__isBlank__(file.getSha1())) {
					if(bytes==null) {
						//TODO get a way to compute sha1 : from given uniform resource locator
					}else
						file.setSha1(new String(new DigestUtils(MessageDigestAlgorithms.SHA_1).digestAsHex(bytes)));
				}
				/*
				if(StringHelperImpl.__isNotBlank__(file.getSha1())) {
					File current = __inject__(FilePersistence.class).readBySha1(file.getSha1());
					if(current!=null)
						__inject__(ThrowableHelper.class).throwRuntimeException("File content already exist");
				}
				*/
				String nameAndExtension = file.getNameAndExtension();
				String extension = file.getExtension();
								
				if(StringHelperImpl.__isBlank__(file.getName())) {
					if(StringHelperImpl.__isNotBlank__(nameAndExtension))
						file.setName(FileHelperImpl.__getName__(nameAndExtension));
				}
				
				if(StringHelperImpl.__isBlank__(extension)) {
					if(StringHelperImpl.__isNotBlank__(nameAndExtension))
						file.setExtension(FileHelperImpl.__getExtension__(nameAndExtension));
				}
				
				if(StringHelperImpl.__isBlank__(file.getMimeType())) {
					if(StringHelperImpl.__isNotBlank__(extension))
						file.setMimeType(FileHelperImpl.__getMimeTypeByExtension__(extension));
					else if(StringHelperImpl.__isNotBlank__(nameAndExtension))
						file.setMimeType(FileHelperImpl.__getMimeTypeByNameAndExtension__(nameAndExtension));
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
				if(StringHelperImpl.__isBlank__(file.getUniformResourceLocator())) {
					if(file.getBytes()!=null) {
						if(Boolean.TRUE.equals(file.getIsBytesAccessibleFromUniformResourceLocator())) {
							try {
								java.io.File __file__ = new java.io.File(ROOT_FOLDER_PATH.toFile(),file.getNameAndExtension());
								file.setUniformResourceLocator(__file__.toURI().toString());
								FileUtils.writeByteArrayToFile(__file__, file.getBytes());
							} catch (IOException exception) {
								//exception.printStackTrace();
								throw new RuntimeException(exception);
							}
						}else {
							__inject__(FileBytesBusiness.class).create(new FileBytes().setFile(file).setBytes(file.getBytes()));	
						}
					}	
				}else {
					
				}
				if(StringHelperImpl.__isNotBlank__(file.getText()))
					__inject__(FileTextBusiness.class).create(new FileText().setFile(file).setText(file.getText()));
			}
		});
	}
	
	@Override
	protected void __listenExecuteDeleteBefore__(File file, Properties properties, BusinessFunctionRemover function) {
		super.__listenExecuteDeleteBefore__(file, properties, function);
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
		
		Paths paths = FileHelperImpl.__getPaths__(directories.get(), null, Boolean.FALSE, Boolean.TRUE, null);
		if(CollectionHelperImpl.__isNotEmpty__(paths)) {
			System.out.println("Number of files paths found : "+paths.getSize());
			paths.removeByUniformResourceIdentifiers(__persistence__.readUniformResourceLocators(null));
			System.out.println("Number of files paths to process : "+paths.getSize());
			Files files = FileHelperImpl.__get__(paths, null);
			if(CollectionHelperImpl.__isNotEmpty__(files)) {
				System.out.println("Number of files read : "+files.getSize());
				System.out.println("Computing checksum...");
				FileHelperImpl.__computeChecksum__(files);
				System.out.println("Building persistables...");
				Collection<File> persistable = null;
				for(org.cyk.utility.file.File indexFile : files.get()) {
					File persistenceEntity = null;
					if(StringHelperImpl.__isNotBlank__(indexFile.getChecksum()) && StringHelperImpl.__isNotBlank__(indexFile.getMimeType()) 
							&& NumberHelperImpl.__isGreaterThanZero__(indexFile.getSize())) {
						//if(StringHelperImpl.__isNotBlank__(indexFile.getChecksum()))
						//	persistenceEntity = __persistence__.readBySha1(indexFile.getChecksum());
						if(persistenceEntity == null) {
							persistenceEntity = new File();
							persistenceEntity.setExtension(indexFile.getExtension());
							persistenceEntity.setMimeType(indexFile.getMimeType());
							persistenceEntity.setName(indexFile.getName());
							persistenceEntity.setSha1(indexFile.getChecksum());
							persistenceEntity.setSize(indexFile.getSize());
							persistenceEntity.setUniformResourceLocator(indexFile.getUniformResourceLocator());
							persistenceEntity.setBytes(indexFile.getBytes());
							if(persistable == null)
								persistable = new ArrayList<>();
							persistable.add(persistenceEntity);
						}
					}
				}
				if(CollectionHelperImpl.__isNotEmpty__(persistable)) {
					System.out.println("Persisting "+persistable.size()+" by batch of "+batchSize+"...");
					createByBatch(persistable, batchSize);
					persistable.stream().map(x -> x.setBytes(null));
					persistable.clear();	
				}
				files.get().stream().map(x -> x.setBytes(null));
				files.removeAll();
				System.gc();
			}
		}
		System.out.println("Done!!!");
		return this;
	}
	
	/*
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
			
			System.out.println("Number of file read : "+CollectionHelperImpl.__getSize__(files));
			if(CollectionHelperImpl.__isNotEmpty__(files)) {
				Collection<File> persistences = new ArrayList<>();
				//System.out.println("FREE MEMORY 01 : "+Runtime.getRuntime().freeMemory());
				for(org.cyk.utility.file.File indexFile : files.get()) {
					File persistenceEntity = null;
					if(StringHelperImpl.__isNotBlank__(indexFile.getMimeType()) && NumberHelperImpl.__isGreaterThanZero__(indexFile.getSize())) {
						if(StringHelperImpl.__isNotBlank__(indexFile.getChecksum()))
							persistenceEntity = __persistence__.readBySha1(indexFile.getChecksum());
						if(persistenceEntity == null) {
							persistenceEntity = __inject__(File.class);
							persistenceEntity.setExtension(indexFile.getExtension());
							persistenceEntity.setMimeType(indexFile.getMimeType());
							persistenceEntity.setName(indexFile.getName());
							persistenceEntity.setSha1(indexFile.getChecksum());
							persistenceEntity.setSize(indexFile.getSize());
							persistenceEntity.setUniformResourceLocator(indexFile.getUniformResourceLocator());
							//persistenceEntity.setBytes(indexFile.getBytes());
							persistences.add(persistenceEntity);
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
	}*/
	
	@Transactional(value=TxType.REQUIRES_NEW)
	private void createFromDirectories(Collection<File> files) {
		createMany(files);
	}

	@Override
	public Collection<File> findWhereNameContains(String string) {
		return __persistence__.readWhereNameContains(string);
	}
	
	@Override
	protected Boolean __isCallDeleteByInstanceOnDeleteByIdentifier__() {
		return Boolean.TRUE;
	}
	
}

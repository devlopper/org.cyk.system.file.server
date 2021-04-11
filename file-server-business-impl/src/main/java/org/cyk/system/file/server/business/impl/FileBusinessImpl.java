package org.cyk.system.file.server.business.impl;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.string.Strings;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.number.Intervals;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

@ApplicationScoped
public class FileBusinessImpl extends AbstractSpecificBusinessImpl<File> implements FileBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	public static Path ROOT_FOLDER_PATH;
	
	@Inject private FileBytesBusiness fileBytesBusiness;
	
	@Override
	protected void __prepare__(File file,Action action) {
		super.__prepare__(file, action);
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		if(Action.CREATE.equals(action)) {
			if(file.getBytes() == null && file.getBytes().length == 0 && StringHelper.isBlank(file.getUniformResourceLocator()))
				throw new RuntimeException("file bytes or uri is required");
			if(StringHelper.isBlank(file.getSha1())) {
				if(file.getBytes() == null) {
					//TODO get a way to compute sha1 : from given uniform resource locator
				}else
					file.setSha1(FileHelper.computeSha1(file.getBytes()));
			}
				
			if(StringHelper.isBlank(file.getName()) && StringHelper.isNotBlank(file.getNameAndExtension()))				
				file.setName(FileHelper.getName(file.getNameAndExtension()));
			
			if(StringHelper.isBlank(file.getExtension()) && StringHelper.isNotBlank(file.getNameAndExtension()))				
				file.setExtension(FileHelper.getExtension(file.getNameAndExtension()));
			
			if(StringHelper.isBlank(file.getMimeType()) && StringHelper.isNotBlank(file.getExtension()))			
				file.setMimeType(FileHelper.getMimeTypeByExtension(file.getExtension()));
			
			if(StringHelper.isBlank(file.getMimeType()) && StringHelper.isNotBlank(file.getNameAndExtension()))
				file.setMimeType(FileHelper.getMimeTypeByNameAndExtension(file.getNameAndExtension()));
			
			if(file.getSize() == null && file.getBytes() != null)				
				file.setSize(Long.valueOf(file.getBytes().length));
			
			if(StringHelper.isBlank(file.getName()))
				file.setName(RandomHelper.getAlphabetic(10));
			
			if(StringHelper.isBlank(file.getMimeType()))
				throwablesMessages.add("mime type is required");
			
			if(NumberHelper.isLessThanOrEqualZero(file.getSize()))
				throwablesMessages.add("file cannot be empty");
		}
		throwablesMessages.throwIfNotEmpty();
	}

	@Override
	protected void __create__(QueryExecutorArguments arguments) {
		arguments.setEntityManager(EntityManagerGetter.getInstance().get());
		super.__create__(arguments);
		fileBytesBusiness.createFromFiles(CollectionHelper.cast(File.class, arguments.getObjects()).stream()
				.filter(x -> Boolean.TRUE.equals(x.getIsBytesPersistableOnCreate())).collect(Collectors.toList()),arguments.getEntityManager());
	}
	
	/*
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
	*/
	
	@Override
	public FileBusiness createFromDirectories(Strings directories,Strings mimeTypeTypes,Strings mimeTypeSubTypes,Strings mimeTypes,Strings extensions,Intervals sizes
			,Integer batchSize,Integer count) {/*
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
		
		Collection<Path> paths = null;//FileHelper.getPaths(directories, RegularExpressionHelper.formatFileNameHavingExtensions(extensions), Boolean.FALSE, Boolean.TRUE, null);
		if(CollectionHelper.isNotEmpty(paths)) {
			System.out.println("Number of files paths found : "+paths.size());
			//FileHelper.removePathsByUniformResourceIdentifiers(paths, __persistence__.readUniformResourceLocators(null));
			System.out.println("Number of files paths to process : "+paths.size());
			Files files = FileHelper.get(paths, null);
			if(CollectionHelper.isNotEmpty(files)) {
				System.out.println("Number of files read : "+files.getSize());
				System.out.println("Computing checksum...");
				FileHelper.computeChecksum(files);
				System.out.println("Building persistables...");
				Collection<File> persistable = null;
				for(org.cyk.utility.__kernel__.file.File indexFile : files.get()) {
					File persistenceEntity = null;
					if(StringHelper.isNotBlank(indexFile.getChecksum()) && StringHelper.isNotBlank(indexFile.getMimeType()) 
							&& NumberHelper.isGreaterThanZero(indexFile.getSize())) {
						//if(StringHelper.isNotBlank(indexFile.getChecksum()))
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
				if(CollectionHelper.isNotEmpty(persistable)) {
					System.out.println("Persisting "+persistable.size()+" by batch of "+batchSize+"...");
					//createByBatch(persistable, batchSize);
					persistable.stream().map(x -> x.setBytes(null));
					persistable.clear();	
				}
				files.get().stream().map(x -> x.setBytes(null));
				files.removeAll();
				System.gc();
			}
		}
		System.out.println("Done!!!");
		*/
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
			
			System.out.println("Number of file read : "+CollectionHelper.__getSize__(files));
			if(CollectionHelper.isNotEmpty(files)) {
				Collection<File> persistences = new ArrayList<>();
				//System.out.println("FREE MEMORY 01 : "+Runtime.getRuntime().freeMemory());
				for(org.cyk.utility.file.File indexFile : files.get()) {
					File persistenceEntity = null;
					if(StringHelper.isNotBlank(indexFile.getMimeType()) && NumberHelperImpl.__isGreaterThanZero__(indexFile.getSize())) {
						if(StringHelper.isNotBlank(indexFile.getChecksum()))
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
		//createMany(files);
	}

	@Override
	public Collection<File> findWhereNameContains(String string) {
		return null;//__persistence__.readWhereNameContains(string);
	}
	
}

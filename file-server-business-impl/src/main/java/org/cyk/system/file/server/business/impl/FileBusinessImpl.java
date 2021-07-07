package org.cyk.system.file.server.business.impl;

import java.io.Serializable;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.io.IOUtils;
import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.business.api.FileTextBusiness;
import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.impl.query.FileNameExtensionMimeTypeSizeBytesReader;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.collection.CollectionProcessor;
import org.cyk.utility.__kernel__.configuration.ConfigurationHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.string.Strings;
import org.cyk.utility.__kernel__.throwable.ThrowableHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.TransactionResult;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.file.PathsProcessor;
import org.cyk.utility.file.PathsScanner;
import org.cyk.utility.number.Intervals;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.query.EntityReader;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

@ApplicationScoped
public class FileBusinessImpl extends AbstractSpecificBusinessImpl<File> implements FileBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	public static Path ROOT_FOLDER_PATH;
	public static final String FILES_PATHS_NAMES = "FILES_PATHS_NAMES";
	public static final String ACCEPTED_PATH_NAME_REGULAR_EXPRESSION = "ACCEPTED_PATH_NAME_REGULAR_EXPRESSION";
	
	@Inject private FileBytesBusiness fileBytesBusiness;
	@Inject private FileTextBusiness fileTextBusiness;
	
	@Override
	public TransactionResult import_(Collection<String> pathsNames,String acceptedPathNameRegularExpression) {
		if(CollectionHelper.isEmpty(pathsNames))
			throw new RuntimeException("paths names required");
		if(StringHelper.isBlank(acceptedPathNameRegularExpression))
			throw new RuntimeException("accepted path name regular expression required");
		
		TransactionResult result = new TransactionResult().setName("files collector").setTupleName("file");
		Collection<Path> paths = PathsScanner.getInstance().scan(new PathsScanner.Arguments().addPathsFromNames(pathsNames)
				.setAcceptedPathNameRegularExpression(acceptedPathNameRegularExpression)
				.setMinimalSize(File.MINIMAL_SIZE).setMaximalSize(File.MAXIMAL_SIZE));
		Collection<String> existingsURLs = FileQuerier.getInstance().readUniformResourceLocators();
		Collection<File> files = new ArrayList<>();
		PathsProcessor.getInstance().process(paths,new CollectionProcessor.Arguments.Processing.AbstractImpl<Path>() {			
			@Override
			protected void __process__(Path path) {
				String url = path.toFile().toURI().toString();
				if(Boolean.TRUE.equals(CollectionHelper.contains(existingsURLs, url)))
					return;
				files.add(instantiateFile(path,url));
			}
		});
		create(files);
		result.incrementNumberOfCreation(Long.valueOf(files.size()));
		result.log(getClass());
		return result;
	}
	
	@Override
	public TransactionResult import_() {
		Collection<String> pathsNames = ConfigurationHelper.getValueAsStrings(FILES_PATHS_NAMES);
		if(CollectionHelper.isEmpty(pathsNames)) {
			LogHelper.logWarning(String.format("No files paths names found under variable named %s", FILES_PATHS_NAMES), getClass());
			return null;
		}
		String acceptedPathNameRegularExpression = ConfigurationHelper.getValueAsString(ACCEPTED_PATH_NAME_REGULAR_EXPRESSION);
		if(StringHelper.isBlank(acceptedPathNameRegularExpression))
			acceptedPathNameRegularExpression = ".pdf";
		if(StringHelper.isBlank(acceptedPathNameRegularExpression)) {
			LogHelper.logWarning(String.format("No accepted path name regular expression found under variable named %s", ACCEPTED_PATH_NAME_REGULAR_EXPRESSION), getClass());
			return null;
		}
		return import_(pathsNames, acceptedPathNameRegularExpression);
	}
	
	private static File instantiateFile(Path path,String url) {
		File file = new File();
		file.setNameAndExtension(path.toFile().getName());
		file.setSize(path.toFile().length());
		file.setUniformResourceLocator(url);		
		return file;
	}
	
	@Override
	protected void __prepare__(File file,Action action,ThrowablesMessages throwablesMessages) {
		super.__prepare__(file, action,throwablesMessages);
		if(Action.CREATE.equals(action)) {
			if((file.getBytes() == null || file.getBytes().length == 0) && StringHelper.isBlank(file.getUniformResourceLocator()))
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
	}

	@Override
	protected TransactionResult __create__(QueryExecutorArguments arguments) {
		arguments.setEntityManager(EntityManagerGetter.getInstance().get());
		super.__create__(arguments);
		return fileBytesBusiness.createFromFiles(CollectionHelper.cast(File.class, arguments.getObjects()).stream()
				.filter(x -> Boolean.TRUE.equals(x.getIsBytesPersistableOnCreate())).collect(Collectors.toList()),arguments.getEntityManager());
	}
	
	@Override
	public TransactionResult extractBytes() {
		TransactionResult result = new TransactionResult().setName("files bytes extractor");
		Long filesCount = FileQuerier.getInstance().countWhereBytesDoNotExists(null);
		LogHelper.logInfo(String.format("%s file(s) to process", filesCount), getClass());
		Collection<File> files = FileQuerier.getInstance().readWhereBytesDoNotExists(null);
		TransactionResult intermediateResult = fileBytesBusiness.createFromFiles(files);
		result.add(intermediateResult);
		result.log(getClass());
		return result;
	}
	
	@Override
	public TransactionResult extractText() {
		TransactionResult result = new TransactionResult().setName("files texts extractor");
		Collection<File> files = EntityReader.getInstance().readMany(File.class);
		fileTextBusiness.createFromFiles(files);
		return result;
	}
	
	@Override
	public File download(String identifier) {
		ThrowableHelper.throwIllegalArgumentExceptionIfBlank("File identifier", identifier);
		Collection<File> files = new FileNameExtensionMimeTypeSizeBytesReader().readByIdentifiersThenInstantiate(List.of(identifier), null);
		if(CollectionHelper.isEmpty(files))
			throw new RuntimeException("File bytes instance not found");
		if(files.size() > 1)
			throw new RuntimeException("Too much files found");
		File file = (File) files.iterator().next();
		ThrowableHelper.throwIllegalArgumentExceptionIfNull("File", file);
		if(file.getBytes() == null && StringHelper.isNotBlank(file.getUniformResourceLocator())) {
			try {
				file.setBytes(IOUtils.toByteArray(new URI(file.getUniformResourceLocator()).toURL()));
				if(file.getBytes() != null)
					file.setSize(Long.valueOf(file.getBytes().length));
			} catch (Exception exception) {
				exception.printStackTrace();
				throw new RuntimeException(exception);
			}
		}
		return file;
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

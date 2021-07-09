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

import org.apache.commons.io.IOUtils;
import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.business.api.FileTextBusiness;
import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.impl.FilePersistenceImpl;
import org.cyk.system.file.server.persistence.impl.query.FileNameExtensionMimeTypeSizeBytesReader;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.collection.CollectionProcessor;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowableHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.TransactionResult;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.file.PathsProcessor;
import org.cyk.utility.file.PathsScanner;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.query.EntityFinder;
import org.cyk.utility.persistence.query.EntityReader;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

@ApplicationScoped
public class FileBusinessImpl extends AbstractSpecificBusinessImpl<File> implements FileBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	//public static Path ROOT_FOLDER_PATH;
	//public static final String FILES_PATHS_NAMES = "FILES_PATHS_NAMES";
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
				.setMinimalSize(FilePersistenceImpl.getMinimalSize()).setMaximalSize(FilePersistenceImpl.getMaximalSize()));
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
		String directory = FilePersistenceImpl.getDirectory();
		if(StringHelper.isBlank(directory))
			return null;
		return import_(List.of(directory), FilePersistenceImpl.getAcceptedPathNameRegularExpression());
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
	public TransactionResult extractBytesOfAll() {
		Long count = FileQuerier.getInstance().countWhereBytesDoNotExists(null);
		LogHelper.logInfo(String.format("%s file(s) having bytes not yet extracted", count), getClass());
		if(NumberHelper.isLessThanOrEqualZero(count))
			return null;
		Collection<File> files = FileQuerier.getInstance().readWhereBytesDoNotExists(null);
		if(CollectionHelper.isEmpty(files))
			return null;
		return extractBytes(files);
	}
	
	@Override
	public TransactionResult extractBytes(Collection<File> files) {
		ThrowableHelper.throwIllegalArgumentExceptionIfEmpty("files", files);
		TransactionResult result = new TransactionResult().setName("files bytes extractor").setTupleName("file bytes");		
		result.add(fileBytesBusiness.createFromFiles(files));
		result.log(getClass());
		return result;
	}
	
	@Override
	public TransactionResult extractBytes(File... files) {
		return extractBytes(ArrayHelper.isEmpty(files) ? null : CollectionHelper.listOf(files));
	}
	
	@Override
	public TransactionResult extractBytesFromIdentifiers(Collection<String> identifiers) {
		ThrowableHelper.throwIllegalArgumentExceptionIfEmpty("files identifiers", identifiers);
		Collection<File> files = EntityFinder.getInstance().findMany(File.class, identifiers);		
		return extractBytes(files);
	}
	
	@Override
	public TransactionResult extractBytesFromIdentifiers(String... identifiers) {
		return extractBytesFromIdentifiers(ArrayHelper.isEmpty(identifiers) ? null : CollectionHelper.listOf(identifiers));
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
		if(file.getBytes() == null) {
			if(StringHelper.isBlank(file.getUniformResourceLocator()))
				throw new RuntimeException(String.format("File bytes with identifier <<%s>> not found",file.getIdentifier()));
			else
				throw new RuntimeException(String.format("File bytes with identifier <<%s>> from url <<%s>> not found",file.getIdentifier(),file.getUniformResourceLocator()));
		}
		return file;
	}
	
	/**/
}
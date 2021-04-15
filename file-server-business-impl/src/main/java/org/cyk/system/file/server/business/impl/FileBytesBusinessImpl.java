package org.cyk.system.file.server.business.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.__kernel__.Helper;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.collection.CollectionProcessor;
import org.cyk.utility.__kernel__.runnable.RunnableHelper;
import org.cyk.utility.__kernel__.runnable.Runner;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.business.TransactionResult;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.query.EntityFinder;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

@ApplicationScoped
public class FileBytesBusinessImpl extends AbstractSpecificBusinessImpl<FileBytes> implements FileBytesBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public TransactionResult createFromFiles(Collection<File> files,EntityManager entityManager) {
		if(CollectionHelper.isEmpty(files))
			return null;
		TransactionResult result = new TransactionResult().setName("create from "+files.size()+" files");
		CollectionProcessor.Arguments<File> collectionProcessorArguments = new CollectionProcessor.Arguments<File>();
		collectionProcessorArguments.setList((List<Object>) CollectionHelper.cast(Object.class, files));
		collectionProcessorArguments.setBatchSize(50);
		collectionProcessorArguments.setRunnerArguments(new Runner.Arguments().setName(result.getName()).setExecutorService(RunnableHelper.instantiateExecutorService(4))
				.setTimeOut(files.size() * 1000l * 5));
		collectionProcessorArguments.setProcessing(new CollectionProcessor.Arguments.Processing.AbstractImpl<File>() {
			@Override
			protected void __process__(File file) {
				if(!Boolean.TRUE.equals(FileQuerier.getInstance().isSizeAllowed(file.getSize())))
					return;
				if(file.getBytes() == null && StringHelper.isNotBlank(file.getUniformResourceLocator()))
					file.setBytes(Helper.getBytesFromURL(file.getUniformResourceLocator()));
			}
			
			@Override
			protected void __process__(List<File> files) {
				super.__process__(files);
				Collection<Object> filesBytes = files.stream()
						.filter(file -> file.getBytes() != null)
						.map(file -> new FileBytes().setFile(file).setBytes(file.getBytes())).collect(Collectors.toList());
				result.incrementNumberOfCreation(Long.valueOf(filesBytes.size()));
				create(new QueryExecutorArguments().setObjects(filesBytes).setEntityManager(entityManager));
				files.forEach(file -> {
					file.setBytes(null);
				});
				filesBytes.clear();
			}
		}.setParallelizable(Boolean.TRUE));
		CollectionProcessor.getInstance().process(File.class, collectionProcessorArguments);
		return result;
	}
	
	@Override
	public TransactionResult createFromFiles(Collection<File> files) {
		return createFromFiles(files,null);
	}
	
	@Override
	public TransactionResult createFromFiles(File... files) {
		if(ArrayHelper.isEmpty(files))
			return null;
		return createFromFiles(CollectionHelper.listOf(files));
	}
	
	@Override
	public TransactionResult createFromFilesIdentifiers(Collection<String> filesIdentifiers) {
		if(CollectionHelper.isEmpty(filesIdentifiers))
			return null;
		return createFromFiles(EntityFinder.getInstance().findMany(File.class, filesIdentifiers));
	}
	
	@Override
	public TransactionResult createFromFilesIdentifiers(String... filesIdentifiers) {
		if(ArrayHelper.isEmpty(filesIdentifiers))
			return null;
		return createFromFilesIdentifiers(CollectionHelper.listOf(filesIdentifiers));
	}
}
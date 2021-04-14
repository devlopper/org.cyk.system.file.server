package org.cyk.system.file.server.business.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.__kernel__.Helper;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.collection.CollectionProcessor;
import org.cyk.utility.__kernel__.computation.ComparisonOperator;
import org.cyk.utility.__kernel__.number.NumberHelper;
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
		collectionProcessorArguments.setBatchSize(100);
		collectionProcessorArguments.setProcessing(new CollectionProcessor.Arguments.Processing.AbstractImpl<File>() {
			@Override
			protected void __process__(File file) {
				//if(NumberHelper.compare(file.getSize(), 1024 * 512, ComparisonOperator.GT))
				//	return;
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
		});
		CollectionProcessor.getInstance().process(File.class, collectionProcessorArguments);
		return result;
		/*
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		Collection<FileBytes> filesBytes = null;
		for(File file : files) {
			byte[] bytes = file.getBytes();
			if(bytes == null && StringHelper.isNotBlank(file.getUniformResourceLocator()))
				bytes = Helper.getBytesFromURL(file.getUniformResourceLocator());
			if(bytes == null)
				throwablesMessages.add(String.format("file identified by %s has no bytes", file.getIdentifier()));
			else {
				if(filesBytes == null)
					filesBytes = new ArrayList<>();
				filesBytes.add(new FileBytes().setFile(file).setBytes(bytes));
			}
		}
		throwablesMessages.throwIfNotEmpty();
		if(CollectionHelper.isEmpty(filesBytes))
			return null;
		TransactionResult transactionResult = new TransactionResult();		
		transactionResult.setNumberOfCreation(Long.valueOf(filesBytes.size()));
		if(CollectionHelper.isNotEmpty(filesBytes))
			create(new QueryExecutorArguments().setObjects(CollectionHelper.cast(Object.class, filesBytes)).setEntityManager(entityManager));		
		return transactionResult;
		*/
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
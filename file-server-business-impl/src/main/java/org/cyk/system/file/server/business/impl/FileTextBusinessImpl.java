package org.cyk.system.file.server.business.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

import org.cyk.system.file.server.business.api.FileTextBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.system.file.server.persistence.entities.FileText;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.query.EntityFinder;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

@ApplicationScoped
public class FileTextBusinessImpl extends AbstractSpecificBusinessImpl<FileText> implements FileTextBusiness,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void createFromFiles(Collection<File> files,EntityManager entityManager) {
		if(CollectionHelper.isEmpty(files))
			return;
		/*ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		Collection<FileBytes> filesBytes = null;
		for(File file : files) {
			byte[] bytes = file.getBytes();
			if(bytes == null && StringHelper.isNotBlank(file.getUniformResourceLocator())) {
				
			}
			if(bytes == null)
				throwablesMessages.add(String.format("file identified by %s has no bytes", file.getIdentifier()));
			else {
				if(filesBytes == null)
					filesBytes = new ArrayList<>();
				filesBytes.add(new FileBytes().setFile(file).setBytes(bytes));
			}
		}
		throwablesMessages.throwIfNotEmpty();
		if(CollectionHelper.isNotEmpty(filesBytes)) {
			create(new QueryExecutorArguments().setObjects(CollectionHelper.cast(Object.class, filesBytes)).setEntityManager(entityManager));
		}*/
	}
	
	@Override
	public void createFromFiles(Collection<File> files) {
		createFromFiles(files,null);
	}
	
	@Override
	public void createFromFiles(File... files) {
		if(ArrayHelper.isEmpty(files))
			return;
		createFromFiles(CollectionHelper.listOf(files));
	}
	
	@Override
	public void createFromFilesIdentifiers(Collection<String> filesIdentifiers) {
		if(CollectionHelper.isEmpty(filesIdentifiers))
			return;
		createFromFiles(EntityFinder.getInstance().findMany(File.class, filesIdentifiers));
	}
	
	@Override
	public void createFromFilesIdentifiers(String... filesIdentifiers) {
		if(ArrayHelper.isEmpty(filesIdentifiers))
			return;
		createFromFilesIdentifiers(CollectionHelper.listOf(filesIdentifiers));
	}	
}
package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;
import org.cyk.utility.server.persistence.query.PersistenceQuery;
import org.cyk.utility.server.persistence.query.PersistenceQueryRepository;

@Singleton
public class FileBytesPersistenceImpl extends AbstractPersistenceEntityImpl<FileBytes> implements FileBytesPersistence,Serializable {
	private static final long serialVersionUID = 1L;

	private String readByFile;
	
	@Override
	protected void __listenPostConstructPersistenceQueries__() {
		super.__listenPostConstructPersistenceQueries__();
		addQueryCollectInstances(readByFile, __instanciateQueryReadBy__(FileBytes.FIELD_FILE));
	}
	
	@Override
	public FileBytes readByFile(File file) {
		return __readOne__(____getQueryParameters____(file));
	}
	
	protected Object[] __getQueryParameters__(String queryIdentifier,Object...objects){
		PersistenceQuery persistenceQuery = __inject__(PersistenceQueryRepository.class).getBySystemIdentifier(queryIdentifier);
		
		if(persistenceQuery.isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readByFile,queryIdentifier))
			return new Object[]{FileBytes.FIELD_FILE, objects[0]};
		
		return super.__getQueryParameters__(queryIdentifier, objects);
	}
	
}

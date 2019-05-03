package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;
import org.cyk.utility.server.persistence.query.PersistenceQuery;
import org.cyk.utility.server.persistence.query.PersistenceQueryRepository;

@Singleton
public class FilePersistenceImpl extends AbstractPersistenceEntityImpl<File> implements FilePersistence,Serializable {
	private static final long serialVersionUID = 1L;

	private String readBySha1;
	
	@Override
	protected void __listenPostConstructPersistenceQueries__() {
		super.__listenPostConstructPersistenceQueries__();
		addQueryCollectInstances(readBySha1, __instanciateQueryReadBy__(File.FIELD_SHA1));
	}
	
	@Override
	public File readBySha1(String sha1) {
		return __readOne__(____getQueryParameters____(sha1));
	}
	
	protected Object[] __getQueryParameters__(String queryIdentifier,Object...objects){
		PersistenceQuery persistenceQuery = __inject__(PersistenceQueryRepository.class).getBySystemIdentifier(queryIdentifier);
		
		if(persistenceQuery.isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readBySha1,queryIdentifier))
			return new Object[]{File.FIELD_SHA1, objects[0]};
		
		return super.__getQueryParameters__(queryIdentifier, objects);
	}
	
}

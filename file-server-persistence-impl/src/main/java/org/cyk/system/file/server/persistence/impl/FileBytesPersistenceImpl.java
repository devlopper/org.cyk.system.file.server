package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;
import org.cyk.utility.server.persistence.query.PersistenceQueryContext;

@ApplicationScoped
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
		Properties properties = new Properties().setQueryIdentifier(readByFile);
		return __readOne__(properties,____getQueryParameters____(properties,file));
	}
	
	@Override
	protected Object[] __getQueryParameters__(PersistenceQueryContext queryContext, Properties properties,Object... objects) {
		if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readByFile))
			return new Object[]{FileBytes.FIELD_FILE, objects[0]};
		return super.__getQueryParameters__(queryContext, properties, objects);
	}
	
}

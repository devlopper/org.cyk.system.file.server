package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;
import java.util.Collection;

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

	private String readByFilesIdentifiers;
	
	@Override
	protected void __listenPostConstructPersistenceQueries__() {
		super.__listenPostConstructPersistenceQueries__();
		addQueryCollectInstances(readByFilesIdentifiers,String.format("SELECT tuple FROM %s tuple WHERE tuple.file.identifier IN :filesIdentifiers",__getTupleName__()));
	}
	
	@Override
	public Collection<FileBytes> readByFilesIdentifiers(Collection<String> filesIdentifiers) {
		Properties properties = new Properties().setQueryIdentifier(readByFilesIdentifiers);
		return __readMany__(properties,____getQueryParameters____(properties,filesIdentifiers));
	}
	
	@Override
	public Collection<FileBytes> readByFilesIdentifiers(String... filesIdentifiers) {
		return readByFilesIdentifiers(__injectCollectionHelper__().instanciate(filesIdentifiers));
	}
	
	@Override
	public FileBytes readByFileIdentifier(String fileIdentifier) {
		return __injectCollectionHelper__().getFirst(readByFilesIdentifiers(fileIdentifier));
	}
	
	@Override
	public FileBytes readByFile(File file) {
		return readByFileIdentifier(file.getIdentifier());
	}
	
	@Override
	protected Object[] __getQueryParameters__(PersistenceQueryContext queryContext, Properties properties,Object... objects) {
		if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readByFilesIdentifiers)) {
			return new Object[]{"filesIdentifiers", objects[0]};
		}
		return super.__getQueryParameters__(queryContext, properties, objects);
	}
	
}

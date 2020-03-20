package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import org.cyk.system.file.server.persistence.api.FileDetailPersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryContext;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.array.ArrayHelperImpl;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;

public abstract class AbstractFileDetailPersistenceImpl<T> extends AbstractPersistenceEntityImpl<T> implements FileDetailPersistence<T>,Serializable {
	private static final long serialVersionUID = 1L;

	private String readByFilesIdentifiers;
	
	@Override
	protected void __listenPostConstructPersistenceQueries__() {
		super.__listenPostConstructPersistenceQueries__();
		addQueryCollectInstances(readByFilesIdentifiers,String.format("SELECT tuple FROM %s tuple WHERE tuple.file.identifier IN :filesIdentifiers",__tupleName__));
	}
	
	@Override
	public Collection<T> readByFilesIdentifiers(Collection<String> filesIdentifiers) {
		Properties properties = new Properties().setQueryIdentifier(readByFilesIdentifiers);
		return __readMany__(properties,____getQueryParameters____(properties,filesIdentifiers));
	}
	
	@Override
	public Collection<T> readByFilesIdentifiers(String... filesIdentifiers) {
		return readByFilesIdentifiers(CollectionHelper.listOf(filesIdentifiers));
	}
	
	@Override
	public Collection<T> readByFiles(Collection<File> files) {
		return CollectionHelper.isEmpty(files) ? null : readByFilesIdentifiers(files.stream().map(File::getIdentifier).collect(Collectors.toList()));
	}
	
	@Override 
	public Collection<T> readByFiles(File... files) {
		return ArrayHelperImpl.__isEmpty__(files) ? null : readByFiles(CollectionHelper.listOf(files));
	}
	
	@Override
	public T readByFileIdentifier(String fileIdentifier) {
		return CollectionHelper.getFirst(readByFilesIdentifiers(fileIdentifier));
	}
	
	@Override
	public T readByFile(File file) {
		return CollectionHelper.getFirst(readByFiles(file));
	}
	
	@Override
	protected Object[] __getQueryParameters__(QueryContext queryContext, Properties properties,Object... objects) {
		if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readByFilesIdentifiers)) {
			return new Object[]{"filesIdentifiers", objects[0]};
		}
		return super.__getQueryParameters__(queryContext, properties, objects);
	}
	
	
}

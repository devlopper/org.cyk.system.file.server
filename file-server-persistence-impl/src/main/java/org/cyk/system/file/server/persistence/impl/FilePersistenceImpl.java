package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Singleton;

import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.array.ArrayHelper;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;
import org.cyk.utility.server.persistence.PersistenceFunctionReader;
import org.cyk.utility.server.persistence.query.PersistenceQuery;
import org.cyk.utility.server.persistence.query.PersistenceQueryRepository;

@Singleton
public class FilePersistenceImpl extends AbstractPersistenceEntityImpl<File> implements FilePersistence,Serializable {
	private static final long serialVersionUID = 1L;

	private String readBySha1,readWhereNameContains;
	
	@Override
	protected void __listenPostConstructPersistenceQueries__() {
		super.__listenPostConstructPersistenceQueries__();
		addQueryCollectInstances(readBySha1, __instanciateQueryReadBy__(File.FIELD_SHA1));
		addQueryCollectInstances(readWhereNameContains, "SELECT tuple FROM File tuple WHERE tuple.name LIKE :name");
	}
	
	@Override
	public File readBySha1(String sha1) {
		return __readOne__(____getQueryParameters____(null,sha1));
	}
	
	@Override
	public Collection<File> readWhereNameContains(String string) {
		return __readMany__(null,____getQueryParameters____(null,string));
	}
	
	@Override
	public Long countWhereNameContains(String string) {
		return __count__(null,____getQueryParameters____(null,string));
	}
	
	@SuppressWarnings("unchecked")
	protected Object[] __getQueryParameters__(String queryIdentifier,Properties properties,Object...objects){
		PersistenceQuery persistenceQuery = __inject__(PersistenceQueryRepository.class).getBySystemIdentifier(queryIdentifier);
		if(persistenceQuery.isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readBySha1,queryIdentifier))
			return new Object[]{File.FIELD_SHA1, objects[0]};
		else if(persistenceQuery.isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readWhereNameContains,queryIdentifier)) {
			if(Boolean.TRUE.equals(__inject__(ArrayHelper.class).isEmpty(objects)))
				objects = new Object[] {__injectCollectionHelper__().getFirst((Collection<String>) Properties.getFromPath(properties,Properties.QUERY_FILTERS))};
			return new Object[]{File.FIELD_NAME, "%"+objects[0]+"%"};
		}
		return super.__getQueryParameters__(queryIdentifier,properties, objects);
	}
	
	@Override
	protected String __getQueryIdentifier__(Class<?> functionClass, Properties properties, Object... parameters) {
		if(PersistenceFunctionReader.class.equals(functionClass) && Properties.getFromPath(properties,Properties.QUERY_FILTERS) != null)
			return readWhereNameContains;
		return super.__getQueryIdentifier__(functionClass, properties, parameters);
	}
	
}

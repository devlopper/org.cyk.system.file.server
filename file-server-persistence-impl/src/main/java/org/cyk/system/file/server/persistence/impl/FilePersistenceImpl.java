package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.array.ArrayHelper;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;
import org.cyk.utility.server.persistence.PersistenceFunctionReader;
import org.cyk.utility.server.persistence.PersistenceQueryIdentifierStringBuilder;
import org.cyk.utility.server.persistence.query.PersistenceQueryContext;
import org.cyk.utility.server.persistence.query.filter.Filter;
import org.cyk.utility.string.StringHelperImpl;

@ApplicationScoped
public class FilePersistenceImpl extends AbstractPersistenceEntityImpl<File> implements FilePersistence,Serializable {
	private static final long serialVersionUID = 1L;

	private String readBySha1,readWhereNameContains,readWhereNameOrTextContains;
	
	@Override
	protected void __listenPostConstructPersistenceQueries__() {
		super.__listenPostConstructPersistenceQueries__();
		addQueryCollectInstances(readBySha1, __instanciateQueryReadBy__(File.FIELD_SHA1));		
		addQueryCollectInstances(readWhereNameContains, "SELECT tuple FROM File tuple WHERE lower(tuple.name) LIKE lower(:name) ORDER BY tuple.name ASC");
		addQueryCollectInstances(readWhereNameOrTextContains, "SELECT tuple FROM File tuple WHERE lower(tuple.name) LIKE lower(:nameOrText)"
				+ " OR EXISTS(SELECT subTuple FROM FileText subTuple WHERE subTuple.file = tuple AND lower(subTuple.text) LIKE lower(:nameOrText)) "
				+ " ORDER BY tuple.name ASC");
	}
	
	@Override
	protected void __addReadQueryCollectInstances__() {
		addQueryCollectInstances(read, "SELECT tuple FROM File tuple ORDER BY tuple.name ASC");
	}
	
	@Override
	public File readBySha1(String sha1) {
		Properties properties = new Properties().setQueryIdentifier(readBySha1);
		return __readOne__(properties,____getQueryParameters____(properties,sha1));
	}
	
	@Override
	public Collection<File> readWhereNameContains(String string) {
		Properties properties = new Properties().setQueryIdentifier(readWhereNameContains);
		return __readMany__(properties,____getQueryParameters____(properties,string));
	}

	@Override
	protected void __listenExecuteReadAfterSetFieldValue__(File file, Field field,Properties properties) {
		super.__listenExecuteReadAfterSetFieldValue__(file, field,properties);
		if(File.FIELD_BYTES.equals(field.getName())) {
			FileBytes fileBytes = __inject__(FileBytesPersistence.class).readByFile(file);
			if(fileBytes!=null)
				file.setBytes(fileBytes.getBytes());
		}else if(File.FIELD_NAME_AND_EXTENSION.equals(field.getName())) {
			file.setNameAndExtension(__inject__(FileHelper.class).concatenateNameAndExtension(file.getName(), file.getExtension()));	
		}
	}
	
	@Override
	public Long countWhereNameContains(String string) {
		Properties properties = new Properties().setQueryIdentifier(__inject__(PersistenceQueryIdentifierStringBuilder.class).setIsDerivedFromQueryIdentifier(Boolean.TRUE)
				.setDerivedFromQueryIdentifier(readWhereNameContains).setIsCountInstances(Boolean.TRUE)
				.execute().getOutput());
		return __count__(properties,____getQueryParameters____(properties,string));
	}
	
	@Override
	protected String __getQueryIdentifier__(Class<?> functionClass, Properties properties, Object... parameters) {
		Filter filter = (Filter) Properties.getFromPath(properties, Properties.QUERY_FILTERS);
		if(PersistenceFunctionReader.class.equals(functionClass)) {
			if(filter != null && StringHelperImpl.__isNotBlank__(filter.getValue()))
				return readWhereNameOrTextContains;
			if(__isFilterByKeys__(properties, File.FIELD_NAME) || 
					(filter!=null && __injectCollectionHelper__().isEmpty(filter.getFields()) && __injectStringHelper__().isNotBlank(filter.getValue())) )
				return readWhereNameContains;
		}
		return super.__getQueryIdentifier__(functionClass, properties, parameters);
	}
	
	@Override
	protected Object[] __getQueryParameters__(PersistenceQueryContext queryContext, Properties properties,Object... objects) {
		if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readBySha1))
			return new Object[]{File.FIELD_SHA1, objects[0]};
		if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readWhereNameOrTextContains))
			return new Object[]{"nameOrText", "%"+queryContext.getFilter().getValue()+"%"};
		else if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readWhereNameContains)) {
			if(Boolean.TRUE.equals(__inject__(ArrayHelper.class).isEmpty(objects)))
				objects = new Object[] {__injectCollectionHelper__().isEmpty(queryContext.getFilter().getFields()) ? queryContext.getFilter().getValue() : queryContext.getFilterByKeysValue(File.FIELD_NAME)};
			return new Object[]{File.FIELD_NAME, "%"+objects[0]+"%"};
		}
		return super.__getQueryParameters__(queryContext, properties, objects);
	}
	
	/**/

}

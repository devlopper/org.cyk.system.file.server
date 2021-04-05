package org.cyk.system.file.server.persistence.impl;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.api.FileTextPersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.system.file.server.persistence.entities.FileText;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.computation.LogicalOperator;
import org.cyk.utility.__kernel__.file.FileHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryStringHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;
import org.cyk.utility.server.persistence.PersistenceFunctionReader;
import org.cyk.utility.server.persistence.PersistenceQueryIdentifierStringBuilder;

@ApplicationScoped
public class FilePersistenceImpl extends AbstractPersistenceEntityImpl<File> implements FilePersistence,Serializable {
	private static final long serialVersionUID = 1L;

	private String readBySha1,readWhereNameContains,readWhereNameOrTextContains,readUniformResourceLocators;
	
	@Override
	protected void __listenPostConstructPersistenceQueries__() {
		super.__listenPostConstructPersistenceQueries__();
		addQueryCollectInstances(readBySha1, __instanciateQueryReadBy__(File.FIELD_SHA1));		
		addQueryCollectInstances(readWhereNameContains, "SELECT tuple FROM File tuple WHERE lower(tuple.name) LIKE lower(:name) ORDER BY tuple.name ASC");
		addQueryCollectInstances(readWhereNameOrTextContains, "SELECT tuple FROM File tuple WHERE lower(tuple.name) LIKE lower(:nameOrText)"
				+ " OR EXISTS(SELECT subTuple FROM FileText subTuple WHERE subTuple.file = tuple AND lower(subTuple.text) LIKE lower(:nameOrText)) "
				+ " ORDER BY tuple.name ASC");
		addQueryCollectInstances(readUniformResourceLocators, "SELECT tuple.uniformResourceLocator FROM File tuple",String.class);
		
		addQueryCollectInstances(readByFiltersLike, 
				"SELECT file FROM File file "
				+ " WHERE ("+QueryStringHelper.formatTupleFieldLikeOrTokens("file", "name","name",4,LogicalOperator.AND)+") "
				+ " ORDER BY file.name ASC");
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
			if(file.getUniformResourceLocator() == null) {
				FileBytes fileBytes = __inject__(FileBytesPersistence.class).readByFile(file);
				if(fileBytes!=null)
					file.setBytes(fileBytes.getBytes());	
			}else {
				try {
					file.setBytes(FileUtils.readFileToByteArray(Paths.get(URI.create(file.getUniformResourceLocator())).toFile()));
				} catch (IOException exception) {
					throw new RuntimeException(exception);
				}
			}			
		}else if(File.FIELD_TEXT.equals(field.getName())) {
			FileText fileText = __inject__(FileTextPersistence.class).readByFile(file);
			if(fileText!=null)
				file.setText(fileText.getText());
		}else if(File.FIELD_NAME_AND_EXTENSION.equals(field.getName())) {
			file.setNameAndExtension(FileHelper.concatenateNameAndExtension(file.getName(), file.getExtension()));	
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
	public Collection<String> readUniformResourceLocators(Properties properties) {
		return __inject__(EntityManager.class).createNamedQuery(readUniformResourceLocators, String.class).getResultList();
	}
	
	@Override
	protected String __getQueryIdentifier__(Class<?> functionClass, Properties properties, Object... parameters) {
		Filter filter = (Filter) Properties.getFromPath(properties, Properties.QUERY_FILTERS);
		if(PersistenceFunctionReader.class.equals(functionClass)) {
			if(filter != null && StringHelper.isNotBlank(filter.getValue()))
				return readWhereNameOrTextContains;
			if(__isFilterByKeys__(properties, File.FIELD_NAME) || 
					(filter!=null && CollectionHelper.isEmpty(filter.getFields()) && StringHelper.isNotBlank(filter.getValue())) )
				return readWhereNameContains;
		}
		return super.__getQueryIdentifier__(functionClass, properties, parameters);
	}
	/*
	@Override
	protected Object[] __getQueryParameters__(QueryContext queryContext, Properties properties,Object... objects) {
		if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readBySha1))
			return new Object[]{File.FIELD_SHA1, objects[0]};
		if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readWhereNameOrTextContains))
			return new Object[]{"nameOrText", "%"+queryContext.getFilter().getValue()+"%"};
		else if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readWhereNameContains)) {
			if(Boolean.TRUE.equals(ArrayHelper.isEmpty(objects)))
				objects = new Object[] {CollectionHelper.isEmpty(queryContext.getFilter().getFields()) ? queryContext.getFilter().getValue() : queryContext.getFilterByKeysValue(File.FIELD_NAME)};
			return new Object[]{File.FIELD_NAME, "%"+objects[0]+"%"};
		}
		if(queryContext.getQuery().isIdentifierEqualsToOrQueryDerivedFromQueryIdentifierEqualsTo(readByFiltersLike)) {
			if(ArrayHelper.isEmpty(objects)) {
				List<String> fileTokens = queryContext.getFieldValueLikes(File.FIELD_NAME,5);
				objects = new Object[] {fileTokens.get(0),fileTokens.get(1),fileTokens.get(2),fileTokens.get(3),fileTokens.get(4)};
			}
			int index = 0;
			objects = new Object[]{"name",objects[index++],"name1",objects[index++],"name2",objects[index++],"name3",objects[index++],"name4",objects[index++]};
			return objects;
		}
		return super.__getQueryParameters__(queryContext, properties, objects);
	}
	*/
	/**/

}

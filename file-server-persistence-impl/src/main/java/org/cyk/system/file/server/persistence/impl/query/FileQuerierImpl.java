package org.cyk.system.file.server.persistence.impl.query;

import static org.cyk.utility.persistence.query.Language.jpql;
import static org.cyk.utility.persistence.query.Language.parenthesis;
import static org.cyk.utility.persistence.query.Language.From.from;
import static org.cyk.utility.persistence.query.Language.Order.asc;
import static org.cyk.utility.persistence.query.Language.Order.order;
import static org.cyk.utility.persistence.query.Language.Select.fields;
import static org.cyk.utility.persistence.query.Language.Select.select;
import static org.cyk.utility.persistence.query.Language.Where.like;
import static org.cyk.utility.persistence.query.Language.Where.or;
import static org.cyk.utility.persistence.query.Language.Where.where;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.file.FileHelper;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutor;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.query.QueryManager;
import org.cyk.utility.persistence.server.query.ReaderByCollection;

public class FileQuerierImpl extends FileQuerier.AbstractImpl implements Serializable {

	@Override
	public Collection<File> readMany(QueryExecutorArguments arguments) {
		if(arguments != null && arguments.getQuery() != null && QUERY_IDENTIFIER_READ_WHERE_FILTER.equals(arguments.getQuery().getIdentifier()))
			return readWhereFilter(arguments);	
		return QueryExecutor.getInstance().executeReadMany(File.class, arguments);
	}
	
	@Override
	public Long count(QueryExecutorArguments arguments) {
		if(arguments != null && arguments.getQuery() != null && QUERY_IDENTIFIER_COUNT_WHERE_FILTER.equals(arguments.getQuery().getIdentifier()))
			return countWhereFilter(arguments);
		return QueryExecutor.getInstance().executeCount(arguments);
	}
	
	@Override
	public Collection<File> readWhereBytesDoNotExists(QueryExecutorArguments arguments) {
		if(arguments == null)
			arguments = new QueryExecutorArguments();
		if(arguments.getQuery() == null)
			arguments.setQueryFromIdentifier(QUERY_IDENTIFIER_READ_WHERE_BYTES_DO_NOT_EXISTS);
		return QueryExecutor.getInstance().executeReadMany(File.class, arguments);
	}
	
	@Override
	public Long countWhereBytesDoNotExists(QueryExecutorArguments arguments) {
		if(arguments == null)
			arguments = new QueryExecutorArguments();
		if(arguments.getQuery() == null)
			arguments.setQueryFromIdentifier(QUERY_IDENTIFIER_COUNT_WHERE_BYTES_DO_NOT_EXISTS);
		return QueryExecutor.getInstance().executeCount(arguments);
	}
	
	@Override
	public Boolean isSizeAllowed(Long size) {
		if(size == null)
			return null;
		return size <= 1024 * 1024;
	}
	
	@Override
	public Collection<File> readWhereFilter(QueryExecutorArguments arguments) {
		prepareWhereFilter(arguments);
		Collection<File> files = QueryExecutor.getInstance().executeReadMany(File.class, arguments);
		if(CollectionHelper.isEmpty(files))
			return null;
		files.forEach(file -> {
			file.setNameAndExtension(FileHelper.concatenateNameAndExtension(file.getName(), file.getExtension()));
		});
		return files;
	}
	
	@Override
	public Long countWhereFilter(QueryExecutorArguments arguments) {
		prepareWhereFilter(arguments);
		return QueryExecutor.getInstance().executeCount(arguments);
	}
	
	private static void prepareWhereFilter(QueryExecutorArguments arguments) {
		Filter filter = new Filter();
		filter.addFieldContainsStringOrWords(PARAMETER_NAME_NAME, NUMBER_OF_WORDS_OF_PARAMETER_NAME_NAME, arguments);
		arguments.setFilter(filter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> readUniformResourceLocators() {
		return EntityManagerGetter.getInstance().get().createNativeQuery(String.format("SELECT t.%1$s FROM File t ORDER BY t.%1$s ASC",File.FIELD_UNIFORM_RESOURCE_LOCATOR))
				.getResultList();
	}
	
	@Override
	public Collection<Object[]> readNamesAndExtensionsByIdentifiers(Collection<String> identifiers) {
		if(CollectionHelper.isEmpty(identifiers))
			return null;
		return new ReaderByCollection.AbstractImpl<String, Object[]>() {
			@SuppressWarnings("unchecked")
			@Override
			protected Collection<Object[]> __read__(Collection<String> values) {
				return EntityManagerGetter.getInstance().get().createNativeQuery("SELECT t.identifier,t.name,t.extension FROM File t WHERE t.identifier IN :identifiers")
						.setParameter("identifiers", values).getResultList();
			}			
		}.read(identifiers);
	}
	
	@Override
	public Collection<Object[]> readNamesAndExtensionsByIdentifiers(String... identifiers) {
		if(ArrayHelper.isEmpty(identifiers))
			return null;
		return readNamesAndExtensionsByIdentifiers(CollectionHelper.listOf(identifiers));
	}
	
	@Override
	public Collection<Object[]> readNamesAndExtensions(Collection<File> files) {
		if(CollectionHelper.isEmpty(files))
			return null;
		return readNamesAndExtensionsByIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(files));
	}
	
	@Override
	public Collection<Object[]> readNamesAndExtensions(File... files) {
		if(ArrayHelper.isEmpty(files))
			return null;
		return readNamesAndExtensions(CollectionHelper.listOf(files));
	}
	
	/**/
	
	public static void initialize() {
		QueryManager.getInstance().register(
			Query.buildSelect(File.class, QUERY_IDENTIFIER_READ_WHERE_FILTER, jpql(
					select(fields("t",File.FIELD_IDENTIFIER,File.FIELD_EXTENSION,File.FIELD_MIME_TYPE,File.FIELD_NAME,File.FIELD_SIZE))
					,getReadWhereFilterFromWhere(),order(asc("t",File.FIELD_NAME))))
				.setTupleFieldsNamesIndexesFromFieldsNames(File.FIELD_IDENTIFIER,File.FIELD_EXTENSION,File.FIELD_MIME_TYPE,File.FIELD_NAME,File.FIELD_SIZE)
			,Query.buildSelect(File.class, QUERY_IDENTIFIER_COUNT_WHERE_FILTER, jpql(select("COUNT(t.identifier)"),getReadWhereFilterFromWhere()))
			
			,Query.buildSelect(File.class, QUERY_IDENTIFIER_READ_WHERE_BYTES_DO_NOT_EXISTS, jpql(select("t"),getReadWhereBytesDoNotExistsFromWhere(),"ORDER BY t.identifier"))
			,Query.buildSelect(File.class, QUERY_IDENTIFIER_COUNT_WHERE_BYTES_DO_NOT_EXISTS, jpql(select("COUNT(t.identifier)"),getReadWhereBytesDoNotExistsFromWhere()))
		);
	}
	
	private static String getReadWhereFilterFromWhere() {
		return jpql(
				from("File t")
				,where(parenthesis(or(  
					like("t", File.FIELD_NAME, PARAMETER_NAME_NAME,NUMBER_OF_WORDS_OF_PARAMETER_NAME_NAME)	
				)))
			);
	}
	
	private static String getReadWhereBytesDoNotExistsFromWhere() {
		return "FROM File t WHERE NOT EXISTS(SELECT fb FROM FileBytes fb WHERE fb.file = t)";
	}
}
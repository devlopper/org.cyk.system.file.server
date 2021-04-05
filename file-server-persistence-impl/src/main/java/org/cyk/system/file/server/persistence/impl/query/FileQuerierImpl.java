package org.cyk.system.file.server.persistence.impl.query;

import static org.cyk.utility.__kernel__.persistence.query.Language.jpql;
import static org.cyk.utility.__kernel__.persistence.query.Language.parenthesis;
import static org.cyk.utility.__kernel__.persistence.query.Language.From.from;
import static org.cyk.utility.__kernel__.persistence.query.Language.Order.asc;
import static org.cyk.utility.__kernel__.persistence.query.Language.Order.order;
import static org.cyk.utility.__kernel__.persistence.query.Language.Select.fields;
import static org.cyk.utility.__kernel__.persistence.query.Language.Select.select;
import static org.cyk.utility.__kernel__.persistence.query.Language.Where.like;
import static org.cyk.utility.__kernel__.persistence.query.Language.Where.or;
import static org.cyk.utility.__kernel__.persistence.query.Language.Where.where;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.file.FileHelper;
import org.cyk.utility.__kernel__.persistence.query.Query;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutor;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.QueryHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.server.query.ReaderByCollection;

public class FileQuerierImpl extends FileQuerier.AbstractImpl implements Serializable {

	@Override
	public Collection<File> readMany(QueryExecutorArguments arguments) {
		if(arguments != null && arguments.getQuery() != null && QUERY_IDENTIFIER_READ_WHERE_FILTER.equals(arguments.getQuery().getIdentifier()))
			return readWhereFilter(arguments);	
		if(arguments != null && arguments.getQuery() != null && QUERY_IDENTIFIER_READ_VIEW_01.equals(arguments.getQuery().getIdentifier()))
			prepare(arguments);				
		return QueryExecutor.getInstance().executeReadMany(File.class, arguments);
	}
	
	@Override
	public Long count(QueryExecutorArguments arguments) {
		if(arguments != null && arguments.getQuery() != null && QUERY_IDENTIFIER_COUNT_WHERE_FILTER.equals(arguments.getQuery().getIdentifier()))
			return countWhereFilter(arguments);
		if(arguments != null && arguments.getQuery() != null && QUERY_IDENTIFIER_COUNT_VIEW_01.equals(arguments.getQuery().getIdentifier()))
			prepare(arguments);			
		return QueryExecutor.getInstance().executeCount(arguments);
	}
	
	private static void prepare(QueryExecutorArguments arguments) {
		Filter filter = new Filter();
		int[] index = {1};
		arguments.getFilterFieldValueLikes(File.FIELD_NAME, 3).forEach(value -> {
			filter.addField(File.FIELD_NAME+(index[0]++), value);
		});
		arguments.setFilter(filter);
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
		QueryHelper.addQueries(
			Query.build(Query.FIELD_IDENTIFIER,QUERY_IDENTIFIER_READ_VIEW_01,Query.FIELD_TUPLE_CLASS,File.class,Query.FIELD_RESULT_CLASS
			,File.class,Query.FIELD_VALUE,QUERY_VALUE_READ_VIEW_01).setTupleFieldsNamesIndexes(QUERY_VALUE_READ_VIEW_01_TUPLE_FIELDS_NAMES_INDEXES)
			
			,Query.buildCount(QUERY_IDENTIFIER_COUNT_VIEW_01,QUERY_VALUE_COUNT_VIEW_01)
			
			,Query.buildSelect(File.class, QUERY_IDENTIFIER_READ_WHERE_FILTER, jpql(
					select(fields("t",File.FIELD_IDENTIFIER,File.FIELD_EXTENSION,File.FIELD_MIME_TYPE,File.FIELD_NAME,File.FIELD_SIZE))
					,getReadWhereFilterFromWhere(),order(asc("t",File.FIELD_NAME))))
				.setTupleFieldsNamesIndexesFromFieldsNames(File.FIELD_IDENTIFIER,File.FIELD_EXTENSION,File.FIELD_MIME_TYPE,File.FIELD_NAME,File.FIELD_SIZE)
			,Query.buildSelect(File.class, QUERY_IDENTIFIER_COUNT_WHERE_FILTER, jpql(select("COUNT(t.identifier)"),getReadWhereFilterFromWhere()))
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
}

package org.cyk.system.file.server.persistence.api.query;
import static org.cyk.utility.__kernel__.persistence.query.Language.jpql;
import static org.cyk.utility.__kernel__.persistence.query.Language.parenthesis;
import static org.cyk.utility.__kernel__.persistence.query.Language.From.from;
import static org.cyk.utility.__kernel__.persistence.query.Language.Order.asc;
import static org.cyk.utility.__kernel__.persistence.query.Language.Order.order;
import static org.cyk.utility.__kernel__.persistence.query.Language.Select.fields;
import static org.cyk.utility.__kernel__.persistence.query.Language.Select.select;
import static org.cyk.utility.__kernel__.persistence.query.Language.Where.like;
//import static org.cyk.utility.__kernel__.persistence.query.Language.Where.equals;
import static org.cyk.utility.__kernel__.persistence.query.Language.Where.or;
import static org.cyk.utility.__kernel__.persistence.query.Language.Where.where;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.Helper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.computation.LogicalOperator;
import org.cyk.utility.__kernel__.file.FileHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.Language;
import org.cyk.utility.__kernel__.persistence.query.Querier;
import org.cyk.utility.__kernel__.persistence.query.Query;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutor;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.QueryHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryIdentifierBuilder;
import org.cyk.utility.__kernel__.persistence.query.QueryName;
import org.cyk.utility.__kernel__.persistence.query.QueryStringHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.value.Value;

public interface FileQuerier extends Querier {

	Integer NUMBER_OF_WORDS_OF_PARAMETER_NAME_NAME = 6;
	
	String QUERY_IDENTIFIER_READ_WHERE_FILTER = QueryIdentifierBuilder.getInstance().build(File.class, QueryName.READ_WHERE_FILTER.getValue());
	Collection<File> readWhereFilter(QueryExecutorArguments arguments);
	
	String QUERY_IDENTIFIER_COUNT_WHERE_FILTER = QueryIdentifierBuilder.getInstance().build(File.class, QueryName.COUNT_WHERE_FILTER.getValue());
	Long countWhereFilter(QueryExecutorArguments arguments);
	
	/* View 01 */
	String QUERY_IDENTIFIER_READ_VIEW_01 = QueryIdentifierBuilder.getInstance().build(File.class, "read.view.01");
	Map<String,Integer> QUERY_VALUE_READ_VIEW_01_TUPLE_FIELDS_NAMES_INDEXES = MapHelper.instantiateStringIntegerByStrings(File.FIELD_IDENTIFIER,File.FIELD_NAME);
	String QUERY_VALUE_READ_VIEW_01_WHERE = Language.Where.of(QueryStringHelper.formatTupleFieldLike("t", "name",3,LogicalOperator.AND));
	String QUERY_VALUE_READ_VIEW_01 = Language.of(Language.Select.of("t.identifier,t.name,t.extension"),Language.From.of("File t"),QUERY_VALUE_READ_VIEW_01_WHERE, Language.Order.of(Language.Order.asc("t", File.FIELD_NAME)));	
	
	String QUERY_IDENTIFIER_COUNT_VIEW_01 = QueryIdentifierBuilder.getInstance().build(File.class, "count.view.01");
	String QUERY_VALUE_COUNT_VIEW_01 = "SELECT COUNT(t.identifier) FROM File t "+ QUERY_VALUE_READ_VIEW_01_WHERE;
		
	/**/
	
	Collection<File> readMany(QueryExecutorArguments arguments);
	Long count(QueryExecutorArguments arguments);
	
	/**/
	
	public static abstract class AbstractImpl extends Querier.AbstractImpl implements FileQuerier,Serializable {
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
	}
	
	static FileQuerier getInstance() {
		return Helper.getInstance(FileQuerier.class, INSTANCE);
	}
	
	Value INSTANCE = new Value();
	
	/**/
	
	static void initialize() {
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
	
	static String getReadWhereFilterFromWhere() {
		return jpql(
				from("File t")
				,where(parenthesis(or(  
					like("t", File.FIELD_NAME, PARAMETER_NAME_NAME,NUMBER_OF_WORDS_OF_PARAMETER_NAME_NAME)	
				)))
			);
	}
}
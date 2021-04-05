package org.cyk.system.file.server.persistence.api.query;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.Helper;
import org.cyk.utility.__kernel__.computation.LogicalOperator;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.Language;
import org.cyk.utility.__kernel__.persistence.query.Querier;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.QueryIdentifierBuilder;
import org.cyk.utility.__kernel__.persistence.query.QueryName;
import org.cyk.utility.__kernel__.persistence.query.QueryStringHelper;
import org.cyk.utility.__kernel__.value.Value;

public interface FileQuerier extends Querier {

	Collection<Object[]> readNamesAndExtensionsByIdentifiers(Collection<String> identifiers);
	Collection<Object[]> readNamesAndExtensionsByIdentifiers(String...identifiers);
	Collection<Object[]> readNamesAndExtensions(Collection<File> files);
	Collection<Object[]> readNamesAndExtensions(File...files);
	
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
		
	}
	
	static FileQuerier getInstance() {
		return Helper.getInstance(FileQuerier.class, INSTANCE);
	}
	
	Value INSTANCE = new Value();
}
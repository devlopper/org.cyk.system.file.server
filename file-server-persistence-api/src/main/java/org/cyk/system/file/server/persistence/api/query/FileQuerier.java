package org.cyk.system.file.server.persistence.api.query;
import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.Helper;
import org.cyk.utility.persistence.query.Querier;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.query.QueryIdentifierBuilder;
import org.cyk.utility.persistence.query.QueryName;
import org.cyk.utility.__kernel__.value.Value;

public interface FileQuerier extends Querier {

	File readOne(QueryExecutorArguments arguments);
	Collection<File> readMany(QueryExecutorArguments arguments);
	Long count(QueryExecutorArguments arguments);
	
	String QUERY_IDENTIFIER_READ_DYNAMIC_ONE = QueryIdentifierBuilder.getInstance().build(File.class, QueryName.READ_DYNAMIC_ONE);
	String QUERY_IDENTIFIER_READ_DYNAMIC = QueryIdentifierBuilder.getInstance().build(File.class, QueryName.READ_DYNAMIC);
	String QUERY_IDENTIFIER_COUNT_DYNAMIC = QueryIdentifierBuilder.getInstance().build(File.class, QueryName.COUNT_DYNAMIC);
	
	Collection<Object[]> readNamesAndExtensionsByIdentifiers(Collection<String> identifiers);
	Collection<Object[]> readNamesAndExtensionsByIdentifiers(String...identifiers);
	Collection<Object[]> readNamesAndExtensions(Collection<File> files);
	Collection<Object[]> readNamesAndExtensions(File...files);
	
	Collection<String> readUniformResourceLocators();
	
	String QUERY_IDENTIFIER_READ_WHERE_BYTES_DO_NOT_EXISTS = QueryIdentifierBuilder.getInstance().build(File.class, "readWhereBytesDoNotExists");
	Collection<File> readWhereBytesDoNotExists(QueryExecutorArguments arguments);
	
	String QUERY_IDENTIFIER_COUNT_WHERE_BYTES_DO_NOT_EXISTS = QueryIdentifierBuilder.getInstance().build(File.class, "countWhereBytesDoNotExists");
	Long countWhereBytesDoNotExists(QueryExecutorArguments arguments);
	
	Boolean isSizeAllowed(Long size);
	
	Integer NUMBER_OF_WORDS_OF_PARAMETER_NAME_NAME = 6;
	
	String QUERY_IDENTIFIER_READ_WHERE_FILTER = QueryIdentifierBuilder.getInstance().build(File.class, QueryName.READ_WHERE_FILTER.getValue());
	Collection<File> readWhereFilter(QueryExecutorArguments arguments);
	
	String QUERY_IDENTIFIER_COUNT_WHERE_FILTER = QueryIdentifierBuilder.getInstance().build(File.class, QueryName.COUNT_WHERE_FILTER.getValue());
	Long countWhereFilter(QueryExecutorArguments arguments);
	
	/**/
	
	public static abstract class AbstractImpl extends Querier.AbstractImpl implements FileQuerier,Serializable {
		
	}
	
	static FileQuerier getInstance() {
		return Helper.getInstance(FileQuerier.class, INSTANCE);
	}
	
	Value INSTANCE = new Value();
}
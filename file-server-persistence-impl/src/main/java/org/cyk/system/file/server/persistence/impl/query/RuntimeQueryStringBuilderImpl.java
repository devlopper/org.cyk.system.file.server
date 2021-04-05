package org.cyk.system.file.server.persistence.impl.query;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.computation.SortOrder;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.query.QueryName;
import org.cyk.utility.persistence.server.query.string.LikeStringBuilder;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;
import org.cyk.utility.persistence.server.query.string.RuntimeQueryStringBuilder;
import org.cyk.utility.persistence.server.query.string.WhereStringBuilder.Predicate;;

@org.cyk.system.file.server.annotation.System
public class RuntimeQueryStringBuilderImpl extends RuntimeQueryStringBuilder.AbstractImpl implements Serializable {
	
	@Override
	protected Collection<String> getDefaultProjections(QueryExecutorArguments arguments) {
		if(arguments.getQuery().isIdentifierEquals(File.class, QueryName.READ_DYNAMIC))
			return List.of(File.FIELD_IDENTIFIER,File.FIELD_NAME_AND_EXTENSION);
		return super.getDefaultProjections(arguments);
	}
	
	@Override
	protected Map<String, SortOrder> getDefaultSortOrders(QueryExecutorArguments arguments) {
		if(arguments.getQuery().isIdentifierEquals(File.class, QueryName.READ_DYNAMIC))
			return Map.of(File.FIELD_NAME,SortOrder.ASCENDING);
		return super.getDefaultSortOrders(arguments);
	}
	
	@Override
	protected void populatePredicate(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		super.populatePredicate(arguments, builderArguments, predicate, filter);
		if(arguments.getQuery().isIdentifierEqualsDynamic(File.class)) {
			if(arguments.getFilter().hasFieldWithPath(File.FIELD_NAME)) {
				predicate.add(LikeStringBuilder.getInstance().build("t", File.FIELD_NAME, File.FIELD_NAME, 2));
				filter.addFieldContainsStringOrWords(File.FIELD_NAME, 2, arguments);
			}
		}
	}
}
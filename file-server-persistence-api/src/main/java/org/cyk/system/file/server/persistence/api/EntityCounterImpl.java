package org.cyk.system.file.server.persistence.api;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.utility.__kernel__.persistence.query.EntityCounter;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;

@org.cyk.system.file.server.annotation.System
public class EntityCounterImpl extends EntityCounter.AbstractImpl implements Serializable {

	@Override
	public <T> Long count(Class<T> tupleClass, QueryExecutorArguments arguments) {
		if(arguments != null && arguments.getQuery() != null) {
			if(FileQuerier.QUERY_IDENTIFIER_COUNT_VIEW_01.equals(arguments.getQuery().getIdentifier()))
				return FileQuerier.getInstance().count(arguments);
			if(FileQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER.equals(arguments.getQuery().getIdentifier()))
				return FileQuerier.getInstance().count(arguments);
		}
		return super.count(tupleClass, arguments);
	}
}
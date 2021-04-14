package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

@org.cyk.system.file.server.annotation.System
public class EntityCounterImpl extends org.cyk.utility.persistence.server.query.EntityCounterImpl implements Serializable {

	@Override
	public Long count(Class<?> tupleClass, QueryExecutorArguments arguments) {
		if(arguments != null && arguments.getQuery() != null) {
			if(FileQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER.equals(arguments.getQuery().getIdentifier()))
				return FileQuerier.getInstance().count(arguments);
		}
		return super.count(tupleClass, arguments);
	}
}
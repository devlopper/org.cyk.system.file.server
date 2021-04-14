package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

@org.cyk.system.file.server.annotation.System
public class EntityReaderImpl extends org.cyk.utility.persistence.server.query.EntityReaderImpl implements Serializable {

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> readMany(Class<T> resultClass, QueryExecutorArguments arguments) {
		if(arguments != null && arguments.getQuery() != null) {
			if(FileQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER.equals(arguments.getQuery().getIdentifier()))
				return (Collection<T>) FileQuerier.getInstance().readMany(arguments);
		}
		return super.readMany(resultClass, arguments);
	}	
}
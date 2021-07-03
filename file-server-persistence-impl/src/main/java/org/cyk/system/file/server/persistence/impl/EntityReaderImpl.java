package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.query.QueryIdentifierBuilder;

@org.cyk.system.file.server.annotation.System
public class EntityReaderImpl extends org.cyk.utility.persistence.server.query.EntityReaderImpl implements Serializable {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T readOne(Class<T> tupleClass, QueryExecutorArguments arguments) {
		if(Boolean.TRUE.equals(QueryIdentifierBuilder.builtFrom(arguments, File.class)))
			return (T) FileQuerier.getInstance().readOne(arguments);
		return super.readOne(tupleClass, arguments);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> readMany(Class<T> resultClass, QueryExecutorArguments arguments) {
		if(Boolean.TRUE.equals(QueryIdentifierBuilder.builtFrom(arguments, File.class)))
			return (Collection<T>) FileQuerier.getInstance().readMany(arguments);
		if(arguments != null && arguments.getQuery() != null) {
			if(FileQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER.equals(arguments.getQuery().getIdentifier()))
				return (Collection<T>) FileQuerier.getInstance().readMany(arguments);
		}
		return super.readMany(resultClass, arguments);
	}	
}
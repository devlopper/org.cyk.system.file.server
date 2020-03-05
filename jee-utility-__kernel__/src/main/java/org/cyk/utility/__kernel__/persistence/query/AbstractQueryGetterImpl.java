package org.cyk.utility.__kernel__.persistence.query;

import java.io.Serializable;

import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.jboss.weld.exceptions.IllegalArgumentException;

public abstract class AbstractQueryGetterImpl extends AbstractObject implements QueryGetter,Serializable {

	@Override
	public Query get(Class<?> resultClass, String queryIdentifier, String queryValue) {
		if(resultClass == null)
			throw new IllegalArgumentException("result class is required");
		if(StringHelper.isBlank(queryIdentifier) && StringHelper.isBlank(queryValue))
			throw new IllegalArgumentException("query identifier or query value is required");
		Query query = null;
		if(StringHelper.isBlank(queryValue)) {
			//no query value specified then get query from registered
			query = QueryHelper.getQueries().getBySystemIdentifier(queryIdentifier);
			if(query == null)
				throw new IllegalArgumentException(String.format("query with identifier %s has not been registered yet", queryIdentifier));
			if(StringHelper.isBlank(query.getValue()))
				throw new IllegalArgumentException(String.format("%s has invalid value : %s", queryIdentifier,query,query.getValue()));
		}else {
			//query value has been specified then register it if not yet done
			if(StringHelper.isBlank(queryIdentifier)) {
				query = Query.build(Query.FIELD_VALUE,queryValue);
				LogHelper.logWarning(String.format("You should assign an identifier to your read query %s(%s)", resultClass.getSimpleName(),queryValue), getClass());
			}else {
				query = QueryHelper.getQueries().getBySystemIdentifier(queryIdentifier);
				if(query == null) {
					query = Query.build(Query.FIELD_IDENTIFIER,queryIdentifier,Query.FIELD_VALUE,queryValue,Query.FIELD_RESULT_CLASS,resultClass);
					QueryHelper.addQueries(query);
				}else {
					if(!queryValue.equalsIgnoreCase(query.getValue()))
						throw new IllegalArgumentException(String.format("%s has already been registered. it cannot be overriden with %s", query,queryValue));
				}
			}
		}
		return query;
	}
	
}
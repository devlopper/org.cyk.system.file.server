package org.cyk.utility.__kernel__.persistence.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.persistence.Tuple;

import org.cyk.utility.__kernel__.Helper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.klass.ClassHelper;
import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.RuntimeException;
import org.cyk.utility.__kernel__.value.Value;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public interface QueryResultMapper {

	<T> Collection<T> map(Class<T> resultClass,Arguments arguments);
	
	/**/
	
	public static abstract class AbstractImpl extends AbstractObject implements QueryResultMapper,Serializable {
		
		@Override
		public <T> Collection<T> map(Class<T> resultClass,Arguments arguments) {
			if(resultClass == null)
				throw new RuntimeException("Result class is required");
			if(arguments == null)
				throw new RuntimeException("Arguments are required");
			if(CollectionHelper.isNotEmpty(arguments.objects) && CollectionHelper.isNotEmpty(arguments.tuples))
				throw new RuntimeException("Collection element must be either object[] or tuple but not both");
			if(CollectionHelper.isEmpty(arguments.objects) && CollectionHelper.isEmpty(arguments.tuples))
				return null;
			arguments.prepare();
			Collection<T> collection = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(arguments.objects)) {				
				arguments.objects.forEach(array -> {
					collection.add(instantiate(resultClass, arguments.query.getTupleFieldsNamesIndexes(), array));
				});
			}else if(CollectionHelper.isNotEmpty(arguments.tuples)){
				arguments.tuples.forEach(tuple -> {
					
				});
			}
			arguments.finalise();
			return collection;
		}
		
		private <T> T instantiate(Class<T> klass,Map<String,Integer> fieldsNamesIndexes,Object[] array) {
			T instance = ClassHelper.instanciate(klass);
			fieldsNamesIndexes.forEach( (fieldName,index) -> {
				FieldHelper.write(instance, fieldName, array[index]);
			});
			return instance;
		}
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain = true)
	public static class Arguments extends AbstractObject implements Serializable {
		private Query query;
		private Collection<Object[]> objects;
		private Collection<Tuple> tuples;
		
		public Arguments prepare() {
			if(query == null)
				throw new RuntimeException("Query is required");
			if(StringHelper.isEmpty(query.getValue()))
				throw new RuntimeException("Query value is required");
			
			return this;
		}
		
		public Arguments finalise() {
			
			return this;
		}
	}
	
	/**/
	
	static QueryResultMapper getInstance() {
		return Helper.getInstance(QueryResultMapper.class, INSTANCE);
	}
	
	Value INSTANCE = new Value();
}
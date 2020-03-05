package org.cyk.utility.client.controller.web.jsf.primefaces.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractInputOutput<VALUE> extends AbstractObjectAjaxable implements Serializable {

	protected VALUE value;
	
	protected Object object;
	protected Field field;
	
	/**/
	
	/**/
	
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_FIELD = "field";
	public static final String FIELD_OBJECT = "object";
	
	/**/
	
	/**/
	
	@SuppressWarnings("rawtypes")
	public static abstract class AbstractConfiguratorImpl<IO extends AbstractInputOutput> extends AbstractObjectAjaxable.AbstractConfiguratorImpl<IO> implements Serializable {

		@SuppressWarnings("unchecked")
		@Override
		public void configure(IO io, Map<Object, Object> arguments) {
			super.configure(io, arguments);
			if(io.field == null) {
				String fieldName = (String) MapHelper.readByKey(arguments, FIELD_FIELD_NAME);
				if(StringHelper.isNotBlank(fieldName)) {
					Object object = MapHelper.readByKey(arguments, FIELD_OBJECT);
					if(object != null) {
						io.object = object;
						io.field = FieldHelper.getByName(object.getClass(), fieldName);
					}
				}
			}
			
			if(io.object != null && io.field != null) {
				io.value = FieldHelper.read(io.object, io.field);
			}
		}
		
		public static final String FIELD_OBJECT = "object";
		public static final String FIELD_FIELD_NAME = "fieldName";
	}
}
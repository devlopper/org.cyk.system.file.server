package org.cyk.utility.server.representation;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.representation.AbstractRepresentationObject;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.field.FieldName;
import org.cyk.utility.field.FieldValueGetter;
import org.cyk.utility.field.FieldValueSetter;
import org.cyk.utility.number.NumberHelper;
import org.cyk.utility.server.persistence.jpa.Persistence;
import org.cyk.utility.value.ValueUsageType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
public abstract class AbstractEntity<PERSISTENCE_ENTITY> extends AbstractRepresentationObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String identifier;
	private String code;
	
	public AbstractEntity(PERSISTENCE_ENTITY persistenceEntity) {
		/*if(persistenceEntity!=null) {
			Object identifier = __inject__(FieldHelper.class).getFieldValueSystemIdentifier(persistenceEntity);
			if( identifier!=null)
				this.identifier = identifier.toString();
			identifier = __inject__(FieldHelper.class).getFieldValueBusinessIdentifier(persistenceEntity);
			if( identifier!=null)
				this.code = identifier.toString();
		}
		*/
		copy(persistenceEntity);
	}
	
	public AbstractEntity<PERSISTENCE_ENTITY> copy(PERSISTENCE_ENTITY persistenceEntity){
		for(FieldName indexFieldName : new FieldName[] {FieldName.IDENTIFIER})
			for(ValueUsageType indexValueUsageType : new ValueUsageType[] {ValueUsageType.SYSTEM,ValueUsageType.BUSINESS}) {
				Object value = __inject__(FieldValueGetter.class).execute(persistenceEntity, indexFieldName, indexValueUsageType).getOutput();
				__inject__(FieldValueSetter.class).execute(this, indexFieldName, indexValueUsageType,__stringify__(value));
			}		
		return this;
	}
	
	protected String __stringify__(Object value) {
		return value == null ? null : value.toString();
	}
	
	public abstract PERSISTENCE_ENTITY getPersistenceEntity();
	
	protected <T> T __getFromBusinessIdentifier__(Class<T> aClass,Object identifier){
		return identifier == null ? null : __inject__(Persistence.class).readOne(aClass,identifier,new Properties().setValueUsageType(ValueUsageType.BUSINESS));
	}
	
	protected Integer __getIntegerFrom__(Object object) {
		return __inject__(NumberHelper.class).getInteger(object);
	}
	
	/*protected <T> T __getFromBusinessIdentifier__(Class<T> aClass,Object identifier){
		
	}*/
	
	/*
	public AbstractEntity<PERSISTENCE_ENTITY> setFromBusinessIdentifier(Field field,Object identifier){
		Class<?> type = __inject__(FieldTypeGetter.class).execute(field).getOutput();
		__inject__(FieldValueSetter.class).setObject(this).setField(field).setValue(__getFromBusinessIdentifier__(type, identifier)).execute();
		return this;
	}
	
	public AbstractEntity<PERSISTENCE_ENTITY> setFromBusinessIdentifier(String fieldName,Object identifier){
		return setFromBusinessIdentifier(__inject__(CollectionHelper.class).getFirst(__inject__(FieldGetter.class).execute(getClass(), fieldName).getOutput()), identifier);
	}*/
}

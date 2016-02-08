package org.cyk.utility.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class ClassRepository implements Serializable {

	private static final long serialVersionUID = 2331517379189246417L;

	private static final ClassRepository INSTANCE = new ClassRepository();
	
	public static Boolean ENABLED = Boolean.TRUE;
	
	private Map<Class<?>, Clazz> map = new HashMap<>();
	private Boolean addOnGet = Boolean.TRUE;
	
	/**/
	
	public Clazz add(Class<?> value){
		Clazz clazz = map.get(value);
		if(clazz==null){
			map.put(value, clazz = new Clazz(value));
			populateFields(value, clazz.getFields());
			//populate(value,null, clazz.get__fields__());
		}
		//populate(value,null, clazz.get__fields__());
		return clazz;
	}
	
	public Clazz get(Class<?> value){
		Clazz clazz = map.get(value);
		if(clazz==null && Boolean.TRUE.equals(addOnGet))
			return add(value);
		return clazz;
	}
	
	public Collection<Field> getFields(Class<?> value){
		Collection<Field> fields = new ArrayList<>();
		if(Boolean.TRUE.equals(ENABLED))
			fields.addAll(get(value).getFields());
		else
			populateFields(value, fields);
		return fields;
	}
	
	/**/
	
	private void populateFields(Class<?> type,Collection<Field> fields) {
		//super class fields first
		if (type.getSuperclass() != null)
			populateFields(type.getSuperclass(),fields);
		//declared class fields second
		for (Field field : type.getDeclaredFields())
			fields.add(field);
	}
	
	private void populate(Class<?> aClass,ClassField parentClassField,Collection<ClassField> classFields) {
		Collection<Field> fields = new HashSet<>(getFields(aClass==null ? parentClassField.getField().getType() : aClass));
		//Collection<Field> fields = new HashSet<>(get(aClass==null ? parentClassField.getField().getType() : aClass).getFields());
		//Collection<Field> fields = parentClassField==null ? parentClassField.get new HashSet<>(getFields(aClass));
		for(Field field : fields){
			ClassField childClassField = new ClassField();
			classFields.add(childClassField);
			childClassField.setParent(parentClassField);
			childClassField.setField(field);
			if(!field.getType().getName().startsWith("java.")){
				populate(null,childClassField, classFields);
			}
		}
	}
	
	public void populate(Clazz clazz) {
		clazz.__fields__.clear();
		populate(clazz.getValue(), null, clazz.__fields__);
	}
	
	private void populateAllDependentFields(Field field,Collection<ClassField> fields) {
		/*ClassField parent = null;
		for(ClassField classField : fields)
			if(classField.getField().getDeclaringClass().equals(type))
				
		
		
		Collection<Field> typeFields = new HashSet<>(getFields(type));
		//for(Field field : typeFields)
		//	fields.add(new org.cyk.utility.common.Field(parent, type, field.getName()));
		for(Field typeField : typeFields){
			ClassField parent = null;
			for(ClassField classField : fields)
				if(classField.getField().get)
			ClassField classField = new ClassField(parent, clazz, field);
			if(!typeField.getType().getName().startsWith("java.")){
				populateAllDependentFields(typeField.getType(), fields);
			}
		}*/
	}
	
	/**/
	
	public static ClassRepository getInstance() {
		return INSTANCE;
	}
	
	@Getter @Setter
	public static class Clazz implements Serializable {

		private static final long serialVersionUID = -4384997700934391685L;

		private final Class<?> value;
		private final Collection<Field> fields = new ArrayList<>();
		private final Collection<Field> allDependentFields = new HashSet<>();
		
		private final Collection<ClassField> __fields__ = new ArrayList<>();
		
		public Clazz(Class<?> value) {
			super();
			this.value = value;
		}
		
		public void populateFields(){
			
		}
	}
	
}

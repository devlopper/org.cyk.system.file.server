package org.cyk.utility.__kernel__.field;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.cyk.utility.__kernel__.value.ValueUsageType;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public interface FieldHelper {
	
	/* manipulate name */
	
	/**
	 * Join many field paths to build a one field path
	 * Example : f1 + f2.f3 = f1.f2.f3
	 * @param paths to join
	 * @return field path
	 */
	static String join(Collection<String> paths) {
		if(paths == null || paths.isEmpty())
			return null;
		return StringUtils.join(paths, DOT);
	}

	/**
	 * {@link #join}
	 * @param paths to join
	 * @return
	 */
	static String join(String... paths) {
		if(paths == null || paths.length == 0)
			return null;
		return StringUtils.join(paths, DOT);
	}

	/**
	 * Disjoin many field paths to a collection of field names
	 * @param paths to disjoin
	 * @return
	 */
	static List<String> disjoin(Collection<String> paths) {
		if(paths == null || paths.isEmpty())
			return null;
		String path = join(paths);
		return List.of(StringUtils.split(path,DOT));
	}
	
	/**
	 * {@link #disjoin}
	 * @param paths to disjoin
	 * @return
	 */
	static List<String> disjoin(String... paths) {
		return paths == null || paths.length == 0 ? null : disjoin(List.of(paths));
	}
	
	static String getName(Class<?> klass,FieldName fieldName,ValueUsageType valueUsageType) {
		ClassFieldNameValueUsageType classFieldNameValueUsageType = new ClassFieldNameValueUsageType(klass,fieldName,valueUsageType);
		String value = CLASSES_FIELDNAMES_MAP.get(classFieldNameValueUsageType);
		if(value == null)
			setName(classFieldNameValueUsageType, value = fieldName.getByValueUsageType(valueUsageType));
		return value;
	}
	
	static void setName(Class<?> klass,FieldName fieldName,ValueUsageType valueUsageType,String name) {
		setName(new ClassFieldNameValueUsageType(klass,fieldName,valueUsageType), name);
	}
	
	static void setName(ClassFieldNameValueUsageType classFieldNameValueUsageType,String name) {
		CLASSES_FIELDNAMES_MAP.put(classFieldNameValueUsageType, name);
	}
	
	static Collection<String> getNames(Collection<Field> fields) {
		if(fields == null || fields.isEmpty())
			return null;
		return fields.stream().map(Field::getName).collect(Collectors.toList());
	}
	
	/* get field */
	
	static Collection<Field> get(Class<?> klass) {
		if(klass == null)
			return null;
		List<Field> fields = CLASS_FIELDS_MAP.get(klass);
		if(fields != null)
			return fields;
		Class<?> indexClass = klass;
		do {
			Field[] declaredFields = indexClass.getDeclaredFields();
			if(declaredFields.length > 0) {
				if(fields == null)
					fields = new ArrayList<>();
				fields.addAll(0,List.of(declaredFields));
			}
			if(indexClass.isInterface()) {
				if(indexClass.getGenericInterfaces().length == 0)
					indexClass = null;
				else
					indexClass = (Class<?>) indexClass.getGenericInterfaces()[0];
				
			}else
				indexClass = indexClass.getSuperclass();
		}while(indexClass!=null && !Object.class.equals(indexClass));
		CLASS_FIELDS_MAP.put(klass, Collections.unmodifiableList(fields));
		return fields;
	}
	
	static Field getByName(Class<?> klass, List<String> fieldNames) {
		if(klass == null || fieldNames == null || fieldNames.isEmpty())
			return null;
		String fieldName = join(fieldNames);
		String key = klass.getName()+DOT+fieldName;
		Field field = FIELDS_MAP.get(key);
		if(field != null)
			return field;
		fieldNames = disjoin(fieldName);
		Integer numberOfFieldNames = fieldNames.size();
		Integer count = 0;
		Class<?> indexClass = klass;
		String indexFieldName;
		do {
			indexFieldName = fieldNames.get(count);
			Collection<Field> fields = get(indexClass);
			if(fields == null)
				return null;
			for(Field index : fields)
				if(index.getName().equals(indexFieldName)) {
					if(numberOfFieldNames - 1 == count) {
						FIELDS_MAP.put(key, index);
						return index;
					}
					indexClass = index.getType();
					break;	
				}
			count++;			
		}while(count < numberOfFieldNames && !indexClass.equals(Object.class));
		return field;
	}
	
	static Field getByName(Class<?> klass, String... fieldNames) {
		if(klass == null || fieldNames == null || fieldNames.length == 0)
			return null;
		return getByName(klass,List.of(fieldNames));
	}
	
	static Field getByName(Class<?> klass,FieldName fieldName,ValueUsageType valueUsageType) {
		return klass == null || fieldName == null || valueUsageType == null ? null : getByName(klass, getName(klass, fieldName, valueUsageType));
	}
	
	static Collection<Field> filter(Class<?> klass, String fieldNameRegularExpression,Integer modifiers) {
		if(klass == null)
			return null;
		Collection<Field> collection = get(klass);
		if(collection ==null || collection.isEmpty())
			return null;
		if(fieldNameRegularExpression!= null && fieldNameRegularExpression.isBlank())
			fieldNameRegularExpression = null;
		Pattern pattern = null;
		if(fieldNameRegularExpression!=null)
			pattern = Pattern.compile(fieldNameRegularExpression);
		Collection<Field> fields = null;
		for(Field index : collection) {
			if(pattern != null && !pattern.matcher(index.getName()).find())
				continue;
			if(modifiers != null) {
				if(!(Modifier.isAbstract(index.getModifiers()) && Modifier.isAbstract(modifiers)))
					continue;
				if(!(Modifier.isFinal(index.getModifiers()) && Modifier.isFinal(modifiers)))
					continue;
				if(!(Modifier.isNative(index.getModifiers()) && Modifier.isNative(modifiers)))
					continue;
				if(!(Modifier.isPrivate(index.getModifiers()) && Modifier.isPrivate(modifiers)))
					continue;
				if(!(Modifier.isProtected(index.getModifiers()) && Modifier.isProtected(modifiers)))
					continue;
				if(!(Modifier.isPublic(index.getModifiers()) && Modifier.isPublic(modifiers)))
					continue;
				if(!(Modifier.isStatic(index.getModifiers()) && Modifier.isStatic(modifiers)))
					continue;
				if(!(Modifier.isStrict(index.getModifiers()) && Modifier.isStrict(modifiers)))
					continue;
				if(!(Modifier.isTransient(index.getModifiers()) && Modifier.isTransient(modifiers)))
					continue;
				if(!(Modifier.isVolatile(index.getModifiers()) && Modifier.isVolatile(modifiers)))
					continue;
			}
			if(fields == null)
				fields = new ArrayList<Field>();
			fields.add(index);
		}
		return fields;
	}
	
	/* get set type */
	
	static Class<?> getFieldType(Class<?> aClass, String fieldName) {
		Class<?> clazz = null;
		if(fieldName!=null && fieldName.length()>0) {
			Field field = FieldUtils.getField(aClass, fieldName, Boolean.TRUE);
			if(field!=null)
				clazz = field.getType();
		}
		return clazz;
	}
	
	static Type getType(Field field,Class<?> klass) {
		if(field == null)
			return null;
		if(klass == null)
			klass = field.getDeclaringClass();
		String key = klass.getName()+DOT+field.getName();
		Type type = TYPES_MAP.get(key);
		if(type != null)
			return type;
		if(field.getType().equals(field.getGenericType()))
			type = field.getType();
		else if(field.getGenericType() instanceof ParameterizedType) {
			type = field.getGenericType();
			for(Type index : ((ParameterizedType)type).getActualTypeArguments()) {
				if(index instanceof TypeVariable<?>) {
					Type actualType = getActualType(klass,field, (TypeVariable<?>) index);
					if(actualType != null)
						type = new ParameterizedType() {
							@Override
							public Type getRawType() {
								return field.getType();
							}
							
							@Override
							public Type getOwnerType() {
								return null;
							}
							
							@Override
							public Type[] getActualTypeArguments() {
								return new Type[]{actualType};
							}
						};
				}
			}
		}else if(field.getGenericType() instanceof TypeVariable<?>) {
			type = getActualType(klass,field, (TypeVariable<?>) field.getGenericType());
		}else
			throw new RuntimeException("get field type from generic "+field.getGenericType()+" not yet handled");
		if(type == null)
			System.err.println("type not found for "+klass+"."+field.getName());
		TYPES_MAP.put(key, type);
		return type;
	}
	
	static Type getActualType(Class<?> klass,Field field,TypeVariable<?> typeVariable) {
		for(Integer index = 0 ; index < field.getDeclaringClass().getTypeParameters().length ; index = index + 1)
			if(field.getDeclaringClass().getTypeParameters()[index].getTypeName().equals(typeVariable.getTypeName()))
				if(klass.getGenericSuperclass() instanceof ParameterizedType)
					return ((ParameterizedType) klass.getGenericSuperclass()).getActualTypeArguments()[index];
				else
					return Object.class;
		return null;
	}
	
	static Type getType(Class<?> klass,List<String> fieldNames) {
		if(klass == null || fieldNames == null || fieldNames.isEmpty())
			return null;
		Field field = getByName(klass, fieldNames);
		if(field == null)
			return null;
		return getType(field,klass);
	}
	
	static Type getType(Class<?> klass,String...fieldNames) {
		if(klass == null || fieldNames == null || fieldNames.length == 0)
			return null;
		Field field = getByName(klass, fieldNames);
		if(field == null)
			return null;
		return getType(field,klass);
	}
	
	static void setType(Class<?> klass,Field field,Type type) {
		if(klass == null || field == null)
			return;
		TYPES_MAP.put(klass.getName()+DOT+field.getName(),type);
	}
	
	static void setType(Class<?> klass,List<String> fieldNames,Type type) {
		if(klass == null || fieldNames == null || fieldNames.isEmpty())
			return;
	}
	
	static void setType(Class<?> klass,Type type,String...fieldNames) {
		if(klass == null || fieldNames == null || fieldNames.length == 0)
			return;
	}
	
	/* read value*/
	
	static Object readStatic(Field field) {
		try {
			return field == null ? null : FieldUtils.readStaticField(field, Boolean.TRUE);
		} catch (IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	static Object readStatic(Class<?> klass,String fieldName) {
		try {
			return klass == null || fieldName == null || fieldName.isBlank() ? null : FieldUtils.readStaticField(klass,fieldName, Boolean.TRUE);
		} catch (IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	static Object read(Object object,Field field,Boolean isGettable) {
		if(field == null)
			return null;
		if(Modifier.isStatic(field.getModifiers()))
			return readStatic(field);
		try {
			return object == null ? null : FieldUtils.readField(field,object, Boolean.TRUE);
		} catch (IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	static Object read(Object object,Field field) {
		return read(object, field, null);
	}
	
	static Object read(Object object,String fieldName,Boolean isGettable) {
		if(object == null || fieldName == null || fieldName.isBlank())
			return null;
		if(object instanceof Class)
			return readStatic((Class<?>) object, fieldName);
		if(StringUtils.contains(fieldName, DOT)) {
			try {
				return PropertyUtils.getNestedProperty(object, fieldName);
			} catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		}else
			try {
				return object == null ? null : FieldUtils.readField(object, fieldName, Boolean.TRUE);
			} catch (IllegalAccessException exception) {
				throw new RuntimeException(exception);
			}
	}

	static Object read(Object object,String fieldName) {
		return read(object, fieldName, null);
	}
	
	static Object read(Object object,Collection<String> fieldNames,Boolean isGettable) {
		return read(object, join(fieldNames), isGettable);
	}
	
	static Object read(Object object,Collection<String> fieldNames) {
		return read(object, join(fieldNames));
	}
	
	static Object read(Object object,String[] fieldNames,Boolean isGettable) {
		return read(object, join(fieldNames), isGettable);
	}
	
	static Object read(Object object,String[] fieldNames) {
		return read(object, join(fieldNames));
	}
	
	static Object read(Object object,FieldName fieldName,ValueUsageType valueUsageType,Boolean isGettable) {
		return object == null ? null : read(object, getByName(object.getClass(), fieldName, valueUsageType), isGettable);
	}
	
	static Object read(Object object,FieldName fieldName,ValueUsageType valueUsageType) {
		return object == null ? null : read(object, getByName(object.getClass(), fieldName, valueUsageType));
	}
	
	static Object readSystemIdentifier(Object object,Boolean isGettable) {
		return read(object, FieldName.IDENTIFIER, ValueUsageType.SYSTEM, isGettable);
	}
	
	static Object readSystemIdentifier(Object object) {
		return read(object, FieldName.IDENTIFIER, ValueUsageType.SYSTEM);
	}
	
	static Object readBusinessIdentifier(Object object,Boolean isGettable) {
		return read(object, FieldName.IDENTIFIER, ValueUsageType.BUSINESS, isGettable);
	}
	
	static Object readBusinessIdentifier(Object object) {
		return read(object, FieldName.IDENTIFIER, ValueUsageType.BUSINESS);
	}
	
	/* write value*/
	
	static void write(Object object,Field field, Object value, Boolean isGettable) {
		if(object == null || field == null)
			return;
		try {
			FieldUtils.writeField(field,object,value, Boolean.TRUE);
		} catch (IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	static void write(Object object,Field field, Object value) {
		write(object,field, value, null);
	}
	
	static void write(Object object, String fieldName, Object value, Boolean isGettable) {
		if(object == null || fieldName == null || fieldName.isBlank())
			return;
		if(StringUtils.contains(fieldName, DOT)) {
			try {
				PropertyUtils.setNestedProperty(object, fieldName, value);
			} catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		}else	
			try {
				if(object instanceof Class)
					FieldUtils.writeStaticField((Class<?>) object, fieldName, value, Boolean.TRUE);
				else
					FieldUtils.writeField(object,fieldName,value, Boolean.TRUE);
			} catch (Exception exception) {
				throw new RuntimeException(exception);
			}
	}
	
	static void write(Object object, String fieldName, Object value) {
		write(object, fieldName, value, null);
	}
	
	static void write(Object object, Collection<String> fieldNames, Object value, Boolean isGettable) {
		write(object, join(fieldNames), value, isGettable);
	}
	
	static void write(Object object, Collection<String> fieldNames, Object value) {
		write(object, join(fieldNames), value);
	}
	
	static void write(Object object, String[] fieldNames, Object value, Boolean isGettable) {
		write(object, join(fieldNames), value, isGettable);
	}
	
	static void write(Object object, String[] fieldNames, Object value) {
		write(object, join(fieldNames), value);
	}
	
	static void write(Object object, FieldName fieldName,ValueUsageType valueUsageType, Object value, Boolean isGettable) {
		if(object != null)
			write(object, getName(object.getClass(), fieldName, valueUsageType), value, isGettable);
	}
	
	static void writeSystemIdentifier(Object object, Object value, Boolean isGettable) {
		if(object != null)
			write(object, getName(object.getClass(), FieldName.IDENTIFIER, ValueUsageType.SYSTEM), value, isGettable);
	}
	
	static void writeSystemIdentifier(Object object, Object value) {
		if(object != null)
			write(object, getName(object.getClass(), FieldName.IDENTIFIER, ValueUsageType.SYSTEM), value);
	}
	
	static void writeBusinessIdentifier(Object object, Object value, Boolean isGettable) {
		if(object != null)
			write(object, getName(object.getClass(), FieldName.IDENTIFIER, ValueUsageType.BUSINESS), value, isGettable);
	}
	
	static void writeBusinessIdentifier(Object object, Object value) {
		if(object != null)
			write(object, getName(object.getClass(), FieldName.IDENTIFIER, ValueUsageType.BUSINESS), value);
	}
	
	static void writeMany(Object object, Collection<Field> fields,Object value) {
		if(object == null || fields == null || fields.isEmpty())
			return;
		for(Field index : fields)
			write(object, index, value);
	}
	
	static void writeMany(Object object, String fieldNameRegularExpression,Integer modifiers,Object value) {
		if(object == null)
			return;
		Collection<Field> fields = filter(object.getClass(), fieldNameRegularExpression, modifiers);
		if(fields == null || fields.isEmpty())
			return;
		writeMany(object, fields, value);
	}
	
	/**
	 * write null value to given fields of object
	 * @param object
	 * @param fields
	 */
	static void nullify(Object object, Collection<Field> fields) {
		writeMany(object, fields, null);
	}
	
	static void nullify(Object object, String fieldNameRegularExpression,Integer modifiers) {
		writeMany(object, fieldNameRegularExpression, modifiers,null);
	}
	
	/*
	static void writeManyFieldsWithSameValue(Object object, Collection<List<String>> fieldsNamesCollection,Object value,Boolean isEqual) {
		Collection<String> collection = null;
		if(Boolean.TRUE.equals(isEqual)) {
			collection = fieldsNames;
		}else {
			Fields fields = __inject__(ClassInstancesRuntime.class).get(object.getClass()).getFields();
			if(__inject__(CollectionHelper.class).isNotEmpty(fields)) {
				collection = new ArrayList<>();
				for(Field index : fields.get()) {
					String fieldName = index.getName();
					if(!Modifier.isFinal(index.getModifiers()) && !Modifier.isStatic(index.getModifiers()) && !index.getType().isPrimitive() && !fieldsNames.contains(fieldName))
						collection.add(fieldName);
				}	
			}
		}
		if(__inject__(CollectionHelper.class).isNotEmpty(collection)) {
			for(String index : collection)
				__write__(object, index, null);
		}
	}*/
	
	/**/
	
	Map<Class<?>,List<Field>> CLASS_FIELDS_MAP = new HashMap<>();
	Map<String,Field> FIELDS_MAP = new HashMap<>();
	Map<String,Type> TYPES_MAP = new HashMap<>();
	String DOT = ".";
	Map<ClassFieldNameValueUsageType,String> CLASSES_FIELDNAMES_MAP = new HashMap<>();
	
	@Getter @Setter @Accessors(chain=true) @EqualsAndHashCode(of= {"klass","fieldName","valueUsageType"}) @AllArgsConstructor
	static class ClassFieldNameValueUsageType implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private Class<?> klass;
		private FieldName fieldName;
		private ValueUsageType valueUsageType;
		
	}
}

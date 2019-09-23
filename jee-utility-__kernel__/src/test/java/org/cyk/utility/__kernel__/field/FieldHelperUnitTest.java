package org.cyk.utility.__kernel__.field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyk.utility.__kernel__.field.FieldHelper.disjoin;
import static org.cyk.utility.__kernel__.field.FieldHelper.filter;
import static org.cyk.utility.__kernel__.field.FieldHelper.get;
import static org.cyk.utility.__kernel__.field.FieldHelper.getByName;
import static org.cyk.utility.__kernel__.field.FieldHelper.getName;
import static org.cyk.utility.__kernel__.field.FieldHelper.getNames;
import static org.cyk.utility.__kernel__.field.FieldHelper.getType;
import static org.cyk.utility.__kernel__.field.FieldHelper.join;
import static org.cyk.utility.__kernel__.field.FieldHelper.nullify;
import static org.cyk.utility.__kernel__.field.FieldHelper.read;
import static org.cyk.utility.__kernel__.field.FieldHelper.setName;
import static org.cyk.utility.__kernel__.field.FieldHelper.write;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.cyk.utility.__kernel__.RegularExpressionHelper;
import org.cyk.utility.__kernel__.test.weld.AbstractWeldUnitTest;
import org.cyk.utility.__kernel__.value.ValueUsageType;
import org.junit.jupiter.api.Test;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class FieldHelperUnitTest extends AbstractWeldUnitTest {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenBefore__() {
		super.__listenBefore__();
		FieldHelper.CLASSES_FIELDNAMES_MAP.clear();
	}
	
	@Test
	public void join_(){
		assertThat(join("f1","f2.f3")).isEqualTo("f1.f2.f3");
	}	
	
	@Test
	public void disjoin_(){
		assertThat(disjoin("f1","f2.f3")).containsExactly("f1","f2","f3");
	}	
	
	@Test
	public void getName_identifier_system() {
		assertThat(getName(Name.class, FieldName.IDENTIFIER, ValueUsageType.SYSTEM)).isEqualTo("identifier");
	}
	
	@Test
	public void getName_identifier_system_isId() {
		setName(Name.class, FieldName.IDENTIFIER, ValueUsageType.SYSTEM, "id");
		assertThat(getName(Name.class, FieldName.IDENTIFIER, ValueUsageType.SYSTEM)).isEqualTo("id");
	}
	
	@Test
	public void getName_identifier_business() {
		assertThat(getName(Name.class, FieldName.IDENTIFIER, ValueUsageType.BUSINESS)).isEqualTo("code");
	}
	
	@Test
	public void getName_identifier_business_isCod() {
		setName(Name.class, FieldName.IDENTIFIER, ValueUsageType.BUSINESS, "cod");
		assertThat(getName(Name.class, FieldName.IDENTIFIER, ValueUsageType.BUSINESS)).isEqualTo("cod");
	}
	
	@Test
	public void get_classIsNull() {
		Collection<Field> fields = get(null);
		assertThat(fields).isNull();
	}
	
	@Test
	public void get_classParent() {
		Collection<Field> fields = get(ClassParent.class);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("parent_string_f01");
	}
	
	@Test
	public void get_class() {
		Collection<Field> fields = get(Class.class);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("parent_string_f01","string_f01");
	}
	
	@Test
	public void get_classChild() {
		Collection<Field> fields = get(ClassChild.class);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("parent_string_f01","string_f01","child_string_f01");
	}
	
	@Test
	public void get_I01() {
		Collection<Field> fields = get(I01.class);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("PROPERTY_F01","PROPERTY_F02","PROPERTY_F03","NOT_PROPERTY_F01");
	}
	
	@Test
	public void get_I01Child() {
		Collection<Field> fields = get(I01Child.class);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("PROPERTY_F01","PROPERTY_F02","PROPERTY_F03","NOT_PROPERTY_F01","PROPERTY_F04","NOT_PROPERTY_F02");
	}
	
	@Test
	public void getByName_classIsNull() {
		Field field = getByName(null,(List<String>)null);
		assertThat(field).isNull();
	}
	
	@Test
	public void getByName_classParent() {
		Field field = getByName(ClassParent.class,"parent_string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getName()).isEqualTo("parent_string_f01");
	}
	
	@Test
	public void getByName_class() {
		Field field = getByName(Class.class,"string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getName()).isEqualTo("string_f01");
	}
	
	@Test
	public void getByName_class_parent_string_f01() {
		Field field = getByName(Class.class,"parent_string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getName()).isEqualTo("parent_string_f01");
	}
	
	@Test
	public void getByName_classChild() {
		Field field = getByName(ClassChild.class,"child_string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getName()).isEqualTo("child_string_f01");
	}
	
	@Test
	public void getByName_classChild_parent_string_f01() {
		Field field = getByName(ClassChild.class,"parent_string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getName()).isEqualTo("parent_string_f01");
	}
	
	@Test
	public void getByName_classChild_string_f01() {
		Field field = getByName(ClassChild.class,"string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getName()).isEqualTo("string_f01");
	}
	
	@Test
	public void getByName_classComposite() {
		Field field = getByName(ClassCompsite.class,"string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getName()).isEqualTo("string_f01");
	}
	
	@Test
	public void getByName_classComposite_parent_parent_string_f01() {
		Field field = getByName(ClassCompsite.class,"parent.parent_string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getDeclaringClass()).isEqualTo(ClassParent.class);
		assertThat(field.getName()).isEqualTo("parent_string_f01");
	}
	
	@Test
	public void getByName_classCompositeComposite() {
		Field field = getByName(ClassCompsiteComposite.class,"string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getName()).isEqualTo("string_f01");
	}
	
	@Test
	public void getByName_classCompositeComposite_composite_parent() {
		Field field = getByName(ClassCompsiteComposite.class,"composite.parent");
		assertThat(field).isNotNull();
		assertThat(field.getDeclaringClass()).isEqualTo(ClassCompsite.class);
		assertThat(field.getName()).isEqualTo("parent");
	}
	
	@Test
	public void getByName_classCompositeComposite_composite_parent_parent_string_f01() {
		Field field = getByName(ClassCompsiteComposite.class,"composite.parent.parent_string_f01");
		assertThat(field).isNotNull();
		assertThat(field.getDeclaringClass()).isEqualTo(ClassParent.class);
		assertThat(field.getName()).isEqualTo("parent_string_f01");
	}
	
	@Test
	public void write_byName() {
		Name name = new Name();
		assertThat(name.getCode()).isNull();
		write(name,"code","c01");
		assertThat(name.getCode()).isEqualTo("c01");
	}
	
	@Test
	public void write_byName_unkonwnFieldName() {
		Name name = new Name();
		assertThat(name.getCode()).isNull();
		write(name,"xxx","c01");
		assertThat(name.getCode()).isNull();
	}
	
	@Test
	public void write_byField() throws NoSuchFieldException, SecurityException {
		Name name = new Name();
		assertThat(name.getCode()).isNull();
		write(name,Name.class.getDeclaredField("code"),"c01");
		assertThat(name.getCode()).isEqualTo("c01");
	}
	
	@Test
	public void write_nested() {
		ClassCompsite compsite = new ClassCompsite();
		compsite.setSelf(new Class());
		assertThat(compsite.getSelf().getString_f01()).isNull();
		write(compsite,"self.string_f01","c01");
		assertThat(compsite.getSelf().getString_f01()).isEqualTo("c01");
	}
	
	@Test
	public void write_static() {
		Name.STATIC_STRING = null;
		assertThat(Name.STATIC_STRING).isNull();
		write(Name.class,"STATIC_STRING","c01");
		assertThat(Name.STATIC_STRING).isEqualTo("c01");
	}
	
	@Test
	public void read_byName() {
		Name name = new Name();
		assertThat(read(name,"code")).isEqualTo(name.getCode());
		name.setCode("h01");
		assertThat(read(name,"code")).isEqualTo(name.getCode()).isEqualTo("h01");
	}
	
	@Test
	public void read_byField() throws NoSuchFieldException, SecurityException {
		Name name = new Name();
		assertThat(read(name,Name.class.getDeclaredField("code"))).isEqualTo(name.getCode());
		name.setCode("h01");
		assertThat(read(name,Name.class.getDeclaredField("code"))).isEqualTo(name.getCode()).isEqualTo("h01");
	}
	
	@Test
	public void read_nested() {
		ClassCompsite compsite = new ClassCompsite();
		compsite.setSelf(new Class());
		assertThat(read(compsite,"self.string_f01")).isNull();
		compsite.getSelf().setString_f01("h01");
		assertThat(read(compsite,"self.string_f01")).isEqualTo("h01");
	}
	
	@Test
	public void read_static() {
		Name.STATIC_STRING = "hello";
		assertThat(read(Name.class,"STATIC_STRING")).isEqualTo("hello");
	}
	
	/* type */
	
	@Test
	public void getType_field_simple(){
		Type type = getType(TypeClass.class, "f01");
		assertThat(type).isEqualTo(Integer.class);
	}
	
	@Test
	public void getType_field_typeVariable_f021(){
		Type type = getType(StringClass.class, "f021");
		assertThat(type).isEqualTo(String.class);
	}
	
	@Test
	public void getType_field_typeVariable_f022(){
		Type type = getType(StringClass.class, "f022");
		assertThat(type).isEqualTo(Integer.class);
	}
	
	@Test
	public void getType_field_parameterized_static(){
		Type type = getType(TypeClass.class, "f05");
		Type expectedType = new ParameterizedType() {
			@Override
			public Type getRawType() {
				return Collection.class;
			}
			
			@Override
			public Type getOwnerType() {
				return null;
			}
			
			@Override
			public Type[] getActualTypeArguments() {
				return new Type[]{Integer.class};
			}
		};
		assertThat(type).isEqualTo(expectedType);	
	}
	
	@Test
	public void getType_field_parameterized_dynamic(){
		ParameterizedType type = (ParameterizedType) getType(LongClass.class, "f03");
		ParameterizedType expectedType = new ParameterizedType() {
			@Override
			public Type getRawType() {
				return Collection.class;
			}
			
			@Override
			public Type getOwnerType() {
				return null;
			}
			
			@Override
			public Type[] getActualTypeArguments() {
				return new Type[]{Long.class};
			}
		};
		assertThat(type.getRawType()).isEqualTo(expectedType.getRawType());	
		assertThat(type.getActualTypeArguments()[0]).isEqualTo(expectedType.getActualTypeArguments()[0]);	
	}
	
	@Test
	public void filter_interface_getFieldsOfInterfaceI01(){
		Collection<Field> fields = filter(I01.class, null, null);
		assertThat(fields).hasSize(4).contains(
				FieldUtils.getField(I01.class, "PROPERTY_F01",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F02",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F03",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "NOT_PROPERTY_F01",Boolean.TRUE)
				);
	}
	
	@Test
	public void filter_interface_getFieldsWhereNameStartWithPropertyOfInterfaceI01(){
		Collection<Field> fields = filter(I01.class,"^PROPERTY_",null);
		assertThat(fields).hasSize(3).contains(
				FieldUtils.getField(I01.class, "PROPERTY_F01",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F02",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F03",Boolean.TRUE)
				);
	}
	
	@Test
	public void filter_interface_getFieldsOfInterfaceI01Child(){
		Collection<Field> fields = filter(I01Child.class,null,null);
		assertThat(fields).hasSize(6).contains(
				FieldUtils.getField(I01Child.class, "PROPERTY_F04",Boolean.TRUE)
				,FieldUtils.getField(I01Child.class, "NOT_PROPERTY_F02",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F01",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F02",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F03",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "NOT_PROPERTY_F01",Boolean.TRUE)
				
				);
	}
	
	@Test
	public void filter_interface_getFieldsWhereNameStartWithPropertyOfInterfaceI01Child(){
		Collection<Field> fields = filter(I01Child.class,"^PROPERTY_",null);
		assertThat(fields).hasSize(4).contains(
				FieldUtils.getField(I01Child.class, "PROPERTY_F04",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F01",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F02",Boolean.TRUE)
				,FieldUtils.getField(I01.class, "PROPERTY_F03",Boolean.TRUE)
				
				);
	}
	
	@Test
	public void filter_ClassFilter_nameRegularExpression_xxx(){
		Collection<Field> fields = filter(ClassFilter.class,RegularExpressionHelper.buildIsExactly("xxx"),null);
		assertThat(fields).isNull();
	}
	
	@Test
	public void filter_ClassFilter_nameRegularExpression_identifier(){
		Collection<Field> fields = filter(ClassFilter.class,RegularExpressionHelper.buildIsExactly("identifier"),null);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("identifier");
	}
	
	@Test
	public void filter_ClassFilter_nameRegularExpression_identifierOrCode(){
		Collection<Field> fields = filter(ClassFilter.class,RegularExpressionHelper.buildIsExactly("identifier","code"),null);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("identifier","code");
	}
	
	@Test
	public void filter_ClassFilter_nameRegularExpression_identifierOrXxx(){
		Collection<Field> fields = filter(ClassFilter.class,RegularExpressionHelper.buildIsExactly("identifier","xxx"),null);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("identifier");
	}
	
	@Test
	public void filter_ClassFilter_nameRegularExpression_identifierOrName(){
		Collection<Field> fields = filter(ClassFilter.class,RegularExpressionHelper.buildIsExactly("identifier","name"),null);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("identifier");
	}
	
	@Test
	public void filter_ClassFilterSub_nameRegularExpression_identifierOrName(){
		Collection<Field> fields = filter(ClassFilterSub.class,RegularExpressionHelper.buildIsExactly("identifier","name"),null);
		assertThat(fields).isNotNull();
		assertThat(getNames(fields)).containsExactly("identifier","name");
	}
	
	@Test
	public void nullify_ClassFilter_identifier(){
		ClassFilter object = new ClassFilter().setIdentifier("i01");
		nullify(object, RegularExpressionHelper.buildIsExactly("identifier"), null);
		assertThat(object.getIdentifier()).isNull();
	}
	
	@Test
	public void nullify_ClassFilter_identifierOnly(){
		ClassFilter object = new ClassFilter().setIdentifier("i01").setCode("c01");
		nullify(object, RegularExpressionHelper.buildIsExactly("identifier"), null);
		assertThat(object.getIdentifier()).isNull();
		assertThat(object.getCode()).isEqualTo("c01");
	}	
	
	@Test
	public void nullify_ClassFilter_notIdentifier(){
		ClassFilter object = new ClassFilter().setIdentifier("i01").setCode("c01");
		nullify(object, RegularExpressionHelper.buildIsNotExactly("identifier"), null);
		assertThat(object.getIdentifier()).isEqualTo("i01");
		assertThat(object.getCode()).isNull();
	}	
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class ClassParent {
		private String parent_string_f01;
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class Class extends ClassParent {
		private String string_f01;
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class ClassChild extends Class {
		private String child_string_f01;
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class ClassCompsite {		
		private String string_f01;
		private ClassParent parent;
		private Class self;
		private ClassParent child;
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class ClassCompsiteComposite {
		private String string_f01;
		private ClassCompsite composite;
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class Name {
		private String identifier;
		private String code;
		public static String STATIC_STRING;
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class TypeClass<T1,T2> {
		private Integer f01;
		private T1 f021;
		private T2 f022;
		private Collection<T1> f03;
		private Map<String,String> f04;
		private Collection<Integer> f05;
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class StringClass extends TypeClass<String,Integer> {
	
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LongClass extends TypeClass<Long,String> {
	
	}
	
	public static interface I01 {
		String PROPERTY_F01 = "v01";
		String PROPERTY_F02 = "v02";
		String PROPERTY_F03 = "v03";
		
		String NOT_PROPERTY_F01 = "notproperty";
	}
	
	public static interface I01Child extends I01 {
		String PROPERTY_F04 = "v04";
		
		String NOT_PROPERTY_F02 = "notproperty";
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class ClassFilter {
		private String identifier;
		private String code;
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class ClassFilterSub extends ClassFilter {
		private String name;
	}
}
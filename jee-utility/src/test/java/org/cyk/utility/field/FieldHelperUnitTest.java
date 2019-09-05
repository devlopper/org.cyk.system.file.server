package org.cyk.utility.field;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.cyk.utility.test.weld.AbstractWeldUnitTest;
import org.cyk.utility.value.ValueUsageType;
import org.junit.jupiter.api.Test;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class FieldHelperUnitTest extends AbstractWeldUnitTest {
	private static final long serialVersionUID = 1L;

	@Test
	public void buildFieldNameSystemIdentifier(){
		assertThat(FieldHelperImpl.__buildFieldName__(MyClass01.class, FieldName.IDENTIFIER, ValueUsageType.SYSTEM)).isEqualTo("identifier");
	}
	
	@Test
	public void buildFieldNameBusinessIdentifier(){
		assertThat(FieldHelperImpl.__buildFieldName__(MyClass01.class, FieldName.IDENTIFIER, ValueUsageType.BUSINESS)).isEqualTo("code");
	}
	
	@Test
	public void getFieldByName_intField(){
		assertThat(FieldHelperImpl.__getFieldByName__(MyClass01.class, "intField")).isEqualTo(FieldUtils.getField(MyClass01.class, "intField",Boolean.TRUE));
	}	

	@Test
	public void getField_sub_sIntField(){
		assertThat(__inject__(FieldHelper.class).getField(MyClass01.class, "sub","sIntField")).isEqualTo(FieldUtils.getField(MyClass01Sub.class, "sIntField",Boolean.TRUE));
	}	
	
	@Test
	public void readFieldValue(){
		assertThat(FieldHelperImpl.__readFieldValue__(new MyClass01().setIdentifier("i01"),"identifier")).isEqualTo("i01");
	}
	
	@Test
	public void readFieldValueSystemIdentifier(){
		assertThat(FieldHelperImpl.__readFieldValueSystemIdentifier__(new MyClass01().setIdentifier("i01"))).isEqualTo("i01");
	}
	
	@Test
	public void readFieldValueBusinessIdentifier(){
		assertThat(FieldHelperImpl.__readFieldValueBusinessIdentifier__(new MyClass01().setCode("c01"))).isEqualTo("c01");
	}
	
	@Test
	public void writeFieldValue(){
		MyClass01 object = new MyClass01();
		assertThat(object.getIdentifier()).isNull();
		FieldHelperImpl.__writeFieldValue__(object,"identifier","i01");
		assertThat(object.getIdentifier()).isEqualTo("i01");
	}
	
	@Test
	public void writeFieldValueSystemIdentifier(){
		MyClass01 object = new MyClass01();
		assertThat(object.getIdentifier()).isNull();
		FieldHelperImpl.__writeFieldValueSystemIdentifier__(object,"i01");
		assertThat(object.getIdentifier()).isEqualTo("i01");
	}
	
	@Test
	public void writeFieldValueBusinessIdentifier(){
		MyClass01 object = new MyClass01();
		assertThat(object.getCode()).isNull();
		FieldHelperImpl.__writeFieldValueBusinessIdentifier__(object,"c01");
		assertThat(object.getCode()).isEqualTo("c01");
	}
	
	@Test
	public void nullify(){
		MyClass01 object = new MyClass01().setIntegerField(1).setStringField("a").setLongValue2(2l);
		assertThat(object.getIntegerField()).isEqualTo(1);
		assertThat(object.getStringField()).isEqualTo("a");
		assertThat(object.getLongValue2()).isEqualTo(2l);
		
		object = new MyClass01().setIntegerField(1).setStringField("a").setLongValue2(2l);
		__inject__(FieldHelper.class).nullify(object,Boolean.TRUE, "integerField");
		assertThat(object.getIntegerField()).isNull();
		assertThat(object.getStringField()).isEqualTo("a");
		assertThat(object.getLongValue2()).isEqualTo(2l);
		
		object = new MyClass01().setIntegerField(1).setStringField("a").setLongValue2(2l);
		__inject__(FieldHelper.class).nullify(object,Boolean.TRUE, "stringField");
		assertThat(object.getIntegerField()).isEqualTo(1);
		assertThat(object.getStringField()).isNull();
		assertThat(object.getLongValue2()).isEqualTo(2l);
		
		object = new MyClass01().setIntegerField(1).setStringField("a").setLongValue2(2l);
		__inject__(FieldHelper.class).nullify(object,Boolean.TRUE, "longValue2");
		assertThat(object.getIntegerField()).isEqualTo(1);
		assertThat(object.getStringField()).isEqualTo("a");
		assertThat(object.getLongValue2()).isNull();
		
		object = new MyClass01().setIntegerField(1).setStringField("a").setLongValue2(2l);
		__inject__(FieldHelper.class).nullify(object,Boolean.TRUE, "longValue2","stringField");
		assertThat(object.getIntegerField()).isEqualTo(1);
		assertThat(object.getStringField()).isNull();
		assertThat(object.getLongValue2()).isNull();
		
		object = new MyClass01().setIntegerField(1).setStringField("a").setLongValue2(2l).setIntField(1489);
		__inject__(FieldHelper.class).nullify(object,Boolean.FALSE, "intField");
		assertThat(object.getIntField()).isEqualTo(1489);
		assertThat(object.getIntegerField()).isNull();
		assertThat(object.getStringField()).isNull();
		assertThat(object.getLongValue2()).isNull();
	}	
	
	@Test
	public void join(){
		assertThat(__inject__(FieldHelper.class).join("f1","f2.f3")).isEqualTo("f1.f2.f3");
	}	
	
	@Test
	public void disjoin(){
		assertThat(__inject__(FieldHelper.class).disjoin("f1","f2.f3").get()).containsExactly("f1","f2","f3");
	}	
	
	
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class MyClass01 {
		private String identifier,code;
		private int intField;
		private Integer integerField;
		private String stringField;
		private long longValue1;
		private Long longValue2;
		private Date dateField;
		private Object x;
		private MyClass01Sub sub;
		
		public MyClass01 setLongValue1(long value) {
			this.longValue1 = value * 2;
			return this;
		}
		
		public MyClass01 setMyProperty(Object value) {
			this.x = value;
			return this;
		}
		
		public Object getMyProperty() {
			return x;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class MyClass01Sub {
		
		private int sIntField;
		private Integer sIntegerField;
		private String sStringField;
		private long sLongValue1;
		private Long sLongValue2;
		private Date sDateField;
		private Object sX;
		
	}
}

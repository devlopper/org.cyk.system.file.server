package org.cyk.utility.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyk.utility.field.FieldInstance;
import org.cyk.utility.test.weld.AbstractWeldUnitTest;
import org.junit.jupiter.api.Test;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class ValueUnitTest extends AbstractWeldUnitTest {
	private static final long serialVersionUID = 1L;

	@Test
	public void get_field_isNull() {
		assertThat(__inject__(Value.class).setObject(new Class()).setFieldInstance(__inject__(FieldInstance.class).setPath("f01")).get()).isNull();
	}
	
	@Test
	public void get_field_isNotNull() {
		assertThat(__inject__(Value.class).setObject(new Class().setF01("v01")).setFieldInstance(__inject__(FieldInstance.class).setPath("f01")).get()).isEqualTo("v01");
	}
	
	@Test
	public void get_literal_isNull() {
		assertThat(__inject__(Value.class).setObject(new Class()).get()).isNull();
	}
	
	@Test
	public void get_literal_isNotNull() {
		assertThat(__inject__(Value.class).setObject(new Class()).set("v01").get()).isEqualTo("v01");
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class Class {
		
		private String f01;
		private Sub sub;
		
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class Sub {
		
		private String f01;
		
	}
}
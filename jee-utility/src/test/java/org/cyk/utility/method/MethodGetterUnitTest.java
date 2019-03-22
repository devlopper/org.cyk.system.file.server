package org.cyk.utility.method;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.string.StringLocation;
import org.cyk.utility.test.arquillian.AbstractArquillianUnitTestWithDefaultDeployment;
import org.junit.Test;

public class MethodGetterUnitTest extends AbstractArquillianUnitTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;

	@Test
	public void get_setter_method_f1(){
		assertionHelper.assertEquals(MethodUtils.getMatchingMethod(MyClass.class, "setF1", String.class),__inject__(CollectionHelper.class).getFirst(
				__inject__(MethodGetter.class).setClazz(MyClass.class).setToken("setF1").setTokenLocation(StringLocation.EXAT).execute().getOutput()));
	}
	
	@Test
	public void get_setter_method_f2(){
		assertionHelper.assertEquals(MethodUtils.getMatchingMethod(MyClass.class, "setF2", String.class),__inject__(CollectionHelper.class).getFirst(
				__inject__(MethodGetter.class).setClazz(MyClass.class).setToken("setF2").setTokenLocation(StringLocation.EXAT).execute().getOutput()));
	}
	
	public static class MyClass {
		
		public String getF1() {
			return null;
		}
		
		public void setF1(String value) {
			
		}
		
		public String getF2() {
			return null;
		}
		
		public MyClass setF2(Object value) {
			return null;
		}
	}
	
}
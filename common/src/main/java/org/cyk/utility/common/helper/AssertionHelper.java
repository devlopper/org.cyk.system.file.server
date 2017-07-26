package org.cyk.utility.common.helper;

import java.io.Serializable;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

@Singleton
public class AssertionHelper extends AbstractHelper implements Serializable {
	private static final long serialVersionUID = 1L;

	private static AssertionHelper INSTANCE;
	
	public static AssertionHelper getInstance() {
		if(INSTANCE == null)
			INSTANCE = new AssertionHelper();
		return INSTANCE;
	}
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
	}
	
	/**/
	
	public static interface Assertion<OUTPUT> extends org.cyk.utility.common.Builder.NullableInput<OUTPUT>  {
		
		public static class Adapter<OUTPUT> extends org.cyk.utility.common.Builder.NullableInput.Adapter.Default<OUTPUT> implements Assertion<OUTPUT> , Serializable {
			private static final long serialVersionUID = 1L;

			public Adapter(Class<OUTPUT> outputClass) {
				super(outputClass);
			}
			
			/**/
			
			public static class Default<OUTPUT> extends Assertion.Adapter<OUTPUT> implements Serializable {
				private static final long serialVersionUID = 1L;

				public Default(Class<OUTPUT> outputClass) {
					super(outputClass);
				}
			}
		}
		
		/**/
		
		public static interface Boolean extends Assertion<java.lang.Boolean>  {
			
			public static class Adapter extends Assertion.Adapter.Default<java.lang.Boolean> implements Boolean , Serializable {
				private static final long serialVersionUID = 1L;

				public Adapter() {
					super(java.lang.Boolean.class);
				}
				
				/**/
				
				public static class Default extends Boolean.Adapter implements Serializable {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected java.lang.Boolean __execute__() {
						return super.__execute__();
					}
					
				}
			}
		}
		
		public static interface Void extends Assertion<java.lang.Void>  {
			
			public static class Adapter extends Assertion.Adapter.Default<java.lang.Void> implements Void , Serializable {
				private static final long serialVersionUID = 1L;

				public Adapter() {
					super(java.lang.Void.class);
				}
				
				/**/
				
				public static class Default extends Boolean.Adapter implements Serializable {
					private static final long serialVersionUID = 1L;

				}
			}
		}
		
	}
	
	public static  interface Builder<INPUT> extends org.cyk.utility.common.Builder<INPUT, java.lang.Boolean> {
		
		public static class Adapter<INPUT> extends org.cyk.utility.common.Builder.Adapter.Default<INPUT, java.lang.Boolean> implements Builder<INPUT> , Serializable {
			private static final long serialVersionUID = 1L;

			public Adapter(Class<INPUT> inputClass, INPUT input) {
				super(inputClass, input, java.lang.Boolean.class);
			}
			
			/**/
			
			public static class Default<INPUT> extends Builder.Adapter<INPUT> implements Serializable {
				private static final long serialVersionUID = 1L;

				public Default(Class<INPUT> inputClass, INPUT input) {
					super(inputClass, input);
				}
				
			}
			
		}
		
		/**/
		
		public static  interface String extends Builder<java.lang.String> {
			
			public static class Adapter extends Builder.Adapter.Default<java.lang.String> implements String , Serializable {
				private static final long serialVersionUID = 1L;

				public Adapter(java.lang.String input) {
					super(java.lang.String.class, input);
				}
				
				/**/
				
				public static class Default extends String.Adapter implements Serializable {
					private static final long serialVersionUID = 1L;

					public Default(java.lang.String input) {
						super(input);
					}
					
					@Override
					protected java.lang.Boolean __execute__() {
						java.lang.String string = getInput();
						if(NumberHelper.getInstance().isNumber(string))
							return new Number.Adapter.Default(NumberHelper.getInstance().get(string)).execute();
						return Boolean.parseBoolean(StringUtils.lowerCase(string));
					}
				}	
			}
		}
		
		public static  interface Number extends Builder<java.lang.Number> {
			
			public static class Adapter extends Builder.Adapter.Default<java.lang.Number> implements Number , Serializable {
				private static final long serialVersionUID = 1L;

				public Adapter(java.lang.Number input) {
					super(java.lang.Number.class, input);
				}
				
				/**/
				
				public static class Default extends Number.Adapter implements Serializable {
					private static final long serialVersionUID = 1L;

					public Default(java.lang.Number input) {
						super(input);
					}
					
					@Override
					protected java.lang.Boolean __execute__() {
						java.lang.Number number = getInput();
						return number.intValue() != 0 || number.doubleValue()!=0;
					}
				}	
			}
		}
		
		/**/
		
		
		
	}
	
}

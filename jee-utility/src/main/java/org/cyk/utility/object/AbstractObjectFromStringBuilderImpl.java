package org.cyk.utility.object;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.clazz.ClassHelper;
import org.cyk.utility.field.FieldInstances;
import org.cyk.utility.field.FieldInstancesRuntime;
import org.cyk.utility.string.Strings;

public abstract class AbstractObjectFromStringBuilderImpl extends AbstractObjectToOrFromStringBuilderImpl<Object> implements ObjectFromStringBuilder,Serializable {
	private static final long serialVersionUID = 1L;

	private String string;
	private Class<?> klass;
	
	@Override
	protected Object __execute__(Strings fieldNamesStrings) throws Exception {
		String string = __injectValueHelper__().returnOrThrowIfBlank("string", getString());
		Class<?> klass = __injectValueHelper__().returnOrThrowIfBlank("class", getKlass());
		FieldInstances fieldInstances = null;
		if(Boolean.TRUE.equals(__inject__(ClassHelper.class).isBelongsToJavaPackages(klass))) {
			
		}else {
			__injectValueHelper__().returnOrThrowIfBlank("field names", fieldNamesStrings);
			if(__injectCollectionHelper__().isNotEmpty(fieldNamesStrings)) {
				fieldInstances = __inject__(FieldInstances.class);
				for(String index : fieldNamesStrings.get())
					fieldInstances.add(__inject__(FieldInstancesRuntime.class).get(klass, index));
			}
				
		}
		return __execute__(string,klass,fieldInstances);
	}
	
	protected abstract Object __execute__(String string,Class<?> klass,FieldInstances fieldInstances) throws Exception;
	
	@Override
	public String getString() {
		return string;
	}
	
	@Override
	public ObjectFromStringBuilder setString(String string) {
		this.string = string;
		return this;
	}
	
	@Override
	public Class<?> getKlass() {
		return klass;
	}
	@Override
	public ObjectFromStringBuilder setKlass(Class<?> klass) {
		this.klass = klass;
		return this;
	}
	
	@Override
	public ObjectFromStringBuilder addFieldNamesStrings(Collection<String> fieldNamesStrings) {
		return (ObjectFromStringBuilder) super.addFieldNamesStrings(fieldNamesStrings);
	}
	
	@Override
	public ObjectFromStringBuilder addFieldNamesStrings(String... fieldNamesStrings) {
		return (ObjectFromStringBuilder) super.addFieldNamesStrings(fieldNamesStrings);
	}
	
}
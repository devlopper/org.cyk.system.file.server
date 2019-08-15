package org.cyk.utility.server.persistence;
import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.cyk.utility.__kernel__.constant.ConstantCharacter;
import org.cyk.utility.__kernel__.constant.ConstantString;
import org.cyk.utility.__kernel__.object.__static__.identifiable.AbstractIdentifiedPersistableByLong;
import org.cyk.utility.__kernel__.object.__static__.identifiable.AbstractIdentifiedPersistableByString;
import org.cyk.utility.array.ArrayHelper;
import org.cyk.utility.clazz.ClassHelper;
import org.cyk.utility.clazz.ClassInstance;
import org.cyk.utility.clazz.ClassInstancesRuntime;
import org.cyk.utility.clazz.Classes;
import org.cyk.utility.clazz.ClassesGetter;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.field.FieldInstance;
import org.cyk.utility.field.FieldInstancesRuntime;
import org.cyk.utility.server.persistence.jpa.AbstractIdentifiedByString;
import org.cyk.utility.string.repository.StringRepositoryResourceBundle;

public abstract class AbstractApplicationScopeLifeCycleListenerEntities extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(org.cyk.utility.server.persistence.ApplicationScopeLifeCycleListener.class).initialize(null);
		String packageName = getClass().getPackage().getName();		
		__inject__(StringRepositoryResourceBundle.class).addBundleAt(packageName+ConstantCharacter.DOT+ConstantString.MESSAGE,0);
		
		Class<?>[] basesClasses = __getClassesBasesClasses__();
		if(__inject__(ArrayHelper.class).isNotEmpty(basesClasses)) {
			Classes classes = __inject__(ClassesGetter.class).addPackageNames(packageName).addBasesClasses(basesClasses).setIsInterface(Boolean.FALSE).execute().getOutput();
			if(__inject__(CollectionHelper.class).isNotEmpty(classes)) {
				for(@SuppressWarnings("rawtypes") Class index : classes.get()) {
					if(!Modifier.isAbstract(index.getModifiers())) {
						ClassInstance classInstance = __inject__(ClassInstancesRuntime.class).get(index);
						FieldInstance fieldInstance = __inject__(FieldInstancesRuntime.class).get(index,classInstance.getSystemIdentifierField().getName());
						if(__inject__(ClassHelper.class).isInstanceOf(index, AbstractIdentifiedPersistableByString.class))
							fieldInstance.setType(String.class);
						else if(__inject__(ClassHelper.class).isInstanceOf(index, AbstractIdentifiedPersistableByLong.class))
							fieldInstance.setType(Long.class);		
					}
				}
			}	
		}
	}
	
	protected Class<?>[] __getClassesBasesClasses__() {
		return new Class<?>[] {AbstractIdentifiedByString.class};
	}
	
}
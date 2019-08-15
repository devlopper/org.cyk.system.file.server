package org.cyk.utility.clazz;

import java.io.Serializable;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.object.dynamic.AbstractObject;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.field.FieldName;
import org.cyk.utility.field.Fields;
import org.cyk.utility.log.Log;
import org.cyk.utility.log.LogLevel;
import org.cyk.utility.value.ValueUsageType;

@ApplicationScoped
public class ClassInstancesRuntimeImpl extends AbstractObject implements ClassInstancesRuntime,Serializable {
	private static final long serialVersionUID = 1L;

	private ClassInstances instances;
	
	@Override
	public ClassInstance get(Class<?> klass) {
		Log log = __inject__(Log.class).setLevel(LogLevel.TRACE);
		ClassInstance instance = klass == null ? null : getInstances(Boolean.TRUE).get(klass);
		if(instance == null) {
			instance = __inject__(ClassInstance.class).setClazz(klass);
			instance.setIsPersistable(klass.isAnnotationPresent(javax.persistence.Entity.class));
			instance.setTupleName(klass.getSimpleName());
			instance.setIsTransferable(klass.isAnnotationPresent(javax.xml.bind.annotation.XmlRootElement.class));
			//TODO loading can be deffered at demand
			Fields fields = instance.getFields(Boolean.TRUE);
			if(__inject__(CollectionHelper.class).isNotEmpty(fields)) {
				instance.setSystemIdentifierField(fields.getByName(klass,FieldName.IDENTIFIER,ValueUsageType.SYSTEM));
				instance.setBusinessIdentifierField(fields.getByName(klass,FieldName.IDENTIFIER,ValueUsageType.BUSINESS));
			}
			getInstances(Boolean.TRUE).add(instance);
			log.getMessageBuilder(Boolean.TRUE).addParameter("class <<"+klass+">> added to runtime collection");
		}else {
			log.getMessageBuilder(Boolean.TRUE).addParameter("class <<"+klass+">> fetched from runtime collection");
		}
		log.execute();
		return instance;
	}
	
	@Override
	public ClassInstances getInstances() {
		return instances;
	}
	
	@Override
	public ClassInstances getInstances(Boolean injectIfNull) {
		ClassInstances instances = (ClassInstances) __getInjectIfNull__(FIELD_INSTANCES, injectIfNull);
		instances.setCollectionClass(Set.class);
		return instances;
	}

	@Override
	public ClassInstancesRuntime setInstances(ClassInstances instances) {
		this.instances = instances;
		return this;
	}

	/**/
	
	public static final String FIELD_INSTANCES = "instances";
}
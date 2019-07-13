package org.cyk.utility.server.persistence.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.context.Dependent;

import org.cyk.utility.__kernel__.function.AbstractFunctionRunnableImpl;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.field.FieldName;
import org.cyk.utility.instance.InstanceGetter;
import org.cyk.utility.server.persistence.Persistence;

@Dependent
public class InstanceGetterFunctionRunnableImpl extends AbstractFunctionRunnableImpl<InstanceGetter> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public InstanceGetterFunctionRunnableImpl() {
		setRunnable(new Runnable() {
			@Override
			public void run() {
				if(FieldName.IDENTIFIER.equals(getFunction().getFieldName())) {
					Object one = __inject__(Persistence.class).readByIdentifier(getFunction().getClazz(), getFunction().getValue(), new Properties().setValueUsageType(getFunction().getValueUsageType()));
					Collection<Object> collection = new ArrayList<>();
					collection.add(one);
					setOutput(collection);
				}
				//setOutput(output);
				/*if(MyData.class.equals( getFunction().getClazz() )) {
					if("a001".equals(getFunction().getValue()))
						setOutput(Arrays.asList(new MyData().setId("159").setNum("a001")));
				}*/
			}
		});
	}
	
}
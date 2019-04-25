package org.cyk.system.file.server.representation.api;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.function.FunctionRunnableMap;
import org.cyk.utility.instance.InstanceBuilderImpl;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(FunctionRunnableMap.class).set(InstanceBuilderImpl.class, InstanceBuilderFunctionRunnableImpl.class,LEVEL);
	}
	
	@Override
	public void __destroy__(Object object) {}
	
	/**/
	
	public static final Integer LEVEL = 1000;//new Integer(org.cyk.system.file.server.business.ApplicationScopeLifeCycleListener.LEVEL+1);
}
package org.cyk.system.file.server.persistence.impl;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.system.file.server.persistence.entities.FileText;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.klass.PersistableClassesGetter;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		PersistableClassesGetter.COLLECTION.set(List.of(FileBytes.class,FileText.class,File.class));
		__inject__(org.cyk.utility.server.persistence.impl.ApplicationScopeLifeCycleListener.class).initialize(null);
		__inject__(org.cyk.system.file.server.persistence.entities.ApplicationScopeLifeCycleListener.class).initialize(null);
	}
	
	@Override
	public void __destroy__(Object object) {}
	
}
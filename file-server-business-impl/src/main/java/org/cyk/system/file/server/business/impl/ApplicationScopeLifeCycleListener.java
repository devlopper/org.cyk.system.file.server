package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.system.node.SystemNodeServer;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(SystemNodeServer.class).setName("Gestion des fichiers");
	}
	
	@Override
	public void __destroy__(Object object) {}

	public void saveDataFromResources() {
		
	}
	
}

package org.cyk.system.file.server.business.impl.unit;

public abstract class AbstractUnitTest extends org.cyk.utility.test.persistence.server.AbstractUnitTest {

	@Override
	protected void initializeEntityManagerFactory(String persistenceUnitName) {
		super.initializeEntityManagerFactory(persistenceUnitName);		
		org.cyk.system.file.server.persistence.impl.ApplicationScopeLifeCycleListener.initialize();
		
	}
}
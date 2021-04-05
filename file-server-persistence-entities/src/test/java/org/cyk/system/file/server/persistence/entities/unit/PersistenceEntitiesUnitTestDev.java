package org.cyk.system.file.server.persistence.entities.unit;

public class PersistenceEntitiesUnitTestDev extends AbstractUnitTestLive {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getPersistenceUnitName() {
		return "dev";
	}
}
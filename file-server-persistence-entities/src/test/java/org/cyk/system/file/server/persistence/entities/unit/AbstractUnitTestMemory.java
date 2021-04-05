package org.cyk.system.file.server.persistence.entities.unit;

public abstract class AbstractUnitTestMemory extends AbstractUnitTest {

	@Override
	protected String getPersistenceUnitName() {
		return "default";
	}
}
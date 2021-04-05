package org.cyk.system.file.server.persistence.impl.unit;

public abstract class AbstractUnitTestMemory extends AbstractUnitTest {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getPersistenceUnitName() {
		return "default";
	}
}
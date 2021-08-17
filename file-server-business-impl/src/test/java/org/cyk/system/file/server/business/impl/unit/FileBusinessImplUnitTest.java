package org.cyk.system.file.server.business.impl.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.cyk.system.file.server.business.impl.FileBusinessImpl;
import org.cyk.system.file.server.business.impl.integration.ApplicationScopeLifeCycleListener;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.persistence.server.query.executor.DynamicManyExecutor;
import org.cyk.utility.test.business.server.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FileBusinessImplUnitTest extends AbstractUnitTestMemory {
	private static final long serialVersionUID = 1L;
	
	@BeforeAll
	public static void beforeAll() {
		ApplicationScopeLifeCycleListener.INTEGRATION = Boolean.FALSE;
	}
	
	@Override
	protected String getPersistenceUnitName() {
		return "files";
	}
	
	@Test
	public void import_() throws Exception{
		assertThat(DynamicManyExecutor.getInstance().count(File.class)).isEqualTo(1l);
		new Transaction.AbstractImpl() {
			@Override
			protected void __run__(EntityManager entityManager) {
				FileBusinessImpl.import_(List.of("src\\test\\resources\\memory\\data\\files\\binaries"), ".pdf|.txt|.xxx", entityManager);
			}
		}.run();
		assertThat(DynamicManyExecutor.getInstance().count(File.class)).isEqualTo(4l);
	}
}
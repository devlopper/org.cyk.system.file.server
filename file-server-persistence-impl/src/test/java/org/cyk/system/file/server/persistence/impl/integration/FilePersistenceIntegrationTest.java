package org.cyk.system.file.server.persistence.impl.integration;

import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.persistence.test.arquillian.AbstractPersistenceArquillianIntegrationTestWithDefaultDeploymentAsSwram;
import org.junit.Test;

public class FilePersistenceIntegrationTest extends AbstractPersistenceArquillianIntegrationTestWithDefaultDeploymentAsSwram {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void createOneFile() throws Exception{
		String identifier = __getRandomIdentifier__();
		File file = new File().setIdentifier(identifier).setName("file").setBytes("Hello".getBytes()).setMimeType("text/plain").setSize(1l);
		userTransaction.begin();
		__inject__(FilePersistence.class).create(file);
		userTransaction.commit();
		
		file = __inject__(FilePersistence.class).readOneBySystemIdentifier(identifier);
		assertionHelper.assertNotNull(file);
	}
	
}

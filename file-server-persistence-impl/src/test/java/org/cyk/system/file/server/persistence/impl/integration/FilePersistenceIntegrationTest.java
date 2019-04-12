package org.cyk.system.file.server.persistence.impl.integration;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.persistence.test.TestPersistenceCreate;
import org.cyk.utility.server.persistence.test.arquillian.AbstractPersistenceArquillianIntegrationTestWithDefaultDeploymentAsSwram;
import org.junit.Test;

public class FilePersistenceIntegrationTest extends AbstractPersistenceArquillianIntegrationTestWithDefaultDeploymentAsSwram {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void createOneFile() throws Exception{
		String code = __getRandomCode__();
		File file = new File().setCode(code).setName(__getRandomCode__()).setBytes("Hello".getBytes()).setMimeType("text/plain").setSize(1l);
		__inject__(TestPersistenceCreate.class).addObjects(file).execute();
	}
	
}

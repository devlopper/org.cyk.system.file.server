package org.cyk.system.file.server.persistence.impl.integration;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.persistence.test.TestPersistenceCreate;
import org.cyk.utility.server.persistence.test.arquillian.AbstractPersistenceArquillianIntegrationTestWithDefaultDeploymentAsSwram;
import org.junit.Test;

public class NestedSetPersistenceIntegrationTest extends AbstractPersistenceArquillianIntegrationTestWithDefaultDeploymentAsSwram {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void createOneFile() throws Exception{
		File file = new File().setCode(__getRandomCode__()).setName(__getRandomCode__());
		__inject__(TestPersistenceCreate.class).addObjects(file).execute();
	}
	
}

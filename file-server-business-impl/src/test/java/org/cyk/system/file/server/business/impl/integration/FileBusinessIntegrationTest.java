package org.cyk.system.file.server.business.impl.integration;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.business.test.TestBusinessCreate;
import org.cyk.utility.server.business.test.arquillian.AbstractBusinessArquillianIntegrationTestWithDefaultDeploymentAsSwram;
import org.junit.Test;

public class FileBusinessIntegrationTest extends AbstractBusinessArquillianIntegrationTestWithDefaultDeploymentAsSwram {
	private static final long serialVersionUID = 1L;
	
	/* Create */
	
	@Test
	public void createOneRoleType() throws Exception{
		File file = new File().setCode(__getRandomCode__()).setName(__getRandomCode__());
		__inject__(TestBusinessCreate.class).addObjects(file).execute();	
				
	}
	
}

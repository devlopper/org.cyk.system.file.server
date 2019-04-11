package org.cyk.system.file.server.persistence.impl.integration;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.file.FileBuilder;
import org.cyk.utility.server.persistence.test.TestPersistenceCreate;
import org.cyk.utility.server.persistence.test.arquillian.AbstractPersistenceArquillianIntegrationTestWithDefaultDeploymentAsSwram;
import org.junit.Test;

public class FilePersistenceIntegrationTest extends AbstractPersistenceArquillianIntegrationTestWithDefaultDeploymentAsSwram {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void createOneFile() throws Exception{
		File file = new File().setCode(__getRandomCode__()).setName(__getRandomCode__());
		org.cyk.utility.file.File __file__ = __inject__(FileBuilder.class).setClazz(getClass()).setName("text01.txt").execute().getOutput();
		file.setBytes(__file__.getBytes());
		file.setExtension(__file__.getExtension());
		file.setMimeType(__file__.getMimeType());
		file.setName(__file__.getName());
		file.setSize(__file__.getSize());
		file.setUniformResourceLocator(__file__.getUniformResourceLocator());
		
		__inject__(TestPersistenceCreate.class).addObjects(file).execute();
	}
	
}

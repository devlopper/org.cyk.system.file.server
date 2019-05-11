package org.cyk.system.file.server.persistence.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.server.persistence.test.TestPersistenceCreate;
import org.cyk.utility.server.persistence.test.arquillian.AbstractPersistenceArquillianIntegrationTestWithDefaultDeployment;
import org.junit.Test;

public class FilePersistenceIntegrationTest extends AbstractPersistenceArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void createOneFile() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello";
		File file = new File().setIdentifier(identifier).setName("file").setExtension("txt").setMimeType("text/plain").setSize(1l)
				.setSha1("sha1");
		FileBytes fileBytes = new FileBytes().setFile(file).setBytes(text.getBytes());
		__inject__(TestPersistenceCreate.class).addObjects(file,fileBytes).addTryEndRunnables(new Runnable() {
			@Override
			public void run() {
				File file = __inject__(FilePersistence.class).readOneBySystemIdentifier(identifier);
				assertThat(file).isNotNull();
				assertThat(file.getExtension()).isEqualTo("txt");
				assertThat(file.getMimeType()).isEqualTo("text/plain");
				assertThat(file.getName()).isEqualTo("file");
				assertThat(file.getUniformResourceLocator()).isEqualTo(null);
				assertThat(file.getBytes()).isNull();
			}
		}).execute();
	}
	
	@Test
	public void readWhereNameContains() throws Exception{
		userTransaction.begin();
		for(Integer index = 0 ; index < 20 ; index = index + 1) {
			String identifier = __getRandomIdentifier__();
			File file = new File().setIdentifier(identifier).setName("file"+index).setExtension("txt").setMimeType("text/plain").setSize(1l)
					.setUniformResourceLocator("url").setSha1("sha1");
			__inject__(FilePersistence.class).create(file);
			
		}
		userTransaction.commit();
		
		assertThat(__inject__(FilePersistence.class).readWhereNameContains("a")).as("file found").isEmpty();
		assertReadWhereNameContains("f",20);
		assertReadWhereNameContains("i",20);
		assertReadWhereNameContains("10",1);
		assertReadWhereNameContains("file0",1);
		assertReadWhereNameContains("file1",11);
		assertReadWhereNameContains("file11",1);
		
		userTransaction.begin();
		__inject__(FilePersistence.class).deleteAll();
		userTransaction.commit();
	}
	
	private void assertReadWhereNameContains(String string,Integer count) {
		assertThat(__inject__(FilePersistence.class).readWhereNameContains(string)).as("number of file where name contains <<"+string+">> is incorrect").hasSize(count);
	}
	
}

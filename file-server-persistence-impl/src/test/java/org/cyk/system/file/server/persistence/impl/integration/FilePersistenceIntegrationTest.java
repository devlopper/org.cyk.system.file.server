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
		File file = new File().setIdentifier(identifier).setName("file").setExtension("txt").setMimeType("text/plain").setSize(1l);
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
	
}

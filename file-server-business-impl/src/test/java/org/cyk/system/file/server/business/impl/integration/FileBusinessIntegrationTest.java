package org.cyk.system.file.server.business.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.business.test.arquillian.AbstractBusinessArquillianIntegrationTestWithDefaultDeploymentAsSwram;
import org.junit.Test;

public class FileBusinessIntegrationTest extends AbstractBusinessArquillianIntegrationTestWithDefaultDeploymentAsSwram {
	private static final long serialVersionUID = 1L;
	
	/* Create */
	
	@Test
	public void createOneRoleType() throws Exception{
		String code = __getRandomCode__();
		String text = "Hello world.";
		File file = new File().setCode(code).setNameAndExtension("text01.txt").setBytes(text.getBytes());
		__inject__(FileBusiness.class).create(file);
		
		file = __inject__(FileBusiness.class).findOneByBusinessIdentifier(code);
		
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getUniformResourceLocator()).isEqualTo(null);
		assertThat(file.getBytes()).isNotNull();
		assertThat(file.getSize()).isEqualTo(file.getBytes().length);
		assertThat(new String(file.getBytes())).isEqualTo(text);	
				
	}
	
}

package org.cyk.system.file.server.business.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.server.business.test.TestBusinessCreate;
import org.cyk.utility.server.business.test.arquillian.AbstractBusinessArquillianIntegrationTestWithDefaultDeployment;
import org.cyk.utility.string.Strings;
import org.junit.Test;

public class FileBusinessIntegrationTest extends AbstractBusinessArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	/* Create */
	
	@Test
	public void createOneFile() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello world.";
		File file = new File().setIdentifier(identifier).setNameAndExtension("text01.txt").setBytes(text.getBytes());
		__inject__(TestBusinessCreate.class).addObjects(file).addTryEndRunnables(new Runnable() {
			@Override
			public void run() {
				//We get file only
				File file = __inject__(FileBusiness.class).findOneBySystemIdentifier(identifier);
				assertThat(file).isNotNull();
				assertThat(file.getExtension()).isEqualTo("txt");
				assertThat(file.getMimeType()).isEqualTo("text/plain");
				assertThat(file.getName()).isEqualTo("text01");
				assertThat(file.getUniformResourceLocator()).isEqualTo(null);
				assertThat(file.getBytes()).isNull();
				
				//We get file bytes only
				FileBytes fileBytes = __inject__(FileBytesPersistence.class).readByFile(file);
				assertThat(fileBytes).isNotNull();
				assertThat(fileBytes.getBytes()).isNotNull();
				assertThat(new String(fileBytes.getBytes())).isEqualTo(text);	
				assertThat(fileBytes.getBytes().length).isEqualTo(file.getSize().intValue());
				
				//We get file and its bytes
				Properties properties = new Properties();
				properties.setFields(File.FIELD_BYTES);
				file = __inject__(FileBusiness.class).findOne(identifier, properties);				
				assertThat(file).isNotNull();
				assertThat(file.getExtension()).isEqualTo("txt");
				assertThat(file.getMimeType()).isEqualTo("text/plain");
				assertThat(file.getName()).isEqualTo("text01");
				assertThat(file.getUniformResourceLocator()).isEqualTo(null);
				assertThat(file.getBytes()).isNotNull();
				assertThat(file.getBytes().length).isEqualTo(file.getSize().intValue());
				assertThat(new String(file.getBytes())).isEqualTo(text);	
				
				//We get many file only
				file = __inject__(FileBusiness.class).findMany().iterator().next();
				assertThat(file).isNotNull();
				assertThat(file.getExtension()).isEqualTo("txt");
				assertThat(file.getMimeType()).isEqualTo("text/plain");
				assertThat(file.getName()).isEqualTo("text01");
				assertThat(file.getUniformResourceLocator()).isEqualTo(null);
				assertThat(file.getBytes()).isNull();
				
				//We get many file and its bytes
				properties = new Properties();
				properties.setFields(File.FIELD_BYTES);
				file = __inject__(FileBusiness.class).findMany(properties).iterator().next();				
				assertThat(file).isNotNull();
				assertThat(file.getExtension()).isEqualTo("txt");
				assertThat(file.getMimeType()).isEqualTo("text/plain");
				assertThat(file.getName()).isEqualTo("text01");
				assertThat(file.getUniformResourceLocator()).isEqualTo(null);
				assertThat(file.getBytes()).isNotNull();
				assertThat(file.getBytes().length).isEqualTo(file.getSize().intValue());
				assertThat(new String(file.getBytes())).isEqualTo(text);	
			}
		}).execute();
				
	}
	
	@Test
	public void createFileFromDirectories() throws Exception{
		/*__inject__(FileBusiness.class).createFromDirectories(__inject__(Strings.class).add("C:\\Users\\CYK\\Downloads\\Partitions\\Acclamation"));
		Collection<File> files = __inject__(FileBusiness.class).findMany();
		assertThat(files).withFailMessage("No file found").isNotNull();
		assertThat(files).withFailMessage("No file found").isNotEmpty();
		assertThat(files.stream().map(x -> x.getNameAndExtension())).hasSize(15).contains("Alléluia de la Maîtrise.pdf");
		Long count = __inject__(FileBusiness.class).count();
		__inject__(FileBusiness.class).createFromDirectories(__inject__(Strings.class).add("C:\\Users\\CYK\\Downloads\\Partitions\\Acclamation"));
		assertThat(__inject__(FileBusiness.class).count()).withFailMessage("Files have been created again").isEqualTo(count);
		__inject__(FileBusiness.class).deleteMany(files);
		*/
	}
	
	/*@Test
	public void createFileFromDirectories() throws Exception{
		__inject__(FileBusiness.class).createFromDirectories(__inject__(Strings.class).add("C:\\Users\\CYK\\Downloads\\Partitions"));
	}*/
	
}

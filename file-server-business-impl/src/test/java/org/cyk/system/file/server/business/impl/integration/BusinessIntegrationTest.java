package org.cyk.system.file.server.business.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.server.business.test.arquillian.AbstractBusinessArquillianIntegrationTestWithDefaultDeployment;
import org.cyk.utility.server.persistence.query.filter.Filter;
import org.junit.Test;

public class BusinessIntegrationTest extends AbstractBusinessArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	/* Create */
	
	@Test
	public void createOneFile() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello world.";
		File file = new File().setIdentifier(identifier).setNameAndExtension("text01.txt").setBytes(text.getBytes());
		__inject__(FileBusiness.class).create(file);
		
		assertThat(__inject__(FileBusiness.class).count()).isEqualTo(1l);
		assertThat(__inject__(FileBytesBusiness.class).count()).isEqualTo(1l);
		
		//We get file only
		file = __inject__(FileBusiness.class).findBySystemIdentifier(identifier);
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
		properties.setFields(File.FIELD_BYTES+",size");
		file = __inject__(FileBusiness.class).findBySystemIdentifier(identifier, properties);				
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo(null);
		assertThat(file.getMimeType()).isEqualTo(null);
		assertThat(file.getName()).isEqualTo(null);
		assertThat(file.getUniformResourceLocator()).isEqualTo(null);
		assertThat(file.getBytes()).isNotNull();
		assertThat(file.getBytes().length).isEqualTo(file.getSize().intValue());
		assertThat(new String(file.getBytes())).isEqualTo(text);	
		
		//We get many file only
		file = __inject__(FileBusiness.class).find().iterator().next();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getUniformResourceLocator()).isEqualTo(null);
		assertThat(file.getBytes()).isNull();
		
		//We get many file and its bytes
		properties = new Properties();
		properties.setFields(File.FIELD_BYTES+",size");
		file = __inject__(FileBusiness.class).find(properties).iterator().next();				
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo(null);
		assertThat(file.getMimeType()).isEqualTo(null);
		assertThat(file.getName()).isEqualTo(null);
		assertThat(file.getUniformResourceLocator()).isEqualTo(null);
		assertThat(file.getBytes()).isNotNull();
		assertThat(file.getBytes().length).isEqualTo(file.getSize().intValue());
		assertThat(new String(file.getBytes())).isEqualTo(text);
				
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

	@Test
	public void findMany_whereNameContains() throws Exception{
		for(Integer index = 0 ; index < 20 ; index = index + 1) {
			String identifier = __getRandomIdentifier__();
			File file = new File().setIdentifier(identifier).setName("file"+index).setExtension("txt").setMimeType("text/plain").setSize(1l)
					.setUniformResourceLocator("url").setSha1("sha1");
			__inject__(FileBusiness.class).create(file);
			
		}
		
		Filter filter = __inject__(Filter.class).setKlass(File.class).addField(File.FIELD_NAME, "a");
		assertThat(__inject__(FileBusiness.class).find(new Properties().setQueryFilters(filter))).as("file found").isEmpty();
		assertFindMany_whereNameContains("f",20);
		assertFindMany_whereNameContains("i",20);
		assertFindMany_whereNameContains("10",1);
		assertFindMany_whereNameContains("file0",1);
		assertFindMany_whereNameContains("file1",11);
		assertFindMany_whereNameContains("file11",1);
	}
	
	private void assertFindMany_whereNameContains(String string,Integer count) {
		Filter filter = __inject__(Filter.class).setKlass(File.class).addField(File.FIELD_NAME, string);
		assertThat(__inject__(FileBusiness.class).find(new Properties().setQueryFilters(filter)))
			.as("number of file from collection where name contains <<"+string+">> is incorrect").hasSize(count);
		assertThat(__inject__(FileBusiness.class).count(new Properties().setQueryFilters(filter)))
		.as("number of file from count where name contains <<"+string+">> is incorrect").isEqualTo(new Long(count));
	}
}

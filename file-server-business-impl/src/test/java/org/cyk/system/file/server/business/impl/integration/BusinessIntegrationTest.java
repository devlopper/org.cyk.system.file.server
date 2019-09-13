package org.cyk.system.file.server.business.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.api.FileBytesBusiness;
import org.cyk.system.file.server.business.api.FileTextBusiness;
import org.cyk.system.file.server.business.impl.FileBusinessImpl;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.server.business.test.arquillian.AbstractBusinessArquillianIntegrationTestWithDefaultDeployment;
import org.cyk.utility.server.persistence.query.filter.Filter;
import org.junit.Test;

public class BusinessIntegrationTest extends AbstractBusinessArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	/* Create */
	
	@Test
	public void file_create_whereFileBytesDoExists() {
		assertThat(__inject__(FileBusiness.class).count()).isEqualTo(0l);
		assertThat(__inject__(FileBytesBusiness.class).count()).isEqualTo(0l);
		assertThat(__inject__(FileTextBusiness.class).count()).isEqualTo(0l);
		
		String identifier = __getRandomIdentifier__();
		String text = "Hello world.";
		File file = new File().setIdentifier(identifier).setNameAndExtension("text01.txt").setText(text).setBytes(text.getBytes());
		__inject__(FileBusiness.class).create(file);
		
		assertThat(__inject__(FileBusiness.class).count()).isEqualTo(1l);
		assertThat(__inject__(FileBytesBusiness.class).count()).isEqualTo(1l);
		assertThat(__inject__(FileTextBusiness.class).count()).isEqualTo(1l);
	}
	
	@Test
	public void file_create_whereFileBytesDoNotExists() throws IOException {
		java.io.File __file__ = new java.io.File(System.getProperty("user.dir")+"\\target\\files\\myfile001.txt");
		__file__.createNewFile();
		String text = "Hello world.";
		FileUtils.writeByteArrayToFile(__file__, text.getBytes());
		
		assertThat(__inject__(FileBusiness.class).count()).isEqualTo(0l);
		assertThat(__inject__(FileBytesBusiness.class).count()).isEqualTo(0l);
		assertThat(__inject__(FileTextBusiness.class).count()).isEqualTo(0l);
		
		String identifier = __getRandomIdentifier__();		
		File file = new File().setIdentifier(identifier).setNameAndExtension("text01.txt").setText(text).setUniformResourceLocator(__file__.toURI().toString())
				.setSha1("sha1").setSize(1l);
		__inject__(FileBusiness.class).create(file);
		
		assertThat(__inject__(FileBusiness.class).count()).isEqualTo(1l);
		assertThat(__inject__(FileTextBusiness.class).count()).isEqualTo(1l);
		
		file = __inject__(FileBusiness.class).findBySystemIdentifier(identifier,new Properties().setFields("bytes"));
		assertThat(file.getUniformResourceLocator()).isNotNull();
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo(text);
	}
	
	@Test
	public void file_read_bytes_whereFileBytesDoExists() {
		String identifier = __getRandomIdentifier__();
		String text = "Hello world.";
		File file = new File().setIdentifier(identifier).setNameAndExtension("text01.txt").setText(text).setBytes(text.getBytes());
		__inject__(FileBusiness.class).create(file);
		
		file = __inject__(FileBusiness.class).findBySystemIdentifier(identifier,new Properties().setFields("bytes"));
		assertThat(file.getUniformResourceLocator()).isNull();
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo(text);
	}
	
	@Test
	public void file_read_bytes_whereFileBytesDoNotExists() {
		assertThat(__inject__(FileBusiness.class).count()).isEqualTo(0l);
		assertThat(__inject__(FileBytesBusiness.class).count()).isEqualTo(0l);
		assertThat(__inject__(FileTextBusiness.class).count()).isEqualTo(0l);
		FileBusinessImpl.ROOT_FOLDER_PATH = Paths.get(System.getProperty("user.dir")+"\\target\\files");
		String identifier = __getRandomIdentifier__();
		String text = "Hello world.";
		File file = new File().setIdentifier(identifier).setNameAndExtension("text01.txt").setText(text).setBytes(text.getBytes())
				.setIsBytesAccessibleFromUniformResourceLocator(Boolean.TRUE);
		__inject__(FileBusiness.class).create(file);
		assertThat(__inject__(FileBusiness.class).count()).isEqualTo(1l);
		assertThat(__inject__(FileBytesBusiness.class).count()).isEqualTo(0l);
		assertThat(__inject__(FileTextBusiness.class).count()).isEqualTo(1l);
		
		file = __inject__(FileBusiness.class).findBySystemIdentifier(identifier,new Properties().setFields("bytes"));
		assertThat(file.getUniformResourceLocator()).isNotNull();
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo(text);
	}
	
	@Test
	public void file_read_text() {
		String identifier = __getRandomIdentifier__();
		String text = "Hello world.";
		File file = new File().setIdentifier(identifier).setNameAndExtension("text01.txt").setText(text).setBytes(text.getBytes());
		__inject__(FileBusiness.class).create(file);
		
		file = __inject__(FileBusiness.class).findBySystemIdentifier(identifier,new Properties().setFields("text"));
		assertThat(file.getText()).isEqualTo(text);
	}
	
	@Test
	public void createFileFromDirectories() {
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
	public void createFileFromDirectories() {
		__inject__(FileBusiness.class).createFromDirectories(__inject__(Strings.class).add("C:\\Users\\CYK\\Downloads\\Partitions"));
	}*/

	@Test
	public void filter_whereContains() {
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

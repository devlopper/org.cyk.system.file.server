package org.cyk.system.file.server.persistence.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.server.persistence.query.filter.Filter;
import org.cyk.utility.server.persistence.test.arquillian.AbstractPersistenceArquillianIntegrationTestWithDefaultDeployment;
import org.cyk.utility.string.StringHelper;
import org.junit.Test;

public class PersistenceIntegrationTest extends AbstractPersistenceArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void create_file() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello";
		File file = new File().setIdentifier(identifier).setName("file").setExtension("txt").setMimeType("text/plain").setSize(new Long(text.getBytes().length))
				.setSha1("sha1");
		FileBytes fileBytes = new FileBytes().setFile(file).setBytes(text.getBytes());
		
		userTransaction.begin();
		__inject__(FilePersistence.class).create(file);
		__inject__(FileBytesPersistence.class).create(fileBytes);
		userTransaction.commit();
		
		assertRead(identifier,null, Boolean.TRUE, "txt", "text/plain", "file", null,new Long(text.getBytes().length), null,null);
		
		fileBytes = __inject__(FileBytesPersistence.class).readByFile(file);
		assertThat(fileBytes).isNotNull();
	}
	
	@Test
	public void read_file_apllyProjection() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello";
		File file = new File().setIdentifier(identifier).setName("file").setExtension("txt").setMimeType("text/plain").setSize(new Long(text.getBytes().length))
				.setSha1("sha1");
		FileBytes fileBytes = new FileBytes().setFile(file).setBytes(text.getBytes());
		
		userTransaction.begin();
		__inject__(FilePersistence.class).create(file);
		__inject__(FileBytesPersistence.class).create(fileBytes);
		userTransaction.commit();
		
		assertRead(identifier,null, Boolean.TRUE, "txt", "text/plain", "file", null,new Long(text.getBytes().length), null,null);
		assertRead(identifier,"identifier", Boolean.TRUE, null, null, null, null, null,null,null);
		assertRead(identifier,"extension", Boolean.TRUE, "txt", null, null, null, null,null,null);
		assertRead(identifier,"mimeType", Boolean.TRUE, null, "text/plain", null, null, null,null,null);
		assertRead(identifier,"name", Boolean.TRUE, null, null, "file", null, null,null,null);
		assertRead(identifier,"nameAndExtension", Boolean.TRUE, null, null, null, null, null,null,"file.txt");
		assertRead(identifier,"size", Boolean.TRUE, null, null, null, null, new Long(text.getBytes().length),null,null);
	}
	
	@Test
	public void read_whereNameContains() throws Exception{
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
	}
	
	@Test
	public void read_filter_whereNameContains() throws Exception{
		userTransaction.begin();
		for(Integer index = 0 ; index < 20 ; index = index + 1) {
			String identifier = __getRandomIdentifier__();
			File file = new File().setIdentifier(identifier).setName("file"+index).setExtension("txt").setMimeType("text/plain").setSize(1l)
					.setUniformResourceLocator("url").setSha1("sha1");
			__inject__(FilePersistence.class).create(file);
			
		}
		userTransaction.commit();
		
		assertRead_whereNameContains("a",0);
		assertRead_whereNameContains("f",20);
		assertRead_whereNameContains("F",20);
		assertRead_whereNameContains("FILE",20);
		assertRead_whereNameContains("fILe",20);
		assertRead_whereNameContains("i",20);
		assertRead_whereNameContains("10",1);
		assertRead_whereNameContains("file0",1);
		assertRead_whereNameContains("file1",11);
		assertRead_whereNameContains("file11",1);
	}
	
	/**/
	
	private void assertRead(String identifier,String fields,Boolean expectedIsNotNull,String expectedExtension,String expectedMimeType,String expectedName,String expectedURL,Long expectedSize,Boolean expectedBytesIsNotNull,String expectedNameAndExtension) {
		File file = __inject__(FilePersistence.class).readBySystemIdentifier(identifier,__inject__(StringHelper.class).isBlank(fields) ? null : new Properties().setFields(fields));
		if(expectedIsNotNull != null && expectedIsNotNull) {
			assertThat(file).as(String.format("file with identifier %s does not exist",identifier)).isNotNull();
			assertThat(file.getExtension()).as("extension does not match").isEqualTo(expectedExtension);
			assertThat(file.getMimeType()).as("mime type does not match").isEqualTo(expectedMimeType);
			assertThat(file.getName()).as("name does not match").isEqualTo(expectedName);
			assertThat(file.getUniformResourceLocator()).as("URL does not match").isEqualTo(expectedURL);
			assertThat(file.getSize()).as("size does not match").isEqualTo(expectedSize);
			if(expectedBytesIsNotNull != null && expectedBytesIsNotNull)
				assertThat(file.getBytes()).as("bytes is null").isNotNull();
			else
				assertThat(file.getBytes()).as("bytes is not null").isNull();
		}else
			assertThat(file).as(String.format("file with identifier %s exists",identifier)).isNull();
	}
	
	private void assertReadWhereNameContains(String string,Integer count) {
		assertThat(__inject__(FilePersistence.class).readWhereNameContains(string)).as("number of file from collection where name contains <<"+string+">> is incorrect").hasSize(count);
		assertThat(__inject__(FilePersistence.class).countWhereNameContains(string)).as("number of file from count where name contains <<"+string+">> is incorrect").isEqualTo(new Long(count));
	}
	
	private void assertRead_whereNameContains(String string,Integer count) {
		Filter filter = __inject__(Filter.class).setKlass(File.class).addField(File.FIELD_NAME, string);
		assertThat(__inject__(FilePersistence.class).read(new Properties().setQueryFilters(filter))).as("specific : number of file from collection where name contains <<"+string+">> is incorrect").hasSize(count);
		assertThat(__inject__(FilePersistence.class).count(new Properties().setQueryFilters(filter))).as("specific : number of file from count where name contains <<"+string+">> is incorrect").isEqualTo(new Long(count));

		filter = __inject__(Filter.class).setValue(string);
		assertThat(__inject__(FilePersistence.class).read(new Properties().setQueryFilters(filter))).as("global : number of file from collection where name contains <<"+string+">> is incorrect").hasSize(count);
		assertThat(__inject__(FilePersistence.class).count(new Properties().setQueryFilters(filter))).as("global : number of file from count where name contains <<"+string+">> is incorrect").isEqualTo(new Long(count));
	}
	
}

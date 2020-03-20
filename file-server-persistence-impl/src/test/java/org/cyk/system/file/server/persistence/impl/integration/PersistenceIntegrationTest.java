package org.cyk.system.file.server.persistence.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;

import org.cyk.system.file.server.persistence.api.FileBytesPersistence;
import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.api.FileTextPersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.system.file.server.persistence.entities.FileText;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.server.persistence.test.arquillian.AbstractPersistenceArquillianIntegrationTestWithDefaultDeployment;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.junit.Test;

public class PersistenceIntegrationTest extends AbstractPersistenceArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void file_create() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello";
		File file = new File().setIdentifier(identifier).setName("file").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length))
				.setSha1("sha1");
		FileBytes fileBytes = new FileBytes().setFile(file).setBytes(text.getBytes());
		FileText fileText = new FileText().setFile(file).setText(text);
		
		userTransaction.begin();
		__inject__(FilePersistence.class).create(file);
		__inject__(FileBytesPersistence.class).create(fileBytes);
		__inject__(FileTextPersistence.class).create(fileText);
		userTransaction.commit();
		
		assertRead(identifier,null, Boolean.TRUE,"file", "txt", "text/plain",  null,null,Long.valueOf(text.getBytes().length), null,null);
		
		fileBytes = __inject__(FileBytesPersistence.class).readByFile(file);
		assertThat(fileBytes).isNotNull();
	}
	
	@Test
	public void file_read_nameAndExtension() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello";
		File file = new File().setIdentifier(identifier).setName("file").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length))
				.setSha1("sha1");
		FileBytes fileBytes = new FileBytes().setFile(file).setBytes(text.getBytes());
		FileText fileText = new FileText().setFile(file).setText(text);
		
		userTransaction.begin();
		__inject__(FilePersistence.class).create(file);
		__inject__(FileBytesPersistence.class).create(fileBytes);
		__inject__(FileTextPersistence.class).create(fileText);
		userTransaction.commit();
		
		assertRead(identifier,"nameAndExtension", Boolean.TRUE,"file", "txt", "text/plain",  "file.txt",null,Long.valueOf(text.getBytes().length), null,null);
	}
	
	@Test
	public void file_read_bytes() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello";
		File file = new File().setIdentifier(identifier).setName("file").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length))
				.setSha1("sha1");
		FileBytes fileBytes = new FileBytes().setFile(file).setBytes(text.getBytes());
		FileText fileText = new FileText().setFile(file).setText(text);
		
		userTransaction.begin();
		__inject__(FilePersistence.class).create(file);
		__inject__(FileBytesPersistence.class).create(fileBytes);
		__inject__(FileTextPersistence.class).create(fileText);
		userTransaction.commit();
		
		assertRead(identifier,"bytes", Boolean.TRUE,"file", "txt", "text/plain",null,null,Long.valueOf(text.getBytes().length), Boolean.TRUE,null);
	}
	
	@Test
	public void file_read_text() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello";
		File file = new File().setIdentifier(identifier).setName("file").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length))
				.setSha1("sha1");
		FileBytes fileBytes = new FileBytes().setFile(file).setBytes(text.getBytes());
		FileText fileText = new FileText().setFile(file).setText(text);
		
		userTransaction.begin();
		__inject__(FilePersistence.class).create(file);
		__inject__(FileBytesPersistence.class).create(fileBytes);
		__inject__(FileTextPersistence.class).create(fileText);
		userTransaction.commit();
		
		assertRead(identifier,"text", Boolean.TRUE,"file", "txt", "text/plain",null,null,Long.valueOf(text.getBytes().length), null,"Hello");
	}
	
	@Test
	public void file_read_nameAndExtension_bytes_text() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello";
		File file = new File().setIdentifier(identifier).setName("file").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length))
				.setSha1("sha1");
		FileBytes fileBytes = new FileBytes().setFile(file).setBytes(text.getBytes());
		FileText fileText = new FileText().setFile(file).setText(text);
		
		userTransaction.begin();
		__inject__(FilePersistence.class).create(file);
		__inject__(FileBytesPersistence.class).create(fileBytes);
		__inject__(FileTextPersistence.class).create(fileText);
		userTransaction.commit();
		
		assertRead(identifier,"nameAndExtension,bytes,text", Boolean.TRUE,"file", "txt", "text/plain",  "file.txt",null,Long.valueOf(text.getBytes().length), Boolean.TRUE,"Hello");
	}
	
	@Test
	public void read_name_alphabetic_order() throws Exception{
		String text = "Hello";
		userTransaction.begin();
		__inject__(FilePersistence.class).createMany(CollectionHelper.listOf(
				new File().setName("c").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length)).setSha1("sha1")
				,new File().setName("a").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length)).setSha1("sha1")
				,new File().setName("b").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length)).setSha1("sha1")
				,new File().setName("e").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length)).setSha1("sha1")
				,new File().setName("d").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length)).setSha1("sha1")
				));
		userTransaction.commit();
		
		assertThat(__inject__(FilePersistence.class).read().stream().map(File::getName).collect(Collectors.toList())).containsExactly("a","b","c","d","e");
		
		userTransaction.begin();
		__inject__(FilePersistence.class).createMany(CollectionHelper.listOf(
				new File().setName("g").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length)).setSha1("sha1")
				,new File().setName("a1").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length)).setSha1("sha1")
				,new File().setName("a").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length)).setSha1("sha1")
				));
		userTransaction.commit();
		
		assertThat(__inject__(FilePersistence.class).read().stream().map(File::getName).collect(Collectors.toList())).containsExactly("a","a","a1","b","c","d","e","g");
		
		userTransaction.begin();
		__inject__(FilePersistence.class).createMany(CollectionHelper.listOf(
				new File().setName("f").setExtension("txt").setMimeType("text/plain").setSize(Long.valueOf(text.getBytes().length)).setSha1("sha1")
				));
		userTransaction.commit();
		
		assertThat(__inject__(FilePersistence.class).read().stream().map(File::getName).collect(Collectors.toList())).containsExactly("a","a","a1","b","c","d","e","f","g");
	}
	
	@Test
	public void read_whereNameContains() throws Exception{
		userTransaction.begin();
		for(Integer index = 0 ; index < 20 ; index = index + 1) {
			String identifier = __getRandomIdentifier__();
			File file = new File().setIdentifier(identifier).setName("file"+index).setExtension("txt").setMimeType("text/plain").setSize(1l)
					.setUniformResourceLocator("url"+index).setSha1("sha1");
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
	public void filter_whereContains() throws Exception{
		userTransaction.begin();
		for(Integer index = 0 ; index < 20 ; index = index + 1) {
			String identifier = __getRandomIdentifier__();
			File file = new File().setIdentifier(identifier).setName("file"+index).setExtension("txt").setMimeType("text/plain").setSize(1l)
					.setUniformResourceLocator("url"+index).setSha1("sha1");
			__inject__(FilePersistence.class).create(file);
			__inject__(FileTextPersistence.class).create(new FileText().setFile(file).setText("content "+index));
		}
		userTransaction.commit();
		
		assertFilter_whereContains("a",0);
		assertFilter_whereContains("f",20);
		assertFilter_whereContains("F",20);
		assertFilter_whereContains("FILE",20);
		assertFilter_whereContains("fILe",20);
		assertFilter_whereContains("i",20);
		assertFilter_whereContains("10",1);
		assertFilter_whereContains("file0",1);
		assertFilter_whereContains("file1",11);
		assertFilter_whereContains("file11",1);
		assertFilter_whereContains("content",20);
		assertFilter_whereContains("content 0",1);
		assertFilter_whereContains("content 1",11);
	}
	
	@Test
	public void file_readUniformResourceLocators() throws Exception{
		userTransaction.begin();
		__inject__(FilePersistence.class).create(new File().setName("file").setMimeType("text/plain").setSize(0l).setSha1("sha1").setUniformResourceLocator("url01"));
		__inject__(FilePersistence.class).create(new File().setName("file").setMimeType("text/plain").setSize(0l).setSha1("sha1").setUniformResourceLocator("url02"));
		__inject__(FilePersistence.class).create(new File().setName("file").setMimeType("text/plain").setSize(0l).setSha1("sha1").setUniformResourceLocator("url03"));
		__inject__(FilePersistence.class).create(new File().setName("file").setMimeType("text/plain").setSize(0l).setSha1("sha1").setUniformResourceLocator("url00"));
		userTransaction.commit();
		assertThat(__inject__(FilePersistence.class).readUniformResourceLocators(null)).containsExactlyInAnyOrder("url00","url01","url02","url03");
	}
	
	/**/
	
	private void assertRead(String identifier,String fields,Boolean expectedIsNotNull,String expectedName,String expectedExtension,String expectedMimeType,String expectedNameAndExtension,String expectedURL,Long expectedSize,Boolean expectedBytesIsNotNull,String expectedText) {
		File file = __inject__(FilePersistence.class).readBySystemIdentifier(identifier,StringHelper.isBlank(fields) ? null : new Properties().setFields(fields));
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
		assertThat(__inject__(FilePersistence.class).countWhereNameContains(string)).as("number of file from count where name contains <<"+string+">> is incorrect").isEqualTo(Long.valueOf(count));
	}
	
	private void assertFilter_whereContains(String string,Integer count) {
		Filter filter = __inject__(Filter.class).setValue(string);
		assertThat(__inject__(FilePersistence.class).read(new Properties().setQueryFilters(filter))).as("global : number of file from collection where name or text contains <<"+string+">> is incorrect").hasSize(count);
		assertThat(__inject__(FilePersistence.class).count(new Properties().setQueryFilters(filter))).as("global : number of file from count where name or text contains <<"+string+">> is incorrect").isEqualTo(Long.valueOf(count));
	}
	
}

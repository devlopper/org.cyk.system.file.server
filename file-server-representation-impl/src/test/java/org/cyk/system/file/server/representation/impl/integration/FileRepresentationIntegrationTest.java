package org.cyk.system.file.server.representation.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.server.representation.AbstractEntityCollection;
import org.cyk.utility.server.representation.test.arquillian.AbstractRepresentationArquillianIntegrationTestWithDefaultDeployment;
import org.junit.Test;

public class FileRepresentationIntegrationTest extends AbstractRepresentationArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	@Test
	public void createOneFile() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "Hello";
		FileDto file = new FileDto().setIdentifier(identifier).setNameAndExtension("text01.txt").setBytes(text.getBytes());
		__inject__(FileRepresentation.class).createOne(file);
		
		file = (FileDto) __inject__(FileRepresentation.class).getOne(identifier, "system",null).getEntity();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getSize()).isEqualTo(text.length());
		//assertThat(file.getUniformResourceLocator()).isEqualTo("http://127.0.0.1:11080/file/server/file/"+file.getIdentifier()+"/download?isinline=true");
		assertThat(file.getBytes()).isNull();
		
		file = (FileDto) __inject__(FileRepresentation.class).getOne(identifier, "system","name,extension,mimeType").getEntity();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getSize()).isNull();
		assertThat(file.getUniformResourceLocator()).isNull();
		assertThat(file.getBytes()).isNull();
		
		file = (FileDto) __inject__(FileRepresentation.class).getOne(identifier, "system","bytes").getEntity();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isNull();
		assertThat(file.getMimeType()).isNull();
		assertThat(file.getName()).isNull();
		assertThat(file.getSize()).isNull();
		assertThat(file.getUniformResourceLocator()).isNull();
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo(text);
		
		file = ((Collection<FileDto>) __inject__(FileRepresentation.class).getMany(null,null,null,null,null).getEntity()).iterator().next();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getSize()).isEqualTo(text.length());
		//assertThat(file.getUniformResourceLocator()).isEqualTo("http://127.0.0.1:11080/file/server/file/"+file.getIdentifier()+"/download?isinline=true");
		assertThat(file.getBytes()).isNull();
		
		file = ((Collection<FileDto>) __inject__(FileRepresentation.class).getMany(null,null,null,"name,extension,mimeType",null).getEntity()).iterator().next();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getSize()).isNull();
		assertThat(file.getUniformResourceLocator()).isNull();
		assertThat(file.getBytes()).isNull();
		
		file = ((Collection<FileDto>) __inject__(FileRepresentation.class).getMany(null,null,null,"bytes",null).getEntity()).iterator().next();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isNull();
		assertThat(file.getMimeType()).isNull();
		assertThat(file.getName()).isNull();
		assertThat(file.getSize()).isNull();
		assertThat(file.getUniformResourceLocator()).isNull();
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo(text);
		
		__inject__(FileBusiness.class).deleteBySystemIdentifier(identifier);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getMany_whereNameContains() throws Exception{
		for(Integer index = 0 ; index < 20 ; index = index + 1) {
			String identifier = __getRandomIdentifier__();
			File file = new File().setIdentifier(identifier).setName("file"+index).setExtension("txt").setMimeType("text/plain").setSize(1l)
					.setUniformResourceLocator("url").setSha1("sha1");
			__inject__(FileBusiness.class).create(file);
			
		}
		
		assertThat((Collection<FileDto>)__inject__(FileRepresentation.class).getMany(null,null,null,null,null).getEntity()).as("file not found").hasSize(5);
		//assertThat((Collection<FileDto>)__inject__(FileRepresentation.class).getMany(null,null,null,(List<String>) __inject__(CollectionHelper.class).instanciate("a")).getEntity()
		//		).as("file found").isEmpty();
		assertGetMany_whereNameContains("f",20);
		assertGetMany_whereNameContains("i",20);
		assertGetMany_whereNameContains("10",1);
		assertGetMany_whereNameContains("file0",1);
		assertGetMany_whereNameContains("file1",11);
		assertGetMany_whereNameContains("file11",1);		
		__inject__(FileBusiness.class).deleteAll();
	}
	
	@Test
	public void downloadOneFile() throws Exception{
		String identifier = __getRandomIdentifier__();
		String text = "This is a content";
		FileDto file = new FileDto().setIdentifier(identifier).setNameAndExtension("text01.txt").setBytes(text.getBytes());
		__inject__(FileRepresentation.class).createOne(file);
		
		byte[] bytes = (byte[]) __inject__(FileRepresentation.class).download(identifier,null).getEntity();
		assertThat(new String(bytes)).isEqualTo(text);
		
		__inject__(FileBusiness.class).deleteBySystemIdentifier(identifier);
	}
	
	@Test
	public void createFromDirectories() throws Exception{
		/*__inject__(FileRepresentation.class).createFromDirectories("C:\\Users\\CYK\\Downloads\\Partitions\\Acclamation");
		@SuppressWarnings("unchecked")
		Collection<FileDto> files = (Collection<FileDto>) __inject__(FileRepresentation.class).getMany(null, null, null).getEntity();
		assertThat(files).withFailMessage("No file found").isNotNull();
		assertThat(files).withFailMessage("No file found").isNotEmpty();
		assertThat(files.stream().map(x -> x.getName())).hasSize(15).contains("Alléluia de la Maîtrise");
		//Long count = __inject__(FileBusiness.class).count();
		//__inject__(FileBusiness.class).createFromDirectories(__inject__(Strings.class).add("C:\\Users\\CYK\\Downloads\\Partitions\\Acclamation"));
		//assertThat(__inject__(FileBusiness.class).count()).withFailMessage("Files have been created again").isEqualTo(count);
		for(FileDto index : files)
			__inject__(FileBusiness.class).deleteBySystemIdentifier(index.getIdentifier());
		*/
	}
	
	@SuppressWarnings("unchecked")
	private void assertGetMany_whereNameContains(String string,Integer count) {
		assertThat((Collection<FileDto>)__inject__(FileRepresentation.class).getMany(Boolean.TRUE,0l,new Long(count),null,(List<String>) __inject__(CollectionHelper.class).instanciate(string)).getEntity())
				.as("number of file where name contains <<"+string+">> is incorrect").hasSize(count);
	}
	
	@Override
	protected <ENTITY> Class<? extends AbstractEntityCollection<ENTITY>> __getEntityCollectionClass__(Class<ENTITY> aClass) {
		return null;
	}
	

}

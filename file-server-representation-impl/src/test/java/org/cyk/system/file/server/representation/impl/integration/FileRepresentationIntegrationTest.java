package org.cyk.system.file.server.representation.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.server.representation.AbstractEntityCollection;
import org.cyk.utility.server.representation.test.arquillian.AbstractRepresentationArquillianIntegrationTestWithDefaultDeploymentAsSwram;
import org.junit.Test;

public class FileRepresentationIntegrationTest extends AbstractRepresentationArquillianIntegrationTestWithDefaultDeploymentAsSwram {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void createOneFile() throws Exception{
		String code = __getRandomCode__();
		String text = "Hello";
		FileDto file = new FileDto().setCode(code).setNameAndExtension("text01.txt").setBytes(text.getBytes());
		__inject__(FileRepresentation.class).createOne(file);
		
		file = (FileDto) __inject__(FileRepresentation.class).getOne(code, "business",null).getEntity();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getSize()).isEqualTo(text.length());
		assertThat(file.getUniformResourceLocator()).isEqualTo(null);
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo(text);
		
		file = (FileDto) __inject__(FileRepresentation.class).getOne(code, "business","name,extension,mimeType").getEntity();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getSize()).isNull();
		assertThat(file.getUniformResourceLocator()).isNull();
		assertThat(file.getBytes()).isNull();
		
		file = (FileDto) __inject__(FileRepresentation.class).getOne(code, "business","bytes").getEntity();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isNull();
		assertThat(file.getMimeType()).isNull();
		assertThat(file.getName()).isNull();
		assertThat(file.getSize()).isNull();
		assertThat(file.getUniformResourceLocator()).isNull();
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo(text);
		
		file = ((Collection<FileDto>) __inject__(FileRepresentation.class).getMany(null).getEntity()).iterator().next();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getSize()).isEqualTo(text.length());
		assertThat(file.getUniformResourceLocator()).isEqualTo(null);
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo(text);
		
		file = ((Collection<FileDto>) __inject__(FileRepresentation.class).getMany("name,extension,mimeType").getEntity()).iterator().next();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo("txt");
		assertThat(file.getMimeType()).isEqualTo("text/plain");
		assertThat(file.getName()).isEqualTo("text01");
		assertThat(file.getSize()).isNull();
		assertThat(file.getUniformResourceLocator()).isNull();
		assertThat(file.getBytes()).isNull();
		
		file = ((Collection<FileDto>) __inject__(FileRepresentation.class).getMany("bytes").getEntity()).iterator().next();
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isNull();
		assertThat(file.getMimeType()).isNull();
		assertThat(file.getName()).isNull();
		assertThat(file.getSize()).isNull();
		assertThat(file.getUniformResourceLocator()).isNull();
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo(text);
		
	}
	
	@Override
	protected <ENTITY> Class<? extends AbstractEntityCollection<ENTITY>> __getEntityCollectionClass__(Class<ENTITY> aClass) {
		return null;
	}
	

}

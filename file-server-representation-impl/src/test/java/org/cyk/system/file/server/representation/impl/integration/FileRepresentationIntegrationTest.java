package org.cyk.system.file.server.representation.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.file.FileBuilder;
import org.cyk.utility.server.representation.AbstractEntityCollection;
import org.cyk.utility.server.representation.test.arquillian.AbstractRepresentationArquillianIntegrationTestWithDefaultDeploymentAsSwram;
import org.junit.Test;

public class FileRepresentationIntegrationTest extends AbstractRepresentationArquillianIntegrationTestWithDefaultDeploymentAsSwram {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void createOneFile() throws Exception{
		org.cyk.utility.file.File __file__ = __inject__(FileBuilder.class)
				/*.setInputStream(getClass().getResourceAsStream("text01.txt"))*/
				.setBytes("Hello".getBytes())
				.setName("text01.txt").execute().getOutput();
	
		String code = __getRandomCode__();
		
		FileDto file = new FileDto();
		file.setCode(code);
		file.setBytes(__file__.getBytes());
		file.setExtension(__file__.getExtension());
		file.setMimeType(__file__.getMimeType());
		file.setName(__file__.getName());
		file.setSize(__file__.getSize());
		file.setUniformResourceLocator(__file__.getUniformResourceLocator());
		
		__inject__(FileRepresentation.class).createOne(file);
		
		file = (FileDto) __inject__(FileRepresentation.class).getOne(code, "business",null).getEntity();
		
		assertThat(file).isNotNull();
		assertThat(file.getExtension()).isEqualTo(__file__.getExtension());
		assertThat(file.getMimeType()).isEqualTo(__file__.getMimeType());
		assertThat(file.getName()).isEqualTo(__file__.getName());
		assertThat(file.getSize()).isEqualTo(__file__.getSize());
		assertThat(file.getUniformResourceLocator()).isEqualTo(__file__.getUniformResourceLocator());
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo("Hello");
		
		file = (FileDto) __inject__(FileRepresentation.class).getOne(code, "business","name,extension,mimeType").getEntity();
		assertThat(file).isNotNull();
		assertThat(file.getName()).isEqualTo(__file__.getName());
		assertThat(file.getExtension()).isEqualTo(__file__.getExtension());
		assertThat(file.getMimeType()).isEqualTo(__file__.getMimeType());
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
		assertThat(new String(file.getBytes())).isEqualTo("Hello");
	}
	
	@Override
	protected <ENTITY> Class<? extends AbstractEntityCollection<ENTITY>> __getEntityCollectionClass__(Class<ENTITY> aClass) {
		return null;
	}
	

}

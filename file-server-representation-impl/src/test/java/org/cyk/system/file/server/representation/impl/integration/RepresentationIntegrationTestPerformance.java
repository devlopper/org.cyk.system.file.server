package org.cyk.system.file.server.representation.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.clazz.ClassInstancesRuntime;
import org.cyk.utility.server.persistence.query.filter.FilterDto;
import org.cyk.utility.server.representation.AbstractEntityCollection;
import org.cyk.utility.server.representation.test.arquillian.AbstractRepresentationArquillianIntegrationTestWithDefaultDeployment;
import org.cyk.utility.string.StringHelper;
import org.junit.Test;

public class RepresentationIntegrationTestPerformance extends AbstractRepresentationArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void get_whereNameContains() throws Exception{
		__inject__(ClassInstancesRuntime.class).get(File.class).setIsActionable(Boolean.FALSE);//FIXME
		Integer numberOfFiles = 1000;
		System.out.println("Generating files : "+numberOfFiles);
		Collection<File> files = new ArrayList<>();
		for(Integer index = 0 ; index < numberOfFiles ; index = index + 1) {
			String identifier = __getRandomIdentifier__();
			File file = new File().setIdentifier(identifier).setName("file"+index).setExtension("txt").setMimeType("text/plain").setSize(1l)
					.setUniformResourceLocator("url").setSha1("sha1");
			files.add(file);
		}
		System.out.println("Creating files : "+numberOfFiles);
		__inject__(FileBusiness.class).createByBatch(files,100);
		
		assertGetMany_whereNameContains(null,0,20);
		assertGetMany_whereNameContains(null,0,100);
		assertGetMany_whereNameContains(null,0,250);
		assertGetMany_whereNameContains(null,0,500);
		assertGetMany_whereNameContains(null,0,1000);
	}
	
	@SuppressWarnings("unchecked")
	private void assertGetMany_whereNameContains(String string,Integer from,Integer count) {
		System.out.print("Getting files from "+from+" , count "+count+" : ");
		FilterDto filter = __inject__(StringHelper.class).isEmpty(string) ? null : new FilterDto().setKlass(File.class).addField(File.FIELD_NAME, string);
		Long t = System.currentTimeMillis();
		assertThat((Collection<FileDto>)__inject__(FileRepresentation.class).getMany(Boolean.TRUE,new Long(from),new Long(count),null,filter).getEntity())
				.as("number of file where name contains <<"+string+">> is incorrect").hasSize(count);
		t = System.currentTimeMillis() - t;
		System.out.println(t);
	}
	
	@Override
	protected <ENTITY> Class<? extends AbstractEntityCollection<ENTITY>> __getEntityCollectionClass__(Class<ENTITY> aClass) {
		return null;
	}
	

}

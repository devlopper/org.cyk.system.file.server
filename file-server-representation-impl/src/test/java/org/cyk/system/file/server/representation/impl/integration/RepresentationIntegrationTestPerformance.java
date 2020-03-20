package org.cyk.system.file.server.representation.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.__kernel__.klass.Property;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.server.representation.test.arquillian.AbstractRepresentationArquillianIntegrationTestWithDefaultDeployment;
import org.junit.Test;

public class RepresentationIntegrationTestPerformance extends AbstractRepresentationArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void get_whereNameContains() throws Exception{
		Property.setProperty(File.class, Property.ACTIONABLE, Boolean.FALSE);
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
		FilterDto filter = StringHelper.isEmpty(string) ? null : new FilterDto().useKlass(File.class).addField(File.FIELD_NAME, string);
		Long t = System.currentTimeMillis();
		assertThat((Collection<FileDto>)__inject__(FileRepresentation.class).getMany(null,Boolean.TRUE,Long.valueOf(from),Long.valueOf(count),null,filter).getEntity())
				.as("number of file where name contains <<"+string+">> is incorrect").hasSize(count);
		t = System.currentTimeMillis() - t;
		System.out.println(t);
	}

}

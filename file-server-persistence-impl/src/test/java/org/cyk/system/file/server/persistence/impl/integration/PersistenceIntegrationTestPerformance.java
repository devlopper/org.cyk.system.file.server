package org.cyk.system.file.server.persistence.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.server.persistence.test.arquillian.AbstractPersistenceArquillianIntegrationTestWithDefaultDeployment;
import org.junit.Test;

public class PersistenceIntegrationTestPerformance extends AbstractPersistenceArquillianIntegrationTestWithDefaultDeployment {
	private static final long serialVersionUID = 1L;
	
	@Test
	public void read_filter_whereNameContains() throws Exception{
		userTransaction.begin();
		Integer numberOfFiles = 10000;
		System.out.println("Generating files : "+numberOfFiles);
		Collection<File> files = new ArrayList<>();
		for(Integer index = 0 ; index < numberOfFiles ; index = index + 1) {
			String identifier = __getRandomIdentifier__();
			File file = new File().setIdentifier(identifier).setName("file"+index).setExtension("txt").setMimeType("text/plain").setSize(1l)
					.setUniformResourceLocator("url").setSha1("sha1");
			files.add(file);
		}
		System.out.println("Creating files : "+numberOfFiles);
		__inject__(FilePersistence.class).createMany(files);
		userTransaction.commit();
		
		assertRead_whereNameContains(null,0,0,numberOfFiles);
		assertRead_whereNameContains(null,0,10,numberOfFiles);
		assertRead_whereNameContains(null,0,50,numberOfFiles);
		assertRead_whereNameContains(null,0,100,numberOfFiles);
		assertRead_whereNameContains(null,0,250,numberOfFiles);
		assertRead_whereNameContains(null,0,500,numberOfFiles);
		assertRead_whereNameContains(null,0,1000,numberOfFiles);
		assertRead_whereNameContains(null,0,2500,numberOfFiles);
		assertRead_whereNameContains(null,0,5000,numberOfFiles);
		assertRead_whereNameContains(null,0,10000,numberOfFiles);
	}
	
	/**/
	
	private void assertRead_whereNameContains(String string,Integer from,Integer count,Integer expectedCount) {
		Long t = System.currentTimeMillis();
		System.out.print("Getting files from "+from+" , count "+count+" : ");
		Filter filter = string == null ? null : __inject__(Filter.class).setKlass(File.class).addField(File.FIELD_NAME, string);
		//assertThat(__inject__(FilePersistence.class).read(new Properties().setQueryFilters(filter))).as("specific : number of file from collection where name contains <<"+string+">> is incorrect").hasSize(expectedCount);
		//assertThat(__inject__(FilePersistence.class).count(new Properties().setQueryFilters(filter))).as("specific : number of file from count where name contains <<"+string+">> is incorrect").isEqualTo(new Long(expectedCount));

		filter = string == null ? null : __inject__(Filter.class).setValue(string);
		assertThat(__inject__(FilePersistence.class).read(new Properties().setQueryFilters(filter))).as("global : number of file from collection where name contains <<"+string+">> is incorrect").hasSize(expectedCount);
		//assertThat(__inject__(FilePersistence.class).count(new Properties().setQueryFilters(filter))).as("global : number of file from count where name contains <<"+string+">> is incorrect").isEqualTo(new Long(expectedCount));
		t = System.currentTimeMillis() - t;
		System.out.println(t);
	}
	
}

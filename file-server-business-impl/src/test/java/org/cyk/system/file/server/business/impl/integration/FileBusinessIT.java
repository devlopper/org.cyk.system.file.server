package org.cyk.system.file.server.business.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.persistence.query.EntityCounter;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

public class FileBusinessIT extends AbstractBusinessIT {

	@Inject private FileBusiness fileBusiness;
	
    @Test @InSequence(1)
    public void file_create_withoutBytesPersisted() {
    	File file = new File().setName("MyFile01").setExtension("txt").setBytes(new String("Hello 01").getBytes());    	
    	Long filesCount = EntityCounter.getInstance().count(File.class);
    	Long filesBytesCount = EntityCounter.getInstance().count(FileBytes.class); 	
		fileBusiness.create(file);			
		assertThat(EntityCounter.getInstance().count(File.class)).isEqualTo(filesCount+1);
		assertThat(EntityCounter.getInstance().count(FileBytes.class)).isEqualTo(filesBytesCount);
    }
    
    @Test @InSequence(2)
    public void file_create_withBytesPersisted() {
    	File file = new File().setName("MyFile02").setExtension("txt").setBytes(new String("Hello 02").getBytes()).setIsBytesPersistableOnCreate(Boolean.TRUE);    	
    	Long filesCount = EntityCounter.getInstance().count(File.class);
    	Long filesBytesCount = EntityCounter.getInstance().count(FileBytes.class);
    	fileBusiness.create(file);				
		assertThat(EntityCounter.getInstance().count(File.class)).isEqualTo(filesCount+1);
		assertThat(EntityCounter.getInstance().count(FileBytes.class)).isEqualTo(filesBytesCount+1);
    }
    
    @Test @InSequence(3)
    public void file_collect() {
    	Long filesCount = EntityCounter.getInstance().count(File.class);
    	Long filesBytesCount = EntityCounter.getInstance().count(FileBytes.class);    	
		fileBusiness.collect();
		assertThat(EntityCounter.getInstance().count(File.class)).isEqualTo(filesCount+3);
		assertThat(EntityCounter.getInstance().count(FileBytes.class)).isEqualTo(filesBytesCount);
    }
    
    @Test @InSequence(4)
    public void file_collect_again() {
    	Long filesCount = EntityCounter.getInstance().count(File.class);
    	fileBusiness.collect();
		assertThat(EntityCounter.getInstance().count(File.class)).as("No file has been collected").isEqualTo(filesCount);
    }
}
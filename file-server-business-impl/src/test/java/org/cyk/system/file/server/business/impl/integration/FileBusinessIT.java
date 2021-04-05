package org.cyk.system.file.server.business.impl.integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.persistence.query.EntityCounter;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

public class FileBusinessIT extends AbstractBusinessIT {

	@Inject private FileBusiness fileBusiness;
	
    @Test @InSequence(1)
    public void file_create() {
    	File file = new File();
    	
    	Long count = EntityCounter.getInstance().count(File.class);
		
    	fileBusiness.create(file);		
		
		assertThat(EntityCounter.getInstance().count(File.class)).isEqualTo(count+1);
    	/*assertThat(scopeFunction.getCode()).isEqualTo("G100000");
    	assertThat(scopeFunction.getName()).isEqualTo("Gestionnaire de credits DTI");
    	scopeFunction = EntityFinder.getInstance().find(ScopeFunction.class, "A1000000");
    	assertThat(scopeFunction.getCode()).isEqualTo("A1000000");
    	assertThat(scopeFunction.getName()).isEqualTo("Assistant gestionnaire de credits DTI");
    	assertThat(scopeFunction.getParentIdentifier()).as("G100000 is parent of A1000000").isEqualTo("G100000");    	
    	assertThat(ScopeFunctionQuerier.getInstance().countByParentsIdentifiers(List.of("G100000"))).as("children of G100000").isEqualTo(1l);
    	*/
    }
}
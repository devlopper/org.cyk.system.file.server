package org.cyk.system.file.server.business.impl.integration;

import org.cyk.utility.persistence.server.query.EntityReaderImpl;
import org.cyk.utility.test.arquillian.bootablejar.AbstractTest;
import org.cyk.utility.test.arquillian.bootablejar.ArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public abstract class AbstractBusinessIT extends AbstractTest {

    @Deployment
    public static Archive<?> buildArchive() {
    	return new ArchiveBuilder().setPersistenceEnabled(Boolean.TRUE)
    			.addPackages("org.cyk.system.file.server.business.impl")
    			.addClasses(AbstractBusinessIT.class,ApplicationScopeLifeCycleListener.class,EntityReaderImpl.class)
    			.build();
    }

}
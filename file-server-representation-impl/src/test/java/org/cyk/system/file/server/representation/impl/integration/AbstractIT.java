package org.cyk.system.file.server.representation.impl.integration;

import org.cyk.utility.test.arquillian.bootablejar.AbstractTest;
import org.cyk.utility.test.arquillian.bootablejar.ArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public abstract class AbstractIT extends AbstractTest {

    @Deployment
    public static Archive<?> buildArchive() {
    	return new ArchiveBuilder().setPersistenceEnabled(Boolean.TRUE)
    			.addPackages("org.cyk.system.file.server.representation.impl")
    			.addClasses(AbstractIT.class,ApplicationScopeLifeCycleListener.class)
    			.build();
    }

}
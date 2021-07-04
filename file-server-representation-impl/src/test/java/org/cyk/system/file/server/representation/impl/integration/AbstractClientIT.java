package org.cyk.system.file.server.representation.impl.integration;

import org.cyk.utility.test.arquillian.AbstractClientTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public abstract class AbstractClientIT extends AbstractClientTest {

	@Deployment
    public static Archive<?> buildArchive() {
    	return AbstractIT.buildArchive();
    }
}
package org.cyk.utility.server.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Deprecated
public interface DependenceProducer {

	EntityManagerFactory getEntityManagerFactory();
	
	EntityManager getEntityManager();
	
}

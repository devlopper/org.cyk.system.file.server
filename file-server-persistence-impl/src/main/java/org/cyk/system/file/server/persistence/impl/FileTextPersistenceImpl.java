package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.api.FileTextPersistence;
import org.cyk.system.file.server.persistence.entities.FileText;

@ApplicationScoped
public class FileTextPersistenceImpl extends AbstractFileDetailPersistenceImpl<FileText> implements FileTextPersistence,Serializable {
	private static final long serialVersionUID = 1L;
	
}

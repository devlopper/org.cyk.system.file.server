package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.business.api.FileTextBusiness;
import org.cyk.system.file.server.persistence.api.FileTextPersistence;
import org.cyk.system.file.server.persistence.entities.FileText;
import org.cyk.utility.server.business.AbstractBusinessEntityImpl;

@ApplicationScoped
public class FileTextBusinessImpl extends AbstractBusinessEntityImpl<FileText, FileTextPersistence> implements FileTextBusiness,Serializable {
	private static final long serialVersionUID = 1L;

}

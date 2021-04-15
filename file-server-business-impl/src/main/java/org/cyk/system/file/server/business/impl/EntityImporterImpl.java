package org.cyk.system.file.server.business.impl;

import javax.inject.Inject;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.persistence.entities.File;

@org.cyk.system.file.server.annotation.System
public class EntityImporterImpl extends org.cyk.utility.business.server.EntityImporterImpl {

	@Inject private FileBusiness fileBusiness;
	
	@Override
	protected void __import__(Arguments arguments) {
		super.__import__(arguments);
		if(File.class.equals(arguments.getEntityClass()))
			fileBusiness.import_();
	}
}
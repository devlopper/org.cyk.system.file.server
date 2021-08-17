package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.business.Initializer;
import org.cyk.utility.file.FileHelper;

@org.cyk.system.file.server.annotation.System
public class InitializerImpl extends Initializer.AbstractImpl implements Serializable {

	@Override
	protected <T> void ____initialize____(Class<T> klass, T entity, Object actionIdentifier) {
		super.____initialize____(klass, entity, actionIdentifier);
		if(File.class.equals(klass))
			initialize((File) entity,actionIdentifier);
	}
	
	/**/
	
	private void initialize(File file,Object actionIdentifier) {
		if(StringHelper.isBlank(file.getSha1())) {
			if(file.getBytes() == null) {
				//TODO get a way to compute sha1 : from given uniform resource locator
			}else
				file.setSha1(FileHelper.computeSha1(file.getBytes()));
		}

		if(StringHelper.isBlank(file.getName()) && StringHelper.isNotBlank(file.getNameAndExtension()))				
			file.setName(FileHelper.getName(file.getNameAndExtension()));
		
		if(StringHelper.isBlank(file.getExtension()) && StringHelper.isNotBlank(file.getNameAndExtension()))				
			file.setExtension(FileHelper.getExtension(file.getNameAndExtension()));
		
		if(StringHelper.isBlank(file.getMimeType()) && StringHelper.isNotBlank(file.getExtension()))			
			file.setMimeType(FileHelper.getMimeTypeByExtension(file.getExtension()));
		
		if(StringHelper.isBlank(file.getMimeType()) && StringHelper.isNotBlank(file.getNameAndExtension()))
			file.setMimeType(FileHelper.getMimeTypeByNameAndExtension(file.getNameAndExtension()));
		
		if(file.getSize() == null && file.getBytes() != null)				
			file.setSize(Long.valueOf(file.getBytes().length));
		
		if(StringHelper.isBlank(file.getName()))
			file.setName(RandomHelper.getAlphabetic(10));
	}
}
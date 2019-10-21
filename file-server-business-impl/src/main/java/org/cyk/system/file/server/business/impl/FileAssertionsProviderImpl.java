package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import org.cyk.system.file.server.business.api.FileAssertionsProvider;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.function.Function;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.assertion.AbstractAssertionsProviderForImpl;
import org.cyk.utility.server.business.BusinessFunctionCreator;

public class FileAssertionsProviderImpl extends AbstractAssertionsProviderForImpl<File> implements FileAssertionsProvider,Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void ____execute____(Function<?,?> function,Object filter, File file) {
		if(function instanceof BusinessFunctionCreator) {
			if(filter==null) {
				if(Boolean.TRUE.equals(file.getIsBytesAccessibleFromUniformResourceLocator())) {
					if(FileBusinessImpl.ROOT_FOLDER_PATH == null)
						throw new RuntimeException("Root folder is required");
				}
				if(StringHelper.isBlank(file.getUniformResourceLocator())) {
					//bytes are required
					//__injectAssertionBuilderNull__().setIsAffirmation(Boolean.FALSE).setFieldValueGetter(file, File.FIELD_BYTES)
					//	.setIsThrownWhenValueIsNotTrue(Boolean.TRUE).execute();					
				}else {
					
				}
				/*
				String sha1 = file.getSha1();
				if(StringHelper.isBlank(sha1)) {
					byte[] bytes = file.getBytes();
					if(bytes==null) {
						
					}else
						file.setSha1(new String(new DigestUtils(MessageDigestAlgorithms.SHA_1).digestAsHex(bytes)));
				}
				
				if(StringHelper.isNotBlank(file.getSha1())) {
					
				}
				*/
			}
		}else {
			
		}
	}
	
}
package org.cyk.system.file.server.business.api;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.function.Function;
import org.cyk.utility.assertion.AbstractAssertionsProviderForImpl;
import org.cyk.utility.server.business.BusinessFunctionCreator;

public class FileAssertionsProviderImpl extends AbstractAssertionsProviderForImpl<File> implements FileAssertionsProvider,Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void ____execute____(Function<?,?> function,Object filter, File file) {
		if(function instanceof BusinessFunctionCreator) {
			if(filter==null) {
				if(__injectStringHelper__().isBlank(file.getUniformResourceLocator())) {
					//bytes are required
					__injectAssertionBuilderNull__().setIsAffirmation(Boolean.FALSE).setFieldValueGetter(file, File.FIELD_BYTES)
						.setIsThrownWhenValueIsNotTrue(Boolean.TRUE).execute();					
				}else {
					
				}
			}
		}else {
			
		}
	}
	
}
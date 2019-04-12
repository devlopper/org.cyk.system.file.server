package org.cyk.system.file.server.business.api;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.function.Function;
import org.cyk.utility.assertion.AbstractAssertionsProviderForImpl;
import org.cyk.utility.server.business.BusinessFunction;
import org.cyk.utility.server.business.BusinessFunctionCreator;

public class FileAssertionsProviderImpl extends AbstractAssertionsProviderForImpl<File> implements FileAssertionsProvider,Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void ____execute____(Function<?,?> function,Object filter, File file) {
		if(function instanceof BusinessFunctionCreator) {
			//file = __inject__(FilePersistence.class).readOneByBusinessIdentifier(((File) ((BusinessFunction)function).getEntity()).getCode());
			file = (File) ((BusinessFunction)function).getEntity();
			if(filter==null) {
				if(__injectStringHelper__().isBlank(file.getUniformResourceLocator())) {
					if(file.getBytes() == null)
						__injectThrowableHelper__().throwRuntimeException("file bytes are required.");					
				}else {
					
				}
			}
		}else {
			
		}
	}
	
}
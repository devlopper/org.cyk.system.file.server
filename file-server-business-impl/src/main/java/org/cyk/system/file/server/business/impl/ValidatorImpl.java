package org.cyk.system.file.server.business.impl;

import java.io.Serializable;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Validator;

@org.cyk.system.file.server.annotation.System
public class ValidatorImpl extends Validator.AbstractImpl implements Serializable {

	@Override
	protected <T> void __validate__(Class<T> klass, T entity, Object actionIdentifier,ThrowablesMessages throwablesMessages) {
		super.__validate__(klass, entity, actionIdentifier, throwablesMessages);
		if(File.class.equals(klass))
			validate(actionIdentifier, (File) entity, throwablesMessages);
	}
	
	/**/
	
	private void validate(Object actionIdentifier,File file,ThrowablesMessages throwablesMessages) {
		throwablesMessages.addIfTrue("file bytes or uri is required",(file.getBytes() == null || file.getBytes().length == 0)
				&& StringHelper.isBlank(file.getUniformResourceLocator()));
		throwablesMessages.addIfTrue("mime type is required", StringHelper.isBlank(file.getMimeType()));
		throwablesMessages.addIfTrue("size is required",file.getSize() == null);
		throwablesMessages.addIfTrue(String.format("size <<%s>> must be greater than 0",file.getSize()),NumberHelper.isLessThanOrEqualZero(file.getSize()));
	}
	
}
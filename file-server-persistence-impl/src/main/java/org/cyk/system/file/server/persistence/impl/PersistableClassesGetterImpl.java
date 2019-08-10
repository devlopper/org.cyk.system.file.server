package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import org.cyk.system.file.server.annotation.System;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.entities.FileBytes;
import org.cyk.utility.clazz.Classes;
import org.cyk.utility.function.AbstractFunctionWithPropertiesAsInputImpl;
import org.cyk.utility.server.persistence.PersistableClassesGetter;

@Dependent @System
public class PersistableClassesGetterImpl extends AbstractFunctionWithPropertiesAsInputImpl<Classes> implements PersistableClassesGetter, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected Classes __execute__() throws Exception {
		Classes classes = __inject__(Classes.class);
		classes.add(FileBytes.class);
		classes.add(File.class);
		return classes;
	}
	
}
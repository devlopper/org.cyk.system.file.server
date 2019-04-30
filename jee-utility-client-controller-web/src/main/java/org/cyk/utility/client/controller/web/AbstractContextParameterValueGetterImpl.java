package org.cyk.utility.client.controller.web;

import java.io.Serializable;

import javax.servlet.ServletContext;

public abstract class AbstractContextParameterValueGetterImpl extends org.cyk.utility.client.controller.AbstractContextParameterValueGetterImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected Object __execute__(Object context, String name) throws Exception {
		return ((ServletContext)context).getInitParameter(name);
	}
	
}

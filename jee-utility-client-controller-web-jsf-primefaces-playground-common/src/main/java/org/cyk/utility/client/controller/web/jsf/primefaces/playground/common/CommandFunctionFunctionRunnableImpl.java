package org.cyk.utility.client.controller.web.jsf.primefaces.playground.common;

import java.io.Serializable;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractCommandFunctionFunctionRunnableImpl;
import org.cyk.utility.system.action.SystemAction;

public class CommandFunctionFunctionRunnableImpl extends AbstractCommandFunctionFunctionRunnableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void __act__(SystemAction action, Object data) {
		if(action!=null && "commandableButtonPageCustomServerSideOnly".equals(action.getIdentifier())) {
			System.out.println("Run custom server side action.");
		}else
			super.__act__(action, data);
	}
	
}
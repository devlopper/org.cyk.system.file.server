package org.cyk.system.file.server.representation.impl;
import java.io.Serializable;

import javax.ws.rs.core.Response;

import org.cyk.utility.server.representation.impl.AbstractDataLoaderImpl;

@org.cyk.system.file.server.annotation.System
public class DataLoaderImpl extends AbstractDataLoaderImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected Response __execute__() throws Exception {
		__inject__(org.cyk.system.file.server.business.impl.ApplicationScopeLifeCycleListener.class).saveDataFromResources();
		return Response.ok().build();
	}
	
}
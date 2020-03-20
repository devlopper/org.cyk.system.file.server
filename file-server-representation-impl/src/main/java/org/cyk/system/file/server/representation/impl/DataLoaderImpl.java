package org.cyk.system.file.server.representation.impl;
import java.io.Serializable;

import javax.ws.rs.core.Response;

import org.cyk.utility.server.representation.AbstractDataLoaderImpl;

@org.cyk.system.file.server.annotation.System
public class DataLoaderImpl extends AbstractDataLoaderImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Response load() {
		__inject__(org.cyk.system.file.server.business.impl.ApplicationScopeLifeCycleListener.class).saveDataFromResources();
		return Response.ok().build();
	}
	
}
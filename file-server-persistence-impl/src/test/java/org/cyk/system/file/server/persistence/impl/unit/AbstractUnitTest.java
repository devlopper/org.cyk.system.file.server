package org.cyk.system.file.server.persistence.impl.unit;

import org.cyk.utility.persistence.query.QueryHelper;
import org.cyk.utility.persistence.query.QueryManager;

public abstract class AbstractUnitTest extends org.cyk.utility.test.persistence.server.AbstractUnitTest {

	@Override
	protected void __listenAfter__() {
		super.__listenAfter__();
		QueryHelper.clear();
		QueryManager.getInstance().clear();
	}

	@Override
	protected void callInitialize() {
		org.cyk.system.file.server.persistence.impl.ApplicationScopeLifeCycleListener.initialize();
	}
}
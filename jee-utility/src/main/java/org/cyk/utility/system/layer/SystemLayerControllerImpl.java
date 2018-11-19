package org.cyk.utility.system.layer;

import java.io.Serializable;

import javax.inject.Singleton;

@Singleton
public class SystemLayerControllerImpl extends AbstractSystemLayerImpl implements SystemLayerController, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		
		getEntityLayer().setClassNameRegularExpression("controller.entities..+Impl$");
		
	}

}

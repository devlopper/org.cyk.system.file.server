package org.cyk.utility.server.business.test.arquillian;

import java.io.Serializable;

import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.server.business.BusinessEntity;
import org.cyk.utility.system.layer.SystemLayer;
import org.cyk.utility.system.layer.SystemLayerBusiness;
import org.cyk.utility.test.arquillian.AbstractSystemServerIntegrationTestImpl;
import org.cyk.utility.test.arquillian.SystemServerIntegrationTest;
import org.cyk.utility.value.ValueUsageType;

@SuppressWarnings({"rawtypes","unchecked"})
public  class AbstractArquillianIntegrationTest extends AbstractSystemServerIntegrationTestImpl<BusinessEntity> implements SystemServerIntegrationTest<BusinessEntity>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected <ENTITY> void ____createEntity____(ENTITY entity, BusinessEntity business) {
		business.create(entity);
	}

	@Override
	protected <ENTITY> ENTITY ____readEntity____(Class<ENTITY> entityClass, Object identifier,ValueUsageType valueUsageType, Properties expectedFieldValues, BusinessEntity business) {
		return (ENTITY) business.findOne(identifier, valueUsageType);
	}

	@Override
	protected <ENTITY> void ____updateEntity____(ENTITY entity, BusinessEntity business) {
		business.update(entity);
	}

	@Override
	protected <ENTITY> void ____deleteEntity____(ENTITY entity, BusinessEntity business) {
		business.delete(entity);
	}
	
	@Override
	protected BusinessEntity ____getLayerEntityInterfaceFromClass____(Class<?> aClass) {
		return __inject__(SystemLayerBusiness.class).injectInterfaceClassFromEntityClassName(aClass,__getLayerEntityInterfaceClass__());
	}

	@Override
	public SystemLayer __getSystemLayer__() {
		return __inject__(SystemLayerBusiness.class);
	}
}

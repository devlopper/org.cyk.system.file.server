package org.cyk.utility.client.controller;

import java.io.Serializable;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.cyk.utility.client.controller.data.DataRepresentationClassGetter;
import org.cyk.utility.client.controller.data.DataTransferObjectClassGetter;
import org.cyk.utility.client.controller.proxy.ProxyGetter;
import org.cyk.utility.server.representation.RepresentationEntity;
import org.cyk.utility.server.representation.ResponseEntityDto;
import org.cyk.utility.system.action.SystemAction;
import org.cyk.utility.system.action.SystemActionCreate;
import org.cyk.utility.system.action.SystemActionUpdate;

public class ControllerFunctionModifierImpl extends AbstractControllerFunctionImpl implements ControllerFunctionModifier , Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		setAction(__inject__(SystemActionUpdate.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void __execute__(SystemAction action) {
		if(action!=null && __injectCollectionHelper__().isNotEmpty(action.getEntities())) {
			/*Class<?> dataTransferClass = __inject__(DataTransferObjectClassGetter.class).setDataClass(action.getEntityClass()).execute().getOutput();
			if(dataTransferClass == null)
				__injectThrowableHelper__().throwRuntimeException("Data Transfer Class is required for "+action.getEntityClass());
			Collection<?> dataTransferObjects = __injectInstanceHelper__().buildMany(dataTransferClass, action.getEntities().get());
			Class<?> dataRepresentationClass = __inject__(DataRepresentationClassGetter.class).setDataClass(action.getEntityClass()).execute().getOutput();
			if(dataRepresentationClass == null)
				__injectThrowableHelper__().throwRuntimeException("Data Representation Class is required for "+action.getEntityClass());
			if(__injectClassHelper__().isInstanceOf(dataRepresentationClass, RepresentationEntity.class)) {
				RepresentationEntity representation = (RepresentationEntity) __inject__(ProxyGetter.class).setClazz(dataRepresentationClass).execute().getOutput();	
				representation.createMany(dataTransferObjects);	
			}
			*/
		}
	}
	
	@Override
	public ControllerFunctionModifier addActionEntities(Object... entities) {
		return (ControllerFunctionModifier) super.addActionEntities(entities);
	}
	
	@Override
	public ControllerFunctionModifier setActionEntityClass(Class<?> entityClass) {
		return (ControllerFunctionModifier) super.setActionEntityClass(entityClass);
	}

	@Override
	protected Response __act__(SystemAction action, Object representation, Collection<?> dataTransferObjects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ResponseEntityDto getResponseEntityDto(SystemAction action, Object representation, Response response) {
		// TODO Auto-generated method stub
		return null;
	}

}

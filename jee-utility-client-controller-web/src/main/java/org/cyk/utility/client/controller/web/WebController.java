package org.cyk.utility.client.controller.web;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.ControllerEntity;
import org.cyk.utility.client.controller.ControllerLayer;

@ApplicationScoped
public class WebController extends AbstractObject implements Serializable {

	public String getRequestParameter(String name) {
		if(StringHelper.isBlank(name))
			return null;
		HttpServletRequest request = __inject__(HttpServletRequest.class);
		return request.getParameter(name);
	}
	
	public String getRequestParameter(ParameterName name) {
		if(name == null)
			return null;
		return getRequestParameter(name.getValue());
	}
	
	public Boolean isPageRenderedAsDialog() {
		return REQUEST_PARAMETER_VALUE_WINDOW_RENDER_TYPE_DIALOG.equalsIgnoreCase(getRequestParameter(REQUEST_PARAMETER_NAME_WINDOW_RENDER_TYPE));
	}
	
	public Action getRequestParameterAction() {
		String value = getRequestParameter(ParameterName.ACTION_IDENTIFIER);
		if(StringHelper.isBlank(value))
			return null;
		return Action.getByNameCaseInsensitive(value);
	}
	
	public <T> T getRequestParameterEntityBySystemIdentifier(Class<T> entityClass) {
		if(entityClass == null)
			return null;
		String identifier = getRequestParameter("entityidentifier");
		if(StringHelper.isBlank(identifier))
			return null;
		ControllerEntity<T> controllerEntity = __inject__(ControllerLayer.class).injectInterfaceClassFromEntityClass(entityClass);
		if(controllerEntity == null)
			return null;
		return controllerEntity.readBySystemIdentifier(identifier);
	}
	
	public <T> T getRequestParameterEntity(Class<T> entityClass) {
		return getRequestParameterEntityBySystemIdentifier(entityClass);
	}
	
	/**/
	
	public static WebController getInstance() {
		return DependencyInjection.inject(WebController.class);
	}
	
	public static final String REQUEST_PARAMETER_NAME_WINDOW_RENDER_TYPE = "windowrendertype";
	
	public static final String REQUEST_PARAMETER_VALUE_WINDOW_RENDER_TYPE_DIALOG = "windowrendertypedialog";
}

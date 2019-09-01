package org.cyk.utility.client.controller.web.jsf.primefaces;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.cyk.utility.client.controller.component.command.CommandableBuilder;
import org.cyk.utility.client.controller.component.window.WindowBuilder;
import org.cyk.utility.client.controller.event.EventBuilder;
import org.cyk.utility.client.controller.event.EventName;
import org.cyk.utility.client.controller.navigation.NavigationBuilder;
import org.cyk.utility.client.controller.web.jsf.primefaces.tag.BlockUI;
import org.cyk.utility.string.StringHelper;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractPageContainerManagedImpl extends org.cyk.utility.client.controller.web.jsf.page.AbstractPageContainerManagedImpl implements PageContainerManaged,Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter protected BlockUI blockUI;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		blockUI = new BlockUI();
		blockUI.getProperties().setBlock("outputPanel");
		blockUI.getProperties().setTrigger("initialisePageContent");
		blockUI.getProperties().setBlocked(Boolean.TRUE);
	}
	
	@Override
	protected WindowBuilder __getWindowBuilder__() {
		WindowBuilder window =  super.__getWindowBuilder__();
		CommandableBuilder commandable = window.getDialog(Boolean.TRUE).getOkCommandable(Boolean.TRUE);
		__processWindowDialogOkCommandable__(window, commandable);
		return window;
	}
	
	protected void __processWindowDialogOkCommandable__(WindowBuilder window,CommandableBuilder commandable) {
		String widgetVar = (String) window.getDialog(Boolean.TRUE).getOutputProperties().getWidgetVar();
		
		String script = __injectPrimefacesHelper__().getScriptInstructionHide(widgetVar);
		
		String url = __processWindowDialogOkCommandableGetUrl__(window, commandable);
		if(__inject__(StringHelper.class).isBlank(url)) {
			NavigationBuilder navigation = commandable.getNavigation();
			if(navigation!=null)
				url = navigation.execute().getOutput().getUniformResourceLocator().toString();	
		}
		
		if(__inject__(StringHelper.class).isNotBlank(url))
			script = script + __injectJavaServerFacesHelper__().getScriptInstructionGoToUrlIfMessageMaximumSeverityIsInfo(url);
				
		EventBuilder event = __inject__(EventBuilder.class).setName(EventName.CLICK).addScriptInstructions(script);
		commandable.addEvents(event);
	}
	
	protected String __processWindowDialogOkCommandableGetUrl__(WindowBuilder window,CommandableBuilder commandable) {
		String url = null;
		Object request = __getRequest__();
		if(request instanceof HttpServletRequest)
			url = ((HttpServletRequest)request).getRequestURL().toString();
		return url;
	}
	
	/**/
	
	protected static final PrimefacesHelper __injectPrimefacesHelper__() {
		return __inject__(PrimefacesHelper.class);
	}
}

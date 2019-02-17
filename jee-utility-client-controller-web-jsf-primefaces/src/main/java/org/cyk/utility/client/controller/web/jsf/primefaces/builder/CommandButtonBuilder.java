package org.cyk.utility.client.controller.web.jsf.primefaces.builder;

import java.io.Serializable;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.cyk.utility.client.controller.component.ComponentRole;
import org.cyk.utility.client.controller.component.VisibleComponent;
import org.cyk.utility.client.controller.component.command.Commandable;
import org.cyk.utility.client.controller.event.Event;
import org.cyk.utility.client.controller.event.Events;
import org.cyk.utility.client.controller.web.ComponentHelper;
import org.cyk.utility.client.controller.web.jsf.JavaServerFacesHelper;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.object.Objects;
import org.cyk.utility.string.StringHelper;
import org.cyk.utility.system.action.SystemAction;
import org.cyk.utility.system.action.SystemActionCreate;
import org.cyk.utility.system.action.SystemActionDelete;
import org.cyk.utility.system.action.SystemActionProcess;
import org.cyk.utility.system.action.SystemActionRead;
import org.cyk.utility.system.action.SystemActionSelect;
import org.cyk.utility.system.action.SystemActionUpdate;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;
import org.primefaces.component.commandbutton.CommandButton;

@Deprecated
public class CommandButtonBuilder extends AbstractBuilder implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public CommandButton build(Commandable commandable) {
		CommandButton commandButton = new CommandButton();
		commandButton.setValue(commandable.getName());
		if(__inject__(CollectionHelper.class).contains(commandable.getRoles(), ComponentRole.CREATOR)) {
			
		}
		String onClickValueExpressionString = null;
		if(commandable.getNavigation()!=null) {
			commandButton.setType("button");
			String url = null;
			SystemAction action = commandable.getNavigation().getSystemAction();
			if(action == null) {
				if(commandable.getCommand()!=null && commandable.getCommand().getFunction()!=null)
					action = commandable.getCommand().getFunction().getAction();
			}
			
			String systemActionClass = null;
			if(action instanceof SystemActionCreate) {
				url = commandable.getNavigation().getUniformResourceLocator().toString();
			}else {
				if(action instanceof SystemActionCreate)
					systemActionClass = "systemActionCreateClass";
				else if(action instanceof SystemActionRead)
					systemActionClass = "systemActionReadClass";
				else if(action instanceof SystemActionUpdate)
					systemActionClass = "systemActionUpdateClass";
				else if(action instanceof SystemActionDelete)
					systemActionClass = "systemActionDeleteClass";
				else if(action instanceof SystemActionSelect)
					systemActionClass = "systemActionSelectClass";
				else if(action instanceof SystemActionProcess)
					systemActionClass = "systemActionProcessClass";
				
				if(systemActionClass!=null)
					url = "#{indexRow.getUrlBySystemActionClass(componentHelper."+systemActionClass+")}";
			}
				
			
			
			if(__inject__(StringHelper.class).isNotBlank(url)) {
				onClickValueExpressionString = "window.open('"+url+"','_self');return false";
			}
			
		}else if(commandable.getCommand()!=null) {
			commandButton.setType("submit");
			/*String update = __inject__(ComponentHelper.class).getGlobalMessagesTargetInlineComponentIdentifier()
					+","+__inject__(ComponentHelper.class).getGlobalMessagesTargetGrowlComponentIdentifier()
					+","+__inject__(ComponentHelper.class).getGlobalMessagesTargetDialogComponentIdentifier();
			*/
			String update = __inject__(ComponentHelper.class).getGlobalMessagesTargetsIdentifiers();
			
			Objects updatables = commandable.getUpdatables();
			
			if(__inject__(CollectionHelper.class).isNotEmpty(updatables))
				for(Object index : updatables.get()) {
					String token = null;
					if(index instanceof VisibleComponent) {
						token = (String)((VisibleComponent)index).getProperties().getIdentifierAsStyleClass();
						if(__inject__(StringHelper.class).isNotBlank(token))
							update = update + " , @(."+token+")";		
					}else
						update = update + " , "+index;
					
				}
			String commandableIdentifier = commandable.getIdentifier().toString();
			if(__inject__(StringHelper.class).isNotBlank(commandable.getCommand().getContainerContextDependencyInjectionBeanName())) {
				String actionExpressionLanguage = commandable.getCommand().getContainerContextDependencyInjectionBeanName()+".getCommandableByIdentifier('"+commandableIdentifier+"').command.function.executeToReturnVoid";
				commandButton.setActionExpression(__inject__(JavaServerFacesHelper.class).buildMethodExpression(actionExpressionLanguage, Void.class,new Class<?>[] {}));	
			}
			
			//update = StringUtils.replace(update, "glo", ":form:glo");
			
			commandButton.setUpdate(update);
			
			//commandButton.setImmediate(Boolean.TRUE);
			//System.out.println("CommandButtonBuilder.build() UPDATE : "+update);
		}else {
			commandButton.setType("button");
			if(commandable.getProperties().getOnClick()!=null)
				onClickValueExpressionString = commandable.getProperties().getOnClick().toString();
		}
		
		if(__inject__(StringHelper.class).isNotBlank(onClickValueExpressionString)) {
			ValueExpression valueExpression = __buildValueExpressionString__(onClickValueExpressionString);
			__setValueExpression__(commandButton, "onclick", valueExpression);	
		}
		
		Events events = commandable.getEvents();
		if(__inject__(CollectionHelper.class).isNotEmpty(events)) {
			String commandableIdentifier = commandable.getIdentifier().toString();
			for(Event index : events.get()) {
				if(index.getScript()==null) {
					//String actionExpressionLanguage = commandable.getCommand().getWindowContainerManaged().getContextDependencyInjectionBeanName()+".getCommandableByIdentifier('"+commandableIdentifier+"').events.getAt(0).properties.function.executeWithOneParameterToReturnVoid";
					String actionExpressionLanguage = commandable.getCommand().getContainerContextDependencyInjectionBeanName()+".getCommandableByIdentifier('"+commandableIdentifier+"').events.getAt(0).properties.function.executeWithOneParameterToReturnVoid";
					AjaxBehavior behavior = (AjaxBehavior) FacesContext.getCurrentInstance().getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
					behavior.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(__inject__(JavaServerFacesHelper.class).buildMethodExpression(actionExpressionLanguage, Void.class,new Class<?>[] {})
							, __inject__(JavaServerFacesHelper.class).buildMethodExpression(actionExpressionLanguage, Void.class,new Class<?>[] {Object.class})));
					
					if(index.getProperties().getUpdate()!=null)
						behavior.setUpdate(index.getProperties().getUpdate().toString());
					//behavior.setListener(methodExpression);
					
					commandButton.addClientBehavior(index.getProperties().getEvent().toString(), behavior);	
				}
			}
		}
		
		return commandButton;
	}
	
}

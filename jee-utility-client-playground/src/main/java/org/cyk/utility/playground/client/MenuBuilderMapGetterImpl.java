package org.cyk.utility.playground.client;

import java.io.Serializable;
import java.security.Principal;

import org.cyk.utility.client.controller.component.menu.AbstractMenuBuilderMapGetterImpl;
import org.cyk.utility.client.controller.component.menu.MenuBuilder;
import org.cyk.utility.client.controller.component.menu.MenuItemBuilder;
import org.cyk.utility.client.controller.icon.Icon;
import org.cyk.utility.playground.client.controller.entities.MyEntity;
import org.cyk.utility.playground.client.controller.entities.Node;

@org.cyk.utility.playground.server.System
public class MenuBuilderMapGetterImpl extends AbstractMenuBuilderMapGetterImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void ____executePrincipalIsNotNull____(MenuBuilder sessionMenuBuilder, Object request, Principal principal) throws Exception {
		
	}

	@Override
	protected void ____executePrincipalIsNull____(MenuBuilder sessionMenuBuilder, Object request) throws Exception {
		if(sessionMenuBuilder == null) {
			
		}//else {
		sessionMenuBuilder.addItems(
				__inject__(MenuItemBuilder.class).setCommandableName("Non hierarchique").setCommandableIcon(Icon.QUESTION)
					.addEntitiesList(MyEntity.class)
				,__inject__(MenuItemBuilder.class).setCommandableName("Hierarchique").setCommandableIcon(Icon.FILE)
					.addEntitiesList(Node.class)
				);	
		//}
	}

	

}

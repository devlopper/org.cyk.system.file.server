package org.cyk.utility.client.controller.component.command;

import org.cyk.utility.client.controller.component.InvisibleComponentBuilder;

public interface CommandBuilder extends InvisibleComponentBuilder<Command> {

	CommandFunction getFunction();
	CommandBuilder setFunction(CommandFunction function);
	CommandFunction getFunction(Boolean injectIfNull);
	
}
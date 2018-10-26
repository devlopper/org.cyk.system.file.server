package org.cyk.utility.client.controller.component;

import org.cyk.utility.client.controller.component.command.Command;
import org.cyk.utility.client.controller.component.layout.LayoutItem;
import org.cyk.utility.css.Style;
import org.cyk.utility.device.DeviceScreenArea;

public interface VisibleComponent extends Component {

	DeviceScreenArea getArea();
	DeviceScreenArea getArea(Boolean injectIfNull);
	VisibleComponent setArea(DeviceScreenArea area);
	
	LayoutItem getLayoutItem();
	LayoutItem getLayoutItem(Boolean injectIfNull);
	VisibleComponent setLayoutItem(LayoutItem layoutItem);
	
	Command getCommand();
	Command getCommand(Boolean injectIfNull);
	VisibleComponent setCommand(Command command);
	
	Style getStyle();
	Style getStyle(Boolean injectIfNull);
	VisibleComponent setStyle(Style style);
}

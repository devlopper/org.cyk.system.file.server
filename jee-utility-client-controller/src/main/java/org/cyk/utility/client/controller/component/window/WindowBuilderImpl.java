package org.cyk.utility.client.controller.component.window;

import java.io.Serializable;
import java.util.Map;

import org.cyk.utility.client.controller.component.AbstractVisibleComponentBuilderImpl;
import org.cyk.utility.client.controller.component.dialog.DialogBuilder;
import org.cyk.utility.client.controller.component.menu.MenuBuilder;
import org.cyk.utility.client.controller.component.menu.MenuBuilderMap;
import org.cyk.utility.client.controller.component.menu.MenuGetter;
import org.cyk.utility.client.controller.component.menu.MenuMap;
import org.cyk.utility.client.controller.component.output.OutputStringTextBuilder;
import org.cyk.utility.client.controller.component.theme.Theme;
import org.cyk.utility.client.controller.component.view.ViewBuilder;

public class WindowBuilderImpl extends AbstractVisibleComponentBuilderImpl<Window> implements WindowBuilder,Serializable {
	private static final long serialVersionUID = 1L;

	private OutputStringTextBuilder title;
	private ViewBuilder view;
	private Theme theme;
	private MenuBuilderMap menuMap;
	private DialogBuilder dialog;
	private WindowRenderType renderType;
	
	@Override
	protected void __execute__(Window window) {
		super.__execute__(window);
		WindowRenderType renderType = getRenderType();
		window.setRenderType(renderType);
		
		OutputStringTextBuilder title = getTitle();
		if(title!=null)
			window.setTitle(title.execute().getOutput());
		ViewBuilder view = getView();
		if(view!=null)
			window.setView(view.execute().getOutput());
		
		if(renderType == null || renderType instanceof WindowRenderTypeNormal) {
			MenuBuilderMap menuMap = getMenuMap();
			if(menuMap!=null) {
				window.setMenuMap(__inject__(MenuMap.class));
				for(@SuppressWarnings("rawtypes") Map.Entry<Class,MenuBuilder> entry : menuMap.getEntries())
					window.getMenuMap().set(entry.getKey(),__inject__(MenuGetter.class).setScopeClass(entry.getKey()).execute().getOutput());
			}	
		}
		
		Theme theme = getTheme();
		window.setTheme(theme);
		DialogBuilder dialog = getDialog(Boolean.TRUE);
		if(dialog!=null)
			window.setDialog(dialog.execute().getOutput());
		
		
	}
	
	@Override
	public OutputStringTextBuilder getTitle() {
		return title;
	}

	@Override
	public OutputStringTextBuilder getTitle(Boolean injectIfNull) {
		return (OutputStringTextBuilder) __getInjectIfNull__(FIELD_TITLE, injectIfNull);
	}
	
	@Override
	public WindowBuilder setTitleValue(String titleValue) {
		getTitle(Boolean.TRUE).setValue(titleValue);
		return this;
	}
	
	@Override
	public WindowBuilder setTitle(OutputStringTextBuilder title) {
		this.title = title;
		return this;
	}

	@Override
	public ViewBuilder getView() {
		return view;
	}
	
	@Override
	public ViewBuilder getView(Boolean injectIfNull) {
		return (ViewBuilder) __getInjectIfNull__(FIELD_VIEW, injectIfNull);
	}

	@Override
	public WindowBuilder setView(ViewBuilder view) {
		this.view = view;
		return this;
	}
	
	@Override
	public Theme getTheme() {
		return theme;
	}
	
	@Override
	public WindowBuilder setTheme(Theme theme) {
		this.theme = theme;
		return this;
	}
	
	@Override
	public MenuBuilderMap getMenuMap() {
		return menuMap;
	}
	
	@Override
	public MenuBuilderMap getMenuMap(Boolean injectIfNull) {
		return (MenuBuilderMap) __getInjectIfNull__(FIELD_MENU_MAP, injectIfNull);
	}
	
	@Override
	public WindowBuilder setMenuMap(MenuBuilderMap menuMap) {
		this.menuMap = menuMap;
		return this;
	}
	
	@Override
	public DialogBuilder getDialog() {
		return dialog;
	}
	
	@Override
	public DialogBuilder getDialog(Boolean injectIfNull) {
		DialogBuilder dialog = (DialogBuilder) __getInjectIfNull__(FIELD_DIALOG, injectIfNull);
		
		return dialog;
	}
	
	@Override
	public WindowBuilder setDialog(DialogBuilder dialog) {
		this.dialog = dialog;
		return this;
	}
	
	@Override
	public WindowRenderType getRenderType() {
		return renderType;
	}
	
	@Override
	public WindowBuilder setRenderType(WindowRenderType renderType) {
		this.renderType = renderType;
		return this;
	}
	
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_VIEW = "view";
	public static final String FIELD_MENU_MAP = "menuMap";
	public static final String FIELD_DIALOG = "dialog";

}

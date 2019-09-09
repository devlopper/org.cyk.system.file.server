package org.cyk.utility.client.controller.component.layout;

import org.cyk.utility.__kernel__.function.FunctionRunnableMap;
import org.cyk.utility.client.controller.ApplicationScopeLifeCycleListener;
import org.cyk.utility.css.StyleClassBuilderWidthImpl;
import org.cyk.utility.test.weld.AbstractWeldUnitTest;
import org.junit.jupiter.api.Test;

public class LayoutItemBuilderUnitTest extends AbstractWeldUnitTest {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenBefore__() {
		super.__listenBefore__();
		__inject__(ApplicationScopeLifeCycleListener.class).__initialize__(null);
		__inject__(FunctionRunnableMap.class).set(StyleClassBuilderWidthImpl.class, StyleClassBuilderWidthCssPrimefacesGridFunctionRunnableImpl.class);
	}
	
	@Test
	public void build() {
		LayoutItem layoutItem = __inject__(LayoutItemBuilder.class).execute().getOutput();
		assertionHelper.assertNotNull(layoutItem);
		assertionHelper.assertNull(layoutItem.getArea());
		assertionHelper.assertNotNull(layoutItem.getStyle());
		
		assertionHelper.assertNull(layoutItem.getLayout());
	}
	
	@Test
	public void buildWithStyle() {
		LayoutItemBuilder layoutItemBuilder = __inject__(LayoutItemBuilder.class);
		layoutItemBuilder.getStyle(Boolean.TRUE).getClasses(Boolean.TRUE).add("myclass");
		LayoutItem layoutItem = layoutItemBuilder.execute().getOutput();
		assertionHelper.assertNotNull(layoutItem);
		assertionHelper.assertNotNull(layoutItem.getStyle());
		assertionHelper.assertEquals("myclass ui-g-12 ui-xl-12 ui-lg-12 ui-md-12 ui-sm-12", layoutItem.getStyle().getClassesAsString());
		assertionHelper.assertNull(layoutItem.getArea());
		
		assertionHelper.assertNull(layoutItem.getLayout());
	}
	
	@Test
	public void buildWithArea() {
		LayoutItem layoutItem = __inject__(LayoutItemBuilder.class).setAreaWidthProportionsAllClasses(1, 2, 3, 4, 5).execute().getOutput();
		assertionHelper.assertNotNull(layoutItem);
		assertionHelper.assertNotNull(layoutItem.getStyle());
		assertionHelper.assertEquals("ui-g-1 ui-xl-2 ui-lg-3 ui-md-4 ui-sm-5", layoutItem.getStyle().getClassesAsString());
		assertionHelper.assertNotNull(layoutItem.getArea());
		
		assertionHelper.assertNull(layoutItem.getLayout());
	}
	
	@Test
	public void buildWithStyleAndArea() {
		LayoutItem layoutItem = __inject__(LayoutItemBuilder.class).setAreaWidthProportionsAllClasses(1, 2, 3, 4, 5).addStyleClasses("myclass").execute().getOutput();
		assertionHelper.assertNotNull(layoutItem);
		assertionHelper.assertNotNull(layoutItem.getStyle());
		assertionHelper.assertEquals("myclass ui-g-1 ui-xl-2 ui-lg-3 ui-md-4 ui-sm-5", layoutItem.getStyle().getClassesAsString());
		assertionHelper.assertNotNull(layoutItem.getArea());
		
		assertionHelper.assertNull(layoutItem.getLayout());
	}
}

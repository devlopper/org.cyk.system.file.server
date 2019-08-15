package org.cyk.utility.internationalization;

import java.util.Collection;

import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.string.Strings;
import org.cyk.utility.test.weld.AbstractWeldUnitTest;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class InternalizationKeyRelatedStringsBuilderUnitTest extends AbstractWeldUnitTest {
	private static final long serialVersionUID = 1L;

	@Test
	public void none(){
		Collection<Strings> collection = __inject__(InternalizationKeyRelatedStringsBuilder.class).execute().getOutput();
		assertionHelper.assertEquals(null, collection);
	}
	
	@Test
	public void empty(){
		Collection<Strings> collection = __inject__(InternalizationKeyRelatedStringsBuilder.class).setKey("").execute().getOutput();
		assertionHelper.assertEquals(null, collection);
	}
	
	@Test
	public void hi(){
		Collection<Strings> collection = __inject__(InternalizationKeyRelatedStringsBuilder.class).setKey("hi").execute().getOutput();
		assertionHelper.assertEquals(null, collection);
	}
	
	@Test
	public void _hi(){
		Collection<Strings> collection = __inject__(InternalizationKeyRelatedStringsBuilder.class).setKey("_hi").execute().getOutput();
		assertionHelper.assertEquals(null, collection);
	}
	
	@Test
	public void eventType(){
		Collection<Strings> collection = __inject__(InternalizationKeyRelatedStringsBuilder.class).setKey("eventType").execute().getOutput();
		assertThat(collection).hasSize(1);
		assertionHelper.assertEquals("type", __inject__(CollectionHelper.class).getElementAt(collection, 0).getAt(0));
		assertionHelper.assertEquals("of", __inject__(CollectionHelper.class).getElementAt(collection, 0).getAt(1));
		assertionHelper.assertEquals("event", __inject__(CollectionHelper.class).getElementAt(collection, 0).getAt(2));
	}
	
	@Test
	public void event_dot_type(){
		Collection<Strings> collection = __inject__(InternalizationKeyRelatedStringsBuilder.class).setKey("event.type").execute().getOutput();
		assertThat(collection).hasSize(1);
		assertionHelper.assertEquals("type", __inject__(CollectionHelper.class).getElementAt(collection, 0).getAt(0));
		assertionHelper.assertEquals("of", __inject__(CollectionHelper.class).getElementAt(collection, 0).getAt(1));
		assertionHelper.assertEquals("event", __inject__(CollectionHelper.class).getElementAt(collection, 0).getAt(2));
	}
	
}
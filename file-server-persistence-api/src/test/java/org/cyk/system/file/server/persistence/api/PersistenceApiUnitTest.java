package org.cyk.system.file.server.persistence.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.utility.__kernel__.test.weld.AbstractPersistenceUnitTest;
import org.junit.jupiter.api.Test;

public class PersistenceApiUnitTest extends AbstractPersistenceUnitTest {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getPersistenceUnitName() {
		return "default";
	}
	
	@Test
	public void fileQuerier_readView01_value(){
		assertThat(FileQuerier.QUERY_VALUE_READ_VIEW_01)
		.isEqualTo("SELECT t.identifier,t.name,t.extension FROM File t "
				+ "WHERE LOWER(t.name) LIKE LOWER(:name1) OR LOWER(t.name) LIKE LOWER(:name2) OR LOWER(t.name) LIKE LOWER(:name3) "
				+ "ORDER BY t.name ASC");
	}
}
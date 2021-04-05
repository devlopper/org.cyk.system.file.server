package org.cyk.system.file.server.persistence.impl.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.query.QueryIdentifierBuilder;
import org.cyk.utility.persistence.query.QueryName;
import org.cyk.utility.persistence.server.query.string.RuntimeQueryStringBuilder;
import org.junit.jupiter.api.Test;

public class PersistenceImplQueryStringUnitTest extends AbstractUnitTestMemory {
	private static final long serialVersionUID = 1L;

	@Test
	public void file_build_dynamic(){
		QueryExecutorArguments queryExecutorArguments = new QueryExecutorArguments();
		queryExecutorArguments.setQuery(new Query().setTupleClass(File.class).setIdentifier(QueryIdentifierBuilder.getInstance().build(File.class, QueryName.READ_DYNAMIC)));
		assertThat(RuntimeQueryStringBuilder.getInstance().build(queryExecutorArguments)).isEqualTo("SELECT t.identifier FROM File t");
	}
}
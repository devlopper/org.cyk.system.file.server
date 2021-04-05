package org.cyk.system.file.server.persistence.impl.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.executor.DynamicManyExecutor;
import org.junit.jupiter.api.Test;

public class PersistenceImplUnitTest extends AbstractUnitTestMemory {
	private static final long serialVersionUID = 1L;

	@Test
	public void file_read_dynamic_filter(){
		assertFileReadDynamicFilter("vivant"
				, new String[] {
						"0047782c-679d-4e46-b801-2a535a34510f"
					}
				, new String[] {
						"il est vivant le seigneur mon dieu.pdf"
					}
			);
		
		assertFileReadDynamicFilter("il"
				, new String[] {
						"011e5af2-2e71-4d17-832c-d4280d4ad157"
						,"0047782c-679d-4e46-b801-2a535a34510f"
					}
				, new String[] {
						"ah qu il es bon.pdf"
						,"il est vivant le seigneur mon dieu.pdf"
					}
			);
	}
	
	private void assertFileReadDynamicFilter(String name,String[] expectedIdentifiers,String[] expectedNamesAndExtensions){
		Collection<File> files = DynamicManyExecutor.getInstance().read(File.class,new QueryExecutorArguments().addFilterField(File.FIELD_NAME, name));
		assertThat(files).isNotEmpty();		
		assertThat(files.stream().map(x -> x.getIdentifier())).containsExactly(expectedIdentifiers);
		//assertThat(files.stream().map(x -> x.getName())).containsExactly((String)null);
		//assertThat(files.stream().map(x -> x.getExtension())).containsExactly((String)null);
		assertThat(files.stream().map(x -> x.getNameAndExtension())).containsExactly(expectedNamesAndExtensions);
		//assertThat(files.stream().map(x -> x.getSha1())).containsExactly((String)null);
	}
}
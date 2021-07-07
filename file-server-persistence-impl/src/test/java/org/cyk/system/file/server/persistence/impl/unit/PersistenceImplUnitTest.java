package org.cyk.system.file.server.persistence.impl.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.persistence.impl.query.FileNameExtensionMimeTypeSizeBytesReader;
import org.cyk.utility.persistence.query.EntityReader;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

public class PersistenceImplUnitTest extends AbstractUnitTestMemory {
	private static final long serialVersionUID = 1L;

	@Test
	public void file_readUniformResourceLocators(){
		assertThat(FileQuerier.getInstance().readUniformResourceLocators()).contains("file:///C:/Users/CYK/Downloads/Partitions/temps%20de%20carême/Chant%20à%20la%20croix.pdf");
	}
	
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
	
	@Test
	public void file_reader_ExtensionMimeTypeBytesReader(){
		Collection<File> files = new FileNameExtensionMimeTypeSizeBytesReader().readByIdentifiersThenInstantiate(List.of("002aadb8-2f00-4bda-8e41-67e7938eed2b"), null);
		assertThat(files).hasSize(1);
		File file = files.iterator().next();
		assertThat(file).isNotNull();
		assertThat(file.getIdentifier()).isEqualTo("002aadb8-2f00-4bda-8e41-67e7938eed2b");
		assertThat(file.getExtension()).isEqualTo("pdf");
		assertThat(file.getMimeType()).isEqualTo("application/pdf");
		assertThat(file.getBytes()).isNotNull();
		assertThat(new String(file.getBytes())).isEqualTo("hello world!");
	}
	
	private void assertFileReadDynamicFilter(String name,String[] expectedIdentifiers,String[] expectedNamesAndExtensions){
		Collection<File> files = EntityReader.getInstance().readManyDynamically(File.class,new QueryExecutorArguments().addFilterField(File.FIELD_NAME, name)
				.addProcessableTransientFieldsNames(File.FIELD_NAME_AND_EXTENSION));
		assertThat(files).isNotEmpty();		
		assertThat(files.stream().map(x -> x.getIdentifier())).containsExactly(expectedIdentifiers);
		//assertThat(files.stream().map(x -> x.getName())).containsExactly((String)null);
		//assertThat(files.stream().map(x -> x.getExtension())).containsExactly((String)null);
		assertThat(files.stream().map(x -> x.getNameAndExtension())).containsExactly(expectedNamesAndExtensions);
		//assertThat(files.stream().map(x -> x.getSha1())).containsExactly((String)null);
	}
}
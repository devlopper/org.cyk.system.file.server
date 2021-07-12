package org.cyk.system.file.server.representation.impl.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.system.file.server.representation.impl.FileRepresentationImpl;
import org.cyk.system.file.server.representation.impl.integration.ApplicationScopeLifeCycleListener;
import org.cyk.utility.representation.server.LinksGenerator;
import org.cyk.utility.test.weld.AbstractWeldUnitTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FileRepresentationImplUnitTest extends AbstractWeldUnitTest {

	@BeforeAll
	public static void beforeAll() {
		ApplicationScopeLifeCycleListener.INTEGRATION = Boolean.FALSE;
	}
	
	@Test
	public void assertLink_download() {
		FileDto file = new FileDto();
		file.setIdentifier("1");
		LinksGenerator.getInstance().generate(List.of(file),List.of(FileRepresentationImpl.LINK_DOWNLOAD));
		assertThat(file.get__links__()).hasSize(1);
		assertThat(file.get__links__().iterator().next().getValue()).isEqualTo("file/1/download?isinline=true");
	}
	
	@Test
	public void assertLink_download_format() {
		FileDto file = new FileDto();
		file.setIdentifier("1");
		LinksGenerator.getInstance().generate(List.of(file),List.of(FileRepresentationImpl.LINK_DOWNLOAD)
				,Map.of(FileRepresentationImpl.LINK_DOWNLOAD,"open/api/file/%s/download?isinline=true"));
		assertThat(file.get__links__()).hasSize(1);
		assertThat(file.get__links__().iterator().next().getValue()).isEqualTo("open/api/file/1/download?isinline=true");
	}
}
package org.cyk.system.file.server.business.impl.unit;

import java.nio.file.Path;
import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.collection.CollectionProcessor;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.file.PathsProcessor;
import org.cyk.utility.file.PathsScanner;
import org.cyk.utility.persistence.query.EntityCreator;
import org.cyk.utility.persistence.query.EntityReader;
import org.junit.jupiter.api.Test;

public class BusinessImplUnitTest extends AbstractUnitTestMemory {
	private static final long serialVersionUID = 1L;
	
	//private String rootPath = "src\\test\\resources\\org\\cyk\\system\\file\\server\\business\\impl";
	private String rootPath = "C:\\Users\\CYK\\Downloads";
	
	@Test
	public void readFromFileSystem() throws Exception{
		Collection<Path> paths = PathsScanner.getInstance().scan(new PathsScanner.Arguments().addPathsFromNames(rootPath).setAcceptedPathNameRegularExpression(".pdf"));
		PathsProcessor.getInstance().process(paths,new CollectionProcessor.Arguments.Processing.AbstractImpl<Path>() {
			
			@Override
			protected void __process__(Path path) {
				File file = new File();
				file.setName(FileHelper.getName(path.toFile().getName()));
				file.setExtension(FileHelper.getExtension(path.toFile().getName()));
				file.setMimeType(FileHelper.getMimeTypeByExtension(file.getExtension()));
				file.setSize(path.toFile().length());
				file.setSha1(FileHelper.computeSha1(path.toFile()));
				EntityCreator.getInstance().createManyInTransaction(file);				
			}
		});
		EntityReader.getInstance().readMany(File.class).stream().forEach(x -> {
			System.out.println(x.getName());
		});
	}
}
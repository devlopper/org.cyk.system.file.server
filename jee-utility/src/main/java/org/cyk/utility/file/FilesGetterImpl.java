package org.cyk.utility.file;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.Consumer;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.function.AbstractFunctionWithPropertiesAsInputImpl;
import org.cyk.utility.string.Strings;

public class FilesGetterImpl extends AbstractFunctionWithPropertiesAsInputImpl<Files> implements FilesGetter,Serializable {
	private static final long serialVersionUID = 1L;

	private Strings directories;
	private Boolean isFileChecksumComputable,isFilterByFileChecksum;
	
	@Override
	protected Files __execute__() throws Exception {
		Boolean isFilterByFileChecksum = __injectValueHelper__().defaultToIfNull(getIsFilterByFileChecksum(),Boolean.FALSE);
		Files files =  __inject__(Files.class).setIsDuplicateChecksumAllowed(!isFilterByFileChecksum);
		Strings directories = __injectValueHelper__().returnOrThrowIfBlank("file directories", getDirectories());
		Boolean isFileChecksumComputable = getIsFileChecksumComputable();
		
		directories.get().forEach(new Consumer<String>() {
			@Override
			public void accept(String directory) {
				try {
					java.nio.file.Files.newDirectoryStream(Paths.get(directory),path -> path.toFile().isDirectory() || path.toFile().isFile()).forEach(new Consumer<Path>() {
						@Override
						public void accept(Path path) {
							if(Boolean.TRUE.equals(path.toFile().isDirectory())) {
								files.add(__inject__(FilesGetter.class).addDirectories(path.toString()).setIsFileChecksumComputable(isFileChecksumComputable)
										.execute().getOutput());
							}else {
								FileBuilder fileBuilder = __inject__(FileBuilder.class).setPath(path.getParent().toString()).setName(path.getFileName().toString());
								if(Boolean.TRUE.equals(isFileChecksumComputable)) {
									try {
										fileBuilder.setChecksum(new String(new DigestUtils(MessageDigestAlgorithms.SHA_1).digestAsHex(path.toFile())));
									} catch (IOException exception) {
										exception.printStackTrace();
									}
								}
								files.add(fileBuilder.execute().getOutput());	
							}
						}
					});;
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		});;
		return files;
	}

	@Override
	public Strings getDirectories() {
		return directories;
	}

	@Override
	public Strings getDirectories(Boolean injectIfNull) {
		return (Strings) __getInjectIfNull__(FIELD_DIRECTORIES, injectIfNull);
	}

	@Override
	public FilesGetter setDirectories(Strings directories) {
		this.directories = directories;
		return this;
	}

	@Override
	public FilesGetter addDirectories(Collection<String> directories) {
		getDirectories(Boolean.TRUE).add(directories);
		return this;
	}

	@Override
	public FilesGetter addDirectories(String... directories) {
		getDirectories(Boolean.TRUE).add(directories);
		return this;
	}
	
	@Override
	public FilesGetter addDirectories(Strings directories) {
		getDirectories(Boolean.TRUE).add(directories);
		return this;
	}
	
	@Override
	public FilesGetter addDirectoriesByJavaFiles(Collection<java.io.File> files) {
		if(__inject__(CollectionHelper.class).isNotEmpty(files))
			files.forEach(new Consumer<java.io.File>() {
				@Override
				public void accept(java.io.File file) {
					if(file.isDirectory())
						addDirectories(file.getPath());
				}
			});
		return this;
	}
	
	@Override
	public FilesGetter addDirectoriesByJavaFiles(java.io.File... files) {
		return addDirectoriesByJavaFiles(__inject__(CollectionHelper.class).instanciate(files));
	}
	
	@Override
	public Boolean getIsFileChecksumComputable() {
		return isFileChecksumComputable;
	}
	
	@Override
	public FilesGetter setIsFileChecksumComputable(Boolean isFileChecksumComputable) {
		this.isFileChecksumComputable = isFileChecksumComputable;
		return this;
	}
	
	@Override
	public Boolean getIsFilterByFileChecksum() {
		return isFilterByFileChecksum;
	}
	
	@Override
	public FilesGetter setIsFilterByFileChecksum(Boolean isFilterByFileChecksum) {
		this.isFilterByFileChecksum = isFilterByFileChecksum;
		return this;
	}
	
	public static final String FIELD_DIRECTORIES = "directories";
	
}

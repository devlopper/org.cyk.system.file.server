package org.cyk.utility.file;

import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.cyk.utility.function.AbstractFunctionWithPropertiesAsInputImpl;

public class FileBuilderImpl extends AbstractFunctionWithPropertiesAsInputImpl<File> implements FileBuilder,Serializable{
	private static final long serialVersionUID = 1L;

	private InputStream inputStream;
	private Class<?> clazz;
	private String path,name,uniformResourceLocator,mimeType,extension;
	private byte[] bytes;
	
	@Override
 	protected File __execute__() throws Exception {
		File file = __inject__(File.class);
		String uniformResourceLocator = getUniformResourceLocator();
		file.setUniformResourceLocator(uniformResourceLocator);
		
		String path = getPath();
		file.setPath(path);
		
		String name = getName();
		file.setName(FilenameUtils.getBaseName(name));
		
		String extension = getExtension();
		if(__injectStringHelper__().isBlank(extension))
			extension = FilenameUtils.getExtension(name);
		file.setExtension(extension);
		
		String mimeType = getMimeType();
		if(__injectStringHelper__().isBlank(mimeType))
			mimeType = __inject__(MimeTypeGetter.class).setExtension(file.getExtension()).execute().getOutput();
		file.setMimeType(mimeType);
		
		byte[] bytes = getBytes();
		file.setBytes(bytes);
		
		if(file.getBytes() == null) {
			InputStream inputStream = getInputStream();
			if(inputStream == null) {
				Class<?> clazz = getClazz();
				if(clazz!=null)
					inputStream = clazz.getResourceAsStream(name);
			}
			if(inputStream!=null)
				file.setBytes(IOUtils.toByteArray(inputStream));
		}
		
		if(file.getSize() == null) {
			if(file.getBytes()!=null)
				file.setSize(new Long(file.getBytes().length));
		}
		
		return file;
	}
	
	@Override
	public InputStream getInputStream() {
		return inputStream;
	}
	
	@Override
	public FileBuilder setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}
	
	@Override
	public Class<?> getClazz() {
		return clazz;
	}

	@Override
	public FileBuilder setClazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public FileBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	@Override
	public String getUniformResourceLocator() {
		return uniformResourceLocator;
	}
	@Override
	public FileBuilder setUniformResourceLocator(String uniformResourceLocator) {
		this.uniformResourceLocator = uniformResourceLocator;
		return this;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
	@Override
	public FileBuilder setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	
	@Override
	public String getMimeType() {
		return mimeType;
	}
	@Override
	public FileBuilder setMimeType(String mimeType) {
		this.mimeType = mimeType;
		return this;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public FileBuilder setPath(String path) {
		this.path = path;
		return this;
	}
	
	@Override
	public String getExtension() {
		return extension;
	}
	
	@Override
	public FileBuilder setExtension(String extension) {
		this.extension = extension;
		return this;
	}
}

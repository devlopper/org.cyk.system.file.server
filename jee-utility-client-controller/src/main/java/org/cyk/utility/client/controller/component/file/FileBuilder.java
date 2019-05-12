package org.cyk.utility.client.controller.component.file;

import org.cyk.utility.client.controller.component.VisibleComponentBuilder;

public interface FileBuilder extends VisibleComponentBuilder<File> {

	org.cyk.utility.file.FileBuilder getValue();
	org.cyk.utility.file.FileBuilder getValue(Boolean injectIfNull);
	FileBuilder setValue(org.cyk.utility.file.FileBuilder value);
	
	Boolean getIsEmbbedable();
	FileBuilder setIsEmbeddable(Boolean isEmbeddable);
	
	Boolean getIsIdentifiable();
	FileBuilder setIsIdentifiable(Boolean isIdentifiable);
	
	FileBuilder setValuePath(String path);
	FileBuilder setValueName(String name);
	FileBuilder setValueClazz(Class<?> clazz);
	FileBuilder setValueBytes(byte[] bytes);
	FileBuilder setValueUniformResourceLocator(String uniformResourceLocator);
	FileBuilder setValueMimeType(String mimeType);
}
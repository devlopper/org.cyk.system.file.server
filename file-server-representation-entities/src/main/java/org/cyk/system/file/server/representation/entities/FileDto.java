package org.cyk.system.file.server.representation.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.cyk.utility.server.representation.AbstractEntityFromPersistenceEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@XmlRootElement @Getter @Setter @Accessors(chain=true) @NoArgsConstructor
public class FileDto extends AbstractEntityFromPersistenceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String uniformResourceLocator;
	
	private byte[] bytes;
	
	private String name;
	private String extension;
	private String mimeType;
	private Long size;
	private String nameAndExtension;
	
	@Override
	public FileDto setIdentifier(String identifier) {
		return (FileDto) super.setIdentifier(identifier);
	}
	
}

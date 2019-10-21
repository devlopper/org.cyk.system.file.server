package org.cyk.system.file.server.representation.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.cyk.utility.__kernel__.object.__static__.representation.AbstractIdentifiedByStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@XmlRootElement @Getter @Setter @Accessors(chain=true) @NoArgsConstructor
public class FileDto extends AbstractIdentifiedByStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private String uniformResourceLocator;
	
	private byte[] bytes;
	
	private String name;
	private String extension;
	private String mimeType;
	private Long size;
	private String nameAndExtension;
	private String sha1;
	private String text;
	
	@Override
	public FileDto setIdentifier(String identifier) {
		return (FileDto) super.setIdentifier(identifier);
	}
	
}

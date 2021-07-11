package org.cyk.system.file.server.representation.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.cyk.utility.representation.entity.AbstractIdentifiableSystemScalarStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@XmlRootElement @Getter @Setter @Accessors(chain=true) @NoArgsConstructor
public class FileDto extends AbstractIdentifiableSystemScalarStringImpl implements Serializable {
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
	
	@Override
	public FileDto add__link__(String name, String value) {
		return (FileDto) super.add__link__(name, value);
	}
	
	public FileDto addDownloadLink(String value) {
		return add__link__(LINK_DOWNLOAD, value);
	}
	
	/**/
	
	public static final String LINK_DOWNLOAD = "download";
}
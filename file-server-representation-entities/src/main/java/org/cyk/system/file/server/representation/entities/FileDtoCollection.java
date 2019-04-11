package org.cyk.system.file.server.representation.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.cyk.utility.server.representation.AbstractEntityCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@XmlRootElement @Getter @Setter @Accessors(chain=true) @NoArgsConstructor
@XmlSeeAlso(FileDto.class)
public class FileDtoCollection extends AbstractEntityCollection<FileDto> implements Serializable {
	private static final long serialVersionUID = 1L;

	public FileDtoCollection add(String code,String name) {
		add(new FileDto().setCode(code).setName(name));
		return this;
	}
	
	public FileDtoCollection add(String code) {
		return add(code, null);
	}
		
}

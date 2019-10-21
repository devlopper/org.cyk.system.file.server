package org.cyk.system.file.server.representation.entities;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.cyk.utility.__kernel__.object.__static__.representation.AbstractCollectionOfIdentifiedByStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@XmlRootElement @Getter @Setter @Accessors(chain=true) @NoArgsConstructor
@XmlSeeAlso(FileDto.class)
public class FileDtoCollection extends AbstractCollectionOfIdentifiedByStringImpl<FileDto> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<FileDto> elements;
	
}

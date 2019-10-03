package org.cyk.utility.server.representation.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.cyk.utility.__kernel__.object.__static__.representation.AbstractIdentifiedByStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@XmlRootElement @Getter @Setter @Accessors(chain=true) @NoArgsConstructor
public class ParentChildDto extends AbstractIdentifiedByStringImpl implements Serializable {	
	private static final long serialVersionUID = 1L;

	private ParentDto parent;
	private ChildDto child;
	
	@Override
	public ParentChildDto setIdentifier(String identifier) {
		return (ParentChildDto) super.setIdentifier(identifier);
	}
	
}

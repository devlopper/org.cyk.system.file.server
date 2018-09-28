package org.cyk.utility.server.representation;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@XmlRootElement @Getter @Setter @Accessors(chain=true) @NoArgsConstructor
public class MessageDtoCollection extends AbstractEntityCollection<MessageDto> implements  Serializable {
	private static final long serialVersionUID = 1L;
	
}

package org.cyk.utility.server.representation.entities;

import org.cyk.utility.server.persistence.entities.Node;
import org.cyk.utility.server.representation.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class NodeDtoMapper extends AbstractMapperSourceDestinationImpl<NodeDto, Node> {
	private static final long serialVersionUID = 1L;
	
}
package org.cyk.system.file.server.representation.impl;

import java.io.Serializable;

import org.cyk.system.file.server.representation.entities.FileDto;

@org.cyk.system.file.server.annotation.System
public class LinksGeneratorImpl extends org.cyk.utility.representation.server.LinksGeneratorImpl implements Serializable {

	@Override
	protected String getDefaultValueFormat(Object owner, String identifier) {
		if(owner instanceof FileDto && FileRepresentationImpl.LINK_DOWNLOAD.equals(identifier))
			return FileRepresentationImpl.LINK_DOWNLOAD_FORMAT;
		return super.getDefaultValueFormat(owner, identifier);
	}
}
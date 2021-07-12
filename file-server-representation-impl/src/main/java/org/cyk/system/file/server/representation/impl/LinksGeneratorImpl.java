package org.cyk.system.file.server.representation.impl;

import java.io.Serializable;

import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.representation.Link;

@org.cyk.system.file.server.annotation.System
public class LinksGeneratorImpl extends org.cyk.utility.representation.server.LinksGeneratorImpl implements Serializable {

	@Override
	protected String getDefaultValueFormat(Object owner, String identifier) {
		if(owner instanceof FileDto && FileRepresentationImpl.LINK_DOWNLOAD.equals(identifier))
			return FileRepresentationImpl.LINK_DOWNLOAD_FORMAT;
		return super.getDefaultValueFormat(owner, identifier);
	}
	
	@Override
	protected String __generateValue__(Object owner, Link link, String valueFormat) {
		String value = super.__generateValue__(owner, link, valueFormat);
		if(owner instanceof FileDto && FileRepresentationImpl.LINK_DOWNLOAD.equals(link.getIdentifier()))
			((FileDto)owner).setDownloadLink(link.getValue());
		return value;
	}
}
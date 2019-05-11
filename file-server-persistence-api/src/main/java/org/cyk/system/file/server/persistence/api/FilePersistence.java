package org.cyk.system.file.server.persistence.api;

import java.util.Collection;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.server.persistence.PersistenceEntity;

public interface FilePersistence extends PersistenceEntity<File> {

	File readBySha1(String sha1);
	Collection<File> readWhereNameContains(String string);
}

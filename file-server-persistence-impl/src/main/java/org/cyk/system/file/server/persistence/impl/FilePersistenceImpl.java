package org.cyk.system.file.server.persistence.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.system.file.server.persistence.api.FilePersistence;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.utility.__kernel__.configuration.ConfigurationHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;

@ApplicationScoped
public class FilePersistenceImpl extends AbstractPersistenceEntityImpl<File> implements FilePersistence,Serializable {
	private static final long serialVersionUID = 1L;

	public static final String DIRECTORY = "FILES_DIRECTORY";
	public static String getDirectory() {
		String directory = ConfigurationHelper.getValueAsString(DIRECTORY);
		if(StringHelper.isBlank(directory)) {
			LogHelper.logWarning(String.format("No files directory found under variable named %s", DIRECTORY), FilePersistenceImpl.class);
			return null;
		}
		return directory;
	}
	
	public static final String ACCEPTED_PATH_NAME_REGULAR_EXPRESSION = "ACCEPTED_PATH_NAME_REGULAR_EXPRESSION";
	public static String getAcceptedPathNameRegularExpression() {
		String acceptedPathNameRegularExpression = ConfigurationHelper.getValueAsString(ACCEPTED_PATH_NAME_REGULAR_EXPRESSION);
		if(StringHelper.isBlank(acceptedPathNameRegularExpression)) {
			LogHelper.logWarning(String.format("No accepted path name regular expression found under variable named %s.", ACCEPTED_PATH_NAME_REGULAR_EXPRESSION), FilePersistenceImpl.class);
			return null;
		}
		if(StringHelper.isBlank(acceptedPathNameRegularExpression)) {
			acceptedPathNameRegularExpression = ".pdf";
			LogHelper.logInfo("<<.pdf>> will be used as accepted path name regular expression", FilePersistenceImpl.class);
		}
		return acceptedPathNameRegularExpression;
	}
	
	public static final String MINIMAL_SIZE = "MINIMAL_SIZE";
	public static Long getMinimalSize() {
		Long size = ConfigurationHelper.getValueAsLong(MINIMAL_SIZE);
		if(NumberHelper.isLessThanOrEqualZero(size)) {
			LogHelper.logWarning(String.format("No minimal size found under variable named %s.", MINIMAL_SIZE), FilePersistenceImpl.class);
			return null;
		}
		if(NumberHelper.isLessThanOrEqualZero(size)) {
			size = File.MINIMAL_SIZE;
			LogHelper.logInfo("<<1>> will be used as minimal size", FilePersistenceImpl.class);
		}
		return size;
	}
	
	public static final String MAXIMAL_SIZE = "MAXIMAL_SIZE";
	public static Long getMaximalSize() {
		Long size = ConfigurationHelper.getValueAsLong(MAXIMAL_SIZE);
		if(NumberHelper.isLessThanOrEqualZero(size)) {
			LogHelper.logWarning(String.format("No maximal size found under variable named %s.", MAXIMAL_SIZE), FilePersistenceImpl.class);
			return null;
		}
		if(NumberHelper.isLessThanOrEqualZero(size)) {
			size = File.MAXIMAL_SIZE;
			LogHelper.logInfo("<<1>> will be used as maximal size", FilePersistenceImpl.class);
		}
		return size;
	}
}
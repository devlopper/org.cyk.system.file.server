package org.cyk.system.file.server.persistence.entities;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.utility.server.persistence.jpa.AbstractIdentifiedByString;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @Entity @Access(AccessType.FIELD)
@Table(name=File.TABLE)
public class File extends AbstractIdentifiedByString implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Physical location. Can be local (file:///) or remote (ftp, http,...)
	 */
	@Column(name=COLUMN_UNIFORM_RESOURCE_LOCATOR) private String uniformResourceLocator;
	
	/**
	 * Logical name
	 */
	@NotNull
	@Column(name=COLUMN_NAME) private String name;
	
	/* 
	 * Derived informations. Those informations can be derived from bytes or collected from inputed file.
	 * */
	
	/**
	 * Extension
	 */
	@Column(name=COLUMN_EXTENSION) private String extension;
	
	/**
	 * Mime type
	 */
	@NotNull
	@Column(name=COLUMN_MIME_TYPE) private String mimeType;
	
	/**
	 * Size
	 */
	@NotNull
	@Column(name=COLUMN_SIZE) private Long size;
		
	/**/
	
	@Transient private String nameAndExtension;
	@Transient private byte[] bytes;
	
	/**/
	
	@Override
	public File setIdentifier(String identifier) {
		return (File) super.setIdentifier(identifier);
	}
	
	public String getNameAndExtension() {
		if(nameAndExtension == null) {
			nameAndExtension = name;
			if(extension!=null && !extension.isEmpty())
				nameAndExtension += "."+extension;	
		}
		return nameAndExtension;
	}
	
	/**/
	
	public static final String FIELD_BYTES = "bytes";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_EXTENSION = "extension";
	public static final String FIELD_MIME_TYPE = "mimeType";
	public static final String FIELD_UNIFORM_RESOURCE_LOCATOR = "uniformResourceLocator";
	public static final String FIELD_SIZE = "size";
	
	public static final String COLUMN_NAME = FIELD_NAME;
	public static final String COLUMN_EXTENSION = FIELD_EXTENSION;
	public static final String COLUMN_MIME_TYPE = FIELD_MIME_TYPE;
	public static final String COLUMN_UNIFORM_RESOURCE_LOCATOR = FIELD_UNIFORM_RESOURCE_LOCATOR;
	public static final String COLUMN_SIZE = FIELD_SIZE;
	
	public static final String TABLE = Constant.TABLE_NAME_PREFIX+"file";
	
	/**/

}

package org.cyk.system.file.server.persistence.entities;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.server.persistence.jpa.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @Entity @Access(AccessType.FIELD)
@Table(name=File.TABLE)
public class File extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Physical location. Can be local (file:///) or remote (ftp, http,...)
	 */
	@Column(name=COLUMN_UNIFORM_RESOURCE_LOCATOR) private String uniformResourceLocator;
	
	/**
	 * Binary content
	 */
	@Column(name=COLUMN_BYTES) private byte[] bytes;
	
	/**
	 * Logical name
	 */
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
	@Column(name=COLUMN_MIME_TYPE) private String mimeType;
	
	/**
	 * Size
	 */
	@Column(name=COLUMN_SIZE) private Long size;
		
	/**/
	
	/**/
	
	@Override
	public File setCode(String code) {
		return (File) super.setCode(code);
	}
	
	/**/
	
	@Override
	public String toString() {
		return getCode()+","+getName();
	}
	
	/**/
	
	public static final String FIELD_BYTES = "bytes";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_EXTENSION = "extension";
	public static final String FIELD_MIME_TYPE = "mimeType";
	public static final String FIELD_UNIFORM_RESOURCE_LOCATOR = "uniformResourceLocator";
	public static final String FIELD_SIZE = "size";
	
	public static final String COLUMN_NAME = FIELD_NAME;
	public static final String COLUMN_BYTES = FIELD_BYTES;
	public static final String COLUMN_EXTENSION = FIELD_EXTENSION;
	public static final String COLUMN_MIME_TYPE = FIELD_MIME_TYPE;
	public static final String COLUMN_UNIFORM_RESOURCE_LOCATOR = FIELD_UNIFORM_RESOURCE_LOCATOR;
	public static final String COLUMN_SIZE = FIELD_SIZE;
	
	public static final String TABLE = Constant.TABLE_NAME_PREFIX+"file";
	
	/**/

}

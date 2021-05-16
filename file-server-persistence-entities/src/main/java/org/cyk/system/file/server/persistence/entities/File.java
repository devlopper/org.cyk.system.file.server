package org.cyk.system.file.server.persistence.entities;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.object.__static__.persistence.AbstractIdentifiableSystemScalarStringImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @Entity @Access(AccessType.FIELD)
@Table(name=File.TABLE_NAME)
public class File extends AbstractIdentifiableSystemScalarStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Physical location. Can be local (file:///) or remote (ftp, http,...)
	 */
	@Column(name=COLUMN_UNIFORM_RESOURCE_LOCATOR,unique=true) private String uniformResourceLocator;
	
	/** 
	 * Logical nam
	 */
	@NotNull
	@Column(name=COLUMN_NAME,nullable = false) private String name;
	
	/* 
	 * Derived informations. Those informations can be derived from bytes or collected from inputed file.
	 * */
	
	/**
	 * Extension
	 */
	@Column(name=COLUMN_EXTENSION,length=10) private String extension;
	
	/**
	 * Mime type
	 */
	@NotNull
	@Column(name=COLUMN_MIME_TYPE,nullable = false,length=50) private String mimeType;
	
	/**
	 * Size
	 */
	@NotNull
	@Column(name=COLUMN_SIZE,nullable = false) private Long size;
		
	/**
	 * SHA1
	 */
	@Column(name=COLUMN_SHA1,length=40) private String sha1;
	
	/**/
	
	@Transient private Boolean isBytesPersistableOnCreate;
	@Transient private Boolean isTextPersistableOnCreate;
	
	@Transient private String nameAndExtension;
	@Transient private byte[] bytes;
	@Transient private Boolean isBytesAccessibleFromUniformResourceLocator;
	@Transient private String text;
	
	/**/
	
	@Override
	public File setIdentifier(String identifier) {
		return (File) super.setIdentifier(identifier);
	}
	
	/**/
	
	public static final String FIELD_NAME = "name";
	public static final String FIELD_BYTES = "bytes";
	public static final String FIELD_TEXT = "text";
	public static final String FIELD_EXTENSION = "extension";
	public static final String FIELD_NAME_AND_EXTENSION = "nameAndExtension";
	public static final String FIELD_MIME_TYPE = "mimeType";
	public static final String FIELD_UNIFORM_RESOURCE_LOCATOR = "uniformResourceLocator";
	public static final String FIELD_SIZE = "size";
	public static final String FIELD_SHA1 = "sha1";
	
	public static final String TABLE_NAME = "at_file";
	
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_EXTENSION = "extension";
	public static final String COLUMN_MIME_TYPE = "mime";
	public static final String COLUMN_UNIFORM_RESOURCE_LOCATOR = "uri";
	public static final String COLUMN_SIZE = "size";
	public static final String COLUMN_SHA1 = "sha1";
	
	/**/
	
	public static final Long MINIMAL_SIZE = 1l; // 1
	public static final Long MAXIMAL_SIZE = 1024l * 1024; // 1M
}
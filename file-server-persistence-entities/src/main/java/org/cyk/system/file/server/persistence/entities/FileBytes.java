package org.cyk.system.file.server.persistence.entities;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.cyk.utility.server.persistence.jpa.AbstractIdentifiedByString;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @Entity @Access(AccessType.FIELD)
@Table(name=FileBytes.TABLE,uniqueConstraints= {
		@UniqueConstraint(name="file_must_be_unique",columnNames= {FileBytes.COLUMN_FILE})
})
/**
 * Local physical storage of file content as bytes. This strategy has been used in order to decouple file properties and its content.
 * Also to avoid eager fetching or loading when querying.
 * @author CYK
 *
 */
public class FileBytes extends AbstractIdentifiedByString implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull @OneToOne @JoinColumn(name=COLUMN_FILE)
	private File file;
	
	/**
	 * Bytes content
	 */
	@NotNull @Lob
	@Column(name=COLUMN_BYTES) private byte[] bytes;
	
	/**/
	
	@Override
	public FileBytes setIdentifier(String identifier) {
		return (FileBytes) super.setIdentifier(identifier);
	}
	
	/**/
	public static final String FIELD_FILE = "file";
	public static final String FIELD_BYTES = "bytes";
	
	public static final String COLUMN_FILE = FIELD_FILE;
	public static final String COLUMN_BYTES = FIELD_BYTES;
	
	public static final String TABLE = Constant.TABLE_NAME_PREFIX+"bytes";
	
	/**/

}

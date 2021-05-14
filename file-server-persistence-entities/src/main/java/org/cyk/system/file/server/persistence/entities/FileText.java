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
import javax.validation.constraints.NotNull;

import org.cyk.utility.server.persistence.jpa.AbstractIdentifiedByString;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @Entity @Access(AccessType.FIELD)
@Table(name=FileText.TABLE)
/**
 * Textual content of the file.<br/>
 * Because text can be large , this strategy has been used in order to decouple file properties and its text.
 * Also to avoid eager fetching or loading when querying.
 * @author CYK
 *
 */
public class FileText extends AbstractIdentifiedByString implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull @OneToOne @JoinColumn(name=COLUMN_FILE,unique=true) private File file;
	@NotNull @Lob @Column(name=COLUMN_TEXT) private String text;
	
	/**/
	
	@Override
	public FileText setIdentifier(String identifier) {
		return (FileText) super.setIdentifier(identifier);
	}
	
	/**/
	
	public static final String FIELD_FILE = "file";
	public static final String FIELD_TEXT = "text";
	
	public static final String TABLE = "at_file_text";
	
	public static final String COLUMN_FILE = "file";
	public static final String COLUMN_TEXT = "text";
	
	/**/
}
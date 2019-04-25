package org.cyk.utility.__kernel__.constant;

public interface ConstantString {

	String CYK = "CYK";
	
	String WORD_JAVA = "java";
	String WORD_JAVAX = WORD_JAVA+"x";
	String WORD_GLOBAL = "global";
	String WORD_APP = "app";
	String WORD_VALIDATION = "validation";
	String WORD_MAIL = "mail";
	String WORD_NAME = "name";
	
	String JAVA_STRING_FORMAT_MARKER_S = "%s";
	
	String JNDI_NAMESPACE_GLOBAL = WORD_JAVA+ConstantCharacter.COLON+WORD_GLOBAL;
	String JNDI_NAMESPACE_APP = WORD_JAVA+ConstantCharacter.COLON+WORD_APP;
	
	String PREFIX_PACKAGE_JAVAX = WORD_JAVAX+ConstantCharacter.DOT;	
	String PREFIX_PACKAGE_BEAN_VALIDATION = PREFIX_PACKAGE_JAVAX+WORD_VALIDATION;
	String PREFIX_PACKAGE_ORG_CYK = "org.cyk.";	
	String PREFIX_PACKAGE_ORG_CYK_SYSTEM = PREFIX_PACKAGE_ORG_CYK+"system.";		
	
	String FIELD_ = "FIELD_";	

	String VARIABLE_RESULT = "result";
	
	String ENCODING_UTF8 = "UTF-8";
		
	String IMPL = "Impl";
	
	String MANY = "many";
	String COLLECTION = "collection";
	String ALL = "all";
	String CREATE = "create";
	String UPDATE = "update";
	String DELETE = "delete";
	String GET = "get";
	String COUNT = "count";
	String IDENTIFIER = "identifier";
	String TYPE = "type";
	String FIELDS = "fields";
	String CODE = "code";
	String FROM = "from";
	String UPLOAD = "upload";
	String DOWNLOAD = "download";
	String FILENAME = "filename";
	String IS = "is";
	String INLINE = "inline";
	String ATTACHMENT = "attachment";
	String IS_INLINE = IS+INLINE;

}
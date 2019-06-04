package org.cyk.utility.server.business;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.array.ArrayInstanceTwoDimensionString;
import org.cyk.utility.clazz.ClassHelper;
import org.cyk.utility.file.excel.FileExcelSheetDataArrayReader;
import org.cyk.utility.map.MapInstanceIntegerToString;
import org.cyk.utility.server.persistence.PersistenceEntity;
import org.cyk.utility.server.persistence.PersistenceLayer;
import org.cyk.utility.value.ValueUsageType;

import lombok.Getter;

public abstract class AbstractBusinessEntityImpl<ENTITY,PERSISTENCE extends PersistenceEntity<ENTITY>> extends AbstractBusinessServiceProviderImpl<ENTITY> implements BusinessEntity<ENTITY>,Serializable {
	private static final long serialVersionUID = 1L;

	@Getter private Class<ENTITY> persistenceEntityClass;
	@Getter private PERSISTENCE persistence;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		persistenceEntityClass = __getPersistenceEntityClass__();
		if(persistenceEntityClass == null) {
			System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+getClass()+" : persistence entity class cannot be derived <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}else {
			persistence = (PERSISTENCE) __inject__(PersistenceLayer.class).injectInterfaceClassFromEntityClass(getPersistenceEntityClass());	
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Class<ENTITY> __getPersistenceEntityClass__(){
		return (Class<ENTITY>) __inject__(ClassHelper.class).getParameterAt(getClass(), 0, Object.class);
	}
	
	//TODO : an idea is to transform array content to json format and transform it java object
	@Override
	public BusinessEntity<ENTITY> saveFromArray(ArrayInstanceTwoDimensionString arrayInstanceTwoDimensionString,MapInstanceIntegerToString columnIndexFieldNameMap, Properties properties) {
		__throwRuntimeExceptionIfEmpty__(arrayInstanceTwoDimensionString, "save from array : array instance");
		Collection<ENTITY> entities = null;
		for(Integer index  = 0; index < arrayInstanceTwoDimensionString.getFirstDimensionElementCount(); index = index + 1) {
			ENTITY entitiy = __injectClassHelper__().instanciate(getPersistenceEntityClass());
			for(Map.Entry<Integer, String> indexEntry : columnIndexFieldNameMap.getEntries()) {
				__injectFieldValueSetter__().execute(entitiy, indexEntry.getValue(), arrayInstanceTwoDimensionString.get(index, indexEntry.getKey()));
			}
			if(entities == null)
				entities = new ArrayList<ENTITY>();
			entities.add(entitiy);
		}
		if(__injectCollectionHelper__().isNotEmpty(entities)) {
			__logInfo__("Saving "+entities.size()+" "+getPersistenceEntityClass().getSimpleName());
			saveMany(entities);		
		}
		return this;
	}
	
	@Override
	public BusinessEntity<ENTITY> saveFromFileExcelSheet(FileExcelSheetDataArrayReader fileExcelSheetDataArrayReader,MapInstanceIntegerToString columnIndexFieldNameMap, Properties properties) {
		saveFromArray(fileExcelSheetDataArrayReader.execute().getOutput(), columnIndexFieldNameMap, properties);
		return this;
	}
	
	//TODO : an idea is to transform excel content to json format and transform it java object
	@Override
	public BusinessEntity<ENTITY> saveFromFileExcelSheet(InputStream workbookInputStream,String sheetName,MapInstanceIntegerToString columnIndexFieldNameMap,Properties properties) {
		__throwRuntimeExceptionIfBlank__(workbookInputStream, "save many from file excel sheet : workbook input stream");
		__throwRuntimeExceptionIfBlank__(sheetName, "save many from file excel sheet : sheet name");
		__throwRuntimeExceptionIfEmpty__(columnIndexFieldNameMap, "create many from file excel sheet : column index and field name mapping");
		
		FileExcelSheetDataArrayReader reader = __inject__(FileExcelSheetDataArrayReader.class);
		reader.setWorkbookInputStream(workbookInputStream).setSheetName(sheetName);
		reader.getRowInterval(Boolean.TRUE).getLow(Boolean.TRUE).setValue(1);
		
		saveFromFileExcelSheet(reader, columnIndexFieldNameMap, properties);		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ENTITY findOne(Object identifier, Properties properties) {
		ENTITY entity = (ENTITY) __inject__(BusinessFunctionReader.class).setEntityClass(getPersistenceEntityClass()).setEntityIdentifier(identifier)
				.setEntityIdentifierValueUsageType(properties == null ? ValueUsageType.SYSTEM: (ValueUsageType) properties.getValueUsageType()).execute().getProperties().getEntity();
		__listenFindOneAfter__(entity, properties);
		return entity;
	}

	@Override
	public ENTITY findOne(Object identifier, ValueUsageType valueUsageType) {
		return findOne(identifier,new Properties().setValueUsageType(valueUsageType));
	}

	@Override
	public ENTITY findOne(Object identifier) {
		return findOne(identifier,(Properties)null);
	}

	@Override
	public ENTITY findOneByBusinessIdentifier(Object identifier) {
		return findOne(identifier, ValueUsageType.BUSINESS);
	}
	
	@Override
	public ENTITY findOneBySystemIdentifier(Object identifier) {
		return findOne(identifier, ValueUsageType.SYSTEM);
	}

	@Override
	public Collection<ENTITY> findMany(Properties properties) {
		Collection<ENTITY> entities = getPersistence().readMany(properties);
		__listenFindManyAfter__(entities,properties);
		return entities;
	}

	@Override
	public Collection<ENTITY> findMany() {
		//TODO use default settings like pagination and sorting
		return findMany(null);
	}

	protected void __listenFindManyAfter__(Collection<ENTITY> entities,Properties properties) {
		if(__injectCollectionHelper__().isNotEmpty(entities)) {
			for(ENTITY index : entities)
				__processAfterRead__(index,properties);	
		}
	}
	
	protected void __listenFindOneAfter__(ENTITY entity,Properties properties) {
		if(entity != null)
			__processAfterRead__(entity, properties);
	}
	
	protected void __processAfterRead__(ENTITY entity,Properties properties) {
		
	}
	
	@Override
	public Long count(Properties properties) {
		return getPersistence().count(properties);
	}

	@Override
	public Long count() {
		//TODO use default settings like pagination and sorting
		return count(null);
	}
	
	/**/
	
	@Override @Transactional
	public BusinessEntity<ENTITY> deleteByIdentifier(Object identifier,ValueUsageType valueUsageType) {
		delete(getPersistence().readOne(identifier,valueUsageType));
		return this;
	}
	
	@Override @Transactional
	public BusinessEntity<ENTITY> deleteBySystemIdentifier(Object identifier) {
		deleteByIdentifier(identifier,ValueUsageType.SYSTEM);
		return this;
	}
	
	@Override @Transactional
	public BusinessEntity<ENTITY> deleteByBusinessIdentifier(Object identifier) {
		deleteByIdentifier(identifier,ValueUsageType.BUSINESS);
		return this;
	}
	
	@Override @Transactional
	public BusinessEntity<ENTITY> deleteAll() {
		/*BusinessFunctionRemover function = __inject__(BusinessFunctionRemover.class);
		function.setEntities(findMany());
		function.setEntityClass(getPersistenceEntityClass());		
		//function.setAll(Boolean.TRUE).setEntityClass(getPersistenceEntityClass());
		function.execute();
		*/
		Collection<ENTITY> entities = findMany();
		if(__injectCollectionHelper__().isNotEmpty(entities))
			for(ENTITY index : entities)
				delete(index);
		return this;
	}
	
	/**/
	
	protected void __create__(Object object) {
		__inject__(Business.class).create(object);
	}
	
	protected void __createIfSystemIdentifierIsBlank__(Object object) {
		__inject__(Business.class).create(object,new Properties().setIsCreateIfSystemIdentifierIsBlank(Boolean.TRUE));	
	}
}

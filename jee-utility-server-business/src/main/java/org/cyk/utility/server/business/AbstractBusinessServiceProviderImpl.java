package org.cyk.utility.server.business;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.assertion.AssertionBuilderNull;
import org.cyk.utility.server.persistence.PersistenceEntity;
import org.cyk.utility.server.persistence.PersistenceLayer;
import org.cyk.utility.system.AbstractSystemServiceProviderImpl;
import org.cyk.utility.system.action.SystemAction;

public abstract class AbstractBusinessServiceProviderImpl<OBJECT> extends AbstractSystemServiceProviderImpl implements BusinessServiceProvider<OBJECT>,Serializable {
	private static final long serialVersionUID = 1L;
	
	public static Boolean IS_CREATE_ONE_BY_ONE = Boolean.FALSE;
	
	@Override @Transactional
	public BusinessServiceProvider<OBJECT> create(OBJECT object, Properties properties) {
		if(object == null) {
			throw new RuntimeException("cannot create null instance. properties are : "+properties);
		}else {
			/*Boolean create = Boolean.FALSE;
			Object isCreateIfSystemIdentifierIsBlank = Properties.getFromPath(properties, Properties.IS_CREATE_IF_SYSTEM_IDENTIFIER_IS_BLANK);
			if(isCreateIfSystemIdentifierIsBlank == null) {
				create = Boolean.TRUE;
			}else {
				if(Boolean.TRUE.equals(__inject__(BooleanHelper.class).get(isCreateIfSystemIdentifierIsBlank))) {
					Object identifier = __injectFieldHelper__().getFieldValueSystemIdentifier(object);
					create = Boolean.TRUE.equals(__injectValueHelper__().isBlank(identifier));	
				}
			}
			if(Boolean.TRUE.equals(create)) {
				BusinessFunctionCreator function = __injectCreatorForOne__();
				function.setEntity(object);
				
				__configure__(function, properties);

				validateOne(object, function.getAction());
				__listenExecuteCreateOneBefore__(object, properties, function);
				function.execute();
				__listenExecuteCreateOneAfter__(object, properties, function);
				validateOne(object);
			}*/
			
			BusinessFunctionCreator function = __injectCreatorForOne__();
			__copyCommonProperties__(function, properties);
			function.setEntity(object);
			__listenExecuteCreateOneBefore__(object, properties, function);
			function.execute();
			__listenExecuteCreateOneAfter__(object, properties, function);
		}
		return this;
	}
	
	protected BusinessFunctionCreator __injectCreatorForOne__(){
		return ____inject____(BusinessFunctionCreator.class);
	}
	
	@Override @Transactional
	public BusinessServiceProvider<OBJECT> create(OBJECT object) {
		return create(object, null);
	}

	protected void __listenExecuteCreateBefore__(OBJECT object, Properties properties,BusinessFunctionCreator function){}
	protected void __listenExecuteCreateAfter__(OBJECT object, Properties properties,BusinessFunctionCreator function){}
	
	protected void __listenExecuteCreateOneBefore__(OBJECT object, Properties properties,BusinessFunctionCreator function){
		__listenExecuteCreateBefore__(object, properties, function);
	}
	protected void __listenExecuteCreateOneAfter__(OBJECT object, Properties properties,BusinessFunctionCreator function){
		__listenExecuteCreateAfter__(object, properties, function);
	}
	
	@Override  @Transactional
	public BusinessServiceProvider<OBJECT> createMany(Collection<OBJECT> objects, Properties properties) {
		BusinessFunctionCreator function = __injectCreatorForMany__();
		__copyCommonProperties__(function, properties);
		if(Boolean.TRUE.equals(__isCreateManyOneByOne__())) {
			//Loop execution
			function.try_().setIsCodeFromFunctionExecutable(Boolean.FALSE).run().addRunnables(new Runnable() {
				@Override
				public void run() {
					for(OBJECT index : objects) {
						create(index,properties);
					}
				}
			});
		}else {
			//Batch execution
			function.setEntities(objects);
			validateMany(objects, function.getAction());
		}
		__listenExecuteCreateManyBefore__(objects, properties, function);
		function.execute();
		__listenExecuteCreateManyAfter__(objects, properties, function);
		/*
		if(Boolean.TRUE.equals(__isCreateManyOneByOne__())) {
			//Loop execution
			
		}else {
			//Batch execution
			validateMany(objects);
		}
		*/
		return this;
	}
	
	protected BusinessFunctionCreator __injectCreatorForMany__(){
		return ____inject____(BusinessFunctionCreator.class);
	}

	@Override @Transactional
	public BusinessServiceProvider<OBJECT> createMany(Collection<OBJECT> objects) {
		return createMany(objects, null);
	}
	
	protected Boolean __isCreateManyOneByOne__() {
		return IS_CREATE_ONE_BY_ONE;
	}
	
	@Override
	public BusinessServiceProvider<OBJECT> createByBatch(Collection<OBJECT> objects, Object batchSize,Properties properties) {
		if(__injectCollectionHelper__().isNotEmpty(objects)) {
			List<List<OBJECT>> lists = __injectCollectionHelper__().getBatches((List<OBJECT>) objects, batchSize);	
			if(__injectCollectionHelper__().isNotEmpty(lists)) {
				for(List<OBJECT> index : lists) {
					createMany(index,properties);
				}
			}
		}
		return this;
	}
	
	@Override
	public BusinessServiceProvider<OBJECT> createByBatch(Collection<OBJECT> objects, Object batchSize) {
		createByBatch(objects, batchSize, null);
		return this;
	}

	protected void __listenExecuteCreateManyBefore__(Collection<OBJECT> objects, Properties properties,BusinessFunctionCreator function){
		if(Boolean.TRUE.equals(__injectCollectionHelper__().isNotEmpty(objects)))
			for(OBJECT index : objects)
				__listenExecuteCreateBefore__(index, properties, function);
	}
	protected void __listenExecuteCreateManyAfter__(Collection<OBJECT> objects, Properties properties,BusinessFunctionCreator function){
		if(Boolean.TRUE.equals(__injectCollectionHelper__().isNotEmpty(objects)))
			for(OBJECT index : objects)
				__listenExecuteCreateAfter__(index, properties, function);
	}
	
	@Override @Transactional
	public BusinessServiceProvider<OBJECT> update(OBJECT object, Properties properties) {
		BusinessFunctionModifier function = ____inject____(BusinessFunctionModifier.class);
		__copyCommonProperties__(function, properties);
		function.setEntity(object);
		validateOne(object, function.getAction());
		__listenExecuteUpdateOneBefore__(object, properties, function);
		function.execute();
		__listenExecuteUpdateOneAfter__(object, properties, function);
		validateOne(object);
		return this;
	}
	
	protected void __listenExecuteUpdateOneBefore__(OBJECT object, Properties properties,BusinessFunctionModifier function){}
	protected void __listenExecuteUpdateOneAfter__(OBJECT object, Properties properties,BusinessFunctionModifier function){}

	@Override @Transactional
	public BusinessServiceProvider<OBJECT> update(OBJECT object) {
		return update(object, null);
	}

	@Override @Transactional
	public BusinessServiceProvider<OBJECT> updateMany(Collection<OBJECT> objects, Properties properties) {
		for(OBJECT index : objects)
			update(index, properties);
		return this;
	}

	@Override @Transactional
	public BusinessServiceProvider<OBJECT> updateMany(Collection<OBJECT> objects) {
		return updateMany(objects, null);
	}

	@Override
	public BusinessServiceProvider<OBJECT> updateByBatch(Collection<OBJECT> objects, Object batchSize,Properties properties) {
		if(__injectCollectionHelper__().isNotEmpty(objects)) {
			List<List<OBJECT>> lists = __injectCollectionHelper__().getBatches((List<OBJECT>) objects, batchSize);	
			if(__injectCollectionHelper__().isNotEmpty(lists)) {
				for(List<OBJECT> index : lists) {
					updateMany(index,properties);
				}
			}
		}
		return this;
	}
	
	@Override
	public BusinessServiceProvider<OBJECT> updateByBatch(Collection<OBJECT> objects, Object batchSize) {
		updateByBatch(objects, batchSize, null);
		return this;
	}
	
	@Override @Transactional
	public BusinessServiceProvider<OBJECT> delete(OBJECT object, Properties properties) {
		BusinessFunctionRemover function =  ____inject____(BusinessFunctionRemover.class);
		__copyCommonProperties__(function, properties);
		function.setEntity(object);
		validateOne(object, function.getAction());
		__listenExecuteDeleteOneBefore__(object, properties, function);
		function.execute();
		__listenExecuteDeleteOneAfter__(object, properties, function);
		//object has been remove so it is not more persisted. no validation
		return this;
	}
	
	protected void __listenExecuteDeleteOneBefore__(OBJECT object, Properties properties,BusinessFunctionRemover function){}
	protected void __listenExecuteDeleteOneAfter__(OBJECT object, Properties properties,BusinessFunctionRemover function){}

	@Override @Transactional
	public BusinessServiceProvider<OBJECT> delete(OBJECT object) {
		return delete(object, null);
	}

	@Override @Transactional
	public BusinessServiceProvider<OBJECT> deleteMany(Collection<OBJECT> objects, Properties properties) {
		for(OBJECT index : objects)
			delete(index, properties);
		return this;
	}

	@Override @Transactional
	public BusinessServiceProvider<OBJECT> deleteMany(Collection<OBJECT> objects) {
		return deleteMany(objects, null);
	}
	
	@Override
	public BusinessServiceProvider<OBJECT> deleteByBatch(Collection<OBJECT> objects, Object batchSize,Properties properties) {
		if(__injectCollectionHelper__().isNotEmpty(objects)) {
			List<List<OBJECT>> lists = __injectCollectionHelper__().getBatches((List<OBJECT>) objects, batchSize);	
			if(__injectCollectionHelper__().isNotEmpty(lists)) {
				for(List<OBJECT> index : lists) {
					deleteMany(index,properties);
				}
			}
		}
		return this;
	}
	
	@Override
	public BusinessServiceProvider<OBJECT> deleteByBatch(Collection<OBJECT> objects, Object batchSize) {
		deleteByBatch(objects, batchSize, null);
		return this;
	}
	
	/**/
	
	@Override @Transactional
	public BusinessServiceProvider<OBJECT> save(OBJECT object, Properties properties) {
		if(Boolean.TRUE.equals(isPersisted(object)))
			update(object,properties);
		else
			create(object,properties);
		return this;
	}
	
	@Override @Transactional
	public BusinessServiceProvider<OBJECT> save(OBJECT object) {
		return save(object, null);
	}

	@Override @Transactional
	public BusinessServiceProvider<OBJECT> saveMany(Collection<OBJECT> objects, Properties properties) {
		if(__injectCollectionHelper__().isNotEmpty(objects))
			for(OBJECT index : objects)
				save(index,properties);
		return this;
	}
	
	@Override @Transactional
	public BusinessServiceProvider<OBJECT> saveMany(Collection<OBJECT> objects) {
		return saveMany(objects, null);
	}

	@Override
	public BusinessServiceProvider<OBJECT> saveByBatch(Collection<OBJECT> objects, Object batchSize,Properties properties) {
		if(__injectCollectionHelper__().isNotEmpty(objects)) {
			List<List<OBJECT>> lists = __injectCollectionHelper__().getBatches((List<OBJECT>) objects, batchSize);	
			if(__injectCollectionHelper__().isNotEmpty(lists)) {
				for(List<OBJECT> index : lists) {
					saveMany(index,properties);
				}
			}
		}
		return this;
	}
	
	@Override
	public BusinessServiceProvider<OBJECT> saveByBatch(Collection<OBJECT> objects, Object batchSize) {
		saveByBatch(objects, batchSize, null);
		return this;
	}
	
	/**/
	
	protected PersistenceEntity<?> __injectPersistenceFromClass__(Class<?> entityClass){
		return (PersistenceEntity<?>) __inject__(PersistenceLayer.class).injectInterfaceClassFromEntityClass(entityClass);
	}
	
	/**/
	
	protected AssertionBuilderNull __injectAssertionBuilderNull__(Boolean isAffirmation,Object object,String...names){
		return __inject__(AssertionBuilderNull.class).setIsAffirmation(isAffirmation).setFieldValueGetter(object,names);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void __validateOne__(Object object, SystemAction action) {
		____validateOne____((OBJECT) object, action);
	}
	
	protected void ____validateOne____(OBJECT object, SystemAction action) {
		
	}

	/**/
	
	protected static BusinessLayer __injectBusinessLayer__() {
		return __inject__(BusinessLayer.class);
	}
	
}

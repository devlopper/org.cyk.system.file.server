package org.cyk.utility.client.controller.component.window;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.character.CharacterConstant;
import org.cyk.utility.client.controller.ControllerLayer;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.string.Strings;

public abstract class AbstractWindowContainerManagedWindowBuilderListImpl extends AbstractWindowContainerManagedWindowBuilderImpl implements WindowContainerManagedWindowBuilderList,Serializable {
	private static final long serialVersionUID = 1L;
	
	private Strings gridColumnsFieldNames;
	private Collection<?> gridObjects;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		Class<?> entityClass = getEntityClass();
		if(entityClass!=null)
			setRowClass(__inject__(ControllerLayer.class).getRowClass(entityClass, getSystemAction()));
	}
	
	@Override
	public Strings getGridColumnsFieldNames() {
		return gridColumnsFieldNames;
	}
	
	@Override
	public Strings getGridColumnsFieldNames(Boolean injectIfNull) {
		return (Strings) __getInjectIfNull__(FIELD_GRID_COLUMNS_FIELD_NAMES, injectIfNull);
	}
	
	@Override
	public WindowContainerManagedWindowBuilderList setGridColumnsFieldNames(Strings gridColumnsFieldNames) {
		this.gridColumnsFieldNames = gridColumnsFieldNames;
		return this;
	}
	
	@Override
	public WindowContainerManagedWindowBuilderList addGridColumnsFieldNames(Collection<String> gridColumnsFieldNames) {
		getGridColumnsFieldNames(Boolean.TRUE).add(gridColumnsFieldNames);
		return this;
	}
	
	@Override
	public WindowContainerManagedWindowBuilderList addGridColumnsFieldNames(String... gridColumnsFieldNames) {
		addGridColumnsFieldNames(__inject__(CollectionHelper.class).instanciate(gridColumnsFieldNames));
		return this;
	}
	
	@Override
	public WindowContainerManagedWindowBuilderList addGridColumnsFieldNamesWithPrefix(String prefix,Collection<String> gridColumnsFieldNames) {
		getGridColumnsFieldNames(Boolean.TRUE).addWithPrefix(__injectStringHelper__().addToEndIfDoesNotEndWith(prefix, CharacterConstant.DOT), gridColumnsFieldNames);
		return this;
	}
	
	@Override
	public WindowContainerManagedWindowBuilderList addGridColumnsFieldNamesWithPrefix(String prefix,String... gridColumnsFieldNames) {
		return addGridColumnsFieldNamesWithPrefix(prefix,__injectCollectionHelper__().instanciate(gridColumnsFieldNames));
	}
	
	@Override
	public Collection<?> getGridObjects() {
		return gridObjects;
	}
	
	@Override
	public WindowContainerManagedWindowBuilderList setGridObjects(Collection<?> gridObjects) {
		this.gridObjects = gridObjects;
		return this;
	}
	
	public static final String FIELD_GRID_COLUMNS_FIELD_NAMES = "gridColumnsFieldNames";
	
}

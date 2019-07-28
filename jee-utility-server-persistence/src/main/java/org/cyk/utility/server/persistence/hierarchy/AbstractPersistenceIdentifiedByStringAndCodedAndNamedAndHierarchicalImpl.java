package org.cyk.utility.server.persistence.hierarchy;
import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.server.persistence.AbstractPersistenceEntityImpl;
import org.cyk.utility.string.Strings;

public abstract class AbstractPersistenceIdentifiedByStringAndCodedAndNamedAndHierarchicalImpl<ENTITY extends AbstractIdentifiedByStringAndCodedAndNamedAndHierarchical<ENTITY,?>,HIERARCHY extends AbstractHierarchy<ENTITY>,HIERARCHIES extends HierarchyCollectionInstance<ENTITY,HIERARCHY>,HIERARCHY_PERSISTENCE extends HierarchyPersistence<HIERARCHY,ENTITY, HIERARCHIES>> extends AbstractPersistenceEntityImpl<ENTITY> implements PersistenceIdentifiedByStringAndCodedAndNamedAndHierarchical<ENTITY>,Serializable {
	private static final long serialVersionUID = 1L;

	private Class<HIERARCHY_PERSISTENCE> hierarchyPersistenceClass;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		hierarchyPersistenceClass = (Class<HIERARCHY_PERSISTENCE>) __injectClassHelper__().getByName(__injectClassHelper__().getParameterAt(getClass(), 3, Object.class).getName());
	}
	
	@Override
	public Collection<ENTITY> readByParentsCodes(Collection<String> parentsCodes) {
		HIERARCHIES hierarchies = __inject__(hierarchyPersistenceClass).readByParentsCodes(parentsCodes);
		return hierarchies == null ? null : hierarchies.getHierarchyChildren();	
	}
	
	@Override
	public Collection<ENTITY> readByParentsCodes(String... parentsCodes) {
		return readByParentsCodes(__injectCollectionHelper__().instanciate(parentsCodes));
	}
	
	@Override
	public Collection<ENTITY> readByParents(Collection<ENTITY> parents) {
		if(__injectCollectionHelper__().isNotEmpty(parents))
			return readByParentsCodes(parents.stream().map(AbstractIdentifiedByStringAndCodedAndNamedAndHierarchical::getCode).collect(Collectors.toList()));
		return null;
	}
	
	@Override
	public Collection<ENTITY> readByParents(@SuppressWarnings("unchecked") ENTITY... parents) {
		return readByParents(__injectCollectionHelper__().instanciate(parents));
	}
	
	@Override
	public Collection<ENTITY> readByChildrenCodes(Collection<String> childrenCodes) {
		HIERARCHIES hierarchies = __inject__(hierarchyPersistenceClass).readByChildrenCodes(childrenCodes);
		return hierarchies == null ? null : hierarchies.getHierarchyParents();	
	}
	
	@Override
	public Collection<ENTITY> readByChildrenCodes(String... childrenCodes) {
		return readByChildrenCodes(__injectCollectionHelper__().instanciate(childrenCodes));
	}
	
	@Override
	public Collection<ENTITY> readByChildren(Collection<ENTITY> children) {
		if(__injectCollectionHelper__().isNotEmpty(children))
			return readByChildrenCodes(children.stream().map(AbstractIdentifiedByStringAndCodedAndNamedAndHierarchical::getCode).collect(Collectors.toList()));
		return null;
	}
	
	@Override
	public Collection<ENTITY> readByChildren(@SuppressWarnings("unchecked") ENTITY... children) {
		return readByChildren(__injectCollectionHelper__().instanciate(children));
	}
	
	@Override
	protected void __listenExecuteReadAfter__(ENTITY entity,Properties properties) {
		super.__listenExecuteReadAfter__(entity,properties);
		Strings fields = __getFieldsFromProperties__(properties);
		if(__injectCollectionHelper__().isNotEmpty(fields))
			fields.get().forEach(new Consumer<String>() {
				@SuppressWarnings("unchecked")
				@Override
				public void accept(String field) {
					if(AbstractIdentifiedByStringAndCodedAndNamedAndHierarchical.FIELD_PARENTS.equals(field)) {
						Collection<ENTITY> parents = readByChildren(entity);
						if(__injectCollectionHelper__().isNotEmpty(parents))
							entity.getParents(Boolean.TRUE).add(parents);
					}else if(AbstractIdentifiedByStringAndCodedAndNamedAndHierarchical.FIELD_CHILDREN.equals(field)) {
						Collection<ENTITY> children = readByParents(entity);
						if(__injectCollectionHelper__().isNotEmpty(children))
							entity.getChildren(Boolean.TRUE).add(children);
					}
				}
			});
	}

}

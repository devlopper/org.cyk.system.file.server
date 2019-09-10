package org.cyk.utility.client.controller.data;
import java.util.List;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.field.FieldHelperImpl;
import org.cyk.utility.field.FieldValueGetter;

public interface TreeNodeListener<NODE> {

	default Object getData(NODE node) {
		return node;
	}
	
	default String getType(NODE node) {
		Object value = FieldHelperImpl.__read__(node, "type");
		if(value instanceof DataIdentifiedByStringAndCoded)
			value = ((DataIdentifiedByStringAndCoded)value).getCode();
		else if(value instanceof org.cyk.utility.client.controller.data.hierarchy.DataIdentifiedByStringAndCoded)
			value = ((org.cyk.utility.client.controller.data.hierarchy.DataIdentifiedByStringAndCoded)value).getCode();
		return value == null ? null : value.toString();
	}
	
	Integer getNumberOfChildren(NODE node);
    List<NODE> getChildren(NODE node);
    //NODE getParent(NODE node);
    default Boolean getIsLeaf(NODE node) {
		return getNumberOfChildren(node) == 0;
	}
	
}
